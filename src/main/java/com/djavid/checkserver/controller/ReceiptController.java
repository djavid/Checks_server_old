package com.djavid.checkserver.controller;

import com.djavid.checkserver.ChecksApplication;
import com.djavid.checkserver.model.entity.Item;
import com.djavid.checkserver.model.entity.Receipt;
import com.djavid.checkserver.model.entity.response.GetReceiptsResponse;
import com.djavid.checkserver.model.repository.ItemRepository;
import com.djavid.checkserver.model.repository.ReceiptRepository;
import org.springframework.beans.support.PagedListHolder;
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
    public GetReceiptsResponse getReceipts(@RequestParam("page") int page) {

        List<Receipt> list = new ArrayList<>();
        receiptRepository.findAll().forEach(list::add);

        PagedListHolder<Receipt> pagedListHolder = new PagedListHolder<>(list);
        pagedListHolder.setPageSize(10);

        if (page < 1 || page > pagedListHolder.getPageCount())
            return new GetReceiptsResponse("Page is incorrect!");

        pagedListHolder.setPage(page);
        GetReceiptsResponse response = new GetReceiptsResponse(pagedListHolder.getPageList());
        return response;
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
