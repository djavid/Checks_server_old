package com.djavid.checkserver.controller;

import com.djavid.checkserver.model.Api;
import com.djavid.checkserver.model.entity.Item;
import com.djavid.checkserver.model.entity.Receipt;
import com.djavid.checkserver.model.entity.RegistrationToken;
import com.djavid.checkserver.model.entity.response.BaseResponse;
import com.djavid.checkserver.model.entity.response.CategoryPercentage;
import com.djavid.checkserver.model.interactor.CategoryInteractor;
import com.djavid.checkserver.model.interactor.ReceiptInteractor;
import com.djavid.checkserver.model.repository.ItemRepository;
import com.djavid.checkserver.model.repository.ReceiptRepository;
import com.djavid.checkserver.model.repository.RegistrationTokenRepository;
import com.djavid.checkserver.service.CheckService;
import io.reactivex.disposables.Disposable;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "stats")
public class StatsController {

    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RegistrationTokenRepository tokenRepository;
    @Autowired
    private ReceiptInteractor receiptInteractor;
    @Autowired
    private CheckService checkService;
    @Autowired
    private CategoryInteractor categoryInteractor;
    @Autowired
    private Api api;

    private Disposable disposable;


    @RequestMapping(method = RequestMethod.GET)
    public BaseResponse getIntervalStats(@RequestHeader("Token") String token,
                                         @RequestParam long start,
                                         @RequestParam long end) {

        RegistrationToken registrationToken = tokenRepository.findRegistrationTokenByToken(token);
        if (registrationToken == null)
            return new BaseResponse("Token is incorrect!");
        registrationToken.setLastVisited(System.currentTimeMillis());
        tokenRepository.save(registrationToken);

        DateTime dateStart = new DateTime(start).withTimeAtStartOfDay();
        DateTime dateEnd = new DateTime(end).plusDays(1).withTimeAtStartOfDay();
        System.out.println(dateStart);
        System.out.println(dateEnd);

        //select those receipts that are in input date interval
        List<Receipt> receipts = receiptInteractor.getReceiptsInInterval(registrationToken, dateStart, dateEnd);

        //get counts and sums for each category
        Map<String, Integer> mapCount = new HashMap<>();
        Map<String, Double> mapSum = new HashMap<>();

        int maxCount = 0;
        for (Receipt receipt : receipts) {
            for (Item item : receipt.getItems()) {
                int count = mapCount.getOrDefault(item.getCategory(), 1);
                mapCount.put(item.getCategory(), count);
                if (count > maxCount) maxCount = count;

                double sum = mapSum.getOrDefault(item.getCategory(), 0.0) / 100.0;
                mapSum.put(item.getCategory(), sum);
            }
        }

        //get them into array
        List<String> categories = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        List<Double> sums = new ArrayList<>();

        mapCount.forEach((s, integer) -> {
            categories.add(s);
            counts.add(integer);
        });
        mapSum.forEach((s, aDouble) -> sums.add(aDouble));

        //add them to response
        List<CategoryPercentage> categoryPercentageList = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            Double percent = counts.get(i).doubleValue() / maxCount;

            CategoryPercentage categoryPercentage =
                    new CategoryPercentage(categories.get(i), percent, sums.get(i));
            categoryPercentageList.add(categoryPercentage);
        }

        return new BaseResponse(categoryPercentageList);
    }
}
