package com.example.crmav1.Model;

public class Payment {

    private String paymentType;
    private String bid;
    private String paymentStatus;
    private String pid;

    public Payment() {
    }

    public Payment(String paymentType, String bid, String paymentStatus, String pid) {
        this.paymentType = paymentType;
        this.bid = bid;
        this.paymentStatus = paymentStatus;
        this.pid = pid;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
