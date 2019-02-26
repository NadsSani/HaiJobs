package com.starwings.app.haijobs.employerPages.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.starwings.app.haijobs.R;
import com.starwings.app.haijobs.data.JobRoles;
import com.starwings.app.haijobs.data.State;

import java.util.List;

public class StatesAdapter extends BaseAdapter {

    List<State> states;
    Context parentObj;
    LayoutInflater inflter;
    public StatesAdapter(List<State> lststates, Context parent)
    {
        this.states=lststates;
        this.parentObj=parent;
        inflter = (LayoutInflater.from(parent));
    }
    @Override
    public int getCount() {
        return states.size();
    }

    @Override
    public Object getItem(int position) {
        return states.get(position);
    }

    @Override
    public long getItemId(int position) {
        return states.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflter.inflate(R.layout.spn_item_row, null);

        TextView names = (TextView) convertView.findViewById(R.id.txtItem);
        names.setText(states.get(position).getStatename());
        return convertView;
    }
}
