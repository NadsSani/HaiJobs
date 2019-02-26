package com.starwings.app.haijobs.responses;

import com.google.gson.annotations.SerializedName;
import com.starwings.app.haijobs.data.Categories;

import java.util.List;

public class CategoryResponse {
    @SerializedName("error")
    private
    boolean error;
    @SerializedName("qualification")
    private
    List<Categories> categories;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Categories> getCategories() {
        return categories;
    }

    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }
}
