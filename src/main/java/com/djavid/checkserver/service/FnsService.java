package com.djavid.checkserver.service;

import com.djavid.checkserver.model.entity.response.CheckResponseFns;
import com.djavid.checkserver.model.repository.FnsRepository;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.DeferredResult;
import retrofit2.HttpException;

import java.io.EOFException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class FnsService {

    @Autowired
    private FnsRepository fnsRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final PublishSubject<Void> retrySubject = PublishSubject.create();


    public DeferredResult<CheckResponseFns> postReceiptString(@RequestParam String fiscalDriveNumber,
                                            @RequestParam String fiscalDocumentNumber,
                                            @RequestParam String fiscalSign) {

        DeferredResult<CheckResponseFns> deferredResult = new DeferredResult<>();
        deferredResult.setResultHandler(new DeferredResult.DeferredResultHandler() {
            @Override
            public void handleResult(Object result) {

            }
        });

        Disposable disposable = fnsRepository.getCheck(fiscalDriveNumber, fiscalDocumentNumber, fiscalSign)
                .retryWhen(new Function<Flowable<Throwable>, Publisher<?>>() {
                    @Override
                    public Publisher<?> apply(Flowable<Throwable> throwableFlowable) {
                        return throwableFlowable.flatMap(new Function<Throwable, Publisher<?>>() {
                            @Override
                            public Publisher<?> apply(Throwable throwable) {
                                if (throwable instanceof EOFException) {
                                    System.out.println("Retrying because 202 Accepted");
                                    return null;
                                } else {
                                    System.out.println("Not retrying because " + throwable.getMessage());
                                    return throwableFlowable;
                                }
                            }
                        });
                    }
                })
                .subscribe(responseFns -> {
                    deferredResult.setResult(responseFns);
                    System.out.println(responseFns);
                }, throwable -> {

                    if (throwable instanceof HttpException) {

                        deferredResult.setErrorResult(((HttpException) throwable).code());
                        System.out.println(((HttpException) throwable).code());

                    } else if (throwable instanceof TimeoutException) {

                        deferredResult.setErrorResult(throwable.getMessage());
                        System.out.println(throwable.getMessage());

                    } else if (throwable instanceof IOException) {

                        deferredResult.setErrorResult(throwable.getMessage());
                        System.out.println(throwable.getMessage());

                    } else {

                        deferredResult.setErrorResult(throwable.getMessage());
                        System.out.println(throwable.getMessage());

                    }
                });

        compositeDisposable.add(disposable);

        return deferredResult;

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        compositeDisposable.dispose();
    }
}
