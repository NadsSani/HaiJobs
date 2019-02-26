package com.starwings.app.haijobs.employerPages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.starwings.app.haijobs.HaiJobApp;
import com.starwings.app.haijobs.HaiJobsActivity;
import com.starwings.app.haijobs.JobApiServices;
import com.starwings.app.haijobs.JobSeekerLogin;
import com.starwings.app.haijobs.JobSeekerRegister;
import com.starwings.app.haijobs.R;
import com.starwings.app.haijobs.api.ApiLinks;
import com.starwings.app.haijobs.components.FancyAlert.Animation;
import com.starwings.app.haijobs.components.FancyAlert.FancyAlertDialog;
import com.starwings.app.haijobs.components.FancyAlert.FancyAlertDialogListener;
import com.starwings.app.haijobs.components.FancyAlert.Icon;
import com.starwings.app.haijobs.data.Employer;
import com.starwings.app.haijobs.data.User;
import com.starwings.app.haijobs.responsePojo.EmployerRegistrationPojo;
import com.starwings.app.haijobs.responsePojo.LoginResponse;
import com.starwings.app.haijobs.utils.Utils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmployerLogin extends HaiJobsActivity {
    Button btnSignup,btnLogin;
    TextInputEditText edLoginid,edPass;
    private static Retrofit retrofit = null;
    JobApiServices jobApiService;
    HaiJobApp haiJobApp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employer_login);

        haiJobApp=(HaiJobApp)getApplication();
        edLoginid=(TextInputEditText)findViewById(R.id.txtLoginID);
        edPass=(TextInputEditText)findViewById(R.id.txtPass);
        btnSignup=(Button)findViewById(R.id.btnsignup) ;
        btnLogin=(Button)findViewById(R.id.btnlogin) ;
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUser();
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegistrationPage();
            }
        });
    }

    private void verifyUser() {

        if(Utils.isEmpty(edLoginid))
        {
            Snackbar.make(btnLogin,"Enter Login ID",Snackbar.LENGTH_SHORT).setActionTextColor(Color.RED).show();

        }
        else if(Utils.isEmpty(edPass))
        {
            Snackbar.make(btnLogin,"Password cannot be Blank",Snackbar.LENGTH_SHORT).setActionTextColor(Color.RED).show();
        }
        else if(!Utils.checkConnectivity(EmployerLogin.this))
        {
            Snackbar.make(btnLogin, "Your device is not connected to internet", Snackbar.LENGTH_SHORT).show();

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
            Call<EmployerRegistrationPojo> call=jobApiService.verifyEmployer( edPass.getText().toString(),edLoginid.getText().toString()
                   );
            call.enqueue(new Callback<EmployerRegistrationPojo>() {
                @Override
                public void onResponse(Call<EmployerRegistrationPojo> call, Response<EmployerRegistrationPojo> response) {

                    if(response.isSuccessful())
                    {
                        if(!response.body().getError())
                        {
                            Employer registeredUser=response.body().getEmployer().get(0);
                            processUserData(registeredUser);
                        }
                        else if(response.body().getError())
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
                public void onFailure(Call<EmployerRegistrationPojo> call, Throwable throwable) {
                    showError("Unknown Error Occured");

                }
            });
        }
    }

    private void processUserData(Employer employer)
    {

        SharedPreferences employerPreference=getSharedPreferences("EMPLOYER_PREF",MODE_PRIVATE);
        SharedPreferences.Editor employerEditor=employerPreference.edit();

        employerEditor.putString("apikey",employer.getApikey());
        employerEditor.putString("employername",employer.getEmployerName());
        employerEditor.putString("addressline1",employer.getAddressLine1());
        employerEditor.putString("addressline2",employer.getAddressLine2());
        employerEditor.putString("pincode",employer.getPincode());
        employerEditor.putString("employerContact",employer.getEmployerContact());
        employerEditor.putString("employermail",employer.getEmployerMail());
        employerEditor.putString("employerWeb",employer.getEmployerWeb());
        employerEditor.putString("employerLogID",employer.getEmpLoginID());
        employerEditor.putString("employerPass",employer.getEmpPassword());
        employerEditor.putString("contactperson",employer.getPersonToContact());
        employerEditor.putString("numbertocontact",employer.getNumberToContact());
        employerEditor.putString("shortdesc",employer.getShortDecription());
        employerEditor.putString("empdigest",employer.getEmpdigest());
        employerEditor.putInt("subscribed",employer.getSubscribed());
        employerEditor.commit();
        haiJobApp.setRegisteredEmployer(employer);

        SharedPreferences userPreferences=getSharedPreferences("USER_PREG",MODE_PRIVATE);
        SharedPreferences.Editor userPrefEdit=userPreferences.edit();
        userPrefEdit.putInt("candidStatus",2);
        userPrefEdit.commit();

        showMessage("Logged in Successfully.");

        callEmployerHome();


    }


    private void showRegistrationPage() {
        Intent employerRegistration=new Intent(this,EmployerRegistration.class);
        startActivity(employerRegistration);
        finish();
    }


    private void callEmployerHome() {

        Intent homePageIntent=new Intent(this,EmployerHome.class);
        startActivity(homePageIntent);
        finish();
    }


}
