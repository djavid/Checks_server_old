package com.djavid.checkserver.model.entity.response;

import java.util.List;

public class FlaskResponse {

    private List<String> categories;
    private List<String> normalized;


    public FlaskResponse(List<String> categories, List<String> normalized) {
        this.categories = categories;
        this.normalized = normalized;
    }


    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getNormalized() {
        return normalized;
    }

    public void setNormalized(List<String> normalized) {
        this.normalized = normalized;
    }
}
