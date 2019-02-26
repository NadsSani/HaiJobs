package com.starwings.app.haijobs.employerPages;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.starwings.app.haijobs.AddressDialog;
import com.starwings.app.haijobs.AppHomePage;
import com.starwings.app.haijobs.HaiJobApp;
import com.starwings.app.haijobs.HaiJobsActivity;
import com.starwings.app.haijobs.JobApiServices;
import com.starwings.app.haijobs.PersonalInfoDialog;
import com.starwings.app.haijobs.ProfileExperienceInfo;
import com.starwings.app.haijobs.ProfileImageViewer;
import com.starwings.app.haijobs.ProfileSettingsDialog;
import com.starwings.app.haijobs.QualificationDialog;
import com.starwings.app.haijobs.R;
import com.starwings.app.haijobs.adapter.CandidateDisplayAdapter;
import com.starwings.app.haijobs.adapter.ExperienceDisplayAdapter;
import com.starwings.app.haijobs.api.ApiLinks;
import com.starwings.app.haijobs.components.FancyAlert.Animation;
import com.starwings.app.haijobs.components.FancyAlert.FancyAlertDialog;
import com.starwings.app.haijobs.components.FancyAlert.Icon;
import com.starwings.app.haijobs.data.Categories;
import com.starwings.app.haijobs.data.Experience;
import com.starwings.app.haijobs.data.QualificationData;
import com.starwings.app.haijobs.data.State;
import com.starwings.app.haijobs.data.SubCategories;
import com.starwings.app.haijobs.data.User;
import com.starwings.app.haijobs.employerPages.adapter.StatesAdapter;
import com.starwings.app.haijobs.responsePojo.FetchExperienceResponse;
import com.starwings.app.haijobs.responsePojo.FetchStatesResponse;
import com.starwings.app.haijobs.responsePojo.ImageUploadResponse;
import com.starwings.app.haijobs.responsePojo.ResumeUploadResponse;
import com.starwings.app.haijobs.responsePojo.StatusChangeResponse;
import com.starwings.app.haijobs.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CandidateProfile extends HaiJobsActivity implements EasyPermissions.PermissionCallbacks {
    HaiJobApp haiJobApp;
    TextView txname,txtdob,txtgender,txcontact,txemail,txtplace,txtdistrict,txtstate,txtpin;
    ImageButton imgLogout,imgexperience,imgpersonal,imgsettings,imgresume,imgqualification,imgaddress;
    User registeredUser;
    Switch activeswitch;
    TextView txtExpStatus;
    private static Retrofit retrofit = null;
    JobApiServices jobApiService;
    RecyclerView expList;
    LinearLayoutManager layoutManager;
    CandidateDisplayAdapter adapter;
    ImageButton imgselectimage;
    public static final int PICK_IMAGE = 100;
    public static final int REQUEST_CODE=300;
    Uri selectedImage;
    Uri selectedFile;
    ImageView img_profile;
    String categoryIDString;
    String subCategoryIDString;
    String edDisplayString;
    Button btnback,btnresume;
    private ArrayList<Experience> experienceArrayList;

    public static final int SELECT_RESUME = 200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate_profile);

            experienceArrayList=new ArrayList<Experience>();
            img_profile = (ImageView) findViewById(R.id.img_profile);
            expList = (RecyclerView) findViewById(R.id.expList);
            haiJobApp=(HaiJobApp)getApplication();



            registeredUser=(User)getIntent().getSerializableExtra("SelectedUser");

            if(ApiLinks.imgbasepath + registeredUser.getProfilepic()!="NA") {
                Glide.with(this)
                        .load(ApiLinks.imgbasepath + registeredUser.getProfilepic())

                        .into(img_profile);


                img_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showImage(ApiLinks.imgbasepath + registeredUser.getProfilepic());
                    }
                });
            }
            else
            {
                img_profile.setImageResource(R.drawable.nophoto);
            }

            txname=(TextView)findViewById(R.id.txtdspname);
            txtdob=(TextView)findViewById(R.id.txtdspdob);
            txtgender=(TextView)findViewById(R.id.txtdsgender);
            txcontact=(TextView)findViewById(R.id.txtdspcontact);
            txemail=(TextView)findViewById(R.id.txtdspemail);
            txtExpStatus=(TextView)findViewById(R.id.txtExpStatus);

            txtplace=(TextView)findViewById(R.id.txtplace);
            txtdistrict=(TextView)findViewById(R.id.txtdistrict);


            txtstate=(TextView)findViewById(R.id.txtstate);
            txtpin=(TextView)findViewById(R.id.txtpin);



            txname.setText(registeredUser.getName());
            txtdob.setText(registeredUser.getDob());

            btnback=(Button)findViewById(R.id.edBackButton);
            btnback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            txtgender.setText(findGender(registeredUser.getGender()));
            txcontact.setText(registeredUser.getContactno());
            txemail.setText(registeredUser.getEmail());

            txtplace.setText(registeredUser.getPlace());
            txtpin.setText(registeredUser.getPincode());
            txtstate.setText(haiJobApp.getStatesList().get((registeredUser.getState()-1)).getStatename());
            txtdistrict.setText(haiJobApp.getDistrictsList().get(registeredUser.getDistrict()-1).getDistrictname());
            if(registeredUser.getExperiencelevel()==0)
            {
                txtExpStatus.setText("FRESHER");

            }
            else
            {
                if(experienceArrayList.isEmpty())
                {
                    checkExperiencedDetails(registeredUser.getApiKey());
                }
                else
                {
                    txtExpStatus.setVisibility(View.GONE);
                    expList.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(this);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    expList.setLayoutManager(layoutManager);
                    expList.setItemAnimator(new DefaultItemAnimator());
                    adapter = new CandidateDisplayAdapter(experienceArrayList,this);
                    expList.setAdapter(adapter);
                }

            }

            populateQualification();


        btnresume=(Button)findViewById(R.id.btnViewResume);
        btnresume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResume();
            }
        });


    }




    private void showResume() {

        Intent resumeIntent=new Intent(this,ResumeDisplay.class);
        resumeIntent.putExtra("fileLink",ApiLinks.imgbasepath+registeredUser.getResumeloc());
        startActivity(resumeIntent);
    }

    private void showAddressEditPage() {

        Intent addressdialog=new Intent(this,AddressDialog.class);
        startActivityForResult(addressdialog,1002);
    }

    private void showQualificationDialog() {

        Intent qualificationdlg=new Intent(this,QualificationDialog.class);
        startActivityForResult(qualificationdlg,1001);

    }
    private void showFiles() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String[] mimeTypes =
                {"application/msword","application/pdf"
                };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
        }

        startActivityForResult(Intent.createChooser(intent, "Choose File"), SELECT_RESUME);


    }

    private void showSettingsPage() {
        Intent expIntent=new Intent(this,ProfileSettingsDialog.class);
        startActivityForResult(expIntent,1030);
    }

    private void showPersonalInfoEditPage() {
        Intent perIntent=new Intent(this,PersonalInfoDialog.class);
        startActivityForResult(perIntent,1020);
    }


    private void showExperienceEditPage() {
        Intent expIntent=new Intent(this,ProfileExperienceInfo.class);
        startActivityForResult(expIntent,1010);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("HaiJobs", "Inside Activity Result");
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Log.e("HaiJobs", "Request Matches");

            selectedImage = data.getData();

            try {
                Bitmap myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                myBitmap = new Utils().rotateImageIfRequired(myBitmap, Uri.parse(getPath(selectedImage)));
                myBitmap = new Utils().getResizedBitmap(myBitmap, 500);
                img_profile.setImageBitmap(myBitmap);

            } catch (Exception e) {

                showError("Unknown Error Occured. Try with another one");
                return;

            }

            if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Log.e("HaiJobs", "Already Permitted");
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor == null) {
                    Log.e("HaiJobs", "No Cursor");
                    return;
                }

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();
                Log.e("HaiJobs", "FilePath" + filePath);
                uploadImageRequest(filePath);
            }

            else {
                Log.e("HaiJobs", "Not Permitted");
                EasyPermissions.requestPermissions(CandidateProfile.this, "This app needs permission to access your photos", REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }

        }
        else if(requestCode == SELECT_RESUME && resultCode == Activity.RESULT_OK && data != null && data.getData() != null)
        {
            selectedFile = data.getData();

            if(EasyPermissions.hasPermissions(this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {
//                Log.e("HaiJobs","Already Permitted");
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                if (cursor == null) {
//                    Log.e("HaiJobs","No Cursor");
//                    return;
//                }
//
//                cursor.moveToFirst();
//
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                String filePath = cursor.getString(columnIndex);
//                cursor.close();
//                Log.e("HaiJobs","FilePath"+filePath);
                uploadResumeRequest(selectedFile);
            }
            else
            {
                Log.e("HaiJobs","Not Permitted");
                EasyPermissions.requestPermissions(CandidateProfile.this,"This app needs permission to access your photos",REQUEST_CODE,Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
        else  if(requestCode==1001  )
        {
            getDistinctMainStreams();

        }
        else  if(requestCode==1002 )
        {
            updateAddress();

        }
        else if(requestCode==1010)
        {
            updateExperience();
        }
        else if(requestCode==1020)
        {
            updatePersonalInfo();
        }
        else if(requestCode==1030)
        {
            updateSettings();
        }
        else
        {
            Log.e("HaiJobs","Request doesnot Matches");
        }
    }

    private void updateSettings() {

    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private void updatePersonalInfo() {
        txtdob.setText(registeredUser.getDob());

        txtgender.setText(findGender(registeredUser.getGender()));
        txcontact.setText(registeredUser.getContactno());
        txemail.setText(registeredUser.getEmail());
    }

    private void updateExperience() {
        if(registeredUser.getExperiencelevel()==0)
        {
            txtExpStatus.setText("FRESHER");
            expList.setVisibility(View.GONE);
            txtExpStatus.setVisibility(View.VISIBLE);
        }
        else
        {

            expList.setVisibility(View.VISIBLE);
            txtExpStatus.setVisibility(View.GONE);
                checkExperiencedDetails(haiJobApp.getRegisteredUser().getApiKey());

//            else
//            {
//                txtExpStatus.setVisibility(View.GONE);
//                expList = (RecyclerView) findViewById(R.id.expList);
//                expList.setHasFixedSize(true);
//                layoutManager = new LinearLayoutManager(this);
//
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                expList.setLayoutManager(layoutManager);
//                expList.setItemAnimator(new DefaultItemAnimator());
//                adapter = new ExperienceDisplayAdapter(haiJobApp.getExperienceArrayList(),this);
//                expList.setAdapter(adapter);
//            }

        }


    }

    private void updateAddress() {

        txtplace.setText(registeredUser.getPlace());
        txtpin.setText(registeredUser.getPincode());

        txtstate.setText(haiJobApp.getStatesList().get(registeredUser.getState()-1).getStatename());
        txtdistrict.setText(haiJobApp.getDistrictsList().get(registeredUser.getDistrict()).getDistrictname());
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
    private void getDistinctMainStreams()
    {

        ArrayList idlist=new ArrayList();
        for(int i=0;i<haiJobApp.getSelectioncatlist().size();i++)
        {
            idlist.add(haiJobApp.getSelectioncatlist().get(i).getParentID());
        }
        Set<String> hs = new HashSet<>();
        hs.addAll(idlist);
        idlist.clear();
        idlist.addAll(hs);

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

        if(haiJobApp.getMainselectionList().isEmpty())
        {
            return;
        }
        categoryIDString="";
        for(int i=0;i<haiJobApp.getMainselectionList().size();i++)
        {
            categoryIDString=categoryIDString+haiJobApp.getMainselectionList().get(i).getCategoryID()+",";
        }
        categoryIDString=categoryIDString.substring(0,categoryIDString.length()-1);

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
        haiJobApp.getRegisteredUser().setQualification(categoryIDString);
        haiJobApp.getRegisteredUser().setStreams(subCategoryIDString);

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

        SharedPreferences userPreferences=getSharedPreferences("USER_PREG",MODE_PRIVATE);

        Call<StatusChangeResponse> call=jobApiService.updateQualification(categoryIDString,subCategoryIDString,userPreferences.getString("apikey","NA"));
        call.enqueue(new Callback<StatusChangeResponse>() {
            @Override
            public void onResponse(Call<StatusChangeResponse> call, Response<StatusChangeResponse> response) {

                if(response.isSuccessful())
                {
                    updatePreferences();
                    if(response.body().getError())
                    {
                        showMessage(response.body().getMessage());

                    }
                    else
                    {
                        showError(response.body().getMessage());
                    }
                }



            }
            @Override
            public void onFailure(Call<StatusChangeResponse> call, Throwable throwable) {
                showError("Unknown Error Occured");

            }
        });






    }

    private void updatePreferences() {

        SharedPreferences userPreferences=getSharedPreferences("USER_PREG",MODE_PRIVATE);
        SharedPreferences.Editor prefEdit=userPreferences.edit();
        prefEdit.putString("qualification",registeredUser.getQualification());
        prefEdit.putString("streams",registeredUser.getStreams());
        prefEdit.commit();
        populateQualification();
    }

    private void uploadResumeRequest(Uri filePath)
    {

        Log.e("HaiJobs","Inside upload resume");
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


        String pathofDocument=Utils.getPath(CandidateProfile.this,filePath);

        File file = new File(pathofDocument);
        haiJobApp=(HaiJobApp)getApplication();

        SharedPreferences userPreferences=getSharedPreferences("USER_PREG",MODE_PRIVATE);



        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("resume", file.getName(), requestFile);

// add another part within the multipart request
        RequestBody api_key =
                RequestBody.create(MediaType.parse("multipart/form-data"), userPreferences.getString("apikey","NA"));




//
        Log.e("HaiJobs","Inside upload image call");
        Call<ResumeUploadResponse> req = jobApiService.uploadResume(api_key,body);
        req.enqueue(new Callback<ResumeUploadResponse>() {
            @Override
            public void onResponse(Call<ResumeUploadResponse> call, Response<ResumeUploadResponse> response) {
                // Do Something
                if(response.isSuccessful())
                {

                    processResumeResult(response);

                }
                else
                {
                   showError("Resume upload failed. Try Later");
                }


            }

            @Override
            public void onFailure(Call<ResumeUploadResponse> call, Throwable t) {
                t.printStackTrace();
               showError("Resume upload failed. Try Later");

            }
        });
    }

    private void processResumeResult(Response<ResumeUploadResponse> response) {

        SharedPreferences userPreferences=getSharedPreferences("USER_PREG",MODE_PRIVATE);
        SharedPreferences.Editor prefEdit=userPreferences.edit();
        prefEdit.putInt("resumestatus",1);
        prefEdit.putString("resumepath",response.body().getFilepath());
        prefEdit.commit();
        HaiJobApp haiJobApp=(HaiJobApp)getApplication();
        haiJobApp.getRegisteredUser().setResumeStatus(1);
        haiJobApp.getRegisteredUser().setResumeloc(response.body().getFilepath());

        showMessage("Resume uploaded Successfully.");
        //Snackbar.make(imgresume, "Resume uploaded Successfully.", Snackbar.LENGTH_SHORT).show();

    }
    private void uploadImageRequest(String filePath)
    {

        Log.e("HaiJobs","Inside upload image");
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



        File file = new File(filePath);
        haiJobApp=(HaiJobApp)getApplication();

        SharedPreferences userPreferences=getSharedPreferences("USER_PREG",MODE_PRIVATE);



        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

// add another part within the multipart request
        RequestBody api_key =
                RequestBody.create(MediaType.parse("multipart/form-data"), userPreferences.getString("apikey","NA"));




//
        Log.e("HaiJobs","Inside upload image call");
        Call<ImageUploadResponse> req = jobApiService.postImage(api_key,body);
        req.enqueue(new Callback<ImageUploadResponse>() {
            @Override
            public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
                // Do Something
                Log.e("HaiJobs",response.isSuccessful()+"");
                if(response.isSuccessful())
                {

                    processImageResult(response);
                }
                else
                {
                   showError("Image upload failed. Try Later");
                }

            }

            @Override
            public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
                t.printStackTrace();

                showError("Image upload failed. Try Later");
            }
        });
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if(selectedImage!=null)
        {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            uploadImageRequest(filePath);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.e("HaiJobs","Permission Denied");
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

                processExperienceDisplayResponse(response);


            }
            @Override
            public void onFailure(Call<FetchExperienceResponse> call, Throwable throwable) {
                showError("Unknown Error Occured");

            }
        });
    }

    private void processExperienceDisplayResponse(Response<FetchExperienceResponse> response) {

        if(response.isSuccessful())
        {
            if(response.body().getCount()!=0)
            {
                List<Experience> expDetails=response.body().getExperience();
                processExperienceData(expDetails);
            }
            else if(response.body().getCount()==0)
            {
                txtExpStatus.setText("Experience were not mentioned");
                txtExpStatus.setVisibility(View.VISIBLE);
            }
            else
            {
                txtExpStatus.setText(response.message());
                txtExpStatus.setVisibility(View.VISIBLE);
            }
        }

    }

    private void processImageResult(Response<ImageUploadResponse> response) {

        SharedPreferences userPreferences=getSharedPreferences("USER_PREG",MODE_PRIVATE);
        SharedPreferences.Editor prefEdit=userPreferences.edit();
        prefEdit.putInt("photostatus",1);
        prefEdit.putString("profile",response.body().getFilepath());
        prefEdit.commit();

        HaiJobApp haiJobApp=(HaiJobApp)getApplication();
        haiJobApp.getRegisteredUser().setPhotoStatus(1);
        haiJobApp.getRegisteredUser().setProfilepic(response.body().getFilepath());

        try {
            if (haiJobApp != null) {
                if (haiJobApp.getRegisteredUser() != null) {
                    if (!haiJobApp.getRegisteredUser().getProfilepic().equals("NA")) {
                        Glide.with(this).load(ApiLinks.imgbasepath + haiJobApp.getRegisteredUser().getProfilepic()).into(img_profile);
                    }
                }
            }
        }catch (Exception et)
        {
            Log.e("HaiJobs","EXception");
        }

        showMessage("Profile Image Updated Successfully. ");

    }
    private void processExperienceData(List<Experience> expDetails) {

       experienceArrayList.clear();
        for(int i=0;i<expDetails.size();i++)
        {
            experienceArrayList.add(expDetails.get(i));
        }
        if(experienceArrayList.isEmpty())
        {
            txtExpStatus.setText("Experience are not mentioned");
        }
        else
        {
            txtExpStatus.setVisibility(View.GONE);
        }
        expList = (RecyclerView) findViewById(R.id.expList);
        expList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        expList.setLayoutManager(layoutManager);
        expList.setItemAnimator(new DefaultItemAnimator());
        adapter = new CandidateDisplayAdapter(experienceArrayList,this);
        expList.setAdapter(adapter);
    }

    private void showImage(String path) {

        Intent photodlg=new Intent(this,ProfileImageViewer.class);
        photodlg.putExtra("Path",path);
        startActivity(photodlg);

    }

    private void logout() {

        SharedPreferences userPreferences=getSharedPreferences("USER_PREG",MODE_PRIVATE);
        SharedPreferences.Editor prefEdit=userPreferences.edit();
        prefEdit.putInt("candidStatus",0);
        prefEdit.commit();
        Intent homePageIntent=new Intent(this,AppHomePage.class);
        homePageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homePageIntent);
        finish();
    }

    private String findGender(int genderid)
    {
        String gender="";
        switch (genderid){
            case 1:
                gender="MALE";
                break;
            case 2:
                gender="FEMALE";
                break;
             default:
                 gender="Not Specified";
                 break;

        }
        return gender;
    }

    private void populateQualification()
    {


        ArrayList<QualificationData> tmpfull=haiJobApp.getQualificationData();

        ArrayList<String> result=new ArrayList<String>();

        String mainCategories[]=registeredUser.getQualification().split(",");

        String streams[]=registeredUser.getStreams().split(",");



        for(int i=0;i<tmpfull.size();i++)
        {
            Categories tmpcat=tmpfull.get(i).getCategoryObject();
            String catText="";
            for(int j=0;j<mainCategories.length;j++)
            {
                if(tmpcat.getCategoryID()==Integer.parseInt(mainCategories[j].trim()))
                {
                    catText=catText+tmpcat.getCategoryName();
                    if(tmpcat.getHasSub()==1)
                    {
                        if(!registeredUser.getStreams().equals("NA"))
                        {
                            ArrayList<SubCategories> tmpsubcat=tmpfull.get(i).getSubCategoryList();
                            for(int k=0;k<tmpsubcat.size();k++)
                            {
                                SubCategories tmpsubcatitem=tmpsubcat.get(k);
                                for(int l=0;l<streams.length;l++)
                                {
                                    String subcatText="";
                                    if(tmpsubcatitem.getCategoryID()==Integer.parseInt(streams[l].trim()))
                                    {
                                        subcatText= catText+" "+tmpsubcatitem.getCategoryName();
                                        result.add(subcatText);
                                    }
                                }
                            }
                        }

                    }
                    else
                    {
                        result.add(catText);
                    }


                }
            }
        }


        LinearLayout edlayout=(LinearLayout)findViewById(R.id.layout_education);
        edlayout.removeAllViews();


        for (int i = 0; i < result.size(); i++) {
            // create a new textview
            final TextView rowTextView = new TextView(this);

            // set some properties of rowTextView or something
            rowTextView.setText(result.get(i));

            rowTextView.setPadding(10,10,10,10);

            rowTextView.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
            // add the textview to the linearlayout
            edlayout.addView(rowTextView);

            // save a reference to the textview for later


            View v = new View(this);
            v.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1
            ));
            v.setBackgroundColor(Color.parseColor("#ec262e"));

            edlayout.addView(v);

        }



    }


}
