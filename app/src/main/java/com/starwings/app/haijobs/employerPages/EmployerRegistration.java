package com.starwings.app.haijobs.employerPages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.starwings.app.haijobs.HaiJobApp;
import com.starwings.app.haijobs.HaiJobsActivity;
import com.starwings.app.haijobs.JobApiServices;
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
import okhttp3.internal.Util;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmployerRegistration extends HaiJobsActivity {
    TextInputEditText edName,edAddress1,edAddress2,edPin,edContact,edEmail,edWeb,edContactPerson,edContactNo,edShortDes,edPass;
    TextInputLayout tnplname,tnpladdress,tnplcontact,tnplcperson,tnplcpersonno,tnplpass;
    Button btnSave;
    private static Retrofit retrofit = null;
    JobApiServices jobApiService;
    HaiJobApp haiJobApp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employer_registration);

        haiJobApp=(HaiJobApp)getApplication();

        tnplname=(TextInputLayout)findViewById(R.id.tinpl_empname);
        tnpladdress=(TextInputLayout)findViewById(R.id.tinpl_empaddress);
        tnplcontact=(TextInputLayout)findViewById(R.id.tinpl_officeno);
        tnplcperson=(TextInputLayout)findViewById(R.id.tinpl_cperson);
        tnplcpersonno=(TextInputLayout)findViewById(R.id.tinpl_cperson_no);
        tnplpass=(TextInputLayout)findViewById(R.id.tinpl_log_pass);


        edName=(TextInputEditText)findViewById(R.id.ed_org_name);
        edAddress1=(TextInputEditText)findViewById(R.id.txtAddressLineOne);
        edAddress2=(TextInputEditText)findViewById(R.id.txtAddressLineTwo);
        edPin=(TextInputEditText)findViewById(R.id.txtPincode);
        edContact=(TextInputEditText)findViewById(R.id.txtContact);
        edEmail=(TextInputEditText)findViewById(R.id.txtMail);
        edWeb=(TextInputEditText)findViewById(R.id.txtWeb);
        edContactPerson=(TextInputEditText)findViewById(R.id.txtContactPerson);
        edContactNo=(TextInputEditText)findViewById(R.id.txtCPersonNumber);
        edShortDes=(TextInputEditText)findViewById(R.id.txtDescription);
        edPass=(TextInputEditText)findViewById(R.id.txtPassword);

        btnSave=(Button)findViewById(R.id.registerbtn) ;

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerEmployer();
            }
        });
    }

    private void registerEmployer() {

        boolean status=true;
        String address2="",pincode="",email="",website="",shortdesc="";

        if(Utils.isEmpty(edName))
        {
            Snackbar.make(btnSave,"You must enter name of organization",Snackbar.LENGTH_SHORT).show();
            tnplname.setError("You must enter name of organization");
            status=false;
        }
        else if(Utils.isEmpty(edAddress1))
        {
            Snackbar.make(btnSave,"Enter Contact Address",Snackbar.LENGTH_SHORT).show();
            tnpladdress.setError("Enter Contact Address");
            status=false;
        }
        else if(Utils.isEmpty(edContact))
        {
            Snackbar.make(btnSave,"Enter Your Office Number",Snackbar.LENGTH_SHORT).show();
            tnplcontact.setError("Enter Your Office Number");
            status=false;
        }
        else if(Utils.isEmpty(edContactPerson))
        {
            Snackbar.make(btnSave,"Enter the name of person to contact",Snackbar.LENGTH_SHORT).show();
            tnplcperson.setError("Enter the name of person to contact");
            status=false;
        }
        else if(Utils.isEmpty(edContactNo))
        {
            Snackbar.make(btnSave,"Enter the contact number",Snackbar.LENGTH_SHORT).show();
            tnplcpersonno.setError("Enter the contact number");
            status=false;
        }

        else if(Utils.isEmpty(edPass))
        {
            Snackbar.make(btnSave,"Enter login password",Snackbar.LENGTH_SHORT).show();
            tnplpass.setError("Enter login password");
            status=false;
        }
        else if(!Utils.isValidMobile(edContactNo.getText().toString()))
        {
            Snackbar.make(btnSave,"Enter Valid Mobile Number",Snackbar.LENGTH_SHORT).show();
            tnplpass.setError("Enter Valid Mobile Number");
            status=false;
        }
        else if(!Utils.isValidMobile(edContact.getText().toString()))
        {
            Snackbar.make(btnSave,"Enter Valid Office Mobile Number",Snackbar.LENGTH_SHORT).show();
            tnplpass.setError("Enter Valid Office Mobile Number");
            status=false;
        }
        else
        {
            if(Utils.isEmpty(edAddress2))
            {
                address2="NA";
            }
            else
            {
                address2=edAddress2.getText().toString();
            }
            if(Utils.isEmpty(edPin))
            {
                pincode="NA";
            }
            else
            {
                pincode=address2=edPin.getText().toString();
            }
            if(Utils.isEmpty(edEmail))
            {
                email="NA";
            }
            else
            {
                email=edEmail.getText().toString();
            }
            if(Utils.isEmpty(edWeb))
            {
                website="NA";
            }
            else
            {
                website=edWeb.getText().toString();
            }
            if(Utils.isEmpty(edShortDes))
            {
                shortdesc="NA";
            }
            else
            {
                shortdesc=edShortDes.getText().toString();
            }

            status=true;
        }
        if(status)
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
            Call<EmployerRegistrationPojo> call=jobApiService.insertEmployer(edName.getText().toString(),edAddress1.getText().toString(),address2,pincode,edContact.getText().toString(),email,website,edContactPerson.getText().toString(),
                    edContactNo.getText().toString(),shortdesc,edPass.getText().toString());
            call.enqueue(new Callback<EmployerRegistrationPojo>() {
                @Override
                public void onResponse(Call<EmployerRegistrationPojo> call, Response<EmployerRegistrationPojo> response) {

                    if(response.isSuccessful())
                    {
                        if(!response.body().getError())
                        {

                            processRegistration(response.body().getEmployer().get(0));
                        }
                        else
                        {
                            showError("Employer Registratin Failed");
                        }
//                        else if(response.body().getStatus()==1)
//                        {
//                            showError("Login Failed");
//                        }
//                        else
//                        {
//                            showError(response.message());
//                        }
                    }



                }
                @Override
                public void onFailure(Call<EmployerRegistrationPojo> call, Throwable throwable) {
                    showError("Unknown Error Occured. Please Try Later");

                }
            });
        }
    }

    private void processRegistration(Employer employer) {

        Log.e("HaiJobs",employer.getEmployerName());
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

        showMessage("Registration Completed Successfully.");
    }


    private void showEmployerHome() {

        Intent emphome=new Intent(this,EmployerHome.class);
        startActivity(emphome);
        finish();

    }


}
