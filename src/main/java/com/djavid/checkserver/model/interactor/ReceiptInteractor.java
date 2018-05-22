package com.djavid.checkserver.model.interactor;

import com.djavid.checkserver.ChecksApplication;
import com.djavid.checkserver.model.Api;
import com.djavid.checkserver.model.entity.Receipt;
import com.djavid.checkserver.model.entity.RegistrationToken;
import com.djavid.checkserver.model.entity.query.FnsValues;
import com.djavid.checkserver.model.entity.response.CheckResponseFns;
import com.djavid.checkserver.model.repository.ReceiptRepository;
import com.djavid.checkserver.util.DateUtil;
import com.djavid.checkserver.util.LogoUtil;
import com.djavid.checkserver.util.StringUtil;
import io.reactivex.Single;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import static com.djavid.checkserver.util.Config.getAuthToken;

@Repository
public class ReceiptInteractor {

    @Autowired
    private Api api;

    @Autowired
    private ReceiptRepository receiptRepository;


    public Single<CheckResponseFns> getReceipt(FnsValues fnsValues) {
        String os = "Android 7.0";
        String email = "no";

        return api.getCheck(fnsValues.fiscalDriveNumber, fnsValues.fiscalDocumentNumber, fnsValues.fiscalSign, email,
                getAuthToken(), "", os, "2", "1.4.4.1",
                "proverkacheka.nalog.ru:9999", "Keep-Alive", "gzip",
                "okhttp/3.0.1");
    }

    public Receipt saveReceipt(Receipt receipt, RegistrationToken token) {

        Receipt existing = receiptRepository.findReceiptByFiscalDriveNumberAndFiscalDocumentNumberAndFiscalSignAndTokenId
                (receipt.getFiscalDriveNumber(), receipt.getFiscalDocumentNumber(), receipt.getFiscalSign(), token.getId());

        if (existing == null) {

            receipt.setTokenId(token.getId());
            receipt.setCreated(System.currentTimeMillis());
            receipt.getItems().forEach(it -> it.setReceipt(receipt));
            if (receipt.getUser() != null && !receipt.getUser().isEmpty())
                receipt.setLogo(LogoUtil.getLogo(receipt.getUser()));
            receipt.setUser(StringUtil.formatShopTitle(receipt.getUser()));

            ChecksApplication.log.info("Saved receipt with token id " + receipt.getTokenId());

            return receiptRepository.save(receipt);
        }

        return existing;
    }

    public void saveEmptyReceipt(FnsValues fnsValues, RegistrationToken token) {

        Receipt existing = receiptRepository.findReceiptByFiscalDriveNumberAndFiscalDocumentNumberAndFiscalSignAndTokenId
                (fnsValues.fiscalDriveNumber, fnsValues.fiscalDocumentNumber, fnsValues.fiscalSign, token.getId());

        if (existing == null) {

            DateTime parsedDateTime = DateUtil.parseDate(fnsValues.date);
            if (parsedDateTime != null) {
                fnsValues.date = parsedDateTime.toString();
            } else {
                ChecksApplication.log.error("Corrupted datetime!");
                return;
            }

            Receipt receipt = new Receipt(fnsValues);
            receipt.setTokenId(token.getId());
            receipt.setCreated(System.currentTimeMillis());
            receiptRepository.save(receipt);

            ChecksApplication.log.info("Saved empty receipt with token id " + receipt.getTokenId());
        }
    }

    public void deleteReceipt(Receipt receipt) {
        receiptRepository.delete(receipt);
    }

    @Nullable
    public Receipt findByFnsValues(FnsValues fnsValues, long tokenId) {
        return receiptRepository.findReceiptByFiscalDriveNumberAndFiscalDocumentNumberAndFiscalSignAndTokenId
                (fnsValues.fiscalDriveNumber, fnsValues.fiscalDocumentNumber, fnsValues.fiscalSign, tokenId);
    }

}
