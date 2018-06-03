package com.djavid.checkserver.controller;

import com.djavid.checkserver.model.entity.Item;
import com.djavid.checkserver.model.entity.RegistrationToken;
import com.djavid.checkserver.model.entity.response.BaseResponse;
import com.djavid.checkserver.model.entity.response.GetItemsResponse;
import com.djavid.checkserver.model.repository.ItemRepository;
import com.djavid.checkserver.model.repository.RegistrationTokenRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "item")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RegistrationTokenRepository tokenRepository;


    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public Iterable<Item> getItems() {

        return itemRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public BaseResponse getItemsByCategory(@RequestHeader("Token") String token, @RequestParam("category") String category,
                                          @RequestParam("page") int page) {

        try {
            RegistrationToken registrationToken = tokenRepository.findRegistrationTokenByToken(token);
            if (registrationToken == null)
                return new BaseResponse("Token is incorrect!");
            registrationToken.setLastVisited(System.currentTimeMillis());
            tokenRepository.save(registrationToken);

            List<Item> list = itemRepository.findItemsByCategoryAndReceiptTokenId(category, registrationToken.getId());
            list.sort((r1, r2) -> {
                long date1 = DateTime.parse(r1.getReceipt().getDateTime()).getMillis();
                long date2 = DateTime.parse(r2.getReceipt().getDateTime()).getMillis();

                return Long.compare(date2, date1);
            });

            PagedListHolder<Item> pagedListHolder = new PagedListHolder<>(list);
            pagedListHolder.setPageSize(10);
            if (page < 0 || page >= pagedListHolder.getPageCount())
                return new BaseResponse("Page is incorrect!");

            pagedListHolder.setPage(page);
            return new BaseResponse(new GetItemsResponse(pagedListHolder.getPageList(),
                    !pagedListHolder.isLastPage()));

        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResponse("Something gone wrong");
        }
    }
}
