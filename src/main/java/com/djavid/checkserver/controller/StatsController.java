package com.djavid.checkserver.controller;

import com.djavid.checkserver.model.Api;
import com.djavid.checkserver.model.entity.Item;
import com.djavid.checkserver.model.entity.Receipt;
import com.djavid.checkserver.model.entity.RegistrationToken;
import com.djavid.checkserver.model.entity.response.BaseResponse;
import com.djavid.checkserver.model.entity.response.Percentage;
import com.djavid.checkserver.model.entity.response.StatPercentResponse;
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


    @RequestMapping(value = "/intervals", method = RequestMethod.GET)
    public BaseResponse getIntervals(@RequestHeader("Token") String token,
                                     @RequestParam int days) {

        RegistrationToken registrationToken = tokenRepository.findRegistrationTokenByToken(token);
        if (registrationToken == null)
            return new BaseResponse("Token is incorrect!");
        registrationToken.setLastVisited(System.currentTimeMillis());
        tokenRepository.save(registrationToken);

        //select those receipts that are in input date interval
        List<Receipt> receipts = receiptRepository.findReceiptsByTokenId(registrationToken.getId());

        //get oldest date from receipts
        DateTime dateStart = new DateTime();

        for (Receipt receipt : receipts) {
            DateTime datetime = DateTime.parse(receipt.getDateTime());

            if (datetime != null && datetime.isBefore(dateStart))
                dateStart = datetime;
        }

        dateStart = dateStart.withTimeAtStartOfDay();
        DateTime dateIndex = new DateTime().withDayOfWeek(7).plusDays(1).withTimeAtStartOfDay().minusMinutes(1);

        List<DateInterval> dateIntervals = new ArrayList<>();

        while (dateIndex.isAfter(dateStart)) {
            if (dateIndex.minusDays(days).isAfter(dateStart)) {
                dateIntervals.add(new DateInterval(dateIndex.minusDays(days).toString(), dateIndex.toString()));
            } else {
                dateIntervals.add(new DateInterval(dateStart.toString(), dateIndex.toString()));
            }

            dateIndex = dateIndex.minusDays(days);
        }

        return new BaseResponse(dateIntervals);
    }

    public class DateInterval {

        public String dateStart;
        public String dateEnd;

        public DateInterval(String dateStart, String dateEnd) {
            this.dateStart = dateStart;
            this.dateEnd = dateEnd;
        }
    }


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
        DateTime dateEnd = new DateTime(end).plusDays(1).withTimeAtStartOfDay().minusMinutes(1);

        //select those receipts that are in input date interval
        List<Receipt> receipts = receiptInteractor.getReceiptsInInterval(registrationToken, dateStart, dateEnd);

        //get counts and sums for each category
        Map<String, Integer> mapCategoryCount = getCountsMap(receipts, false);
        Map<String, Double> mapCategorySum = getSumsMap(receipts, false);

        Map<String, Integer> mapShopCount = getCountsMap(receipts, true);
        Map<String, Double> mapShopSum = getSumsMap(receipts, true);

        //get them into array
        List<String> titlesCategory = new ArrayList<>(mapCategoryCount.keySet());
        List<Integer> countsCategory = new ArrayList<>(mapCategoryCount.values());
        List<Double> sumsCategory = new ArrayList<>(mapCategorySum.values());

        List<String> titlesShop = new ArrayList<>(mapShopCount.keySet());
        List<Integer> countsShop = new ArrayList<>(mapShopCount.values());
        List<Double> sumsShop = new ArrayList<>(mapShopSum.values());

        int allCountCategory = mapCategoryCount.values().stream().mapToInt(Integer::intValue).sum();
        int allCountShop = mapShopCount.values().stream().mapToInt(Integer::intValue).sum();

        double allSum = mapCategorySum.values().stream().mapToDouble(Double::doubleValue).sum();

        //add them to response
        List<Percentage> categories = getPercentages(titlesCategory, countsCategory, sumsCategory, allCountCategory, allSum);
        List<Percentage> shops = getPercentages(titlesShop, countsShop, sumsShop, allCountShop, allSum);

        StatPercentResponse response = new StatPercentResponse(categories, shops, allSum);

        return new BaseResponse(response);
    }

    private List<Percentage> getPercentages(List<String> titles, List<Integer> counts, List<Double> sums,
                                            int allCount, double allSum) {
        List<Percentage> percentages = new ArrayList<>();

        for (int i = 0; i < titles.size(); i++) {
            Double percentCount = counts.get(i).doubleValue() / allCount;
            Double percentSum = sums.get(i) / allSum;

            Percentage percentage =
                    new Percentage(titles.get(i), percentCount, percentSum, sums.get(i), counts.get(i));
            percentages.add(percentage);
        }

        return percentages;
    }

    private Map<String, Integer> getCountsMap(List<Receipt> receipts, boolean shop) {
        Map<String, Integer> mapCount = new HashMap<>();

        if (shop) {
            for (Receipt receipt : receipts) {
                int count = mapCount.getOrDefault(receipt.getUser(), 0) + 1;
                mapCount.put(receipt.getUser(), count);
            }
        } else {
            for (Receipt receipt : receipts) {
                for (Item item : receipt.getItems()) {
                    int count = mapCount.getOrDefault(item.getCategory(), 0) + 1;
                    mapCount.put(item.getCategory(), count);
                }
            }
        }

        return mapCount;
    }

    private Map<String, Double> getSumsMap(List<Receipt> receipts, boolean shop) {
        Map<String, Double> mapSum = new HashMap<>();

        if (shop) {
            for (Receipt receipt : receipts) {
                double sum = mapSum.getOrDefault(receipt.getUser(), 0.0) + (receipt.getTotalSum() / 100.0);
                mapSum.put(receipt.getUser(), sum);
            }
        } else {
            for (Receipt receipt : receipts) {
                for (Item item : receipt.getItems()) {
                    double sum = mapSum.getOrDefault(item.getCategory(), 0.0) + (item.getSum() / 100.0);
                    mapSum.put(item.getCategory(), sum);
                }
            }
        }

        return mapSum;
    }

}
