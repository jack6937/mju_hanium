package com.example.myapplication;

public class PersonalData {
    private String member_ID;
    private String member_name;
    private String member_HP;
    private String member_OTP;

    public String getMember_ID() {
        return member_ID;
    }

    public String getMember_name() {
        return member_name;
    }

    public String getMember_HP() {
        return member_HP;
    }

    public String getMember_OTP() { return member_OTP; }

    public void setMember_id(String member_ID) {
        this.member_ID = member_ID;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public void setMember_HP(String member_HP) {
        this.member_HP = member_HP;
    }

    public void setMember_OTP(String member_OTP) { this.member_OTP = member_OTP; }
}