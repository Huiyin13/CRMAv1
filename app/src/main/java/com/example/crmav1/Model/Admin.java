package com.example.crmav1.Model;

public class Admin {

    private String uid;
    private String userType;
    private String aEmail;
    private String aPassword;

    public Admin(){

    }

    public Admin(String uid, String userType, String aEmail, String aPassword){
        this.uid = uid;
        this.userType = userType;
        this.aEmail = aEmail;
        this.aPassword = aPassword;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getaEmail() {
        return aEmail;
    }

    public void setaEmail(String aEmail) {
        this.aEmail = aEmail;
    }

    public String getaPassword() {
        return aPassword;
    }

    public void setaPassword(String aPassword) {
        this.aPassword = aPassword;
    }
}

