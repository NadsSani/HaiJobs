package com.starwings.app.haijobs.responses;


import com.google.gson.annotations.SerializedName;
import com.starwings.app.haijobs.data.Categories;
import com.starwings.app.haijobs.data.SubCategories;

import java.util.List;

public class SubCategoryResponse {
    @SerializedName("error")
    private
    boolean error;
    @SerializedName("spqualification")
    private
    List<SubCategories> categories;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<SubCategories> getCategories() {
        return categories;
    }

    public void setCategories(List<SubCategories> categories) {
        this.categories = categories;
    }
}
