package com.starwings.app.haijobs.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Employer {


    @SerializedName("employerID")
    @Expose
    private Integer employerID;
    @SerializedName("employerName")
    @Expose
    private String employerName;
    @SerializedName("AddressLine1")
    @Expose
    private String addressLine1;
    @SerializedName("AddressLine2")
    @Expose
    private String addressLine2;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("employerContact")
    @Expose
    private String employerContact;
    @SerializedName("empLoginID")
    @Expose
    private String empLoginID;
    @SerializedName("empPassword")
    @Expose
    private String empPassword;
    @SerializedName("employerMail")
    @Expose
    private String employerMail;
    @SerializedName("employerWeb")
    @Expose
    private String employerWeb;
    @SerializedName("personToContact")
    @Expose
    private String personToContact;
    @SerializedName("numberToContact")
    @Expose
    private String numberToContact;
    @SerializedName("shortDecription")
    @Expose
    private String shortDecription;
    @SerializedName("apikey")
    @Expose
    private String apikey;
    @SerializedName("empdigest")
    @Expose
    private String empdigest;
    @SerializedName("subscribed")
    @Expose
    private Integer subscribed;

    public Integer getEmployerID() {
        return employerID;
    }

    public void setEmployerID(Integer employerID) {
        this.employerID = employerID;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getEmployerContact() {
        return employerContact;
    }

    public void setEmployerContact(String employerContact) {
        this.employerContact = employerContact;
    }

    public String getEmpLoginID() {
        return empLoginID;
    }

    public void setEmpLoginID(String empLoginID) {
        this.empLoginID = empLoginID;
    }

    public String getEmpPassword() {
        return empPassword;
    }

    public void setEmpPassword(String empPassword) {
        this.empPassword = empPassword;
    }

    public String getEmployerMail() {
        return employerMail;
    }

    public void setEmployerMail(String employerMail) {
        this.employerMail = employerMail;
    }

    public String getEmployerWeb() {
        return employerWeb;
    }

    public void setEmployerWeb(String employerWeb) {
        this.employerWeb = employerWeb;
    }

    public String getPersonToContact() {
        return personToContact;
    }

    public void setPersonToContact(String personToContact) {
        this.personToContact = personToContact;
    }

    public String getNumberToContact() {
        return numberToContact;
    }

    public void setNumberToContact(String numberToContact) {
        this.numberToContact = numberToContact;
    }

    public String getShortDecription() {
        return shortDecription;
    }

    public void setShortDecription(String shortDecription) {
        this.shortDecription = shortDecription;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getEmpdigest() {
        return empdigest;
    }

    public void setEmpdigest(String empdigest) {
        this.empdigest = empdigest;
    }

    public Integer getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Integer subscribed) {
        this.subscribed = subscribed;
    }
}