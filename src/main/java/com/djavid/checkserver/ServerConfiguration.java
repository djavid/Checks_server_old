package com.djavid.checkserver;

import com.djavid.checkserver.model.Api;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Configuration
public class ServerConfiguration {

    @Bean
    public OkHttpClient okHttpClient() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();
    }

    @Bean
    public Retrofit retrofit(OkHttpClient client) {

        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://predictcheck.herokuapp.com")
                .client(client)
                .build();
    }

    @Bean
    public Api apiService(Retrofit retrofit) {
        return retrofit.create(Api.class);
    }
}
