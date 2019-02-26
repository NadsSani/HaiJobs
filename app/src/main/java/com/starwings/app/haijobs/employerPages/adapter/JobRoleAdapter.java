package com.starwings.app.haijobs.employerPages.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.starwings.app.haijobs.R;
import com.starwings.app.haijobs.data.JobRoles;

import java.util.List;

public class JobRoleAdapter extends BaseAdapter {

    List<JobRoles> jobRoles;
    Context parentObj;
    LayoutInflater inflter;
    public JobRoleAdapter( List<JobRoles> jobRolesList,Context parent)
    {
        this.jobRoles=jobRolesList;
        this.parentObj=parent;
        inflter = (LayoutInflater.from(parent));
    }
    @Override
    public int getCount() {
        return jobRoles.size();
    }

    @Override
    public Object getItem(int position) {
        return jobRoles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return jobRoles.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflter.inflate(R.layout.spn_item_row, null);

        TextView names = (TextView) convertView.findViewById(R.id.txtItem);
        names.setText(jobRoles.get(position).getCategoryOfJob());
        return convertView;
    }
}
