package com.starwings.app.haijobs.employerPages.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.starwings.app.haijobs.R;
import com.starwings.app.haijobs.data.District;
import com.starwings.app.haijobs.data.JobTypeItem;

import java.util.List;

public class JobNatureAdapter extends BaseAdapter {

    List<JobTypeItem> jobTypeList;
    Context parentObj;
    LayoutInflater inflter;
    public JobNatureAdapter(List<JobTypeItem> lstJobItem, Context parent)
    {
        this.jobTypeList=lstJobItem;
        this.parentObj=parent;
        inflter = (LayoutInflater.from(parent));
    }
    @Override
    public int getCount() {
        return jobTypeList.size();
    }

    @Override
    public Object getItem(int position) {
        return jobTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return jobTypeList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflter.inflate(R.layout.spn_item_row, null);

        TextView names = (TextView) convertView.findViewById(R.id.txtItem);
        names.setText(jobTypeList.get(position).getJobType());
        return convertView;
    }
}
