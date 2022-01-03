package com.example.crmav1.Model;

public class FAQ {
    private String fid;
    private String title;
    private String answer;
    private String userType;

    public FAQ() {
    }

    public FAQ(String fid, String title, String answer, String userType) {
        this.fid = fid;
        this.title = title;
        this.answer = answer;
        this.userType = userType;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
