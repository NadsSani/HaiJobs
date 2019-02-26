package com.starwings.app.haijobs.responsePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.starwings.app.haijobs.data.JobRoles;

import java.util.List;

public class FetchJobRoleResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("jobcategories")
    @Expose
    private List<JobRoles> jobcategories = null;

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

    public List<JobRoles> getJobcategories() {
        return jobcategories;
    }

    public void setJobcategories(List<JobRoles> jobcategories) {
        this.jobcategories = jobcategories;
    }
}
