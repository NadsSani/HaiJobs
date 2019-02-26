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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.starwings.app.haijobs.api.ApiLinks;
import com.starwings.app.haijobs.components.FancyAlert.Animation;
import com.starwings.app.haijobs.components.FancyAlert.FancyAlertDialog;
import com.starwings.app.haijobs.components.FancyAlert.FancyAlertDialogListener;
import com.starwings.app.haijobs.components.FancyAlert.Icon;
import com.starwings.app.haijobs.data.Age;
import com.starwings.app.haijobs.data.Categories;
import com.starwings.app.haijobs.data.District;
import com.starwings.app.haijobs.data.QualificationData;
import com.starwings.app.haijobs.data.SubCategories;
import com.starwings.app.haijobs.data.User;
import com.starwings.app.haijobs.employerPages.adapter.DistrictAdapter;
import com.starwings.app.haijobs.employerPages.adapter.StatesAdapter;
import com.starwings.app.haijobs.responsePojo.RegisterResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JobSeekerRegister extends HaiJobsActivity {
    EditText edDob,edName,edmail,edpass;
    Spinner spnGender,spnState,spnDistrict;
    EditText edPin,edLocation;
    HaiJobApp haiJobApp;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    ImageButton imgadbtn;
    Button btnregister;
    EditText txtPhone;
    String categoryIDString;
    String subCategoryIDString;
    String edDisplayString;
    TextView edQualification;
    RadioGroup rgbExperience;
    List<District> currentDistrictList;
    int experienceLevel;
    private static Retrofit retrofit = null;
    JobApiServices jobApiService;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_seeker_registration_layout);
        haiJobApp=(HaiJobApp)getApplication();

        edDob=(EditText)findViewById(R.id.input_dob);
        edName=(EditText)findViewById(R.id.inp_name);
        edmail=(EditText)findViewById(R.id.input_email);
        edpass=(EditText)findViewById(R.id.input_pass);
        edLocation=(EditText)findViewById(R.id.input_place);
        edPin=(EditText)findViewById(R.id.input_pincode);
        spnState=(Spinner)findViewById(R.id.spnState);
        spnDistrict=(Spinner)findViewById(R.id.spnDistrict);

        processStates();

        rgbExperience=(RadioGroup)findViewById(R.id.rgbExperience);
        rgbExperience.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId)
                {
                    case R.id.rbFresher:
                        experienceLevel=0;
                        break;
                    case R.id.rbExperienced:
                        experienceLevel=1;
                        break;
                        default:
                            experienceLevel=0;
                            break;
                }
            }
        });

        edQualification=(TextView)findViewById(R.id.input_edu);
        edDisplayString="";

        spnGender=(Spinner)findViewById(R.id.spnGender);
        imgadbtn=(ImageButton)findViewById(R.id.imgadbtn);




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
                new DatePickerDialog(JobSeekerRegister.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        txtPhone=(EditText) findViewById(R.id.input_phone);

        imgadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQualificationDialog();
            }
        });


        btnregister=(Button)findViewById(R.id.registerbtn);
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDetails();
            }
        });


    }
    private void fetchDistrictsByState(int i) {
        Log.e("Error123","Clicked"+i);
        currentDistrictList=new ArrayList<District>();
        for(int j=0;j<haiJobApp.getDistrictsList().size();j++)
        {
            if(haiJobApp.getDistrictsList().get(j).getStatecode()==(i+1))
            {
                currentDistrictList.add(haiJobApp.getDistrictsList().get(j));
            }
        }
        DistrictAdapter districtAdapter=new DistrictAdapter(currentDistrictList,this);
        spnDistrict.setAdapter(districtAdapter);
    }
    private void processStates() {
        StatesAdapter statesAdapter=new StatesAdapter(haiJobApp.getStatesList(),this);
        spnState.setAdapter(statesAdapter);
        spnState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fetchDistrictsByState(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }
    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean isValidMobile(String phone) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length() < 6 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;
               // txtPhone.setError("Not Valid Number");
            } else {
                check = true;
            }
        } else {
            check=false;
        }
        return check;
    }
    private void submitDetails() {

     boolean status=false;
     if(isEmpty(edName))
     {
         Snackbar.make(btnregister,"You must enter your name",Snackbar.LENGTH_SHORT).show();
         status=false;
     }
     else if(isEmpty(edDob))
     {
         Snackbar.make(btnregister,"Specify your Date of Birth",Snackbar.LENGTH_SHORT).show();
         status=false;
     }
     else if(isEmpty(txtPhone))
     {
         Snackbar.make(btnregister,"Specify your Contact Number",Snackbar.LENGTH_SHORT).show();
         status=false;
     }
     else if(isEmpty(edmail))
     {
         Snackbar.make(btnregister,"Specify your Contact EMail",Snackbar.LENGTH_SHORT).show();
         status=false;
     }
     else if(isEmpty(edpass))
     {
         Snackbar.make(btnregister,"Enter your log in password",Snackbar.LENGTH_SHORT).show();
         status=false;
     }
     else if(isEmpty(edLocation))
     {
         Snackbar.make(btnregister,"Enter your location",Snackbar.LENGTH_SHORT).show();
         status=false;
     }
     else if(isEmpty(edPin))
     {
         Snackbar.make(btnregister,"Enter your pincode",Snackbar.LENGTH_SHORT).show();
         status=false;
     }
     else if(isValidPostalCode(edPin.getText().toString()))
     {
         Snackbar.make(btnregister,"Enter a valid Pincode",Snackbar.LENGTH_SHORT).show();
         status=false;
     }
     else if(!isValidEmail(edmail.getText().toString()))
     {
         Snackbar.make(btnregister,"Enter a valid EMail",Snackbar.LENGTH_SHORT).show();
         status=false;
     }
     else if(!isValidMobile(txtPhone.getText().toString()))
     {
         Snackbar.make(btnregister,"Enter a valid Contact Number",Snackbar.LENGTH_SHORT).show();
         status=false;
     }
     else if(!(calculateAge(edDob.getText().toString()).getYears()>14))
     {
         Snackbar.make(btnregister,"Check the Date of Birth",Snackbar.LENGTH_SHORT).show();
         status=false;
     }
     else if((haiJobApp.getMainselectionList().size()==0))
     {
         Snackbar.make(btnregister,"Select your qualification",Snackbar.LENGTH_SHORT).show();
         status=false;
     }
        if(!checkConnectivity())
        {
            Snackbar.make(btnregister, "Your device is not connected to internet", Snackbar.LENGTH_SHORT).show();
            return;
        }

        else
     {
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



     jobApiService = retrofit.create(JobApiServices.class);
     Call<RegisterResponse> call=jobApiService.insertUser(edName.getText().toString(),
             edDob.getText().toString(),
             spnGender.getSelectedItemPosition()+1,
             categoryIDString, subCategoryIDString, txtPhone.getText().toString(), edmail.getText().toString(),
             edpass.getText().toString(),experienceLevel,edLocation.getText().toString(),currentDistrictList.get(spnDistrict.getSelectedItemPosition()).getId(),spnState.getSelectedItemPosition()+1,edPin.getText().toString());
     call.enqueue(new Callback<RegisterResponse>() {
         @Override
         public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

             if(response.isSuccessful())
             {
                 if(response.body().getStatus()==0)
                 {
                     User registeredUser=response.body().getUser().get(0);
                     processUserData(registeredUser);
                 }
                 else if(response.body().getStatus()==1)
                 {
                     showError("Registration Failed");
                 }
                 else if(response.body().getStatus()==2)
                 {
                     showError("User already exists");
                 }
                 else
                 {
                     showError(response.message());
                 }
             }



         }
         @Override
         public void onFailure(Call<RegisterResponse> call, Throwable throwable) {
             showError("Unknown Error Occured");

         }
     });

 }




    }
    private static Age calculateAge(String birthDate)
    {
        int years = 0;
        int months = 0;
        int days = 0;

        //create calendar object for birth day
        String birthArray[]=birthDate.split("/");
        Calendar birthDay = Calendar.getInstance();
        birthDay.set(Calendar.DATE,Integer.parseInt(birthArray[0]));
        birthDay.set(Calendar.MONTH,Integer.parseInt(birthArray[1])-1);
        birthDay.set(Calendar.YEAR,Integer.parseInt(birthArray[2]));

        //create calendar object for current day
        long currentTime = System.currentTimeMillis();
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTime);

        //Get difference between years
        years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        int currMonth = now.get(Calendar.MONTH) + 1;
        int birthMonth = birthDay.get(Calendar.MONTH) + 1;

        //Get difference between months
        months = currMonth - birthMonth;

        //if month difference is in negative then reduce years by one
        //and calculate the number of months.
        if (months < 0)
        {
            years--;
            months = 12 - birthMonth + currMonth;
            if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
                months--;
        } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
        {
            years--;
            months = 11;
        }

        //Calculate the days
        if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
            days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
        else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
        {
            int today = now.get(Calendar.DAY_OF_MONTH);
            now.add(Calendar.MONTH, -1);
            days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
        }
        else
        {
            days = 0;
            if (months == 12)
            {
                years++;
                months = 0;
            }
        }
        //Create new Age object
        return new Age(days, months, years);
    }

    private boolean checkConnectivity()
    {
        boolean status=false;
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                status=true;
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                status=true;
        }
        else
            status=false;
        return status;
    }
   private boolean isValidPostalCode(String code) {
        // must have 6 digits
        if(code.length() != 6)
            return false;

        // make if uppercase for not having to chack for A to Z AND a to z
        code = code.toUpperCase();
        // translate into digit
        char[] digit = code.toCharArray();

        for(int i = 0; i < 4; ++i) {
            if(digit[i] < '0' || digit[i] > '9')
                return false;
        }
        for(int i = 4; i < 6; ++i) {
            if(digit[i] < 'A' || digit[i] > 'Z')
                return false;
        }
        // sounds OK to me
        return true;
    }


    private void processUserData(User registeredUser)
    {
       showMessage("Registered Successfully");
        SharedPreferences userPreferences=getSharedPreferences("USER_PREG",MODE_PRIVATE);
        SharedPreferences.Editor prefEdit=userPreferences.edit();
        prefEdit.putString("apikey",registeredUser.getApiKey());
        prefEdit.putString("username",registeredUser.getContactno());
        prefEdit.putString("email",registeredUser.getEmail());
        prefEdit.putString("name",registeredUser.getName());
        prefEdit.putString("dob",registeredUser.getDob());
        prefEdit.putInt("gender",registeredUser.getGender());
        prefEdit.putString("qualification",registeredUser.getQualification());
        prefEdit.putString("streams",registeredUser.getStreams());
        prefEdit.putString("pass",registeredUser.getPassword());
        prefEdit.putString("profile",registeredUser.getProfilepic());
        prefEdit.putString("resumepath",registeredUser.getResumeloc());
        prefEdit.putInt("phverified",registeredUser.getPhonestatus());
        prefEdit.putInt("emverified",registeredUser.getEmailstatus());
        prefEdit.putInt("active",registeredUser.getActive());
        prefEdit.putInt("photostatus",registeredUser.getPhotoStatus());
        prefEdit.putInt("resumestatus",registeredUser.getResumeStatus());
        prefEdit.putInt("experiencelevel",registeredUser.getExperiencelevel());
        prefEdit.putString("place",registeredUser.getPlace());
        prefEdit.putInt("district",registeredUser.getDistrict());
        prefEdit.putInt("state",registeredUser.getState());
        prefEdit.putString("pincode",registeredUser.getPincode());
        prefEdit.commit();
        haiJobApp.setRegisteredUser(registeredUser);
        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method

            @Override
            public void run() {

                showOTPPage();
            }
        }, 1*1000); // wait for 5 seconds


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
    private void showOTPPage() {

        Intent phoneverification=new Intent(this,RegistrationPhoneEntryActivity.class);
        phoneverification.putExtra("phone",txtPhone.getText());
        startActivity(phoneverification);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // check if the request code is same as what is passed  here it is 2
        if(requestCode==1001)
        {
            getDistinctMainStreams();

        }
    }
    private void showQualificationDialog() {

       Intent qualificationdlg=new Intent(this,QualificationDialog.class);
       startActivityForResult(qualificationdlg,1001);

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edDob.setText(sdf.format(myCalendar.getTime()));
    }


}
