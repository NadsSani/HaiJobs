package com.starwings.app.haijobs.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Experience implements Serializable {
    @SerializedName("slno")
    @Expose
    private Integer slno;
    @SerializedName("name_of_organisation")
    @Expose
    private String nameOfOrganisation;
    @SerializedName("worked_from")
    @Expose
    private String workedFrom;
    @SerializedName("worked_to")
    @Expose
    private String workedTo;
    @SerializedName("job_position")
    @Expose
    private String jobPosition;
    @SerializedName("currentfirm")
    @Expose
    private Integer currentfirm;
    @SerializedName("apikey")
    @Expose
    private String apikey;

    public Integer getSlno() {
        return slno;
    }

    public void setSlno(Integer slno) {
        this.slno = slno;
    }

    public String getNameOfOrganisation() {
        return nameOfOrganisation;
    }

    public void setNameOfOrganisation(String nameOfOrganisation) {
        this.nameOfOrganisation = nameOfOrganisation;
    }

    public String getWorkedFrom() {
        return workedFrom;
    }

    public void setWorkedFrom(String workedFrom) {
        this.workedFrom = workedFrom;
    }

    public String getWorkedTo() {
        return workedTo;
    }

    public void setWorkedTo(String workedTo) {
        this.workedTo = workedTo;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public Integer getCurrentfirm() {
        return currentfirm;
    }

    public void setCurrentfirm(Integer currentfirm) {
        this.currentfirm = currentfirm;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }
}