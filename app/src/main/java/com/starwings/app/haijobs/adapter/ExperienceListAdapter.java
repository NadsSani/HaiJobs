package com.starwings.app.haijobs.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.starwings.app.haijobs.ProfileExperienceInfo;
import com.starwings.app.haijobs.R;
import com.starwings.app.haijobs.data.Experience;

import java.util.ArrayList;

public class ExperienceListAdapter extends RecyclerView.Adapter {

    private ArrayList<Experience> experienceArrayList;
    ProfileExperienceInfo parentObject;

    public ExperienceListAdapter(ArrayList<Experience> data,ProfileExperienceInfo parent) {
        this.setExperienceArrayList(data);
        this.parentObject=parent;
    }
    @NonNull
    @Override
    public  ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(parentObject).inflate(R.layout.experience_list_row, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        // set the view's size, margins, paddings and layout parameters
        ExperienceListAdapter.ItemHolder vh = new ExperienceListAdapter.ItemHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ItemHolder vholder=(ItemHolder)viewHolder;
        vholder.txtposition.setText(getExperienceArrayList().get(i).getJobPosition());
        vholder.txtcompany.setText(getExperienceArrayList().get(i).getNameOfOrganisation());
        vholder.txtfromto.setText(getExperienceArrayList().get(i).getWorkedFrom()+" - "+ getExperienceArrayList().get(i).getWorkedTo());
        vholder.imgedit.setVisibility(View.GONE);

    }




    @Override
    public int getItemCount() {
        return getExperienceArrayList().size();
    }

    public ArrayList<Experience> getExperienceArrayList() {
        return experienceArrayList;
    }

    public void setExperienceArrayList(ArrayList<Experience> experienceArrayList) {
        this.experienceArrayList = experienceArrayList;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {

        TextView txtposition,txtcompany,txtfromto;
        ImageButton imgedit;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            this.txtposition = (TextView) itemView.findViewById(R.id.txtJobTitle);
            this.txtcompany = (TextView) itemView.findViewById(R.id.txtCompanyName);
            this.txtfromto = (TextView) itemView.findViewById(R.id.txtFromTo);
            this.imgedit = (ImageButton) itemView.findViewById(R.id.imgEdit);
        }
    }
}
