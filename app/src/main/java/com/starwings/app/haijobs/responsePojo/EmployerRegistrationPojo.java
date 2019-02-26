package com.starwings.app.haijobs.responsePojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.starwings.app.haijobs.data.Employer;

import java.util.List;

public class EmployerRegistrationPojo {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("employer")
    @Expose
    private List<Employer> employer = null;

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

    public List<Employer> getEmployer() {
        return employer;
    }

    public void setEmployer(List<Employer> employer) {
        this.employer = employer;
    }

}