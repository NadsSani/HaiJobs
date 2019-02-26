package com.starwings.app.haijobs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class JobSeekerHome extends HaiJobsActivity {
    ImageButton imbProfile,imgabout,imglogout;
    CardView cardProfile;
    CardView cardlogout;
    private ViewFlipper mViewFlipper;
    HaiJobApp haijobs;
    int currentItem=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_job_seeker);

        haijobs=(HaiJobApp)getApplication();


        cardProfile=(CardView)findViewById(R.id.profile_card);
        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfilePage();
            }
        });

        imglogout=(ImageButton)findViewById(R.id.imglogout);
        imglogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        imgabout=(ImageButton)findViewById(R.id.imgabout);
        imbProfile=(ImageButton)findViewById(R.id.imgprofilebutton);
        imbProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfilePage();
            }
        });
        imgabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutPage();
            }
        });

        mViewFlipper=(ViewFlipper)findViewById(R.id.alertFlipper);
        mViewFlipper.setAutoStart(true);
        mViewFlipper.setFlipInterval(10000);

        createAlertViews();

    }

    private void createAlertViews()
    {
        String missingCaption="";
        if(haijobs.getRegisteredUser().getResumeStatus()==0)
        {
            missingCaption="BioData";



        }
        if(haijobs.getRegisteredUser().getPhotoStatus()==0)
        {
            missingCaption=missingCaption+"and Photo";

        }
        if(missingCaption.length()!=0)
        {
            View resumeAlert=getLayoutInflater().inflate(R.layout.profile_alert_layout,null);
            TextView txtalert=(TextView)resumeAlert.findViewById(R.id.txtmessage);
            Button btnAlertAction=(Button) resumeAlert.findViewById(R.id.btnAction);
            txtalert.setText("Your "+missingCaption+"  missing.  View Profile Page for uploading "+missingCaption);
            btnAlertAction.setText("View Profile");
            btnAlertAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProfilePage();
                }
            });
            mViewFlipper.addView(resumeAlert);
        }






    }

    private void showAboutPage() {

        Intent aboutpage=new Intent(this,AboutPageScreen.class);
        startActivity(aboutpage);
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

    private void showProfilePage() {

        Intent jobSeekerProfile=new Intent(this,JobSeekerProfile.class);
        startActivity(jobSeekerProfile);
    }
}
