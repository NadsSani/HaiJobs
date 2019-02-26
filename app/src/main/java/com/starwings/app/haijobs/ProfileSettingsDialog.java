package com.starwings.app.haijobs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.starwings.app.haijobs.api.ApiLinks;
import com.starwings.app.haijobs.components.FancyAlert.Animation;
import com.starwings.app.haijobs.components.FancyAlert.FancyAlertDialog;
import com.starwings.app.haijobs.components.FancyAlert.FancyAlertDialogListener;
import com.starwings.app.haijobs.components.FancyAlert.Icon;
import com.starwings.app.haijobs.data.Experience;
import com.starwings.app.haijobs.responsePojo.ExperienceEntryResponse;
import com.starwings.app.haijobs.responsePojo.StatusChangeResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileSettingsDialog extends HaiJobsActivity {
    EditText edPassWord;
    Button imgSave;
    private static Retrofit retrofit = null;
    JobApiServices jobApiService;
    HaiJobApp haiJobApp;
    Button btnback;
    ProgressBar prgStatus;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_settings);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        haiJobApp = (HaiJobApp) getApplication();
        edPassWord=(EditText)findViewById(R.id.edPass);
        SharedPreferences userPreferences = getSharedPreferences("USER_PREG", MODE_PRIVATE);
        edPassWord.setText(userPreferences.getString("pass","NA"));
        imgSave=(Button)findViewById(R.id.edUpdate);
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });
        btnback=(Button)findViewById(R.id.edBackButton);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        prgStatus=(ProgressBar)findViewById(R.id.prgStatus);
        prgStatus.setVisibility(View.INVISIBLE);
    }
    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }
    private void updatePassword() {
        boolean status=false;
        if(isEmpty(edPassWord))
        {
            Snackbar.make(imgSave, "Password Cannot be Blank", Snackbar.LENGTH_SHORT).show();
            status=false;
        }
        else {
            SharedPreferences userPreferences = getSharedPreferences("USER_PREG", MODE_PRIVATE);
            String apikey = userPreferences.getString("apikey", "NA");

            prgStatus.setVisibility(View.VISIBLE);

            if (!apikey.equals("NA")) {

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
                Call<StatusChangeResponse> call = jobApiService.updatePassword(edPassWord.getText().toString(), apikey);
                call.enqueue(new Callback<StatusChangeResponse>() {
                    @Override
                    public void onResponse(Call<StatusChangeResponse> call, Response<StatusChangeResponse> response) {

                        if (response.isSuccessful()) {
                            if (response.body().getError()) {
                                showMessage(response.body().getMessage());
                                prgStatus.setVisibility(View.GONE);
                            } else {
                                showError(response.message());
                                prgStatus.setVisibility(View.GONE);
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<StatusChangeResponse> call, Throwable throwable) {
                        showError("Unknown Error Occured");
                        prgStatus.setVisibility(View.GONE);

                    }
                });
            }
        }

    }
    @Override
    protected void showMessage(String message) {
        haiJobApp.getRegisteredUser().setPassword(edPassWord.getText().toString());
        SharedPreferences userPreferences=getSharedPreferences("USER_PREG",MODE_PRIVATE);
        SharedPreferences.Editor prefEdit=userPreferences.edit();
        prefEdit.putString("pass",edPassWord.getText().toString());

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
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        Intent intent=new Intent();
                        setResult(  1030,intent);
                        finish();//finishing activity
                    }
                })

                 .build();

    }




}
