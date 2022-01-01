package com.example.crmav1.Model;

public class Booking {
    private String bid;
    private String cid;
    private String sId;
    private String coId;
    private String timeF;
    private String timeT;
    private String placeD;
    private String placeP;
    private String dateF;
    private String dateT;
    private String rentH;
    private String ttlFee;
    private String bStatus;
    private String bRejectReason;
    private String bCancelReason;
    private String finalTo;
    private String finalFrom;


    public Booking() {
    }

    public Booking(String bid, String cid, String sId, String coId, String timeF, String timeT, String placeD, String placeP, String dateF, String dateT, String rentH, String ttlFee, String bStatus, String bRejectReason, String bCancelReason, String finalTo, String finalFrom) {
        this.bid = bid;
        this.cid = cid;
        this.sId = sId;
        this.coId = coId;
        this.timeF = timeF;
        this.timeT = timeT;
        this.placeD = placeD;
        this.placeP = placeP;
        this.dateF = dateF;
        this.dateT = dateT;
        this.rentH = rentH;
        this.ttlFee = ttlFee;
        this.bStatus = bStatus;
        this.bRejectReason = bRejectReason;
        this.bCancelReason = bCancelReason;
        this.finalTo = finalTo;
        this.finalFrom = finalFrom;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getCoId() {
        return coId;
    }

    public void setCoId(String coId) {
        this.coId = coId;
    }

    public String getTimeF() {
        return timeF;
    }

    public void setTimeF(String timeF) {
        this.timeF = timeF;
    }

    public String getTimeT() {
        return timeT;
    }

    public void setTimeT(String timeT) {
        this.timeT = timeT;
    }

    public String getPlaceD() {
        return placeD;
    }

    public void setPlaceD(String placeD) {
        this.placeD = placeD;
    }

    public String getPlaceP() {
        return placeP;
    }

    public void setPlaceP(String placeP) {
        this.placeP = placeP;
    }

    public String getDateF() {
        return dateF;
    }

    public void setDateF(String dateF) {
        this.dateF = dateF;
    }

    public String getDateT() {
        return dateT;
    }

    public void setDateT(String dateT) {
        this.dateT = dateT;
    }

    public String getRentH() {
        return rentH;
    }

    public void setRentH(String rentH) {
        this.rentH = rentH;
    }

    public String getTtlFee() {
        return ttlFee;
    }

    public void setTtlFee(String ttlFee) {
        this.ttlFee = ttlFee;
    }

    public String getbStatus() {
        return bStatus;
    }

    public void setbStatus(String bStatus) {
        this.bStatus = bStatus;
    }

    public String getbRejectReason() {
        return bRejectReason;
    }

    public void setbRejectReason(String bRejectReason) {
        this.bRejectReason = bRejectReason;
    }

    public String getbCancelReason() {
        return bCancelReason;
    }

    public void setbCancelReason(String bCancelReason) {
        this.bCancelReason = bCancelReason;
    }

    public String getFinalTo() {
        return finalTo;
    }

    public void setFinalTo(String finalTo) {
        this.finalTo = finalTo;
    }

    public String getFinalFrom() {
        return finalFrom;
    }

    public void setFinalFrom(String finalFrom) {
        this.finalFrom = finalFrom;
    }
}
