package com.starwings.app.haijobs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.starwings.app.haijobs.api.ApiLinks;
import com.starwings.app.haijobs.data.Categories;
import com.starwings.app.haijobs.data.District;
import com.starwings.app.haijobs.data.Employer;
import com.starwings.app.haijobs.data.QualificationData;
import com.starwings.app.haijobs.data.State;
import com.starwings.app.haijobs.data.SubCategories;
import com.starwings.app.haijobs.data.User;
import com.starwings.app.haijobs.employerPages.EmployerHome;
import com.starwings.app.haijobs.employerPages.adapter.DistrictAdapter;
import com.starwings.app.haijobs.fragments.MainQualification;
import com.starwings.app.haijobs.responsePojo.DistrictsResponse;
import com.starwings.app.haijobs.responsePojo.FetchStatesResponse;
import com.starwings.app.haijobs.responses.CategoryResponse;
import com.starwings.app.haijobs.responses.SubCategoryResponse;
import com.starwings.app.haijobs.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppSplashPage extends HaiJobsActivity {
    HaiJobApp haiJobApp;
    View view;
    private static Retrofit retrofit = null;
    JobApiServices jobApiService;
    ArrayList<QualificationData> qualificationList;
    HaiJobApp appObject;
    RecyclerView recyclerView;
    ProgressBar pbProcessing;
    MainQualification.QualificationSelectionListener mListener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        appObject=(HaiJobApp)getApplication();

        pbProcessing=(ProgressBar)findViewById(R.id.pbProcessing);
        pbProcessing.setVisibility(View.INVISIBLE);

        getQualificationList();




        //loadProgress=(ProgressBar)findViewById(R.id.progressBar);


       // txtcright=(TextView)findViewById(R.id.txtcright);


//        SmartSerch appobj=(SmartSerch)getApplication();
//        txtcright.setTypeface(appobj.getLatobold());
    }
    private void fetchDistricts()
    {


        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiLinks.basepath)
                    .client(clientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        jobApiService = retrofit.create(JobApiServices.class);
        Call<DistrictsResponse> call=jobApiService.fetchDistricts();
        call.enqueue(new Callback<DistrictsResponse>() {
            @Override
            public void onResponse(Call<DistrictsResponse> call, Response<DistrictsResponse> response) {

                if(response.isSuccessful())
                {
                    if(!response.body().getError())
                    {
                        List<District> lstDistricts=response.body().getDistricts();
                        processDistricts(lstDistricts);
                    }

                }



            }
            @Override
            public void onFailure(Call<DistrictsResponse> call, Throwable throwable) {

                Snackbar.make(pbProcessing,R.string.unkonwnError,Snackbar.LENGTH_SHORT).show();
                pbProcessing.setVisibility(View.GONE);
            }
        });


    }
    private void processDistricts(List<District> lstDistricts)
    {
        appObject.setDistrictsList(lstDistricts);
        pbProcessing.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method

            @Override
            public void run() {

                homePage();
            }
        }, 2*1000); // wait for 5 seconds

    }
    private void fetchStates() {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiLinks.basepath)
                    .client(clientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        jobApiService = retrofit.create(JobApiServices.class);
        Call<FetchStatesResponse> call=jobApiService.fetchStates();
        call.enqueue(new Callback<FetchStatesResponse>() {
            @Override
            public void onResponse(Call<FetchStatesResponse> call, Response<FetchStatesResponse> response) {

                if(response.isSuccessful())
                {
                    if(!response.body().getError())
                    {
                        List<State> lstStates=response.body().getStates();
                        processStates(lstStates);
                    }

                }



            }
            @Override
            public void onFailure(Call<FetchStatesResponse> call, Throwable throwable) {

                Snackbar.make(pbProcessing,R.string.unkonwnError,Snackbar.LENGTH_SHORT).show();
                pbProcessing.setVisibility(View.GONE);
            }
        });

    }

    private void processStates(List<State> lstStates) {

        appObject.setStatesList(lstStates);
        fetchDistricts();

    }
    private void getQualificationList()
    {
        if(!Utils.checkConnectivity(AppSplashPage.this))
        {
            Snackbar.make(pbProcessing,R.string.noInternet,Snackbar.LENGTH_SHORT).show();
            return;
        }
        pbProcessing.setVisibility(View.VISIBLE);
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiLinks.basepath)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        jobApiService = retrofit.create(JobApiServices.class);

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
                Snackbar.make(pbProcessing,R.string.unkonwnError,Snackbar.LENGTH_SHORT).show();
                pbProcessing.setVisibility(View.GONE);

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
                Snackbar.make(pbProcessing,R.string.unkonwnError,Snackbar.LENGTH_SHORT).show();
                pbProcessing.setVisibility(View.GONE);

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

        fetchStates();

    }
    private void homePage() {

        SharedPreferences userPreferences=getSharedPreferences("USER_PREG",MODE_PRIVATE);
        int userRole=userPreferences.getInt("candidStatus",0);
        if(userRole==1)
        {
            User currentUser=new User();

            currentUser.setApiKey(userPreferences.getString("apikey","NA"));
            currentUser.setContactno(userPreferences.getString("username","NA"));
            currentUser.setEmail(userPreferences.getString("email","NA"));
            currentUser.setName(userPreferences.getString("name","NA"));
            currentUser.setDob(userPreferences.getString("dob","NA"));
            currentUser.setGender(userPreferences.getInt("gender",-1));
            currentUser.setQualification(userPreferences.getString("qualification","NA"));
            currentUser.setStreams(userPreferences.getString("streams","NA"));
            currentUser.setPassword(userPreferences.getString("pass","NA"));
            currentUser.setProfilepic(userPreferences.getString("profile","NA"));
            currentUser.setResumeloc(userPreferences.getString("resumepath","NA"));
            currentUser.setPhonestatus(userPreferences.getInt("phverified",0));
            currentUser.setEmailstatus(userPreferences.getInt("emverified",0));
            currentUser.setActive(userPreferences.getInt("active",0));
            currentUser.setPhotoStatus(userPreferences.getInt("photostatus",0));
            currentUser.setResumeStatus(userPreferences.getInt("resumestatus",0));
            currentUser.setExperiencelevel(userPreferences.getInt("experiencelevel",0));
            currentUser.setPlace(userPreferences.getString("place","NA"));
            currentUser.setDistrict(userPreferences.getInt("district",1));
            currentUser.setState(userPreferences.getInt("state",1));
            currentUser.setPincode(userPreferences.getString("pincode","NA"));




            haiJobApp=(HaiJobApp)getApplication();
            haiJobApp.setRegisteredUser(currentUser);
            Intent homePageIntent=new Intent(this,JobSeekerHome.class);
            startActivity(homePageIntent);
        }
        else if(userRole==2)
        {
            SharedPreferences employerPreference=getSharedPreferences("EMPLOYER_PREF",MODE_PRIVATE);
            Employer registeredEmployer=new Employer();

            registeredEmployer.setApikey(employerPreference.getString("apikey","NA"));
            registeredEmployer.setEmployerName(employerPreference.getString("employername","NA"));
            registeredEmployer.setAddressLine1(employerPreference.getString("addressline1","NA"));
            registeredEmployer.setAddressLine2(employerPreference.getString("addressline2","NA"));
            registeredEmployer.setPincode(employerPreference.getString("pincode","NA"));
            registeredEmployer.setEmployerContact(employerPreference.getString("employerContact","NA"));
            registeredEmployer.setEmployerMail(employerPreference.getString("employermail","NA"));
            registeredEmployer.setEmployerWeb(employerPreference.getString("employerWeb","NA"));
            registeredEmployer.setEmpLoginID(employerPreference.getString("employerLogID","NA"));
            registeredEmployer.setEmpPassword(employerPreference.getString("employerPass","NA"));
            registeredEmployer.setPersonToContact(employerPreference.getString("contactperson","NA"));
            registeredEmployer.setNumberToContact(employerPreference.getString("numbertocontact","NA"));
            registeredEmployer.setShortDecription(employerPreference.getString("shortdesc","NA"));
            registeredEmployer.setEmpdigest(employerPreference.getString("empdigest","NA"));
            registeredEmployer.setSubscribed(employerPreference.getInt("subscribed",0));
            haiJobApp=(HaiJobApp)getApplication();
            haiJobApp.setRegisteredEmployer(registeredEmployer);
            Intent homePageIntent=new Intent(this,EmployerHome.class);
            startActivity(homePageIntent);
        }
        else
        {
            Intent homePageIntent=new Intent(this,AppHomePage.class);
            startActivity(homePageIntent);
        }


        finish();
    }
}
