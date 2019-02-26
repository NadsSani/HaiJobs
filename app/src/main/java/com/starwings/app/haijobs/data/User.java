package com.starwings.app.haijobs.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("gender")
    @Expose
    private Integer gender;
    @SerializedName("qualification")
    @Expose
    private String qualification;
    @SerializedName("streams")
    @Expose
    private String streams;
    @SerializedName("contactno")
    @Expose
    private String contactno;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("api_key")
    @Expose
    private String apiKey;

    @SerializedName("profilepic")
    @Expose
    private String profilepic;

    @SerializedName("resumeloc")
    @Expose
    private String resumeloc;


    @SerializedName("active")
    @Expose
    private int active;


    @SerializedName("phverified")
    @Expose
    private int phonestatus;


    @SerializedName("photo_status")
    @Expose
    private int photoStatus;

    @SerializedName("resume_status")
    @Expose
    private int resumeStatus;

    @SerializedName("emverified")
    @Expose
    private int emailstatus;

    @SerializedName("experiencelevel")
    @Expose
    private int experiencelevel;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getDistrict() {
        return district;
    }

    public void setDistrict(int district) {
        this.district = district;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    @SerializedName("place")
    @Expose
    private String place;

    @SerializedName("district")
    @Expose
    private int district;

    @SerializedName("state")
    @Expose
    private int state;

    @SerializedName("pincode")
    @Expose
    private String pincode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getStreams() {
        return streams;
    }

    public void setStreams(String streams) {
        this.streams = streams;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getPhonestatus() {
        return phonestatus;
    }

    public void setPhonestatus(int phonestatus) {
        this.phonestatus = phonestatus;
    }

    public int getEmailstatus() {
        return emailstatus;
    }

    public void setEmailstatus(int emailstatus) {
        this.emailstatus = emailstatus;
    }

    public int getPhotoStatus() {
        return photoStatus;
    }

    public void setPhotoStatus(int photoStatus) {
        this.photoStatus = photoStatus;
    }

    public int getResumeStatus() {
        return resumeStatus;
    }

    public void setResumeStatus(int resumeStatus) {
        this.resumeStatus = resumeStatus;
    }


    public String getResumeloc() {
        return resumeloc;
    }

    public void setResumeloc(String resumeloc) {
        this.resumeloc = resumeloc;
    }

    public int getExperiencelevel() {
        return experiencelevel;
    }

    public void setExperiencelevel(int experiencelevel) {
        this.experiencelevel = experiencelevel;
    }
}