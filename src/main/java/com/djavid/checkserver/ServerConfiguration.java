package com.djavid.checkserver;

import com.djavid.checkserver.model.api.Api;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;

@Configuration
public class ServerConfiguration {

    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient client = new OkHttpClient();
        Logger logger = LoggerFactory.getLogger("LoggingInterceptor");

        client.interceptors().add(chain -> {
            Request request = chain.request();

            long t1 = System.nanoTime();
            logger.info(String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            logger.info(String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        });
        return client;
    }

    @Bean
    public Retrofit retrofit(OkHttpClient client, Gson gson) {

        return new Retrofit.Builder()
                //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://predictcheck.herokuapp.com")
                .client(client)
                .build();
    }

    @Bean
    public Api randomNumberService(Retrofit retrofit) {
        return retrofit.create(Api.class);
    }
}
