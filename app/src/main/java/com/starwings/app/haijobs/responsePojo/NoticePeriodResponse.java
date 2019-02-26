package com.starwings.app.haijobs.responsePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.starwings.app.haijobs.data.NoticePeriod;

import java.util.List;

public class NoticePeriodResponse {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("noticePeriods")
    @Expose
    private List<NoticePeriod> noticePeriods = null;

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

    public List<NoticePeriod> getNoticePeriods() {
        return noticePeriods;
    }

    public void setNoticePeriods(List<NoticePeriod> noticePeriods) {
        this.noticePeriods = noticePeriods;
    }
}
