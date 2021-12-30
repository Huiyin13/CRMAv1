package com.example.crmav1.Model;

public class Student {
    private String sPhone;
    private String sEmail;
    private String sIC;
    private String sName;
    private String sPassword;
    private String userType;
    private String sId;
    private String sCampus;
    private String sMatricID;
    private String sIDUri;
    private String sICUri;
    private String sLicenceUri;

    public Student(){

    }

    public Student(String sPhone, String sEmail, String sIC, String sName, String sPassword, String userType, String sId, String sCampus, String sMatricID, String sIDUri, String sICUri, String sLicenceUri) {
        this.sPhone = sPhone;
        this.sEmail = sEmail;
        this.sIC = sIC;
        this.sName = sName;
        this.sPassword = sPassword;
        this.userType = userType;
        this.sId = sId;
        this.sCampus = sCampus;
        this.sMatricID = sMatricID;
        this.sIDUri = sIDUri;
        this.sICUri = sICUri;
        this.sLicenceUri = sLicenceUri;
    }

    public String getsPhone() {
        return sPhone;
    }

    public void setsPhone(String sPhone) {
        this.sPhone = sPhone;
    }

    public String getsEmail() {
        return sEmail;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public String getsIC() {
        return sIC;
    }

    public void setsIC(String sIC) {
        this.sIC = sIC;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsPassword() {
        return sPassword;
    }

    public void setsPassword(String sPassword) {
        this.sPassword = sPassword;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getsCampus() {
        return sCampus;
    }

    public void setsCampus(String sCampus) {
        this.sCampus = sCampus;
    }

    public String getsMatricID() {
        return sMatricID;
    }

    public void setsMatricID(String sMatricID) {
        this.sMatricID = sMatricID;
    }

    public String getsIDUri() {
        return sIDUri;
    }

    public void setsIDUri(String sIDUri) {
        this.sIDUri = sIDUri;
    }

    public String getsICUri() {
        return sICUri;
    }

    public void setsICUri(String sICUri) {
        this.sICUri = sICUri;
    }

    public String getsLicenceUri() {
        return sLicenceUri;
    }

    public void setsLicenceUri(String sLicenceUri) {
        this.sLicenceUri = sLicenceUri;
    }
}

