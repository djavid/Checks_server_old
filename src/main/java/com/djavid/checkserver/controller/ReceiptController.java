package com.djavid.checkserver.controller;

import com.djavid.checkserver.ChecksApplication;
import com.djavid.checkserver.model.entity.Item;
import com.djavid.checkserver.model.entity.Receipt;
import com.djavid.checkserver.model.repository.ItemRepository;
import com.djavid.checkserver.model.repository.ReceiptRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public List<Receipt> getReceipts(@RequestParam("page") int page) {

        if (page < 1) return new ArrayList<>();

        List<Receipt> list = new ArrayList<>();
        for (Receipt item : receiptRepository.findAll()) {
            list.add(item);
        }

//        for (int j = list.size() - 1; j > list.size() - page; j--) {
//            res.add(list.get(j));
//        }

        List<Receipt> res = new ArrayList<>();
        int start = list.size() - 1 - (page - 1) * 10;

        int interval = 10;
        if (start < 10)  interval = start;

        for (int j = start; j > start - interval; j--) {
            res.add(list.get(j));
        }

        return res;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public Receipt getReceiptById(@PathVariable("id") long id) {
        return receiptRepository.findReceiptByReceiptId(id);
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public Receipt postReceipt(@RequestBody Receipt receipt) {

        List<Item> items = receipt.getItems();
        for (Item item: items) {
            item.setReceipt(receipt);
        }

        Receipt res = receiptRepository.save(receipt);
        ChecksApplication.log.info("Saved receipt with id " + receipt.getReceiptId());

        return res;
    }

}
