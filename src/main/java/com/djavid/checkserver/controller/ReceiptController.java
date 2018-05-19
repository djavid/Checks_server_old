package com.djavid.checkserver.controller;

import com.djavid.checkserver.ChecksApplication;
import com.djavid.checkserver.model.Api;
import com.djavid.checkserver.model.entity.Item;
import com.djavid.checkserver.model.entity.Receipt;
import com.djavid.checkserver.model.entity.RegistrationToken;
import com.djavid.checkserver.model.entity.query.FlaskValues;
import com.djavid.checkserver.model.entity.query.FnsValues;
import com.djavid.checkserver.model.entity.response.BaseResponse;
import com.djavid.checkserver.model.entity.response.CheckResponseFns;
import com.djavid.checkserver.model.entity.response.GetReceiptsResponse;
import com.djavid.checkserver.model.interactor.CategoryInteractor;
import com.djavid.checkserver.model.interactor.ReceiptInteractor;
import com.djavid.checkserver.model.repository.ItemRepository;
import com.djavid.checkserver.model.repository.ReceiptRepository;
import com.djavid.checkserver.model.repository.RegistrationTokenRepository;
import com.djavid.checkserver.service.CheckService;
import com.djavid.checkserver.util.LogoUtil;
import com.djavid.checkserver.util.StringUtil;
import io.reactivex.disposables.Disposable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import retrofit2.http.Body;

import java.util.ArrayList;
import java.util.List;

import static com.djavid.checkserver.util.Config.*;
import static io.reactivex.schedulers.Schedulers.io;
import static io.reactivex.schedulers.Schedulers.newThread;


@RestController
@RequestMapping(value = "receipt")
public class ReceiptController {

    private final ReceiptRepository receiptRepository;
    private final ItemRepository itemRepository;
    private final RegistrationTokenRepository tokenRepository;
    private final ReceiptInteractor receiptInteractor;
    private final Api api;
    private Disposable disposable;

    @Autowired
    private CheckService checkService;
    @Autowired
    private CategoryInteractor categoryInteractor;


    public ReceiptController(ReceiptRepository receiptRepository, ItemRepository itemRepository,
                             RegistrationTokenRepository tokenRepository, Api api, ReceiptInteractor receiptInteractor) {
        this.receiptRepository = receiptRepository;
        this.itemRepository = itemRepository;
        this.tokenRepository = tokenRepository;
        this.receiptInteractor = receiptInteractor;
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
            e.printStackTrace();
            return new BaseResponse("Something gone wrong");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public Receipt getReceiptById(@PathVariable("id") long id) {
        return receiptRepository.findReceiptByReceiptId(id);
    }


    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public BaseResponse postReceipt(@RequestHeader("Token") String token, @RequestBody Receipt receipt) {

        try {
            if (receipt == null)
                return  new BaseResponse("Sent null entity!");

            RegistrationToken registrationToken = tokenRepository.findRegistrationTokenByToken(token);
            if (registrationToken == null)
                return new BaseResponse("Token is incorrect!");

            receipt.setTokenId(registrationToken.getId());
            receipt.setCreated(System.currentTimeMillis());
            receipt.getItems().forEach(it -> it.setReceipt(receipt));
            receipt.setLogo(LogoUtil.getLogo(receipt.getUser()));
            receipt.setUser(StringUtil.formatShopTitle(receipt.getUser()));

            Receipt res = receiptRepository.save(receipt);
            ChecksApplication.log.info("Saved receipt with id " + receipt.getReceiptId());

            List<Item> items = res.getItems();


            List<String> values = new ArrayList<>();
            items.forEach(it -> values.add(it.getName()));
            disposable = api.getCategories(new FlaskValues(values))
                    .observeOn(io())
                    .subscribeOn(newThread())
                    .retry(3)
                    .subscribe(it -> {

                        for (int i = 0; i < items.size(); i++) {
                            items.get(i).setName(it.getNormalized().get(i));
                            items.get(i).setCategory(it.getCategories().get(i));
                            itemRepository.save(items.get(i));
                        }

                    }, Throwable::printStackTrace);

            return new BaseResponse(res);

        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResponse(ERROR_SHIT_HAPPENS);
        }
    }


    @RequestMapping(value = "/post", method = RequestMethod.POST, produces = "application/json")
    public DeferredResult<BaseResponse> postReceiptString(@RequestHeader("Token") String token,
                                                          @Body FnsValues fnsValues) {

        RegistrationToken registrationToken = tokenRepository.findRegistrationTokenByToken(token);
        if (registrationToken == null) {
            DeferredResult<BaseResponse> deferredResult = new DeferredResult<>();
            deferredResult.setErrorResult(new BaseResponse(ERROR_TOKEN_INCORRECT));
            return deferredResult;
        }
        registrationToken.setLastVisited(System.currentTimeMillis());
        tokenRepository.save(registrationToken);

        DeferredResult<BaseResponse> fnsResult = new DeferredResult<>();

        //load check from fns
        if (!receiptRepository.existsByFiscalDriveNumberAndFiscalDocumentNumberAndFiscalSignAndIsEmpty(
                fnsValues.fiscalDriveNumber, fnsValues.fiscalDocumentNumber, fnsValues.fiscalSign, false)) {
            fnsResult = checkService.getReceipt(fnsValues, registrationToken);
        } else {
            fnsResult.setErrorResult(new BaseResponse(ERROR_CHECK_EXISTS));
            return fnsResult;
        }

        //handle check result
        fnsResult.setResultHandler(result -> {
            try {
                //save receipt to db
                CheckResponseFns checkResponse = (CheckResponseFns) ((BaseResponse) result).getResult();
                Receipt receipt = checkResponse.getDocument().getReceipt();
                receipt = receiptInteractor.saveReceipt(receipt, registrationToken);

                //async get from server categories and save them to db
                List<Item> items = receipt.getItems();
                categoryInteractor.getAndSaveCategories(items);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return fnsResult;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        disposable.dispose();
    }
}
