package com.starwings.app.haijobs.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JobRoles {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("categoryOfJob")
    @Expose
    private String categoryOfJob;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryOfJob() {
        return categoryOfJob;
    }

    public void setCategoryOfJob(String categoryOfJob) {
        this.categoryOfJob = categoryOfJob;
    }
}
