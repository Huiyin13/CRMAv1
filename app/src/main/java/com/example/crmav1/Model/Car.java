package com.example.crmav1.Model;

import android.net.Uri;

public class Car {

    private String uid;
    private String cid;
    private String cType;
    private String cPlate;
    private String cPerson;
    private String description;
    private String rentFee;
    private String cStatus;
    private String cStrickerUri;
    private String cHideReason;
    private String cModel;

    public Car(){

    }

    public Car(String uid, String cid, String cType, String cPlate, String cPerson, String description, String rentFee, String cStatus, String cStrickerUri, String cHideReason, String cModel) {
        this.uid = uid;
        this.cid = cid;
        this.cType = cType;
        this.cPlate = cPlate;
        this.cPerson = cPerson;
        this.description = description;
        this.rentFee = rentFee;
        this.cStatus = cStatus;
        this.cStrickerUri = cStrickerUri;
        this.cHideReason = cHideReason;
        this.cModel = cModel;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getcType() {
        return cType;
    }

    public void setcType(String cType) {
        this.cType = cType;
    }

    public String getcPlate() {
        return cPlate;
    }

    public void setcPlate(String cPlate) {
        this.cPlate = cPlate;
    }

    public String getcPerson() {
        return cPerson;
    }

    public void setcPerson(String cPerson) {
        this.cPerson = cPerson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRentFee() {
        return rentFee;
    }

    public void setRentFee(String rentFee) {
        this.rentFee = rentFee;
    }

    public String getcStatus() {
        return cStatus;
    }

    public void setcStatus(String cStatus) {
        this.cStatus = cStatus;
    }

    public String getcStrickerUri() {
        return cStrickerUri;
    }

    public void setcStrickerUri(String cStrickerUri) {
        this.cStrickerUri = cStrickerUri;
    }

    public String getcHideReason() {
        return cHideReason;
    }

    public void setcHideReason(String cHideReason) {
        this.cHideReason = cHideReason;
    }

    public String getcModel() {
        return cModel;
    }

    public void setcModel(String cModel) {
        this.cModel = cModel;
    }
}
