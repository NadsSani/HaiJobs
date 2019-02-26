package com.starwings.app.haijobs.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubCategories implements Serializable,Parcelable {
    @SerializedName("id")
    private
    int categoryID;

    @SerializedName("cname")
    private
    String categoryName;

    @SerializedName("parent")
    private
    int parentID;

    public SubCategories(int categoryid,String categoryname,int parentid)
    {
        this.setCategoryID(categoryid);
        this.setCategoryName(categoryname);
        this.setParentID(parentid);
    }

    public SubCategories(Parcel in){
        this.setCategoryID(in.readInt());
        this.setCategoryName(in.readString());
        this.setParentID(in.readInt());
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

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.categoryID);
        dest.writeString(this.categoryName);
        dest.writeInt(this.parentID);
    }
    public static final Parcelable.Creator<SubCategories> CREATOR = new Parcelable.Creator<SubCategories>() {
        public SubCategories createFromParcel(Parcel in) {
            return new SubCategories(in);
        }

        public SubCategories[] newArray(int size) {
            return new SubCategories[size];
        }
    };
}
