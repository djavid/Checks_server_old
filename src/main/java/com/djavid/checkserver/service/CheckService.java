package com.djavid.checkserver.service;

import com.djavid.checkserver.ChecksApplication;
import com.djavid.checkserver.model.entity.Receipt;
import com.djavid.checkserver.model.entity.RegistrationToken;
import com.djavid.checkserver.model.entity.query.FnsValues;
import com.djavid.checkserver.model.entity.response.BaseResponse;
import com.djavid.checkserver.model.interactor.ReceiptInteractor;
import com.djavid.checkserver.model.repository.ReceiptRepository;
import com.djavid.checkserver.model.repository.RegistrationTokenRepository;
import com.djavid.checkserver.util.DateUtil;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import org.joda.time.DateTime;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import retrofit2.HttpException;

import java.io.EOFException;
import java.util.concurrent.TimeUnit;

import static com.djavid.checkserver.util.Config.*;

@Service
public class CheckService {

    @Autowired
    private ReceiptInteractor receiptInteractor;
    @Autowired
    ReceiptRepository receiptRepository;
    @Autowired
    RegistrationTokenRepository tokenRepository;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        compositeDisposable.dispose();
    }


    public DeferredResult<BaseResponse> getReceipt(FnsValues fnsValues, RegistrationToken token) {

        DeferredResult<BaseResponse> deferredResult = new DeferredResult<>();

        //check if exists the same
        Receipt existing = receiptInteractor.findByFnsValues(fnsValues, token.getId());

        if (existing == null || existing.isEmpty()) {

            Disposable disposable = receiptInteractor.getReceipt(fnsValues)
                    .retryWhen(retryHandler)
                    .subscribe(
                            responseFns ->
                                    deferredResult.setResult(new BaseResponse(responseFns)),
                            throwable -> {
                                ChecksApplication.log.error(throwable.getMessage());

                                errorHandler(throwable, deferredResult, fnsValues, token);
                            });
            compositeDisposable.add(disposable);

        } else {
            deferredResult.setErrorResult(new BaseResponse(ERROR_CHECK_EXISTS));
            return deferredResult;
        }

        return deferredResult;
    }


    private Function<? super Flowable<Throwable>, ? extends Publisher<?>> retryHandler =
            (Flowable<Throwable> throwableFlowable) -> throwableFlowable
                    .flatMap(throwable -> subscriber -> {
                        if (throwable instanceof EOFException) {
                            ChecksApplication.log.info("Retrying because HTTP 202 Accepted");
                            subscriber.onNext(1);
                        } else {
                            ChecksApplication.log.info("Not retrying because " + throwable.getMessage());
                            subscriber.onError(throwable);
                        }
                    })
                    .delay(CHECK_FNS_POLLING_DELAY_SECONDS, TimeUnit.SECONDS)
                    .zipWith(Flowable.range(1, CHECK_FNS_POLLING_COUNT), (n, i) -> i);


    private void errorHandler(Throwable throwable, DeferredResult<BaseResponse> deferredResult, FnsValues fnsValues,
                              RegistrationToken token) {

        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;

            if (httpException.code() == 406) {

                DateTime currentDate = new DateTime();
                DateTime checkDate = DateUtil.parseDate(fnsValues.date);
                if (checkDate == null) {
                    deferredResult.setErrorResult(new BaseResponse(ERROR_CHECK_DATE_CORRUPTED));
                    return;
                }

                if (checkDate.isAfter(currentDate.minusHours(CHECK_EXPIRE_HOURS))) {
                    //чек напечатан в последние 25 часов и пока не поступил в налоговую,
                    //поэтому сохраняем его в бд, чтобы получить потом
                    deferredResult.setErrorResult(new BaseResponse(ERROR_CHECK_NOT_LOADED));
                    receiptInteractor.saveEmptyReceipt(fnsValues, token);
                } else
                    //чек устарел
                    deferredResult.setErrorResult(new BaseResponse(ERROR_CHECK_NOT_FOUND));
            } else
                deferredResult.setErrorResult(new BaseResponse(httpException.code() + " " + httpException.message()));
        } else
            deferredResult.setErrorResult(new BaseResponse(throwable.getMessage()));
    }

}
