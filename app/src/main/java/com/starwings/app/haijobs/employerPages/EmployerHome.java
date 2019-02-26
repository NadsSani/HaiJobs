package com.starwings.app.haijobs.employerPages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.clans.fab.FloatingActionButton;
import com.starwings.app.haijobs.AboutPageScreen;
import com.starwings.app.haijobs.AppHomePage;
import com.starwings.app.haijobs.HaiJobApp;
import com.starwings.app.haijobs.HaiJobsActivity;
import com.starwings.app.haijobs.R;
import com.starwings.app.haijobs.components.FancyAlert.Animation;
import com.starwings.app.haijobs.components.FancyAlert.FancyAlertDialog;
import com.starwings.app.haijobs.components.FancyAlert.Icon;

public class EmployerHome extends HaiJobsActivity {
    Button btnViewProf,btnViewAcc;
    HaiJobApp haiJobApp;
    FloatingActionButton btnPostJob,btnLogOut,btnAboutUs;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_home);

        btnLogOut=(FloatingActionButton)findViewById(R.id.menu_logout);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        btnViewProf=(Button)findViewById(R.id.btnViewProf);
        btnViewProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileListingPage();
            }
        });

        btnViewAcc=(Button)findViewById(R.id.btnViewAcc);
        btnViewAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmployerProfile();
            }
        });

        btnPostJob=(FloatingActionButton)findViewById(R.id.menu_post_job);
        btnPostJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showJobPostingPage();
            }
        });

        btnAboutUs=(FloatingActionButton)findViewById(R.id.menu_about_us);
        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutPage();
            }
        });

        haiJobApp=(HaiJobApp)getApplication();
    }

    private void showAboutPage() {
        Intent aboutpage=new Intent(this,AboutPageScreen.class);
        startActivity(aboutpage);
    }

    private void showEmployerProfile() {
        Intent empProfile=new Intent(this,EmployerProfile.class);
        startActivity(empProfile);
    }

    private void showJobPostingPage() {
        if((haiJobApp.getRegisteredEmployer().getSubscribed()==0))
        {
            showError("Please Subscribe for viewing the profiles");
            return;
        }
        Intent profilesIntent=new Intent(this,JobPosting.class);
        startActivity(profilesIntent);
    }

    private void showProfileListingPage() {
        if((haiJobApp.getRegisteredEmployer().getSubscribed()==0))
        {
            showError("Please Subscribe for viewing the profiles");
            return;
        }
        Intent profilesIntent=new Intent(this,ProfilesListing.class);
        startActivity(profilesIntent);
    }


    private void logout() {

        SharedPreferences userPreferences=getSharedPreferences("USER_PREG",MODE_PRIVATE);
        SharedPreferences.Editor userPrefEdit=userPreferences.edit();
        userPrefEdit.putInt("candidStatus",0);
        userPrefEdit.commit();
        Intent homePageIntent=new Intent(this,AppHomePage.class);
        homePageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homePageIntent);
        finish();
    }
}
