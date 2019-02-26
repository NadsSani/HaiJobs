package com.starwings.app.haijobs.employerPages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.starwings.app.haijobs.HaiJobApp;
import com.starwings.app.haijobs.HaiJobsActivity;
import com.starwings.app.haijobs.R;
import com.starwings.app.haijobs.data.Employer;

public class EmployerProfile extends HaiJobsActivity {
    HaiJobApp haiJobApp;
    Employer current;
    TextView edTitle,edShortDesc,edAddressLine1,edAddressLine2,edPin;
    TextView edComContact,edMail,edWebsite,edCntPerson,edCntNumber,edSubscribed;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_profile);
        haiJobApp=(HaiJobApp)getApplication();
        current=haiJobApp.getRegisteredEmployer();

        edTitle=(TextView)findViewById(R.id.edTitle);
        edTitle.setText(current.getEmployerName());

        edShortDesc=(TextView)findViewById(R.id.txtshortdesc);
        edShortDesc.setText(current.getShortDecription());

        edAddressLine1=(TextView)findViewById(R.id.txtLine1);
        edAddressLine1.setText(current.getAddressLine1());

        edAddressLine2=(TextView)findViewById(R.id.txtLine2);
        edAddressLine2.setText(current.getAddressLine2());

        edPin=(TextView)findViewById(R.id.txtPincode);
        edPin.setText(current.getPincode());

        edComContact=(TextView)findViewById(R.id.txtempcontact);
        edComContact.setText(current.getEmployerContact());

        edMail=(TextView)findViewById(R.id.txtempemail);
        edMail.setText(current.getEmployerMail());

        edWebsite=(TextView)findViewById(R.id.txtwebsite);
        edWebsite.setText(current.getEmployerWeb());

        edCntPerson=(TextView)findViewById(R.id.txtcontactperson);
        edCntPerson.setText(current.getPersonToContact());

        edCntNumber=(TextView)findViewById(R.id.txtcontactnumber);
        edCntNumber.setText(current.getNumberToContact());

        edSubscribed=(TextView)findViewById(R.id.txtsubscribed);
        edSubscribed.setText(current.getSubscribed()==0?"Not Subscribed":"Subscribed");



    }
}
