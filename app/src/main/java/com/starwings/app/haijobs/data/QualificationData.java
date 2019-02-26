package com.starwings.app.haijobs.data;

import java.io.Serializable;
import java.util.ArrayList;

public class QualificationData implements Serializable {
    private Categories categoryObject;
    private ArrayList<SubCategories> subCategoryList;

    public QualificationData(Categories catobject,ArrayList<SubCategories> subcatlist)
    {
        this.categoryObject=catobject;
        this.subCategoryList=subcatlist;
    }

    public Categories getCategoryObject() {
        return categoryObject;
    }

    public void setCategoryObject(Categories categoryObject) {
        this.categoryObject = categoryObject;
    }

    public ArrayList<SubCategories> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(ArrayList<SubCategories> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }
}
