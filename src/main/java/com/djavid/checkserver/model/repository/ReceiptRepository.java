package com.djavid.checkserver.model.repository;

import com.djavid.checkserver.model.entity.Receipt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends CrudRepository<Receipt, Long> {

    Receipt findReceiptByReceipt_id(Long id);
    Receipt findReceiptByShop(String user);

}
