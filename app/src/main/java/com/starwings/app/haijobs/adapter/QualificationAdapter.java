package com.starwings.app.haijobs.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.starwings.app.haijobs.HaiJobApp;
import com.starwings.app.haijobs.R;
import com.starwings.app.haijobs.data.Categories;
import com.starwings.app.haijobs.data.QualificationData;
import com.starwings.app.haijobs.fragments.MainQualification;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class QualificationAdapter extends RecyclerView.Adapter {
    ArrayList<QualificationData> qualificationList;
    Context parentObject;
    MainQualification.QualificationSelectionListener onselectionListener;
    HaiJobApp haijobapp;
    public QualificationAdapter(ArrayList<QualificationData> qualificationlist, Context parent, MainQualification.QualificationSelectionListener selectionlistener,HaiJobApp hjobapp)
    {
        this.qualificationList=qualificationlist;
        this.parentObject=parent;
        this.onselectionListener=selectionlistener;
        this.haijobapp=hjobapp;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // infalte the item Layout
        View v = LayoutInflater.from(parentObject).inflate(R.layout.qualification_list_row, null, false);
        // set the view's size, margins, paddings and layout parameters
        QualificationViewHolder vh = new QualificationViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        QualificationViewHolder vholder=(QualificationViewHolder)viewHolder;
        vholder.textView.setText(qualificationList.get(i).getCategoryObject().getCategoryName());
        if(qualificationList.get(i).getCategoryObject().getHasSub()==1)
        {
            vholder.chkSelect.setVisibility(View.GONE);
            vholder.imgView.setVisibility(View.VISIBLE);
            vholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onselectionListener.qualificationSelected(qualificationList.get(i));
                }
            });

        }
        else {
            vholder.chkSelect.setVisibility(View.VISIBLE);
            vholder.imgView.setVisibility(View.GONE);
            vholder.chkSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        addSelectedCategorytoArrayList(qualificationList.get(i).getCategoryObject());

                    }
                    else
                    {
                        removeSelection(qualificationList.get(i).getCategoryObject());
                    }
                }
            });
            for(int k=0;k<haijobapp.getMainselectionList().size();k++)
            {
                if(haijobapp.getMainselectionList().get(k).getCategoryID()==qualificationList.get(i).getCategoryObject().getCategoryID())
                {
                    vholder.chkSelect.setChecked(true);
                    Log.e("HaiJobApp","Entering Here"+haijobapp.getMainselectionList().get(k).getCategoryName());
                    break;
                }

            }
        }
//        final int pos=i;
//        for(int l=0;l<haijobapp.getMainselectionList().size();l++)
//        {
//            if(haijobapp.getMainselectionList().get(l).getCategoryID()==qualificationList.get(i).getCategoryObject().getCategoryID())
//            {
//                vholder.chkSelect.setChecked(true);
//                break;
//            }
//        }

//            vholder.chkSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    //addSelectedCategorytoArrayList(isChecked);
//                    if(isChecked)
//                    {
//
//                        addSelectedCategorytoArrayList(qualificationList.get(i).getCategoryObject());
//                    }
//                }
//            });







    }

    private void addSelectedCategorytoArrayList(Categories categoryObject) {

        ArrayList<Categories> tempCategories=haijobapp.getMainselectionList();
        boolean found=false;
        for(int i=0;i<tempCategories.size();i++)
        {
            if(tempCategories.get(i).getCategoryID()==categoryObject.getCategoryID())
            {
                found=true;
                break;
            }
            else
            {
                found=false;
            }
        }
        if(!found)
        {
            tempCategories.add(categoryObject);
        }

        haijobapp.setMainselectionList(tempCategories);
    }

    private void removeSelection(Categories categoryObject) {

        for (int i=0;i<haijobapp.getMainselectionList().size();i++)
        {
            if(haijobapp.getMainselectionList().get(i).getCategoryID()==categoryObject.getCategoryID())
            {
                haijobapp.getMainselectionList().remove(i);
            }

        }
    }


    @Override
    public int getItemCount() {
        return qualificationList.size();
    }

    public class QualificationViewHolder extends RecyclerView.ViewHolder {
        TextView textView;// init the item view's
        ImageView imgView;
        CheckBox chkSelect;
        public QualificationViewHolder(View itemView) {
            super(itemView);

// get the reference of item view's
            textView = (TextView) itemView.findViewById(R.id.txtcaption);
            imgView = (ImageView) itemView.findViewById(R.id.imgnavigation);
            chkSelect = (CheckBox) itemView.findViewById(R.id.chkselection);
        }
    }
}
