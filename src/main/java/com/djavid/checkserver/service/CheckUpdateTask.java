package com.djavid.checkserver.service;

import com.djavid.checkserver.ChecksApplication;
import com.djavid.checkserver.model.entity.Receipt;
import com.djavid.checkserver.model.entity.RegistrationToken;
import com.djavid.checkserver.model.entity.response.BaseResponse;
import com.djavid.checkserver.model.entity.response.CheckResponseFns;
import com.djavid.checkserver.model.interactor.ReceiptInteractor;
import com.djavid.checkserver.model.repository.ReceiptRepository;
import com.djavid.checkserver.model.repository.RegistrationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

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
    CheckService checkService;


    @Scheduled(fixedDelay = CHECK_UPDATE_DELAY)
    public void listenForCheckUpdates() {
        ChecksApplication.log.info("listenForCheckUpdates()");

        Iterable<Receipt> iterable = receiptRepository.findAll();
        iterable.forEach(it -> {
            if (it.isEmpty()) {
                try {

                    RegistrationToken token = tokenRepository.findRegistrationTokenById(it.getTokenId());
                    if (token == null) return;

                    DeferredResult<BaseResponse> deferredResult = checkService.getReceipt(it.getFnsValues(), token);

                    deferredResult.setResultHandler(result -> {
                        if (result instanceof  BaseResponse) {
                            BaseResponse baseResponse = ((BaseResponse) result);
                            handleBaseResponse(baseResponse, it, token);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handleBaseResponse(BaseResponse baseResponse, Receipt it, RegistrationToken token) {

        if (baseResponse.getError().isEmpty() && baseResponse.getResult() instanceof CheckResponseFns) {
            ChecksApplication.log.info("Check successfully loaded " + it.toString());

            Receipt receipt = ((CheckResponseFns) baseResponse.getResult()).getDocument().getReceipt();

            receiptInteractor.saveReceipt(receipt, token);
            receiptRepository.delete(it);

        } else if (baseResponse.getError().equals(ERROR_CHECK_NOT_FOUND)) {
            ChecksApplication.log.info("Check not found and is being deleted " + it.toString());
            receiptRepository.delete(it);

        } else if (baseResponse.getError().equals(ERROR_CHECK_NOT_LOADED)) {
            ChecksApplication.log.info(ERROR_CHECK_NOT_LOADED);
        }
    }

}
