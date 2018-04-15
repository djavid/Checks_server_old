package com.djavid.check_server.controller

import com.djavid.check_server.model.entity.Item
import com.djavid.check_server.model.repository.ItemRepository
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["item"])
class ItemController constructor(
        private val itemRepository: ItemRepository
) {

    @RequestMapping(method = [RequestMethod.GET], produces = ["application/json"])
    fun getItems() : Iterable<Item> = itemRepository.findAll()

}