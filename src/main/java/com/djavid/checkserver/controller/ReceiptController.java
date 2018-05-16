package com.djavid.checkserver.controller;

import com.djavid.checkserver.ChecksApplication;
import com.djavid.checkserver.model.api.Api;
import com.djavid.checkserver.model.entity.Item;
import com.djavid.checkserver.model.entity.Receipt;
import com.djavid.checkserver.model.entity.RegistrationToken;
import com.djavid.checkserver.model.entity.query.FlaskValues;
import com.djavid.checkserver.model.entity.response.BaseResponse;
import com.djavid.checkserver.model.entity.response.GetReceiptsResponse;
import com.djavid.checkserver.model.repository.ItemRepository;
import com.djavid.checkserver.model.repository.ReceiptRepository;
import com.djavid.checkserver.model.repository.RegistrationTokenRepository;
import io.reactivex.disposables.Disposable;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "receipt")
public class ReceiptController {

    private final ReceiptRepository receiptRepository;
    private final ItemRepository itemRepository;
    private final RegistrationTokenRepository tokenRepository;
    private final Api api;
    private Disposable disposable;


    public ReceiptController(ReceiptRepository receiptRepository, ItemRepository itemRepository,
                             RegistrationTokenRepository tokenRepository, Api api) {
        this.receiptRepository = receiptRepository;
        this.itemRepository = itemRepository;
        this.tokenRepository = tokenRepository;
        this.api = api;
    }


    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
    public BaseResponse getReceipts() {
        return new BaseResponse(receiptRepository.findAll());
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public BaseResponse getReceiptsByToken(@RequestHeader("Token") String token, @RequestParam("page") int page) {

        try {
            RegistrationToken registrationToken = tokenRepository.findRegistrationTokenByToken(token);
            if (registrationToken == null)
                return new BaseResponse("Token is incorrect!");
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
            receipt.getItems().forEach(it -> it.setReceipt(receipt));

            Receipt res = receiptRepository.save(receipt);
            ChecksApplication.log.info("Saved receipt with id " + receipt.getReceiptId());

            List<Item> items = res.getItems();
            List<String> values = new ArrayList<>();
            items.forEach(it -> values.add(it.getName()));

            disposable = api.getCategories(new FlaskValues(values))
                    .observeOn(io.reactivex.schedulers.Schedulers.io())
                    .subscribeOn(io.reactivex.schedulers.Schedulers.newThread())
                    .retry(2)
                    .subscribe(it -> {

                        for (int i = 0; i < items.size(); i++) {
                            items.get(i).setName(it.getNormalized().get(i));
                            items.get(i).setCategory(it.getCategories().get(i));
                            itemRepository.save(items.get(i));
                        }

                    }, Throwable::printStackTrace);

            return new BaseResponse(res);

        } catch (Exception e) {
            return new BaseResponse("Something gone wrong");
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        disposable.dispose();
    }
}
