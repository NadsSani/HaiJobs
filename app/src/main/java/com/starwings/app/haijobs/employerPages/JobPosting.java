package com.starwings.app.haijobs.employerPages;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.starwings.app.haijobs.HaiJobApp;
import com.starwings.app.haijobs.HaiJobsActivity;
import com.starwings.app.haijobs.JobApiServices;
import com.starwings.app.haijobs.QualificationDialog;
import com.starwings.app.haijobs.R;
import com.starwings.app.haijobs.api.ApiLinks;
import com.starwings.app.haijobs.components.FancyAlert.Animation;
import com.starwings.app.haijobs.components.FancyAlert.FancyAlertDialog;
import com.starwings.app.haijobs.components.FancyAlert.FancyAlertDialogListener;
import com.starwings.app.haijobs.components.FancyAlert.Icon;
import com.starwings.app.haijobs.data.Categories;
import com.starwings.app.haijobs.data.District;
import com.starwings.app.haijobs.data.Experience;
import com.starwings.app.haijobs.data.JobRoles;
import com.starwings.app.haijobs.data.JobTypeItem;
import com.starwings.app.haijobs.data.NoticePeriod;
import com.starwings.app.haijobs.data.QualificationData;
import com.starwings.app.haijobs.data.ScaleOfPay;
import com.starwings.app.haijobs.data.State;
import com.starwings.app.haijobs.data.SubCategories;
import com.starwings.app.haijobs.employerPages.adapter.DistrictAdapter;
import com.starwings.app.haijobs.employerPages.adapter.JobNatureAdapter;
import com.starwings.app.haijobs.employerPages.adapter.JobRoleAdapter;
import com.starwings.app.haijobs.employerPages.adapter.NoticePeriodAdapter;
import com.starwings.app.haijobs.employerPages.adapter.ScaleOfPayAdapter;
import com.starwings.app.haijobs.employerPages.adapter.StatesAdapter;
import com.starwings.app.haijobs.responsePojo.DistrictsResponse;
import com.starwings.app.haijobs.responsePojo.FetchDistrictsResponse;
import com.starwings.app.haijobs.responsePojo.FetchExperienceResponse;
import com.starwings.app.haijobs.responsePojo.FetchJobRoleResponse;
import com.starwings.app.haijobs.responsePojo.FetchStatesResponse;
import com.starwings.app.haijobs.responsePojo.JobPostResponse;
import com.starwings.app.haijobs.responsePojo.JobTypeResponse;
import com.starwings.app.haijobs.responsePojo.NoticePeriodResponse;
import com.starwings.app.haijobs.responsePojo.ScaleOfPayResponse;
import com.starwings.app.haijobs.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JobPosting extends HaiJobsActivity {
    private static Retrofit retrofit = null;
    JobApiServices jobApiService;
    HaiJobApp haiJobApp;
    TextInputEditText edJobTitle,edVacancyCount,edDescription;
    Spinner spnJobTitle,spnJobState,spnDistricts,spnGender,spnJobNature,spnScalePay,spnNoticePeriod;
    ImageButton imgadbtn;
    String categoryIDString;
    String subCategoryIDString;
    String edDisplayString;
    TextView edQualification;
    Button btnPost;
    TextInputLayout txtinplayout1,txtinplayout2,txtinplayout3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_posting);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        haiJobApp=(HaiJobApp)getApplication();
        spnJobTitle=(Spinner)findViewById(R.id.spnJobTitle);
        spnJobState=(Spinner)findViewById(R.id.spnJobState);
        spnDistricts=(Spinner)findViewById(R.id.spnJobDistrict);
        spnJobNature=(Spinner)findViewById(R.id.spnJobNature);
        spnScalePay=(Spinner)findViewById(R.id.spnScalePay);
        imgadbtn=(ImageButton)findViewById(R.id.imgadbtn);
        spnGender=(Spinner)findViewById(R.id.spnGender);
        spnNoticePeriod=(Spinner)findViewById(R.id.spnTimeToJoin);
        btnPost=(Button)findViewById(R.id.btnPost);

        txtinplayout1=(TextInputLayout)findViewById(R.id.txtinplayout1);
        txtinplayout2=(TextInputLayout)findViewById(R.id.txtinplayout2);
        txtinplayout3=(TextInputLayout)findViewById(R.id.txtinplayout3);


        edJobTitle=(TextInputEditText)findViewById(R.id.txtPostTitle);
        edVacancyCount=(TextInputEditText)findViewById(R.id.txtVacancyCount);
        edDescription=(TextInputEditText)findViewById(R.id.txtJobDescription) ;
        fetchJobTitles();
        processStates();
        imgadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQualificationDialog();
            }
        });
        edQualification=(TextView)findViewById(R.id.input_edu);
        edDisplayString="";
        fetchJobNature();
        fetchScaleOfPay();
        fetchNoticePeriods();
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postJob();
            }
        });


    }

    private void postJob() {
        String description=edDescription.getText().toString();
        if(Utils.isEmpty(edJobTitle))
        {
            txtinplayout1.setError("Enter Title of the Job");
            return;
        }
        else if(Utils.isEmpty(edVacancyCount))
        {
            txtinplayout1.setError("Enter No of Vacancies");
            return;
        }
        else if(Utils.isEmpty(edDescription))
        {
            description="NA";
        }
        else if(!Utils.checkConnectivity(JobPosting.this))
        {
            showError("Your Device is not Connected to the Internet");
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

            jobApiService = retrofit.create(JobApiServices.class);
            Call<JobPostResponse> call=jobApiService.postJobVacancy(haiJobApp.getRegisteredEmployer().getApikey(),edJobTitle.getText().toString(),spnJobTitle.getSelectedItemPosition()+1,spnDistricts.getSelectedItemPosition()+1,spnJobState.getSelectedItemPosition()+1,categoryIDString,subCategoryIDString,spnGender.getSelectedItemPosition()+1,spnJobNature.getSelectedItemPosition()+1,spnScalePay.getSelectedItemPosition()+1,Integer.parseInt(edVacancyCount.getText().toString().trim()),spnNoticePeriod.getSelectedItemPosition()+1,description);
            call.enqueue(new Callback<JobPostResponse>() {
                @Override
                public void onResponse(Call<JobPostResponse> call, Response<JobPostResponse> response) {

                    if(response.isSuccessful())
                    {
                        if(!response.body().getError())
                        {
                            showMessage("Job Posted Successfully");
                        }
                        else
                        {
                            showError("Job Posting Failed");
                        }

                    }



                }
                @Override
                public void onFailure(Call<JobPostResponse> call, Throwable throwable) {

                    showError("Job Posting Failed");
                }
            });
        }



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
                        finish();//finishing activity
                    }
                })
                .build();



    }
    private void fetchNoticePeriods() {
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
        Call<NoticePeriodResponse> call=jobApiService.fetchNoticePeriods(haiJobApp.getRegisteredEmployer().getApikey());
        call.enqueue(new Callback<NoticePeriodResponse>() {
            @Override
            public void onResponse(Call<NoticePeriodResponse> call, Response<NoticePeriodResponse> response) {

                if(response.isSuccessful())
                {
                    if(!response.body().getError())
                    {
                        List<NoticePeriod> noticePeriodList=response.body().getNoticePeriods();
                        processNoticePeriods(noticePeriodList);
                    }

                }



            }
            @Override
            public void onFailure(Call<NoticePeriodResponse> call, Throwable throwable) {


            }
        });
    }

    private void processNoticePeriods(List<NoticePeriod> noticePeriodList) {
        NoticePeriodAdapter noticePeriodAdapter=new NoticePeriodAdapter(noticePeriodList,this);
        spnNoticePeriod.setAdapter(noticePeriodAdapter);
    }

    private void fetchScaleOfPay() {
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
        Call<ScaleOfPayResponse> call=jobApiService.fetchScaleOfPay(haiJobApp.getRegisteredEmployer().getApikey());
        call.enqueue(new Callback<ScaleOfPayResponse>() {
            @Override
            public void onResponse(Call<ScaleOfPayResponse> call, Response<ScaleOfPayResponse> response) {

                if(response.isSuccessful())
                {
                    if(!response.body().getError())
                    {
                        List<ScaleOfPay> scaleOfPayList=response.body().getScaleOfPay();
                        processScaleOfPay(scaleOfPayList);
                    }

                }



            }
            @Override
            public void onFailure(Call<ScaleOfPayResponse> call, Throwable throwable) {


            }
        });
    }

    private void processScaleOfPay(List<ScaleOfPay> scaleOfPayList) {
        ScaleOfPayAdapter scaleOfPayAdapter=new ScaleOfPayAdapter(scaleOfPayList,this);
        spnScalePay.setAdapter(scaleOfPayAdapter);
    }

    private void fetchJobNature() {

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
        Call<JobTypeResponse> call=jobApiService.fetchJobTypes(haiJobApp.getRegisteredEmployer().getApikey());
        call.enqueue(new Callback<JobTypeResponse>() {
            @Override
            public void onResponse(Call<JobTypeResponse> call, Response<JobTypeResponse> response) {

                if(response.isSuccessful())
                {
                    if(!response.body().getError())
                    {
                        List<JobTypeItem> jobTypeItem=response.body().getJobTypeItem();
                        processJobItems(jobTypeItem);
                    }

                }



            }
            @Override
            public void onFailure(Call<JobTypeResponse> call, Throwable throwable) {


            }
        });
    }

    private void processJobItems(List<JobTypeItem> jobTypeItem) {
        JobNatureAdapter jobtypeAdapter=new JobNatureAdapter(jobTypeItem,this);
        spnJobNature.setAdapter(jobtypeAdapter);

    }

    private void showQualificationDialog() {

        Intent qualificationdlg=new Intent(this,QualificationDialog.class);
        startActivityForResult(qualificationdlg,1001);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001)
        {
            getDistinctMainStreams();

        }
    }
    private void getDistinctMainStreams()
    {

        ArrayList idlist=new ArrayList();
        if(!haiJobApp.getSelectioncatlist().isEmpty())
        {
            for(int i=0;i<haiJobApp.getSelectioncatlist().size();i++)
            {
                idlist.add(haiJobApp.getSelectioncatlist().get(i).getParentID());
            }
            Set<String> hs = new HashSet<>();
            hs.addAll(idlist);
            idlist.clear();
            idlist.addAll(hs);
        }

        if(!idlist.isEmpty())
        {
            for (int i=0;i<haiJobApp.getQualificationData().size();i++)
            {
                QualificationData temp=haiJobApp.getQualificationData().get(i);
                for(int j=0;j<idlist.size();j++)
                {
                    if(temp.getCategoryObject().getCategoryID()==Integer.parseInt(idlist.get(j).toString()))
                    {
                        addSelectedCategorytoArrayList(temp.getCategoryObject());
                    }
                }

            }
        }
        categoryIDString="";
        if(!haiJobApp.getMainselectionList().isEmpty())
        {
            for(int i=0;i<haiJobApp.getMainselectionList().size();i++)
            {
                categoryIDString=categoryIDString+haiJobApp.getMainselectionList().get(i).getCategoryID()+",";
            }
            categoryIDString=categoryIDString.substring(0,categoryIDString.length()-1);
        }



        subCategoryIDString="";
        if(haiJobApp.getSelectioncatlist().size()>0)
        {
            for(int i=0;i<haiJobApp.getSelectioncatlist().size();i++)
            {
                subCategoryIDString=subCategoryIDString+haiJobApp.getSelectioncatlist().get(i).getCategoryID()+",";
            }
            subCategoryIDString=subCategoryIDString.substring(0,subCategoryIDString.length()-1);
        }
        else
        {
            subCategoryIDString="NA";
        }

        edDisplayString="";

        if(!haiJobApp.getMainselectionList().isEmpty()) {

            for(int i=0;i<haiJobApp.getMainselectionList().size();i++)
            {
                Categories temcat=haiJobApp.getMainselectionList().get(i);
                edDisplayString=edDisplayString+temcat.getCategoryName()+" ";
                if(temcat.getHasSub()==1)
                {
                    if(!haiJobApp.getSelectioncatlist().isEmpty())
                    {
                        for(int j=0;j<haiJobApp.getSelectioncatlist().size();j++)
                        {
                            SubCategories temsub=haiJobApp.getSelectioncatlist().get(j);
                            if(temcat.getCategoryID()==temsub.getParentID())
                            {
                                edDisplayString=edDisplayString+temsub.getCategoryName()+"|";
                            }
                        }
                    }

                    edDisplayString=edDisplayString.substring(0,edDisplayString.length()-1);
                    edDisplayString=edDisplayString+",";
                }
                else
                {
                    edDisplayString=edDisplayString+",";
                }

            }
            edDisplayString=edDisplayString.substring(0,edDisplayString.length()-1);

            edQualification.setText(edDisplayString);
        }





    }
    private void addSelectedCategorytoArrayList(Categories categoryObject) {

        ArrayList<Categories> tempCategories=haiJobApp.getMainselectionList();
        boolean found=false;
        for(int i=0;i<tempCategories.size();i++)
        {
            if(tempCategories.get(i).getCategoryID()==categoryObject.getCategoryID())
            {
                found=true;
                break;
            }
            else
            {
                found=false;
            }
        }
        if(!found)
        {
            tempCategories.add(categoryObject);
        }

        haiJobApp.setMainselectionList(tempCategories);
    }


    private void processStates() {
        StatesAdapter statesAdapter=new StatesAdapter(haiJobApp.getStatesList(),this);
        spnJobState.setAdapter(statesAdapter);
        spnJobState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fetchDistrictsByState(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
    }

    private void fetchDistrictsByState(int i) {
        Log.e("Error123","Clicked"+i);
        List<District> districtlist=new ArrayList<District>();
        for(int j=0;j<haiJobApp.getDistrictsList().size();j++)
        {
            if(haiJobApp.getDistrictsList().get(j).getStatecode()==(i+1))
            {
                districtlist.add(haiJobApp.getDistrictsList().get(j));
            }
        }
        DistrictAdapter districtAdapter=new DistrictAdapter(districtlist,this);
        spnDistricts.setAdapter(districtAdapter);
    }
    private void processDistricts(List<District> lstDistricts)
    {
        DistrictAdapter districtAdapter=new DistrictAdapter(lstDistricts,this);
        spnDistricts.setAdapter(districtAdapter);
    }
    private void fetchJobTitles() {
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
        Call<FetchJobRoleResponse> call=jobApiService.fetchJobRoles(haiJobApp.getRegisteredEmployer().getApikey());
        call.enqueue(new Callback<FetchJobRoleResponse>() {
            @Override
            public void onResponse(Call<FetchJobRoleResponse> call, Response<FetchJobRoleResponse> response) {

                if(response.isSuccessful())
                {
                    if(!response.body().getError())
                    {
                        List<JobRoles> jobRoles=response.body().getJobcategories();
                        processJobRoles(jobRoles);
                    }

                }



            }
            @Override
            public void onFailure(Call<FetchJobRoleResponse> call, Throwable throwable) {


            }
        });
    }

    private void processJobRoles(List<JobRoles> expDetails) {
        JobRoleAdapter jobaroleadapter=new JobRoleAdapter(expDetails,this);
        spnJobTitle.setAdapter(jobaroleadapter);


    }



}
