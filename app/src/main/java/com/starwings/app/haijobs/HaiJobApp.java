package com.starwings.app.haijobs;

import android.app.Application;
import android.content.SharedPreferences;

import com.starwings.app.haijobs.data.Categories;
import com.starwings.app.haijobs.data.District;
import com.starwings.app.haijobs.data.Employer;
import com.starwings.app.haijobs.data.Experience;
import com.starwings.app.haijobs.data.QualificationData;
import com.starwings.app.haijobs.data.State;
import com.starwings.app.haijobs.data.SubCategories;
import com.starwings.app.haijobs.data.User;

import java.util.ArrayList;
import java.util.List;

public class HaiJobApp extends Application {
    private ArrayList<QualificationData> qualificationData;
    private ArrayList<SubCategories> selectioncatlist;
    private ArrayList<Categories> mainselectionList;
    private ArrayList<Experience> experienceArrayList;
    private List<State> statesList;
    private List<District> districtsList;
    private User registeredUser;
    private Employer registeredEmployer;
    @Override
    public void onCreate() {
        super.onCreate();
        setQualificationData(new ArrayList<QualificationData>());
        setSelectioncatlist(new ArrayList<SubCategories>());
        setMainselectionList(new ArrayList<Categories>());
        setExperienceArrayList(new ArrayList<Experience>());
        setStatesList(new ArrayList<State>());
        setDistrictsList(new ArrayList<District>());
        if(registeredUser==null)
        {
            SharedPreferences userPreferences=getSharedPreferences("USER_PREG",MODE_PRIVATE);
                registeredUser = new User();
                registeredUser.setApiKey(userPreferences.getString("apikey", "NA"));
                registeredUser.setContactno(userPreferences.getString("username", "NA"));
                registeredUser.setEmail(userPreferences.getString("email", "NA"));
                registeredUser.setName(userPreferences.getString("name", "NA"));
                registeredUser.setDob(userPreferences.getString("dob", "NA"));
                registeredUser.setGender(userPreferences.getInt("gender", -1));
                registeredUser.setQualification(userPreferences.getString("qualification", "NA"));
                registeredUser.setStreams(userPreferences.getString("streams", "NA"));
                registeredUser.setPassword(userPreferences.getString("pass", "NA"));
                registeredUser.setProfilepic(userPreferences.getString("profile", "NA"));
                registeredUser.setResumeloc(userPreferences.getString("resumepath", "NA"));
                registeredUser.setPhonestatus(userPreferences.getInt("phverified", 0));
                registeredUser.setEmailstatus(userPreferences.getInt("emverified", 0));
                registeredUser.setActive(userPreferences.getInt("active", 0));
                registeredUser.setPhotoStatus(userPreferences.getInt("photostatus", 0));
                registeredUser.setResumeStatus(userPreferences.getInt("resumestatus", 0));
                registeredUser.setExperiencelevel(userPreferences.getInt("experiencelevel", 0));
                registeredUser.setPlace(userPreferences.getString("place", "NA"));
                registeredUser.setDistrict(userPreferences.getInt("district", 1));
                registeredUser.setState(userPreferences.getInt("state", 1));
                registeredUser.setPincode(userPreferences.getString("pincode", "NA"));


        }
        if(registeredEmployer==null)
        {
            SharedPreferences employerPreference=getSharedPreferences("EMPLOYER_PREF",MODE_PRIVATE);
            registeredEmployer=new Employer();

            registeredEmployer.setApikey(employerPreference.getString("apikey","NA"));
            registeredEmployer.setEmployerName(employerPreference.getString("employername","NA"));
            registeredEmployer.setAddressLine1(employerPreference.getString("addressline1","NA"));
            registeredEmployer.setAddressLine2(employerPreference.getString("addressline2","NA"));
            registeredEmployer.setPincode(employerPreference.getString("pincode","NA"));
            registeredEmployer.setEmployerContact(employerPreference.getString("employerContact","NA"));
            registeredEmployer.setEmployerMail(employerPreference.getString("employermail","NA"));
            registeredEmployer.setEmployerWeb(employerPreference.getString("employerWeb","NA"));
            registeredEmployer.setEmpLoginID(employerPreference.getString("employerLogID","NA"));
            registeredEmployer.setEmpPassword(employerPreference.getString("employerPass","NA"));
            registeredEmployer.setPersonToContact(employerPreference.getString("contactperson","NA"));
            registeredEmployer.setNumberToContact(employerPreference.getString("numbertocontact","NA"));
            registeredEmployer.setShortDecription(employerPreference.getString("shortdesc","NA"));
            registeredEmployer.setEmpdigest(employerPreference.getString("empdigest","NA"));
            registeredEmployer.setSubscribed(employerPreference.getInt("subscribed",0));
        }
    }

    public ArrayList<QualificationData> getQualificationData() {
        return qualificationData;
    }

