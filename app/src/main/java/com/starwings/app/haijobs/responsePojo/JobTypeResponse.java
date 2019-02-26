package com.starwings.app.haijobs.responsePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.starwings.app.haijobs.data.JobTypeItem;

import java.util.List;

public class JobTypeResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("jobTypeItem")
    @Expose
    private List<JobTypeItem> jobTypeItem = null;

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

    public List<JobTypeItem> getJobTypeItem() {
        return jobTypeItem;
    }

    public void setJobTypeItem(List<JobTypeItem> jobTypeItem) {
        this.jobTypeItem = jobTypeItem;
    }
}
