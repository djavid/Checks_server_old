package com.djavid.checkserver.controller;

import com.djavid.checkserver.ChecksApplication;
import com.djavid.checkserver.model.entity.Item;
import com.djavid.checkserver.model.entity.Receipt;
import com.djavid.checkserver.model.repository.ItemRepository;
import com.djavid.checkserver.model.repository.ReceiptRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "receipt")
public class ReceiptController {

    private final ReceiptRepository receiptRepository;
    private final ItemRepository itemRepository;

    public ReceiptController(ReceiptRepository receiptRepository,
                             ItemRepository itemRepository) {
        this.receiptRepository = receiptRepository;
        this.itemRepository = itemRepository;
    }



    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public Iterable<Receipt> getReceipts() {
        return receiptRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public Receipt postReceipt(@RequestBody Receipt receipt) {

        List<Item> items = receipt.getItems();
        for (Item item: items) {
            item.setReceipt(receipt);
            //itemRepository.save()
        }

        Receipt res = receiptRepository.save(receipt);
        ChecksApplication.log.info("Saved " + receipt.toString());

        return res;
    }

}
