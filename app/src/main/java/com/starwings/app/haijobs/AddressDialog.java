package com.starwings.app.haijobs;

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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.starwings.app.haijobs.api.ApiLinks;
import com.starwings.app.haijobs.components.FancyAlert.Animation;
import com.starwings.app.haijobs.components.FancyAlert.FancyAlertDialog;
import com.starwings.app.haijobs.components.FancyAlert.FancyAlertDialogListener;
import com.starwings.app.haijobs.components.FancyAlert.Icon;
import com.starwings.app.haijobs.data.District;
import com.starwings.app.haijobs.employerPages.adapter.DistrictAdapter;
import com.starwings.app.haijobs.employerPages.adapter.StatesAdapter;
import com.starwings.app.haijobs.responsePojo.StatusChangeResponse;
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

public class AddressDialog extends HaiJobsActivity {
    Spinner spnState,spnDistrict;
    EditText edPin,edLocation;
    HaiJobApp hijobapp;
    Button btnUpdate,btnback;
    private static Retrofit retrofit = null;
    JobApiServices jobApiService;
    List<District> currentDistrictList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        edLocation=(EditText)findViewById(R.id.input_place);
        edPin=(EditText)findViewById(R.id.input_pincode);
        spnState=(Spinner)findViewById(R.id.spnState);
        spnDistrict=(Spinner)findViewById(R.id.spnDistrict);
         btnUpdate=(Button)findViewById(R.id.edUpdate);
        btnback=(Button)findViewById(R.id.edBackButton);
         btnback.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });
        hijobapp=(HaiJobApp)getApplication();

        edLocation.setText(hijobapp.getRegisteredUser().getPlace());
        edPin.setText(hijobapp.getRegisteredUser().getPincode());
        processStates();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAddress();
            }
        });



    }

    private void fetchDistrictsByState(int i) {
        Log.e("Error123","Clicked"+i);
        currentDistrictList=new ArrayList<District>();
        for(int j=0;j<hijobapp.getDistrictsList().size();j++)
        {
            if(hijobapp.getDistrictsList().get(j).getStatecode()==i)
            {
                currentDistrictList.add(hijobapp.getDistrictsList().get(j));
            }
        }
        DistrictAdapter districtAdapter=new DistrictAdapter(currentDistrictList,this);
        spnDistrict.setAdapter(districtAdapter);
        for(int k=0;k<currentDistrictList.size();k++)
        {
            if(currentDistrictList.get(k).getId()==hijobapp.getRegisteredUser().getDistrict())
            {
                spnDistrict.setSelection(k);
            }
        }

    }
    private void processStates() {
        StatesAdapter statesAdapter=new StatesAdapter(hijobapp.getStatesList(),this);
        spnState.setAdapter(statesAdapter);
        spnState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fetchDistrictsByState(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        spnState.setSelection(hijobapp.getRegisteredUser().getState()-1);

    }

    @Override
    protected void showMessage(String message) {

        hijobapp.getRegisteredUser().setPincode(edPin.getText().toString());
        hijobapp.getRegisteredUser().setPlace(edLocation.getText().toString());
        hijobapp.getRegisteredUser().setDistrict(currentDistrictList.get(spnDistrict.getSelectedItemPosition()).getId());
        hijobapp.getRegisteredUser().setState(spnState.getSelectedItemPosition()+1);

        SharedPreferences userPreferences=getSharedPreferences("USER_PREG",MODE_PRIVATE);
        SharedPreferences.Editor prefEdit=userPreferences.edit();
        prefEdit.putString("place",hijobapp.getRegisteredUser().getPlace());
        prefEdit.putInt("district",hijobapp.getRegisteredUser().getDistrict());
        prefEdit.putInt("state",hijobapp.getRegisteredUser().getState());
        prefEdit.putString("pincode",hijobapp.getRegisteredUser().getPincode());
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
                        setResult(1002,intent);
                        finish();//finishing activity
                    }
                })
                .build();



    }
    private void updateAddress() {
        boolean status = false;
        if (Utils.isEmpty(edPin)) {
            Snackbar.make(btnUpdate, "Specify your Pincode", Snackbar.LENGTH_SHORT).show();
            status = false;
        } else if (Utils.isEmpty(edLocation)) {
            Snackbar.make(btnUpdate, "Specify your Location", Snackbar.LENGTH_SHORT).show();
            status = false;
        } else if (Utils.isValidPostalCode(edPin.getText().toString())) {
            Snackbar.make(btnUpdate, "Enter Correct Pincode", Snackbar.LENGTH_SHORT).show();
            status = false;
        }
        else if(!Utils.checkConnectivity(AddressDialog.this))
        {
            Snackbar.make(btnUpdate, "Your device is not connected to internet", Snackbar.LENGTH_SHORT).show();
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

            int kkkk=spnDistrict.getSelectedItemPosition();
            SharedPreferences userPreferences = getSharedPreferences("USER_PREG", MODE_PRIVATE);
            String apikey = userPreferences.getString("apikey", "NA");
            jobApiService = retrofit.create(JobApiServices.class);
            Call<StatusChangeResponse> call = jobApiService.updateAddress(edLocation.getText().toString(),
                    spnState.getSelectedItemPosition() + 1,currentDistrictList.get(spnDistrict.getSelectedItemPosition()).getId(),
                    edPin.getText().toString(),  apikey
            );

            call.enqueue(new Callback<StatusChangeResponse>() {
                @Override
                public void onResponse(Call<StatusChangeResponse> call, Response<StatusChangeResponse> response) {

                    if (response.isSuccessful()) {
                        if (!response.body().getError()) {
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
}
