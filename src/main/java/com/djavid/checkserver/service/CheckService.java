package com.djavid.checkserver.service;

import com.djavid.checkserver.ChecksApplication;
import com.djavid.checkserver.model.entity.Item;
import com.djavid.checkserver.model.entity.Receipt;
import com.djavid.checkserver.model.entity.RegistrationToken;
import com.djavid.checkserver.model.entity.query.FlaskValues;
import com.djavid.checkserver.model.entity.response.BaseResponse;
import com.djavid.checkserver.model.repository.CheckRepository;
import com.djavid.checkserver.model.repository.ItemRepository;
import com.djavid.checkserver.model.repository.ReceiptRepository;
import com.djavid.checkserver.util.LogoUtil;
import com.djavid.checkserver.util.StringUtil;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.DeferredResult;
import retrofit2.HttpException;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CheckService {

    @Autowired
    private CheckRepository checkRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ReceiptRepository receiptRepository;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public DeferredResult<BaseResponse> getCheckFromFns(@RequestParam String fiscalDriveNumber,
                                                        @RequestParam String fiscalDocumentNumber,
                                                        @RequestParam String fiscalSign) {

        DeferredResult<BaseResponse> deferredResult = new DeferredResult<>();

        Disposable disposable = checkRepository.getCheck(fiscalDriveNumber, fiscalDocumentNumber, fiscalSign)
                .retryWhen(throwableFlowable -> throwableFlowable
                        .flatMap(throwable -> subscriber -> {
                            if (throwable instanceof EOFException) {
                                ChecksApplication.log.info("Retrying because HTTP 202 Accepted");
                                subscriber.onNext(1);
                            } else {
                                ChecksApplication.log.info("Not retrying because " + throwable.getMessage());
                                subscriber.onError(throwable);
                            }
                        })
                        .delay(1, TimeUnit.SECONDS)
                        .zipWith(Flowable.range(1, 5), (n, i) -> i))
                .subscribe(
                        responseFns -> deferredResult.setResult(new BaseResponse(responseFns)),
                        throwable -> {
                            ChecksApplication.log.error(throwable.getMessage());

                            if (throwable instanceof HttpException) {
                                HttpException httpException = (HttpException) throwable;
                                deferredResult.setErrorResult(
                                        new BaseResponse(httpException.code() + " " + httpException.message()));

                                System.out.println(httpException.response().message());
                                System.out.println(httpException.response().body());
                            } else {
                                deferredResult.setErrorResult(new BaseResponse(throwable.getMessage()));
                            }
                        });

        compositeDisposable.add(disposable);

        return deferredResult;
    }

    public void getAndSaveCategories(List<Item> items) {

        List<String> values = new ArrayList<>();
        items.forEach(it -> values.add(it.getName()));

        Disposable disposable = checkRepository.getCategories(new FlaskValues(values))
                .subscribe(it -> {
                    for (int i = 0; i < items.size(); i++) {
                        items.get(i).setName(it.getNormalized().get(i));
                        items.get(i).setCategory(it.getCategories().get(i));
                        itemRepository.save(items.get(i));
                    }
                }, Throwable::printStackTrace);

        compositeDisposable.add(disposable);
    }

    public Receipt saveReceipt(Receipt receipt, RegistrationToken token) {
        receipt.setTokenId(token.getId());
        receipt.setCreated(System.currentTimeMillis());
        receipt.getItems().forEach(it -> it.setReceipt(receipt));
        receipt.setLogo(LogoUtil.getLogo(receipt.getUser()));
        receipt.setUser(StringUtil.formatShopTitle(receipt.getUser()));

        ChecksApplication.log.info("Saved receipt with id " + receipt.getReceiptId());

        return receiptRepository.save(receipt);
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        compositeDisposable.dispose();
    }
}
