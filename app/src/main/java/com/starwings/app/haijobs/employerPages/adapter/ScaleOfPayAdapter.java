package com.starwings.app.haijobs.employerPages.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.starwings.app.haijobs.R;
import com.starwings.app.haijobs.data.District;
import com.starwings.app.haijobs.data.ScaleOfPay;

import java.util.List;

public class ScaleOfPayAdapter extends BaseAdapter {

    List<ScaleOfPay> scaleOfPays;
    Context parentObj;
    LayoutInflater inflter;
    public ScaleOfPayAdapter(List<ScaleOfPay> scaleofpay, Context parent)
    {
        this.scaleOfPays=scaleofpay;
        this.parentObj=parent;
        inflter = (LayoutInflater.from(parent));
    }
    @Override
    public int getCount() {
        return scaleOfPays.size();
    }

    @Override
    public Object getItem(int position) {
        return scaleOfPays.get(position);
    }

    @Override
    public long getItemId(int position) {
        return scaleOfPays.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflter.inflate(R.layout.spn_item_row, null);

        TextView names = (TextView) convertView.findViewById(R.id.txtItem);
        names.setText(scaleOfPays.get(position).getScaleofpayitem());
        return convertView;
    }
}
