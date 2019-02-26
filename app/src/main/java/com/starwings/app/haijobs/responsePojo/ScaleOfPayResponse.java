package com.starwings.app.haijobs.responsePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.starwings.app.haijobs.data.ScaleOfPay;

import java.util.List;

public class ScaleOfPayResponse {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("scaleOfPay")
    @Expose
    private List<ScaleOfPay> scaleOfPay = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ScaleOfPay> getScaleOfPay() {
        return scaleOfPay;
    }

    public void setScaleOfPay(List<ScaleOfPay> scaleOfPay) {
        this.scaleOfPay = scaleOfPay;
    }
}
