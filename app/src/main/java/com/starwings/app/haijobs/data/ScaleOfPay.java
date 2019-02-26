package com.starwings.app.haijobs.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScaleOfPay {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("scaleofpayitem")
    @Expose
    private String scaleofpayitem;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScaleofpayitem() {
        return scaleofpayitem;
    }

    public void setScaleofpayitem(String scaleofpayitem) {
        this.scaleofpayitem = scaleofpayitem;
    }
}
