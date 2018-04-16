package com.djavid.checkserver.controller;

import com.djavid.checkserver.model.entity.Item;
import com.djavid.checkserver.model.entity.response.ResponseItem;
import com.djavid.checkserver.model.repository.ItemRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "item")
public class ItemController {

    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public Iterable<ResponseItem> getItems() {

        List<ResponseItem> itemsResponse = new ArrayList<>();
        for (Item item : itemRepository.findAll()) {
            itemsResponse.add(new ResponseItem(item));
        }

        return itemsResponse;
    }
}
