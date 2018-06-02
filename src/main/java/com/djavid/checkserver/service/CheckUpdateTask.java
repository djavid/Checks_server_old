package com.djavid.checkserver.service;

import com.djavid.checkserver.ChecksApplication;
import com.djavid.checkserver.model.entity.Item;
import com.djavid.checkserver.model.entity.Receipt;
import com.djavid.checkserver.model.entity.RegistrationToken;
import com.djavid.checkserver.model.entity.query.FlaskValues;
import com.djavid.checkserver.model.entity.query.FnsValues;
import com.djavid.checkserver.model.interactor.CategoryInteractor;
import com.djavid.checkserver.model.interactor.ReceiptInteractor;
import com.djavid.checkserver.model.repository.ItemRepository;
import com.djavid.checkserver.model.repository.ReceiptRepository;
import com.djavid.checkserver.model.repository.RegistrationTokenRepository;
import com.djavid.checkserver.util.DateUtil;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import retrofit2.HttpException;

import java.util.ArrayList;
import java.util.List;

import static com.djavid.checkserver.util.Config.*;

@Service
public class CheckUpdateTask {

    @Autowired
    private ReceiptInteractor receiptInteractor;
    @Autowired
    ReceiptRepository receiptRepository;
    @Autowired
    RegistrationTokenRepository tokenRepository;
    @Autowired
    private CategoryInteractor categoryInteractor;
    @Autowired
    private ItemRepository itemRepository;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        compositeDisposable.dispose();
    }


    @Scheduled(fixedDelay = CHECK_UPDATE_DELAY)
    public void listenForCheckUpdates() {
        ChecksApplication.log.info("listenForCheckUpdates()");

        Iterable<Receipt> iterable = receiptRepository.findAll();
        iterable.forEach(it -> {
            if (it.isEmpty()) {
                try {
                    RegistrationToken token = tokenRepository.findRegistrationTokenById(it.getTokenId());
                    if (token == null) return;

                    getReceipt(it.getFnsValues(), token, it);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void getReceipt(FnsValues fnsValues, RegistrationToken token, Receipt it) {

        Disposable disposable = receiptInteractor.getReceipt(fnsValues)
                .retryWhen(CheckService.retryHandler)
                .subscribe(
                        responseFns -> {
                            Receipt receipt = responseFns.getDocument().getReceipt();
                            receiptRepository.delete(it);
                            receipt = receiptInteractor.saveReceipt(receipt, token);
                            ChecksApplication.log.info("Check successfully loaded " + it.toString());

                            //get categories from server and save them to db
                            List<Item> items = receipt.getItems();
                            List<String> values = new ArrayList<>();
                            items.forEach(item -> values.add(item.getName()));

                            categoryInteractor.getCategories(new FlaskValues(values))
                                    .subscribe(item -> {
                                        for (int i = 0; i < items.size(); i++) {
                                            items.get(i).setName(item.getNormalized().get(i));
                                            items.get(i).setCategory(item.getCategories().get(i));
                                            itemRepository.save(items.get(i));
                                        }

                                    }, Throwable::printStackTrace);
                        },
                        throwable -> {
                            errorHandler(throwable, fnsValues, token, it);
                        });
        compositeDisposable.add(disposable);
    }

    private void errorHandler(Throwable throwable, FnsValues fnsValues,
                              RegistrationToken token, Receipt receipt) {
        ChecksApplication.log.error(throwable.getMessage());

        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;

            if (httpException.code() == 406) {

                DateTime currentDate = new DateTime();
                DateTime checkDate = DateUtil.parseDate(fnsValues.date);
                if (checkDate == null) return;

                if (checkDate.isAfter(currentDate.minusHours(CHECK_EXPIRE_HOURS))) {
                    //чек напечатан в последние 25 часов и пока не поступил в налоговую
                    ChecksApplication.log.info(ERROR_CHECK_NOT_LOADED);
                } else {
                    //чек устарел
                    ChecksApplication.log.info("Check not found and is being deleted " + receipt.toString());
                    receiptRepository.delete(receipt);
                }
            }
        }
    }



}
