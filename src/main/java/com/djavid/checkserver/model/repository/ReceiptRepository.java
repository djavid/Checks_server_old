package com.djavid.checkserver.model.repository;

import com.djavid.checkserver.model.entity.Receipt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptRepository extends CrudRepository<Receipt, Long> {

    Receipt findReceiptByReceiptId(Long id);
    Receipt findReceiptByUser(String user);
    List<Receipt> findReceiptsByTokenId(Long tokenId);

    Receipt findReceiptByFiscalDriveNumberAndFiscalDocumentNumberAndFiscalSignAndTokenId
            (String fiscalDriveNumber, String fiscalDocumentNumber, String fiscalSign, long tokenId);
}
