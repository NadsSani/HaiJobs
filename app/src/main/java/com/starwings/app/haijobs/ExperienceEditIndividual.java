package com.starwings.app.haijobs;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;

import com.starwings.app.haijobs.api.ApiLinks;
import com.starwings.app.haijobs.components.CircleButton;
import com.starwings.app.haijobs.data.Experience;
import com.starwings.app.haijobs.responsePojo.ExperienceEntryResponse;
import com.starwings.app.haijobs.utils.Utils;

import java.lang.reflect.Field;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExperienceEditIndividual extends HaiJobsActivity {
    EditText edFromYear,edToYear,edOrganization,edPosition;
    private CheckBox chkCurrent;
    Experience experienceSelected;
    CircleButton btnSave;
    private int mYear,mYearTo;
    private int mMonth,mMonthTo;
    private int mDay,mDayTo;
    static final int DATE_DIALOG_ID_FROM = 1;
    static final int DATE_DIALOG_ID_TO = 2;
    private boolean fromSelected,toSelected,dtAccurate,stillWorking;
    private static Retrofit retrofit = null;
    Button btnback;
    JobApiServices jobApiService;
    private String getMonthString(int month)
    {
        String monstring="";
        switch (month)
        {
            case 1:
                monstring= "JAN";
                break;
            case 2:
                monstring=  "FEB";
                break;

            case 3:
                monstring= "MAR";
                break;
            case 4:
                monstring= "APR";
                break;
            case 5:
                monstring= "MAY";
                break;
            case 6:
                monstring= "JUN";
                break;
            case 7:
                monstring= "JUL";
                break;
            case 8:
                monstring= "AUG";
                break;
            case 9:
                monstring= "SEP";
                break;
            case 10:
                monstring= "OCT";
                break;
            case 11:
                monstring= "NOV";
                break;
            case 12:
                monstring= "DEC";
                break;
        }

        return  monstring;
    }
    DatePickerDialog.OnDateSetListener mDateSetListner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDate();
        }
    };
    DatePickerDialog.OnDateSetListener mDateSetListnerTo = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            mYearTo = year;
            mMonthTo = monthOfYear;
            mDayTo = dayOfMonth;
            updateDateTo();
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience_edit);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        experienceSelected=(Experience)getIntent().getSerializableExtra("EToEdit");
        edFromYear=(EditText)findViewById(R.id.input_datefrom);
        edPosition=(EditText)findViewById(R.id.ed_org_position);
        edToYear=(EditText)findViewById(R.id.input_dateto);
        chkCurrent=(CheckBox)findViewById(R.id.chkCurrent);
        edOrganization=(EditText)findViewById(R.id.ed_org_name);
        btnback=(Button)findViewById(R.id.edBackButton);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edOrganization.setText(experienceSelected.getNameOfOrganisation());
        edFromYear.setText(experienceSelected.getWorkedFrom());
        fromSelected=true;
        chkCurrent.setChecked(experienceSelected.getCurrentfirm()==1?true:false);
        if(chkCurrent.isChecked())
        {

            toSelected=false;
            stillWorking=true;
            edToYear.setEnabled(false);
        }
        else
        {
            toSelected=true;
            edToYear.setEnabled(true);
            edToYear.setText(experienceSelected.getWorkedTo());
        }
        chkCurrent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stillWorking=isChecked;
                if(isChecked)
                {
                    edToYear.setEnabled(false);
                }
                else
                {
                    edToYear.setEnabled(true);
                }
            }
        });
        edPosition.setText(experienceSelected.getJobPosition());


        btnSave=(CircleButton)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveChanges();
            }
        });

        edFromYear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDialog(DATE_DIALOG_ID_FROM);
            }
        });

        edToYear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDialog(DATE_DIALOG_ID_TO);
            }
        });

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);


        mYearTo = c.get(Calendar.YEAR);
        mMonthTo = c.get(Calendar.MONTH);
    }

    private void saveChanges() {

        boolean status=true;
        if(Utils.isEmpty(edOrganization))
        {
            Snackbar.make(btnSave, "Enter name of Organization", Snackbar.LENGTH_SHORT).show();
            status=false;
        }
        if(Utils.isEmpty(edPosition))
        {
            Snackbar.make(btnSave, "Enter Your Job Position", Snackbar.LENGTH_SHORT).show();
            status=false;
        }

        if(!fromSelected)
        {
            Snackbar.make(btnSave, "Specify Date of Joining", Snackbar.LENGTH_SHORT).show();
            status=false;
        }
        if(!stillWorking)
        {
            if(!toSelected)
            {
                Snackbar.make(btnSave, "Specify Date of Resigning", Snackbar.LENGTH_SHORT).show();
                status=false;
            }
            else if(!dtAccurate)
            {
                Snackbar.make(btnSave, "Please Select Correct Dates", Snackbar.LENGTH_SHORT).show();
                status=false;
            }
        }
        if(!Utils.checkConnectivity(ExperienceEditIndividual.this))
        {
            Snackbar.make(btnSave, "Your device is not connected to internet", Snackbar.LENGTH_SHORT).show();
            status = false;
        }
        if(status) {

            SharedPreferences userPreferences = getSharedPreferences("USER_PREG", MODE_PRIVATE);
            String apikey = userPreferences.getString("apikey", "NA");
            if(!apikey.equals("NA"))
            {

            }
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
            Call<ExperienceEntryResponse> call=jobApiService.updateExperience(experienceSelected.getSlno(),edOrganization.getText().toString(),
                    edFromYear.getText().toString(),
                    stillWorking?"PRESENT":edToYear.getText().toString(),
                    edPosition.getText().toString(), stillWorking?1:0, apikey);
            call.enqueue(new Callback<ExperienceEntryResponse>() {
                @Override
                public void onResponse(Call<ExperienceEntryResponse> call, Response<ExperienceEntryResponse> response) {


                    processExpResponse(response);



                }
                @Override
                public void onFailure(Call<ExperienceEntryResponse> call, Throwable throwable) {
                    showError("Unknown Error Occured");

                }
            });

            resetFields();
        }
        else
        {
            showError("Please enter all the informations");
        }
}
    private void resetFields()
    {
        edOrganization.setText("");
        edPosition.setText("");
        edToYear.setText("");
        edFromYear.setText("");
        fromSelected=false;
        toSelected=false;
        dtAccurate=false;
        stillWorking=false;
        chkCurrent.setChecked(false);
    }
    private void processExpResponse(Response<ExperienceEntryResponse> response) {

        if(response.isSuccessful())
        {
            if(!response.body().getError())
            {
                showMessage("Experience Details Updated");

            }
            else
            {
                showError("Failed to update experience details");
            }
        }
    }

    protected void updateDate() {
        int localMonth = (mMonth + 1);
        String monthString = localMonth < 10 ? "0" + localMonth : Integer
                .toString(localMonth);
        String localYear = Integer.toString(mYear);
        String localmstring=getMonthString(localMonth);
        edFromYear.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(localmstring).append("/").append(localYear).append(" "));
        showDialog(DATE_DIALOG_ID_FROM);
        fromSelected=true;
        verifyDates(fromSelected,toSelected);
    }

    private void verifyDates(boolean fromSelected, boolean toSelected) {
        if(stillWorking)
        {
            if(fromSelected)
            {
                dtAccurate=true;
            }
        }
        else {
            if (fromSelected && toSelected) {
                if (mYearTo < mYear) {
                    dtAccurate = false;
                    Snackbar.make(edFromYear, "Please check the selected year", Snackbar.LENGTH_SHORT).show();
                } else if (mYearTo == mYear) {
                    if (mMonthTo < mMonth) {
                        dtAccurate = false;
                        Snackbar.make(edFromYear, "Please check the selected month", Snackbar.LENGTH_SHORT).show();
                    } else {
                        dtAccurate = true;
                    }
                } else {
                    dtAccurate = true;

                }
            }
        }
    }

    protected void updateDateTo() {
        int localMonth = (mMonthTo + 1);
        String monthString = localMonth < 10 ? "0" + localMonth : Integer
                .toString(localMonth);
        String localYear = Integer.toString(mYearTo);
        String localmstring=getMonthString(localMonth);
        edToYear.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(localmstring).append("/").append(localYear).append(" "));
        showDialog(DATE_DIALOG_ID_TO);
        toSelected=true;
        verifyDates(fromSelected,toSelected);

    }
    @Override
    protected Dialog onCreateDialog(int id) {
        DatePickerDialog datePickerDialog=null;
        switch (id) {
            case DATE_DIALOG_ID_FROM:
                /*
                 * return new DatePickerDialog(this, mDateSetListner, mYear, mMonth,
                 * mDay);
                 */
                datePickerDialog = this.customDatePicker();
                break;
            case DATE_DIALOG_ID_TO:
                datePickerDialog = this.customDatePickerTo();
                break;
        }
        return datePickerDialog;
    }
    private DatePickerDialog customDatePicker() {
        DatePickerDialog dpd = new DatePickerDialog(this, mDateSetListner,
                mYear, mMonth, mDay);
        try {

            Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField
                            .get(dpd);
                    Field datePickerFields[] = datePickerDialogField.getType()
                            .getDeclaredFields();
                    for (Field datePickerField : datePickerFields) {
                        if ("mDayPicker".equals(datePickerField.getName())
                                || "mDaySpinner".equals(datePickerField
                                .getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = new Object();
                            dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }

            }
        } catch (Exception ex) {
        }
        return dpd;
    }

    private DatePickerDialog customDatePickerTo() {
        DatePickerDialog dpd = new DatePickerDialog(this, mDateSetListnerTo,
                mYearTo, mMonthTo, mDayTo);
        try {

            Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField
                            .get(dpd);
                    Field datePickerFields[] = datePickerDialogField.getType()
                            .getDeclaredFields();
                    for (Field datePickerField : datePickerFields) {
                        if ("mDayPicker".equals(datePickerField.getName())
                                || "mDaySpinner".equals(datePickerField
                                .getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = new Object();
                            dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }

            }
        } catch (Exception ex) {
        }
        return dpd;
    }
}
