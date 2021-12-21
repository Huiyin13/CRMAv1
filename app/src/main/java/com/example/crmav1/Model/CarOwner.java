package com.example.crmav1.Model;

public class CarOwner {
    private String coPhone;
    private String coEmail;
    private String coIC;
    private String coName;
    private String coPassword;
    private String userType;
    private String uid;
    private String coCampus;
    private String coMatricID;
    private String coIDUri;
    private String coICUri;
    private String reason;
    private String coStatus;

    public CarOwner(){

    }

    public CarOwner(String coPhone, String coEmail, String coIC, String coName,
                    String coPassword, String userType, String uid, String coCampus, String coMatricID,
                    String coIDUri, String coICUri, String reason, String coStatus){
        this.coCampus = coCampus;
        this.coEmail = coEmail;
        this.coICUri = coICUri;
        this.coIDUri = coIDUri;
        this.coMatricID = coMatricID;
        this.coIC = coIC;
        this.coName = coName;
        this.coPassword = coPassword;
        this.coStatus = coStatus;
        this.reason = reason;
        this.coPhone = coPhone;
        this.uid = uid;
        this.userType = userType;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public String getCoStatus() {
        return coStatus;
    }

    public void setCoStatus(String coStatus) {
        this.coStatus = coStatus;
    }

    public String getCoPhone() {
        return coPhone;
    }

    public void setCoPhone(String coPhone) {
        this.coPhone = coPhone;
    }

    public String getCoPassword() {
        return coPassword;
    }

    public void setCoPassword(String coPassword) {
        this.coPassword = coPassword;
    }

    public String getCoName() {
        return coName;
    }

    public void setCoName(String coName) {
        this.coName = coName;
    }

    public String getCoMatricID() {
        return coMatricID;
    }

    public void setCoMatricID(String coMatricID) {
        this.coMatricID = coMatricID;
    }

    public String getCoIC() {
        return coIC;
    }

    public void setCoIC(String coIC) {
        this.coIC = coIC;
    }

    public String getCoEmail() {
        return coEmail;
    }

    public void setCoEmail(String coEmail) {
        this.coEmail = coEmail;
    }

    public String getCoCampus() {
        return coCampus;
    }

    public void setCoCampus(String coCampus) {
        this.coCampus = coCampus;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCoICUri() {
        return coICUri;
    }

    public void setCoICUri(String coICUri) {
        this.coICUri = coICUri;
    }

    public String getCoIDUri() {
        return coIDUri;
    }

    public void setCoIDUri(String coIDUri) {
        this.coIDUri = coIDUri;
    }
}