    public void setQualificationData(ArrayList<QualificationData> qualificationData) {
        this.qualificationData = qualificationData;
    }

    public ArrayList<SubCategories> getSelectioncatlist() {
        return selectioncatlist;
    }

    public void setSelectioncatlist(ArrayList<SubCategories> selectioncatlist) {
        this.selectioncatlist = selectioncatlist;
    }

    public ArrayList<Categories> getMainselectionList() {
        return mainselectionList;
    }

    public void setMainselectionList(ArrayList<Categories> mainselectionList) {
        this.mainselectionList = mainselectionList;
    }

    public User getRegisteredUser() {
        if(registeredUser==null)
        {
            SharedPreferences userPreferences=getSharedPreferences("USER_PREG",MODE_PRIVATE);

                registeredUser = new User();
                registeredUser.setApiKey(userPreferences.getString("apikey", "NA"));
                registeredUser.setContactno(userPreferences.getString("username", "NA"));
                registeredUser.setEmail(userPreferences.getString("email", "NA"));
                registeredUser.setName(userPreferences.getString("name", "NA"));
                registeredUser.setDob(userPreferences.getString("dob", "NA"));
                registeredUser.setGender(userPreferences.getInt("gender", -1));
                registeredUser.setQualification(userPreferences.getString("qualification", "NA"));
                registeredUser.setStreams(userPreferences.getString("streams", "NA"));
                registeredUser.setPassword(userPreferences.getString("pass", "NA"));
                registeredUser.setProfilepic(userPreferences.getString("profile", "NA"));
                registeredUser.setResumeloc(userPreferences.getString("resumepath", "NA"));
                registeredUser.setPhonestatus(userPreferences.getInt("phverified", 0));
                registeredUser.setEmailstatus(userPreferences.getInt("emverified", 0));
                registeredUser.setActive(userPreferences.getInt("active", 0));
                registeredUser.setPhotoStatus(userPreferences.getInt("photostatus", 0));
                registeredUser.setResumeStatus(userPreferences.getInt("resumestatus", 0));
                registeredUser.setExperiencelevel(userPreferences.getInt("experiencelevel", 0));
                registeredUser.setPlace(userPreferences.getString("place", "NA"));
                registeredUser.setDistrict(userPreferences.getInt("district", 1));
                registeredUser.setState(userPreferences.getInt("state", 1));
                registeredUser.setPincode(userPreferences.getString("pincode", "NA"));


        }
        return registeredUser;
    }

    public void setRegisteredUser(User registeredUser) {
        this.registeredUser = registeredUser;
    }

    public ArrayList<Experience> getExperienceArrayList() {
        return experienceArrayList;
    }

    public void setExperienceArrayList(ArrayList<Experience> experienceArrayList) {
        this.experienceArrayList = experienceArrayList;
    }

    public Employer getRegisteredEmployer() {
        if(registeredEmployer==null)
        {
            SharedPreferences employerPreference=getSharedPreferences("EMPLOYER_PREF",MODE_PRIVATE);
            registeredEmployer=new Employer();

            registeredEmployer.setApikey(employerPreference.getString("apikey","NA"));
            registeredEmployer.setEmployerName(employerPreference.getString("employername","NA"));
            registeredEmployer.setAddressLine1(employerPreference.getString("addressline1","NA"));
            registeredEmployer.setAddressLine2(employerPreference.getString("addressline2","NA"));
            registeredEmployer.setPincode(employerPreference.getString("pincode","NA"));
            registeredEmployer.setEmployerContact(employerPreference.getString("employerContact","NA"));
            registeredEmployer.setEmployerMail(employerPreference.getString("employermail","NA"));
            registeredEmployer.setEmployerWeb(employerPreference.getString("employerWeb","NA"));
            registeredEmployer.setEmpLoginID(employerPreference.getString("employerLogID","NA"));
            registeredEmployer.setEmpPassword(employerPreference.getString("employerPass","NA"));
            registeredEmployer.setPersonToContact(employerPreference.getString("contactperson","NA"));
            registeredEmployer.setNumberToContact(employerPreference.getString("numbertocontact","NA"));
            registeredEmployer.setShortDecription(employerPreference.getString("shortdesc","NA"));
            registeredEmployer.setEmpdigest(employerPreference.getString("empdigest","NA"));
            registeredEmployer.setSubscribed(employerPreference.getInt("subscribed",0));
        }
        return registeredEmployer;
    }

    public void setRegisteredEmployer(Employer registeredEmployer) {
        this.registeredEmployer = registeredEmployer;
    }

    public List<State> getStatesList() {
        return statesList;
    }

    public void setStatesList(List<State> statesList) {
        this.statesList = statesList;
    }

    public List<District> getDistrictsList() {
        return districtsList;
    }

    public void setDistrictsList(List<District> districtsList) {
        this.districtsList = districtsList;
    }
}
