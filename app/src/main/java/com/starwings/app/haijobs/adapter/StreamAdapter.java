package com.starwings.app.haijobs.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.starwings.app.haijobs.HaiJobApp;
import com.starwings.app.haijobs.R;
import com.starwings.app.haijobs.data.Categories;
import com.starwings.app.haijobs.data.QualificationData;
import com.starwings.app.haijobs.data.SubCategories;
import com.starwings.app.haijobs.fragments.MainQualification;
import com.starwings.app.haijobs.fragments.QualificationStream;

import java.util.ArrayList;

public class StreamAdapter extends RecyclerView.Adapter {
    QualificationData qualificationSelection;
    QualificationStream parentObject;
    QualificationStream.StreamSelectionListener onselectionListener;
    HaiJobApp haiJobApp;
    public StreamAdapter(QualificationData qualificationlist, QualificationStream parent, QualificationStream.StreamSelectionListener selectionlistener,HaiJobApp hjapp)
    {
        this.qualificationSelection =qualificationlist;
        this.parentObject=parent;
        this.onselectionListener=selectionlistener;
        this.haiJobApp=hjapp;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // infalte the item Layout
        View v = LayoutInflater.from(parentObject.getContext()).inflate(R.layout.qualification_list_row, null, false);
        // set the view's size, margins, paddings and layout parameters
        QualificationViewHolder vh = new QualificationViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        QualificationViewHolder vholder=(QualificationViewHolder)viewHolder;
        final int pos=i;
        vholder.textView.setText(qualificationSelection.getSubCategoryList().get(i).getCategoryName());
        vholder.imgView.setVisibility(View.GONE);
        vholder.chkSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    addToSubStreamSelection(qualificationSelection.getSubCategoryList().get(i));
                }
                else
                {
                    removeSelection(qualificationSelection.getSubCategoryList().get(i));
                }
            }
        });
        for(int k=0;k<haiJobApp.getSelectioncatlist().size();k++)
        {
            if(haiJobApp.getSelectioncatlist().get(k).getCategoryID()==qualificationSelection.getSubCategoryList().get(i).getCategoryID())
            {
                vholder.chkSelect.setChecked(true);
                Log.e("HaiJobApp","Entering Here"+haiJobApp.getSelectioncatlist()
                        .get(k).getCategoryName());
                break;
            }

        }




    }
    private void addToSubStreamSelection(SubCategories categoryObject) {

        ArrayList<SubCategories> tempCategories=haiJobApp.getSelectioncatlist();
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

        haiJobApp.setSelectioncatlist(tempCategories);
    }

    private void removeSelection(SubCategories categoryObject) {

        for (int i=0;i<haiJobApp.getSelectioncatlist().size();i++)
        {
            if(haiJobApp.getSelectioncatlist().get(i).getCategoryID()==categoryObject.getCategoryID())
            {
                haiJobApp.getSelectioncatlist().remove(i);
            }

        }
    }

    @Override
    public int getItemCount() {
        return qualificationSelection.getSubCategoryList().size();
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
