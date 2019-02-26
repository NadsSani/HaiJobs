package com.starwings.app.haijobs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.starwings.app.haijobs.components.CircleButton;
import com.starwings.app.haijobs.data.Employer;
import com.starwings.app.haijobs.data.User;
import com.starwings.app.haijobs.employerPages.EmployerLogin;

public class AppHomePage extends HaiJobsActivity {
    CircleButton img_job_seeker,img_recruiter;
    HaiJobApp haiJobApp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        img_job_seeker=(CircleButton)findViewById(R.id.img_job_seeker);
        img_job_seeker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showJobSeekerLogin();
            }
        });
        img_recruiter=(CircleButton)findViewById(R.id.img_recruiter);
        img_recruiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEmployerLogin();
            }
        });

    }

    private void showEmployerLogin() {

        SharedPreferences employerPreference=getSharedPreferences("EMPLOYER_PREF",MODE_PRIVATE);
        Employer currentEmployer=new Employer();

        currentEmployer.setApikey(employerPreference.getString("apikey","NA"));
        currentEmployer.setEmployerName(employerPreference.getString("employername","NA"));
        currentEmployer.setAddressLine1(employerPreference.getString("addressline1","NA"));
        currentEmployer.setAddressLine2(employerPreference.getString("addressline2","NA"));
        currentEmployer.setPincode(employerPreference.getString("pincode","NA"));
        currentEmployer.setEmployerContact(employerPreference.getString("employerContact","NA"));
        currentEmployer.setEmployerMail(employerPreference.getString("employermail","NA"));
        currentEmployer.setEmployerWeb(employerPreference.getString("employerWeb","NA"));
        currentEmployer.setEmpLoginID(employerPreference.getString("employerLogID","NA"));
        currentEmployer.setEmpPassword(employerPreference.getString("employerPass","NA"));
        currentEmployer.setPersonToContact(employerPreference.getString("contactperson","NA"));
        currentEmployer.setNumberToContact(employerPreference.getString("numbertocontact","NA"));
        currentEmployer.setShortDecription(employerPreference.getString("shortdesc","NA"));
        currentEmployer.setEmpdigest(employerPreference.getString("empdigest","NA"));
        currentEmployer.setSubscribed(employerPreference.getInt("subscribed",0));

        haiJobApp=(HaiJobApp)getApplication();
        haiJobApp.setRegisteredEmployer(currentEmployer);

        Intent employerLogin=new Intent(this,EmployerLogin.class);
        startActivity(employerLogin);
        finish();
    }

    private void showJobSeekerLogin() {

        SharedPreferences userPreferences=getSharedPreferences("USER_PREG",MODE_PRIVATE);
        User currentUser=new User();

        currentUser.setApiKey(userPreferences.getString("apikey","NA"));
        currentUser.setContactno(userPreferences.getString("username","NA"));
        currentUser.setEmail(userPreferences.getString("email","NA"));
        currentUser.setName(userPreferences.getString("name","NA"));
        currentUser.setDob(userPreferences.getString("dob","NA"));
        currentUser.setGender(userPreferences.getInt("gender",-1));
        currentUser.setQualification(userPreferences.getString("qualification","NA"));
        currentUser.setStreams(userPreferences.getString("streams","NA"));
        currentUser.setPassword(userPreferences.getString("pass","NA"));
        currentUser.setProfilepic(userPreferences.getString("profile","NA"));
        currentUser.setResumeloc(userPreferences.getString("resumepath","NA"));
        currentUser.setPhonestatus(userPreferences.getInt("phverified",0));
        currentUser.setEmailstatus(userPreferences.getInt("emverified",0));
        currentUser.setActive(userPreferences.getInt("active",0));
        currentUser.setPhotoStatus(userPreferences.getInt("photostatus",0));
        currentUser.setResumeStatus(userPreferences.getInt("resumestatus",0));
        currentUser.setExperiencelevel(userPreferences.getInt("experiencelevel",0));


        haiJobApp=(HaiJobApp)getApplication();
        haiJobApp.setRegisteredUser(currentUser);
        Intent jobSeekerLogin=new Intent(this,JobSeekerLogin.class);
        startActivity(jobSeekerLogin);
        finish();
    }
}
