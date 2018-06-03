package com.djavid.checkserver.model.repository;

import com.djavid.checkserver.model.entity.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {

    Item findItemByItemId(Long id);
    List<Item> findItemsByCategoryAndReceiptTokenId(String category, Long token_id);

}
