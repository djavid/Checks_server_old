package com.djavid.checkserver.controller;

import com.djavid.checkserver.ChecksApplication;
import com.djavid.checkserver.model.entity.Item;
import com.djavid.checkserver.model.entity.Receipt;
import com.djavid.checkserver.model.entity.RegistrationToken;
import com.djavid.checkserver.model.entity.response.BaseResponse;
import com.djavid.checkserver.model.entity.response.GetReceiptsResponse;
import com.djavid.checkserver.model.repository.ItemRepository;
import com.djavid.checkserver.model.repository.ReceiptRepository;
import com.djavid.checkserver.model.repository.RegistrationTokenRepository;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "receipt")
public class ReceiptController {

    private final ReceiptRepository receiptRepository;
    private final ItemRepository itemRepository;
    private final RegistrationTokenRepository tokenRepository;

    public ReceiptController(ReceiptRepository receiptRepository, ItemRepository itemRepository,
                             RegistrationTokenRepository tokenRepository) {
        this.receiptRepository = receiptRepository;
        this.itemRepository = itemRepository;
        this.tokenRepository = tokenRepository;
    }


    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public BaseResponse getReceipts() {
        return new BaseResponse(receiptRepository.findAll());
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public BaseResponse getReceiptsByToken(@RequestHeader("Token") String token, @RequestParam("page") int page) {

        try {
            RegistrationToken registrationToken = tokenRepository.findRegistrationTokenByToken(token);
            if (registrationToken == null) return null;
            registrationToken.setLastVisited(System.currentTimeMillis());
            tokenRepository.save(registrationToken);

            List<Receipt> list = receiptRepository.findReceiptsByTokenId(registrationToken.getId());

            PagedListHolder<Receipt> pagedListHolder = new PagedListHolder<>(list);
            pagedListHolder.setPageSize(10);
            if (page < 0 || page >= pagedListHolder.getPageCount())
                return new BaseResponse("Page is incorrect!");

            pagedListHolder.setPage(page);
            return new BaseResponse(new GetReceiptsResponse(pagedListHolder.getPageList(),
                    !pagedListHolder.isLastPage()));

        } catch (Exception e) {
            return new BaseResponse("Something gone wrong");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public Receipt getReceiptById(@PathVariable("id") long id) {
        return receiptRepository.findReceiptByReceiptId(id);
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public BaseResponse postReceipt(@RequestBody Receipt receipt) {
        if (receipt == null)
            return  new BaseResponse("Sent null entity!");

        try {
            receipt.setCreated(System.currentTimeMillis());
            List<Item> items = receipt.getItems();
            for (Item item: items) {
                item.setReceipt(receipt);
            }

            Receipt res = receiptRepository.save(receipt);
            ChecksApplication.log.info("Saved receipt with id " + receipt.getReceiptId());

            return new BaseResponse(res);

        } catch (Exception e) {
            return new BaseResponse("Something gone wrong");
        }
    }

}
