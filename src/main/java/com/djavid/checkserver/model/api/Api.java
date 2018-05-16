package com.djavid.checkserver.model.api;

import com.djavid.checkserver.model.entity.query.FlaskValues;
import com.djavid.checkserver.model.entity.response.FlaskResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {

    @POST("https://predictcheck.herokuapp.com/predict")
    Single<FlaskResponse> getCategories(@Body FlaskValues values);

}
