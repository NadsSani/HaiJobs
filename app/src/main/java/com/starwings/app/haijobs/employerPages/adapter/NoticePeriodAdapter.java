package com.starwings.app.haijobs.employerPages.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.starwings.app.haijobs.R;
import com.starwings.app.haijobs.data.District;
import com.starwings.app.haijobs.data.NoticePeriod;

import java.util.List;

public class NoticePeriodAdapter extends BaseAdapter {

    List<NoticePeriod> lstNoticePeriod;
    Context parentObj;
    LayoutInflater inflter;
    public NoticePeriodAdapter(List<NoticePeriod> lstnperiod, Context parent)
    {
        this.lstNoticePeriod=lstnperiod;
        this.parentObj=parent;
        inflter = (LayoutInflater.from(parent));
    }
    @Override
    public int getCount() {
        return lstNoticePeriod.size();
    }

    @Override
    public Object getItem(int position) {
        return lstNoticePeriod.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lstNoticePeriod.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflter.inflate(R.layout.spn_item_row, null);

        TextView names = (TextView) convertView.findViewById(R.id.txtItem);
        names.setText(lstNoticePeriod.get(position).getNoticePeriod());
        return convertView;
    }
}
