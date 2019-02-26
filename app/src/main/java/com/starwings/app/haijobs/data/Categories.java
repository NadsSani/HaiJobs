package com.starwings.app.haijobs.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Categories implements Serializable {
    @SerializedName("id")
    private  int categoryID;

    @SerializedName("cname")
    private  String categoryName;

    @SerializedName("hassub")
    private int hasSub;

    public Categories(int categoryid,String categoryname,int hassub)
    {
        this.setCategoryID(categoryid);
        this.setCategoryName(categoryname);
        this.setHasSub(hassub);
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public int getHasSub() {
        return hasSub;
    }

    public void setHasSub(int hasSub) {
        this.hasSub = hasSub;
    }


}
