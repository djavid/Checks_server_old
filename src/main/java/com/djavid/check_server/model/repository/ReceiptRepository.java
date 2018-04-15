package com.djavid.check_server.model.repository;

import com.djavid.check_server.model.entity.Receipt;
import org.springframework.data.repository.CrudRepository;

public interface ReceiptRepository extends CrudRepository<Receipt, Long> {

    Receipt findReceiptById(Long id);
    Receipt findReceiptByUser(String user);

}
