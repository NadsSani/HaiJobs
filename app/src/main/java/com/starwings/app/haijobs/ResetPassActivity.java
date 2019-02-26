package com.starwings.app.haijobs;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.starwings.app.haijobs.api.ApiLinks;
import com.starwings.app.haijobs.components.FancyAlert.Animation;
import com.starwings.app.haijobs.components.FancyAlert.FancyAlertDialog;
import com.starwings.app.haijobs.components.FancyAlert.FancyAlertDialogListener;
import com.starwings.app.haijobs.components.FancyAlert.Icon;
import com.starwings.app.haijobs.responsePojo.StatusChangeResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResetPassActivity extends HaiJobsActivity {
    
    EditText edPass,edCPass;
    Button updatePass;
    HaiJobApp haiJobApp;
    String mobile;
    private static Retrofit retrofit = null;
    JobApiServices jobApiService;
    ProgressBar prgUpdate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        haiJobApp = (HaiJobApp) getApplication();
        edPass=(EditText)findViewById(R.id.txtpass);
        edCPass=(EditText)findViewById(R.id.txtcpass);
        updatePass=(Button)findViewById(R.id.btnlogin);
        mobile =getIntent().getStringExtra("mobile");
        updatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callUpdatePassAPI();
            }
        });
        prgUpdate=(ProgressBar)findViewById(R.id.prgStatus);
        prgUpdate.setVisibility(View.INVISIBLE);
    }


    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }
    private void callUpdatePassAPI() {

        boolean status=false;
        if(isEmpty(edPass))
        {
            Snackbar.make(updatePass, "Password Cannot be Blank", Snackbar.LENGTH_SHORT).show();
            status=false;
        }
        else if(isEmpty(edCPass))
        {
            Snackbar.make(updatePass, "Confirm your password", Snackbar.LENGTH_SHORT).show();
            status=false;
        }
        else if(!edCPass.getText().toString().toLowerCase().equals(edPass.getText().toString().toLowerCase()))
        {
            Snackbar.make(updatePass, "Passwords are not matching.", Snackbar.LENGTH_SHORT).show();
            status=false;
        }
        else {



                prgUpdate.setVisibility(View.VISIBLE);
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
                Call<StatusChangeResponse> call = jobApiService.updatePasswordViaPhone(mobile,edPass.getText().toString());
                call.enqueue(new Callback<StatusChangeResponse>() {
                    @Override
                    public void onResponse(Call<StatusChangeResponse> call, Response<StatusChangeResponse> response) {

                        if (response.isSuccessful()) {
                            if (response.body().getError()) {
                                showMessage(response.body().getMessage());
                                prgUpdate.setVisibility(View.GONE);
                            } else {
                                showError(response.message());
                                prgUpdate.setVisibility(View.GONE);
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<StatusChangeResponse> call, Throwable throwable) {
                        showError("Unknown Error Occured");
                        prgUpdate.setVisibility(View.GONE);

                    }
                });
            }
        }
        @Override
    public void showMessage(String message) {
        haiJobApp.getRegisteredUser().setPassword(edPass.getText().toString());
        SharedPreferences userPreferences=getSharedPreferences("USER_PREG",MODE_PRIVATE);
        SharedPreferences.Editor prefEdit=userPreferences.edit();
        prefEdit.putString("pass",edPass.getText().toString());

        prefEdit.commit();


        new FancyAlertDialog.Builder(this)

                .setTitle("HaiJobs")
                .setBackgroundColor(Color.parseColor("#19a8ff"))  //Don't pass R.color.colorvalue
                .setMessage(message)
                .setPositiveBtnBackground(Color.parseColor("#FF4081"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("DISMISS")
                .setAnimation(Animation.POP)
                .isCancellable(false)
                .setIcon(R.drawable.ic_info,Icon.Visible)
                 .build();

    }





}
