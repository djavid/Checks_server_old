package com.djavid.checkserver.service;

import com.djavid.checkserver.ChecksApplication;
import com.djavid.checkserver.model.entity.response.BaseResponse;
import com.djavid.checkserver.model.repository.FnsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import retrofit2.HttpException;

@Service
public class FnsService {

    @Autowired
    private FnsRepository fnsRepository;


    public BaseResponse postReceiptString(@RequestParam String fiscalDriveNumber,
                                          @RequestParam String fiscalDocumentNumber,
                                          @RequestParam String fiscalSign) {

//            return new BaseResponse(fnsRepository.getCheck(fiscalDriveNumber, fiscalDocumentNumber, fiscalSign)
//                    .doOnError(Throwable::printStackTrace)
//                    .blockingGet().getDocument().getReceipt());

        final ResponseEntity response = fnsRepository.getCheck(fiscalDriveNumber, fiscalDocumentNumber, fiscalSign)
                .doOnError(throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException error = (HttpException) throwable;
                        ChecksApplication.log.error(error.code() + " " + error.message());

                        if (error.code() == 406) {
                            //try it again in 24/48 hours
                            //return response
                        } else if (error.code() == 400) {

                        }
                    }
                })
                .onErrorReturn(throwable ->
                        new ResponseEntity(HttpStatus.resolve(((HttpException) throwable).code())))
                .blockingGet();

        if (response.getStatusCode().value() == 202) {

            ChecksApplication.log.info(response.toString());
            return new BaseResponse(response.getStatusCode());

        } else if (response.getStatusCode().value() == 200) {

            ChecksApplication.log.info(response.toString());
            return new BaseResponse(response.getBody());

        } else if (response.getStatusCode().value() == 406) {

            ChecksApplication.log.info(response.toString());
            return new BaseResponse(response.getStatusCode());

        } else if (response.getStatusCode().value() == 400) {

            ChecksApplication.log.info(response.toString());
            return new BaseResponse(response.getStatusCode());

        }

        ChecksApplication.log.info(response.toString());
        return new BaseResponse("Something gone wrong!");
    }

}
