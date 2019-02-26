package com.starwings.app.haijobs.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class District {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("districtname")
    @Expose
    private String districtname;
    @SerializedName("statecode")
    @Expose
    private Integer statecode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDistrictname() {
        return districtname;
    }

    public void setDistrictname(String districtname) {
        this.districtname = districtname;
    }

    public Integer getStatecode() {
        return statecode;
    }

    public void setStatecode(Integer statecode) {
        this.statecode = statecode;
    }

}
