package com.starwings.app.haijobs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.starwings.app.haijobs.api.ApiLinks;
import com.starwings.app.haijobs.components.FancyAlert.Animation;
import com.starwings.app.haijobs.components.FancyAlert.FancyAlertDialog;
import com.starwings.app.haijobs.components.FancyAlert.FancyAlertDialogListener;
import com.starwings.app.haijobs.components.FancyAlert.Icon;
import com.starwings.app.haijobs.data.Age;
import com.starwings.app.haijobs.data.User;
import com.starwings.app.haijobs.responsePojo.RegisterResponse;
import com.starwings.app.haijobs.responsePojo.StatusChangeResponse;
import com.starwings.app.haijobs.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PersonalInfoDialog extends HaiJobsActivity {

    Button imgSave;
    EditText edDob, edPhone, edMail;
    Spinner spnGender;
    HaiJobApp haiJobApp;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    private static Retrofit retrofit = null;
    JobApiServices jobApiService;
    Button btnback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_personal_info);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        haiJobApp = (HaiJobApp) getApplication();

        edDob = (EditText) findViewById(R.id.input_dob);
        edPhone = (EditText) findViewById(R.id.input_phone);
        edMail = (EditText) findViewById(R.id.input_email);
        spnGender = (Spinner) findViewById(R.id.spnGender);
        spnGender.setSelection(haiJobApp.getRegisteredUser().getGender() - 1);

        SharedPreferences userPreferences = getSharedPreferences("USER_PREG", MODE_PRIVATE);
        edDob.setText(userPreferences.getString("dob","NA"));
        edPhone.setText(userPreferences.getString("username","NA"));
        edMail.setText(userPreferences.getString("email","NA"));
        imgSave = (Button) findViewById(R.id.edUpdate);
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updatePersonalInfo();

            }
        });

        btnback=(Button)findViewById(R.id.edBackButton);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edDob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(PersonalInfoDialog.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edDob.setText(sdf.format(myCalendar.getTime()));
    }

    private void updatePersonalInfo() {
        boolean status = false;
        if (Utils.isEmpty(edDob)) {
            Snackbar.make(imgSave, "Specify your Date of Birth", Snackbar.LENGTH_SHORT).show();
            status = false;
        } else if (Utils.isEmpty(edPhone)) {
            Snackbar.make(imgSave, "Specify your Contact Number", Snackbar.LENGTH_SHORT).show();
            status = false;
        } else if (Utils.isEmpty(edMail)) {
            Snackbar.make(imgSave, "Specify your Contact EMail", Snackbar.LENGTH_SHORT).show();
            status = false;
        } else if (!Utils.isValidEmail(edMail.getText().toString())) {
            Snackbar.make(imgSave, "Enter a valid EMail", Snackbar.LENGTH_SHORT).show();
            status = false;
        } else if (!Utils.isValidMobile(edPhone.getText().toString())) {
            Snackbar.make(imgSave, "Enter a valid Contact Number", Snackbar.LENGTH_SHORT).show();
            status = false;
        }
        else if(!(Utils.calculateAge(edDob.getText().toString()).getYears()>14))
        {
            Snackbar.make(imgSave,"Enter Valid Date of Birth",Snackbar.LENGTH_SHORT).show();
            status=false;
        }
        else if (!Utils.checkConnectivity(PersonalInfoDialog.this)) {
            Snackbar.make(imgSave, "Enter a valid Contact Number", Snackbar.LENGTH_SHORT).show();
            status = false;
        }
        else
        {
            status=true;
        }
        if (status) {
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


            SharedPreferences userPreferences = getSharedPreferences("USER_PREG", MODE_PRIVATE);
            String apikey = userPreferences.getString("apikey", "NA");
            jobApiService = retrofit.create(JobApiServices.class);
            Call<StatusChangeResponse> call = jobApiService.updatePersonalInfo(edDob.getText().toString(),
                    spnGender.getSelectedItemPosition() + 1,
                    edPhone.getText().toString(), edMail.getText().toString(), apikey
            );

            call.enqueue(new Callback<StatusChangeResponse>() {
                @Override
                public void onResponse(Call<StatusChangeResponse> call, Response<StatusChangeResponse> response) {

                    if (response.isSuccessful()) {
                        if (!response.body().getError()) {
                            modifyPersonalDetails();
                            showMessage(response.body().getMessage());
                        } else
                            showError(response.message());
                    }
                }




                @Override
                public void onFailure(Call<StatusChangeResponse> call, Throwable throwable) {
                    showError("Unknown Error Occured");

                }
            });
        }
    }

    private void modifyPersonalDetails() {

        User registereduser=haiJobApp.getRegisteredUser();
        registereduser.setDob(edDob.getText().toString());
        registereduser.setGender(spnGender.getSelectedItemPosition()+1);
        registereduser.setContactno(edPhone.getText().toString());
        registereduser.setEmail(edMail.getText().toString());
        haiJobApp.setRegisteredUser(registereduser);
    }

    @Override
    protected void showMessage(String message) {

        new FancyAlertDialog.Builder(this)

                .setTitle("HaiJobs")
                .setBackgroundColor(Color.parseColor("#19a8ff"))  //Don't pass R.color.colorvalue
                .setMessage(message)
                .setPositiveBtnBackground(Color.parseColor("#FF4081"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("DISMISS")
                .setAnimation(Animation.POP)
                .isCancellable(false)
                .setIcon(R.drawable.ic_info,Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        Intent intent=new Intent();
                        setResult(1020,intent);
                        finish();//finishing activity
                    }
                })

                .build();



    }
    @Override
    protected void showError(String message)
    {
        haiJobApp.getRegisteredUser().setDob(edDob.getText().toString());
        haiJobApp.getRegisteredUser().setEmail(edMail.getText().toString());
        haiJobApp.getRegisteredUser().setGender(spnGender.getSelectedItemPosition()+1);
        haiJobApp.getRegisteredUser().setContactno(edPhone.getText().toString());
        SharedPreferences userPreferences=getSharedPreferences("USER_PREG",MODE_PRIVATE);
        SharedPreferences.Editor prefEdit=userPreferences.edit();
        prefEdit.putString("email",edMail.getText().toString());
        prefEdit.putString("username",edPhone.getText().toString());
        prefEdit.putString("dob",edDob.getText().toString());
        prefEdit.putInt("gender",spnGender.getSelectedItemPosition()+1);
        prefEdit.commit();
        new FancyAlertDialog.Builder(this)

                .setTitle("HaiJobs")
                .setBackgroundColor(Color.parseColor("#FF0000"))  //Don't pass R.color.colorvalue
                .setMessage(message)
                .setPositiveBtnBackground(Color.parseColor("#FF4081"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("DISMISS")
                .setAnimation(Animation.POP)
                .isCancellable(false)
                .setIcon(R.drawable.ic_close_black_24dp,Icon.Visible)
                .build();
    }





}
