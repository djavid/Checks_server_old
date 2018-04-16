package com.djavid.checkserver.model.repository;

import com.djavid.checkserver.model.entity.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {

    Item findItemByitem_id(Long id);

}
