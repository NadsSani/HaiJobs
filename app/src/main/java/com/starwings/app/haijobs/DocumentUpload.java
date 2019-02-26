package com.starwings.app.haijobs;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.starwings.app.haijobs.api.ApiLinks;
import com.starwings.app.haijobs.components.FancyAlert.Animation;
import com.starwings.app.haijobs.components.FancyAlert.FancyAlertDialog;
import com.starwings.app.haijobs.components.FancyAlert.FancyAlertDialogListener;
import com.starwings.app.haijobs.components.FancyAlert.Icon;
import com.starwings.app.haijobs.responsePojo.ImageUploadResponse;
import com.starwings.app.haijobs.responsePojo.ResumeUploadResponse;
import com.starwings.app.haijobs.utils.FetchPath;
import com.starwings.app.haijobs.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okhttp3.logging.HttpLoggingInterceptor;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DocumentUpload extends HaiJobsActivity implements EasyPermissions.PermissionCallbacks{
    private static Retrofit retrofit = null;
    public static final int PICK_IMAGE = 100;
    public static final int SELECT_RESUME = 200;
    public static final int REQUEST_CODE=300;
    android.net.Uri selectedImage;
    android.net.Uri selectedFile;
    JobApiServices jobApiService;
    HaiJobApp haiJobApp;
    CircleImageView civprofile;
    ImageButton btnresume,btn;
    ProgressBar prgImage,prgResume;
    Button btnHome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploader);

        prgImage=(ProgressBar)findViewById(R.id.picuploadprogress);
        prgResume=(ProgressBar)findViewById(R.id.resumeuploadprogress);

        btnHome=(Button)findViewById(R.id.btnHome);
        btnHome.setVisibility(View.GONE);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHome();
            }
        });

        prgImage.getIndeterminateDrawable().setColorFilter(Color.BLUE, android.graphics.PorterDuff.Mode.MULTIPLY);
        prgResume.getIndeterminateDrawable().setColorFilter(Color.BLUE, android.graphics.PorterDuff.Mode.MULTIPLY);
         btn = (ImageButton) findViewById(R.id.imgPic);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(EasyPermissions.hasPermissions(DocumentUpload.this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
                }

            else
            {
                Log.e("HaiJobs","Not Permitted");
                EasyPermissions.requestPermissions(DocumentUpload.this,"This app needs permission to access your photos",REQUEST_CODE,Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            }
        });

         btnresume=(ImageButton)findViewById(R.id.imgresume) ;
        btnresume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EasyPermissions.hasPermissions(DocumentUpload.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    String[] mimeTypes =
                            {"application/msword", "application/pdf"
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
                        intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
                    }

                    startActivityForResult(Intent.createChooser(intent, "Choose File"), SELECT_RESUME);
                }
                else
                {
                    Log.e("HaiJobs","Not Permitted");
                    EasyPermissions.requestPermissions(DocumentUpload.this,"This app needs permission to access your storage",REQUEST_CODE,Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
        });

        civprofile=(CircleImageView)findViewById(R.id.img_profile);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("HaiJobs","Inside Activity Result");
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Log.e("HaiJobs","Request Matches");

            selectedImage = data.getData();
            if (selectedImage != null) {
                String filePath = FetchPath.getPath(this, selectedImage);


            try {
                Bitmap myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                myBitmap = new Utils().rotateImageIfRequired(myBitmap, Uri.parse(getPath(selectedImage)));
                myBitmap = new Utils().getResizedBitmap(myBitmap, 500);

                CircleImageView croppedImageView = (CircleImageView) findViewById(R.id.img_profile);
                croppedImageView.setImageBitmap(myBitmap);
                civprofile.setImageBitmap(myBitmap);

            } catch (Exception e) {

                showError("Unknown Error Occured. Try with another one");
                return;

            }


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
                uploadImageRequest(filePath);
            }

        }
        else if(requestCode == SELECT_RESUME && resultCode == Activity.RESULT_OK && data != null && data.getData() != null)
        {
            selectedFile = data.getData();
            uploadResumeRequest(selectedFile);


        }
        else
        {
            Log.e("HaiJobs","Request doesnot Matches");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private void uploadResumeRequest(Uri filePath)
    {

        if(!Utils.checkConnectivity(DocumentUpload.this))
        {
           showInfo("Your device is not connected to internet");
            return;
        }

        Log.e("HaiJobs","Inside upload resume");
        prgResume.setVisibility(View.VISIBLE);
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



        String pathofDocument=Utils.getPath(DocumentUpload.this,filePath);

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
        retrofit2.Call<ResumeUploadResponse> req = jobApiService.uploadResume(api_key,body);
        req.enqueue(new Callback<ResumeUploadResponse>() {
            @Override
            public void onResponse(Call<ResumeUploadResponse> call, Response<ResumeUploadResponse> response) {
                // Do Something
                if(response.isSuccessful())
                {
                    prgResume.setVisibility(View.GONE);
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
                prgResume.setVisibility(View.GONE);
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
        if((haiJobApp.getRegisteredUser().getPhotoStatus()==1)&&(haiJobApp.getRegisteredUser().getResumeStatus()==1))
        {
           showMessage("Registration Completed");
            showHome();

        }
        else
        {
            showInfo("Resume uploaded Successfully.");
        }
    }

    private void showHome() {
        Intent homeIntent=new Intent(this,JobSeekerHome.class);
        startActivity(homeIntent);
        finish();
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
        if((haiJobApp.getRegisteredUser().getPhotoStatus()==1)&&(haiJobApp.getRegisteredUser().getResumeStatus()==1))
        {

            showMessage("Registration Completed. ");
            showHome();

        }
        else {
            showInfo( "Profile Image Updated Successfully. ");
        }
    }


    private void uploadImageRequest(String filePath)
    {

        Log.e("HaiJobs","Inside upload image");
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);
        prgImage.setVisibility(View.VISIBLE);
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
       retrofit2.Call<ImageUploadResponse> req = jobApiService.postImage(api_key,body);
       req.enqueue(new Callback<ImageUploadResponse>() {
           @Override
            public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
               // Do Something
               Log.e("HaiJobs",response.isSuccessful()+"");
                if(response.isSuccessful())
                {
                    prgImage.setVisibility(View.GONE);
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
                prgImage.setVisibility(View.GONE);
                showError("Image upload failed. Try Later");
            }
        });
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if(selectedImage!=null)
        {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
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






}
