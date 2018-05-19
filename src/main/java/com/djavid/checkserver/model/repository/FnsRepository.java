package com.djavid.checkserver.model.repository;

import com.djavid.checkserver.model.api.Api;
import io.reactivex.Single;
import org.springframework.stereotype.Repository;
import retrofit2.Response;

import static com.djavid.checkserver.util.Config.getAuthToken;
import static io.reactivex.schedulers.Schedulers.io;
import static io.reactivex.schedulers.Schedulers.newThread;

@Repository
public class FnsRepository {

    private final Api api;

    public FnsRepository(Api api) {
        this.api = api;
    }

    public Single<Response> getCheck(String fiscalDriveNumber, String fiscalDocumentNumber,
                                     String fiscalSign) {
        String os = "Android 7.0";
        String email = "no";

        return api.getCheck(fiscalDriveNumber, fiscalDocumentNumber, fiscalSign, email,
                getAuthToken(), "", os, "2", "1.4.4.1",
                "proverkacheka.nalog.ru:9999", "Keep-Alive", "gzip",
                "okhttp/3.0.1")
                .observeOn(io())
                .subscribeOn(newThread())
                .retry(3);
    }

}
