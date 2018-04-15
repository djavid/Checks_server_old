package com.djavid.check_server.model.repository;

import com.djavid.check_server.model.entity.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long> {

    Item findItemById(Long id);

}
