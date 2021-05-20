package com.example.openeyes.bean;

public class RegisterEvent {

    private String count;
    private String password;

    public RegisterEvent(String count, String password) {
        this.count = count;
        this.password = password;
    }

    public String getCount() {
        return count;
    }

    public String getPassword() {
        return password;
    }
}
