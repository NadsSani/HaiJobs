package com.starwings.app.haijobs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.starwings.app.haijobs.api.ApiLinks;
import com.starwings.app.haijobs.components.FancyAlert.Animation;
import com.starwings.app.haijobs.components.FancyAlert.FancyAlertDialog;
import com.starwings.app.haijobs.components.FancyAlert.Icon;
import com.starwings.app.haijobs.data.Experience;
import com.starwings.app.haijobs.data.User;
import com.starwings.app.haijobs.responsePojo.FetchExperienceResponse;
import com.starwings.app.haijobs.responsePojo.FetchProfilesResponse;
import com.starwings.app.haijobs.responsePojo.LoginResponse;
import com.starwings.app.haijobs.utils.Utils;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JobSeekerLogin extends HaiJobsActivity {

    Button btnSignup,btnlogin,btnreset;
    TextInputEditText txtmobile,txtpassword;
    private static Retrofit retrofit = null;
    JobApiServices jobApiService;
    HaiJobApp haiJobApp;
    TextView txtmessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_seeker_login_layout);
        setTitle("Select Your Qualifications");

        haiJobApp=(HaiJobApp)getApplication();
        btnSignup=(Button)findViewById(R.id.btnsignup);
        btnreset=(Button)findViewById(R.id.btnresetpass);
        btnlogin=(Button)findViewById(R.id.btnlogin);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegistrationPage();
            }
        });

        txtmobile=(TextInputEditText)findViewById(R.id.txtMobile);
        txtpassword=(TextInputEditText)findViewById(R.id.txtlogpass);

        txtmessage=(TextView)findViewById(R.id.txtmessage);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyLogin();
            }
        });
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPage();
            }
        });


    }

    private void resetPage() {

        Intent resetPhoneIntent=new Intent(this,ResetPhoneEntryActivity.class);
        startActivity(resetPhoneIntent);

    }


    private void verifyLogin() {
        if(txtmobile.getText().length()==0)
        {
            Snackbar.make(btnlogin,"Mobile Number Cannot be Blank",Snackbar.LENGTH_SHORT).setActionTextColor(Color.RED).show();

        }
        else if(txtpassword.getText().length()==0)
        {
            Snackbar.make(btnlogin,"Password cannot be Blank",Snackbar.LENGTH_SHORT).setActionTextColor(Color.RED).show();
        }
        else if(!Utils.checkConnectivity(JobSeekerLogin.this))
        {
            Snackbar.make(btnlogin, "Your device is not connected to internet", Snackbar.LENGTH_SHORT).show();

        }
        else
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


            SharedPreferences userPreferences=getSharedPreferences("USER_PREG",MODE_PRIVATE);
            String apikey=userPreferences.getString("apikey","NA");

            jobApiService = retrofit.create(JobApiServices.class);
            Call<LoginResponse> call=jobApiService.checkLogin(txtmobile.getText().toString(),
                    txtpassword.getText().toString(),apikey
                    );
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                    if(response.isSuccessful())
                    {
                        if(response.body().getStatus()==0)
                        {
                            User registeredUser=response.body().getUser().get(0);
                            processUserData(registeredUser);
                        }
                        else if(response.body().getStatus()==1)
                        {
                            showError("Login Failed");
                        }                       
                        else
                        {
                            showError(response.message());
                        }
                    }



                }
                @Override
                public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                    showError("Unknown Error Occured");

                }
            });
        }
    }
    @Override
    protected void showMessage(String message) {

        Snackbar.make(btnlogin,message,Snackbar.LENGTH_SHORT).setActionTextColor(Color.RED).show();



    }



    private void processUserData(User registeredUser) {

        SharedPreferences userPreferences=getSharedPreferences("USER_PREG",MODE_PRIVATE);
        SharedPreferences.Editor prefEdit=userPreferences.edit();
        prefEdit.putString("apikey",registeredUser.getApiKey());
        prefEdit.putString("username",registeredUser.getContactno());
        prefEdit.putString("email",registeredUser.getEmail());
        prefEdit.putString("name",registeredUser.getName());
        prefEdit.putString("dob",registeredUser.getDob());
        prefEdit.putInt("gender",registeredUser.getGender());
        prefEdit.putString("qualification",registeredUser.getQualification());
        prefEdit.putString("streams",registeredUser.getStreams());
        prefEdit.putString("pass",registeredUser.getPassword());
        prefEdit.putString("profile",registeredUser.getProfilepic());
        prefEdit.putString("resumepath",registeredUser.getResumeloc());
        prefEdit.putInt("phverified",registeredUser.getPhonestatus());
        prefEdit.putInt("emverified",registeredUser.getEmailstatus());
        prefEdit.putInt("active",registeredUser.getActive());
        prefEdit.putInt("photostatus",registeredUser.getPhotoStatus());
        prefEdit.putInt("resumestatus",registeredUser.getResumeStatus());
        prefEdit.putInt("experiencelevel",registeredUser.getExperiencelevel());
        prefEdit.putInt("candidStatus",1);
        prefEdit.commit();

        showMessage("Login Successful. Please Wait");

        haiJobApp.setRegisteredUser(registeredUser);

        if(registeredUser.getExperiencelevel()==1)
        {
            checkExperiencedDetails(registeredUser.getApiKey());
        }

        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method

            @Override
            public void run() {

                showJobSeekerHome();
            }
        }, 1*5000); // wait for 5 seconds


    }
    private void checkExperiencedDetails(String apiKey) {

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
        Call<FetchExperienceResponse> call=jobApiService.fetchExperience(apiKey);
        call.enqueue(new Callback<FetchExperienceResponse>() {
            @Override
            public void onResponse(Call<FetchExperienceResponse> call, Response<FetchExperienceResponse> response) {

                if(response.isSuccessful())
                {
                    if(response.body().getCount()!=0)
                    {
                        List<Experience> expDetails=response.body().getExperience();
                        processExperienceData(expDetails);
                    }
                    else if(response.body().getCount()==0)
                    {

                    }
                    else
                    {

                    }
                }



            }
            @Override
            public void onFailure(Call<FetchExperienceResponse> call, Throwable throwable) {


            }
        });
    }

    private void processExperienceData(List<Experience> expDetails) {

        haiJobApp.getExperienceArrayList().clear();
        for(int i=0;i<expDetails.size();i++)
        {
          haiJobApp.getExperienceArrayList().add(expDetails.get(i));
        }


    }

    private void showJobSeekerHome() {

        Intent JobSeekerHome=new Intent(this,JobSeekerHome.class);
        startActivity(JobSeekerHome);
        finish();
    }

    private void showRegistrationPage() {
        SharedPreferences userPreferences=getSharedPreferences("USER_PREG",MODE_PRIVATE);
        if(userPreferences.contains("apikey"))
        {
            if(!userPreferences.getString("apikey","NA").equals("NA"))
            {

               if(userPreferences.getInt("phverified",0)==0)
               {
                   Intent jobSeekerLogin=new Intent(this,RegistrationPhoneEntryActivity.class);
                   startActivity(jobSeekerLogin);
                   finish();
               }
               else if(userPreferences.getInt("resumestatus",0)==0)

               {
                   Intent jobSeekerLogin=new Intent(this,DocumentUpload.class);
                   startActivity(jobSeekerLogin);
                   finish();
               }

               else if(userPreferences.getInt("photostatus",0)==0)

               {
                   Intent jobSeekerLogin=new Intent(this,DocumentUpload.class);
                   startActivity(jobSeekerLogin);
                   finish();
               }
               else
               {
                  showMessage("You have already registered.  Please login with the details");
               }

            }

        }
        else
        {
            Intent jobSeekerLogin=new Intent(this,JobSeekerRegister.class);
            startActivity(jobSeekerLogin);
            finish();
        }

    }
}

