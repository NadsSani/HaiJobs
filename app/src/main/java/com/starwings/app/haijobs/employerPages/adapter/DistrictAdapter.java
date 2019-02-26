package com.starwings.app.haijobs.employerPages.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.starwings.app.haijobs.R;
import com.starwings.app.haijobs.data.District;
import com.starwings.app.haijobs.data.State;

import java.util.List;

public class DistrictAdapter extends BaseAdapter {

    List<District> districts;
    Context parentObj;
    LayoutInflater inflter;
    public DistrictAdapter(List<District> lstdistrict, Context parent)
    {
        this.districts=lstdistrict;
        this.parentObj=parent;
        inflter = (LayoutInflater.from(parent));
    }
    @Override
    public int getCount() {
        return districts.size();
    }

    @Override
    public Object getItem(int position) {
        return districts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return districts.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflter.inflate(R.layout.spn_item_row, null);

        TextView names = (TextView) convertView.findViewById(R.id.txtItem);
        names.setText(districts.get(position).getDistrictname());
        return convertView;
    }
}
