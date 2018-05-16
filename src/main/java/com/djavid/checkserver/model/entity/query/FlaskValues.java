package com.djavid.checkserver.model.entity.query;

import java.util.List;

public class FlaskValues {

    private List<String> values;


    public FlaskValues(List<String> values) {
        this.values = values;
    }


    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
