package com.djavid.checkserver.model;

import com.djavid.checkserver.model.entity.query.FlaskValues;
import com.djavid.checkserver.model.entity.response.CheckResponseFns;
import com.djavid.checkserver.model.entity.response.FlaskResponse;
import io.reactivex.Single;
import retrofit2.http.*;

public interface Api {

    @POST("https://predictcheck.herokuapp.com/predict")
    Single<FlaskResponse> getCategories(@Body FlaskValues values);

    @GET("https://proverkacheka.nalog.ru:9999/" + "v1/inns/*/kkts/*/fss/{fss}/tickets/{tickets}")
    Single<CheckResponseFns> getCheck(
            @Path("fss") String fiscalDriveNumber,
            @Path("tickets") String fiscalDocumentNumber,

            @Query("fiscalSign") String fiscalSign,
            @Query("sendToEmail") String sendToEmail,

            @Header("Authorization") String authKey,
            @Header("Device-Id") String deviceId,
            @Header("Device-OS") String deviceOS,
            @Header("Version") String version,
            @Header("ClientVersion") String clientVersion,
            @Header("Host") String host,
            @Header("Connection") String connection,
            @Header("Accept-Encoding") String acceptEncoding,
            @Header("User-Agent") String userAgent);

}
