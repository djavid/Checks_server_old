package com.djavid.checkserver.model.interactor;

import com.djavid.checkserver.model.Api;
import com.djavid.checkserver.model.entity.Item;
import com.djavid.checkserver.model.entity.query.FlaskValues;
import com.djavid.checkserver.model.entity.response.FlaskResponse;
import com.djavid.checkserver.model.repository.ItemRepository;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static io.reactivex.schedulers.Schedulers.io;
import static io.reactivex.schedulers.Schedulers.newThread;

@Repository
public class CategoryInteractor {

    @Autowired
    private Api api;
    @Autowired
    private ItemRepository itemRepository;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        compositeDisposable.dispose();
    }


    private Single<FlaskResponse> getCategories(FlaskValues values) {
        return api.getCategories(values)
                .observeOn(io())
                .subscribeOn(newThread())
                .retry(3);
    }

    public void getAndSaveCategories(List<Item> items) {

        List<String> values = new ArrayList<>();
        items.forEach(it -> values.add(it.getName()));

        Disposable disposable = getCategories(new FlaskValues(values))
                .subscribe(it -> {
                    for (int i = 0; i < items.size(); i++) {
                        items.get(i).setName(it.getNormalized().get(i));
                        items.get(i).setCategory(it.getCategories().get(i));
                        itemRepository.save(items.get(i));
                    }
                }, Throwable::printStackTrace);

        compositeDisposable.add(disposable);
    }

}
