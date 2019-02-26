package com.starwings.app.haijobs.employerPages;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.starwings.app.haijobs.HaiJobApp;
import com.starwings.app.haijobs.HaiJobsActivity;
import com.starwings.app.haijobs.JobApiServices;
import com.starwings.app.haijobs.R;
import com.starwings.app.haijobs.adapter.ProfileListingAdapter;
import com.starwings.app.haijobs.api.ApiLinks;
import com.starwings.app.haijobs.components.FancyAlert.Animation;
import com.starwings.app.haijobs.components.FancyAlert.FancyAlertDialog;
import com.starwings.app.haijobs.components.FancyAlert.Icon;
import com.starwings.app.haijobs.data.User;
import com.starwings.app.haijobs.responsePojo.FetchProfilesResponse;
import com.starwings.app.haijobs.responsePojo.StatusChangeResponse;
import com.starwings.app.haijobs.utils.Utils;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfilesListing extends HaiJobsActivity {

    TextView txtinfo;
    RecyclerView recProfiles;
    ProgressBar prgLoading;
    HaiJobApp haiJobApp;
    private static Retrofit retrofit = null;
    JobApiServices jobApiService;
    ProfileListingAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_listing);

        txtinfo=(TextView)findViewById(R.id.txtinfo);
        recProfiles=(RecyclerView)findViewById(R.id.recProfiles);
        prgLoading=(ProgressBar)findViewById(R.id.prgloading);

        txtinfo.setVisibility(View.GONE);
        prgLoading.setVisibility(View.GONE);

        haiJobApp=(HaiJobApp)getApplication();

        fetchProfiles();
    }

    private void fetchProfiles() {

        if(!Utils.checkConnectivity(ProfilesListing.this))
        {
            showError("Your Device is not connected to Internet");
            txtinfo.setText("Failed to Fetch Profiles");
            txtinfo.setVisibility(View.VISIBLE);
            recProfiles.setVisibility(View.GONE);
            return;
        }

        prgLoading.setVisibility(View.VISIBLE);

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

        Call<FetchProfilesResponse> call=jobApiService.fetchProfiles(haiJobApp.getRegisteredEmployer().getApikey());
        call.enqueue(new Callback<FetchProfilesResponse>() {
            @Override
            public void onResponse(Call<FetchProfilesResponse> call, Response<FetchProfilesResponse> response) {

                if(response.isSuccessful())
                {
                        if(!response.body().getError())
                        {
                            processProfiles(response.body().getUser());
                        }
                        else
                        {
                            showError("Failed to Fetch Profiles");
                            prgLoading.setVisibility(View.GONE);
                            txtinfo.setText("Failed to Fetch Profiles");
                            txtinfo.setVisibility(View.VISIBLE);
                            recProfiles.setVisibility(View.GONE);
                        }
                }



            }
            @Override
            public void onFailure(Call<FetchProfilesResponse> call, Throwable throwable) {

                showError("Failed to Fetch Profiles");
                prgLoading.setVisibility(View.GONE);
                txtinfo.setText("Failed to Fetch Profiles");
                txtinfo.setVisibility(View.VISIBLE);
                recProfiles.setVisibility(View.GONE);
            }
        });



    }

    private void processProfiles(List<User> user) {

        prgLoading.setVisibility(View.GONE);
        txtinfo.setVisibility(View.GONE);
        recProfiles.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        recProfiles.setLayoutManager(layoutManager);
        recProfiles.setItemAnimator(new DefaultItemAnimator());
        adapter = new ProfileListingAdapter(user,this);
        recProfiles.setAdapter(adapter);

    }


}
