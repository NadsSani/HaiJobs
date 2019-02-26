package com.starwings.app.haijobs.responsePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.starwings.app.haijobs.data.ExperienceItem;

import java.util.List;

public class FetchExperienceYears {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("experienceitem")
    @Expose
    private List<ExperienceItem> experienceitem = null;

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

    public List<ExperienceItem> getExperienceitem() {
        return experienceitem;
    }

    public void setExperienceitem(List<ExperienceItem> experienceitem) {
        this.experienceitem = experienceitem;
    }
}
