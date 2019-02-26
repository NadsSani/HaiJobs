package com.starwings.app.haijobs.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.starwings.app.haijobs.HaiJobApp;
import com.starwings.app.haijobs.QualificationDialog;
import com.starwings.app.haijobs.R;
import com.starwings.app.haijobs.adapter.StreamAdapter;
import com.starwings.app.haijobs.data.QualificationData;
import com.starwings.app.haijobs.data.SubCategories;

import java.util.ArrayList;

public class QualificationStream extends Fragment {
    View view;
    HaiJobApp appObject;
    RecyclerView recyclerView;
    StreamSelectionListener mListener;
    QualificationData selected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dlg_qualification, null, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView)view.findViewById(R.id.qualification_list);

        selected=(QualificationData)getArguments().getSerializable("mainselection");
        appObject=(HaiJobApp)getActivity().getApplication();
        ((QualificationDialog) ((AppCompatActivity)getActivity()))
                .setTitle(selected.getCategoryObject().getCategoryName());
        populateListDisplay(selected);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (QualificationStream.StreamSelectionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }


    }






    private void populateListDisplay(QualificationData qualificationSelection) {


// set a LinearLayoutManager with default horizontal orientation and false value for reverseLayout to show the items from start to end
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        HaiJobApp haiJobApp=(HaiJobApp)((AppCompatActivity)getActivity()).getApplication();
        StreamAdapter qadapter=new StreamAdapter(qualificationSelection,this,mListener,haiJobApp);
        recyclerView.setAdapter(qadapter);
    }



    public interface StreamSelectionListener
    {
        public void streamSelected(QualificationData selection,SubCategories subselection);
    }
}
