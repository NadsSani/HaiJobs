package com.starwings.app.haijobs.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NoticePeriod {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("noticePeriod")
    @Expose
    private String noticePeriod;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNoticePeriod() {
        return noticePeriod;
    }

    public void setNoticePeriod(String noticePeriod) {
        this.noticePeriod = noticePeriod;
    }
}
