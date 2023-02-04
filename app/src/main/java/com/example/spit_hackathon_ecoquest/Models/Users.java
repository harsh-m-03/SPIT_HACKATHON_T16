package com.example.spit_hackathon_ecoquest.Models;

public class Users {
    String fName, lName, phoneNumber, email, password,uid, greenPoints;

    public Users(String fName, String lName, String phoneNumber, String email, String password, String uid, String greenPoints) {
        this.fName = fName;
        this.lName = lName;
        this.phoneNumber = phoneNumber;
        this.greenPoints=greenPoints;
        this.email = email;
        this.password = password;
        this.uid=uid;
    }

    public String getGreenPoints() {
        return greenPoints;
    }

    public void setGreenPoints(String greenPoints) {
        this.greenPoints = greenPoints;
    }

    public Users() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
}
