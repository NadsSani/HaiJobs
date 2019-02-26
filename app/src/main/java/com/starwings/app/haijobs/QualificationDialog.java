package com.starwings.app.haijobs;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.starwings.app.haijobs.adapter.QualificationAdapter;
import com.starwings.app.haijobs.api.ApiLinks;
import com.starwings.app.haijobs.components.FancyAlert.Animation;
import com.starwings.app.haijobs.components.FancyAlert.FancyAlertDialog;
import com.starwings.app.haijobs.components.FancyAlert.Icon;
import com.starwings.app.haijobs.data.Categories;
import com.starwings.app.haijobs.data.QualificationData;
import com.starwings.app.haijobs.data.SubCategories;
import com.starwings.app.haijobs.fragments.MainQualification;
import com.starwings.app.haijobs.fragments.QualificationStream;
import com.starwings.app.haijobs.responses.CategoryResponse;
import com.starwings.app.haijobs.responses.SubCategoryResponse;
import com.starwings.app.haijobs.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QualificationDialog extends HaiJobsActivity implements MainQualification.QualificationSelectionListener,QualificationStream.StreamSelectionListener {
    QualificationStream fragment;
    MainQualification mfragment;
    Button btnFinish;
    HaiJobApp haiJobApp;
    Button btnback;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Specify Your Qualification");
        setContentView(R.layout.qualification_dialog_wrapper);
        haiJobApp=(HaiJobApp)getApplication();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        btnback=(Button)findViewById(R.id.edBackButton);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                    Intent intent=new Intent();
                    setResult(1001,intent);
                    finish();//finishing activity


            }
        });

        if(Utils.checkConnectivity(QualificationDialog.this))
        {
            mfragment = new MainQualification();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, mfragment);
            fragmentTransaction.commit(); // save the changes
        }
        else
        {
            new FancyAlertDialog.Builder(this)

                    .setTitle("HaiJobs")
                    .setBackgroundColor(Color.parseColor("#19a8ff"))  //Don't pass R.color.colorvalue
                    .setMessage("Your Device is not connected with the net")
                    .setPositiveBtnBackground(Color.parseColor("#FF4081"))  //Don't pass R.color.colorvalue
                    .setPositiveBtnText("DISMISS")
                    .setAnimation(Animation.POP)
                    .isCancellable(false)
                    .setIcon(R.drawable.ic_info,Icon.Visible)
                    .build();

        }



        btnFinish=(Button)findViewById(R.id.btnupdateqqualification);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mainselection=haiJobApp.getMainselectionList().size();
                int streamselection=haiJobApp.getSelectioncatlist().size();

                    Intent intent=new Intent();
                    setResult(1001,intent);
                    finish();//finishing activity


            }
        });

    }


    @Override
    public void qualificationSelected(QualificationData selection) {

        FragmentManager fm = getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragment=new QualificationStream();
        Bundle params=new Bundle();
        params.putSerializable("mainselection",selection);
        fragment.setArguments(params);
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack("Main");
        fragmentTransaction.commit(); // save the changes
    }

    @Override
    public void streamSelected(QualificationData selection,SubCategories subcat) {

    }


    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
