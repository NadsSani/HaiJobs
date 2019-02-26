package com.starwings.app.haijobs.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.starwings.app.haijobs.HaiJobApp;
import com.starwings.app.haijobs.JobApiServices;
import com.starwings.app.haijobs.R;
import com.starwings.app.haijobs.adapter.QualificationAdapter;
import com.starwings.app.haijobs.api.ApiLinks;
import com.starwings.app.haijobs.data.Categories;
import com.starwings.app.haijobs.data.QualificationData;
import com.starwings.app.haijobs.data.SubCategories;
import com.starwings.app.haijobs.responses.CategoryResponse;
import com.starwings.app.haijobs.responses.SubCategoryResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainQualification extends Fragment {
    View view;
    private static Retrofit retrofit = null;
    JobApiServices jobApiService;
    ArrayList<QualificationData> qualificationList;
    HaiJobApp appObject;
    RecyclerView recyclerView;
    QualificationSelectionListener mListener;

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
        }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (QualificationSelectionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }
    private void getQualificationList()
    {

        Call<CategoryResponse> call = jobApiService.getQualificationList();

        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                List<Categories> categories = response.body().getCategories();
                Log.d("HaiJobs", "Number of categories received: " + categories.size());
                processCategories(categories);

            }
            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable throwable) {
                Log.d("HaiJobs", "Error");

            }
        });
    }

    private void processCategories(List<Categories> categories) {
        qualificationList=new ArrayList<QualificationData>();
        for(int i=0;i<categories.size();i++)
        {
            QualificationData temp=new QualificationData(categories.get(i),new ArrayList<SubCategories>());
            qualificationList.add(temp);
        }
        getQualificationStreams();
    }

    private void getQualificationStreams()
    {


        Call<SubCategoryResponse> call = jobApiService.getQualificationStreams();

        call.enqueue(new Callback<SubCategoryResponse>() {
            @Override
            public void onResponse(Call<SubCategoryResponse> call, Response<SubCategoryResponse> response) {
                List<SubCategories> categories = response.body().getCategories();
                Log.d("HaiJobs", "Number of categories received: " + categories.size());
                processStreams(categories);
            }
            @Override
            public void onFailure(Call<SubCategoryResponse> call, Throwable throwable) {
                Log.d("HaiJobs", "Error");

            }
        });
    }

    private void processStreams(List<SubCategories> categories) {
        for(int i=0;i<qualificationList.size();i++) {

            Categories temp=qualificationList.get(i).getCategoryObject();
            for(int j=0;j<categories.size();j++)
            {
                if(temp.getCategoryID()==categories.get(j).getParentID())
                {
                    qualificationList.get(i).getSubCategoryList().add(categories.get(j));
                }
            }

        }
        Log.d("HaiJobs", "Number of categories received: " + qualificationList.size());

        appObject.setQualificationData(qualificationList);
        populateListDisplay(qualificationList);
    }

    private void populateListDisplay(ArrayList<QualificationData> qualificationList) {


// set a LinearLayoutManager with default horizontal orientation and false value for reverseLayout to show the items from start to end
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        HaiJobApp haiJobApp=(HaiJobApp)((AppCompatActivity)getActivity()).getApplication();
        QualificationAdapter qadapter=new QualificationAdapter(qualificationList,getContext(),mListener,haiJobApp);
        recyclerView.setAdapter(qadapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiLinks.basepath)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        jobApiService = retrofit.create(JobApiServices.class);
        appObject=(HaiJobApp)getActivity().getApplication();
        if(appObject.getQualificationData().size()==0)
        {
            getQualificationList();
        }
        else
        {
            qualificationList=appObject.getQualificationData();
            populateListDisplay(qualificationList);
        }


    }

    public interface QualificationSelectionListener
    {
        public void qualificationSelected(QualificationData selection);
    }
}
