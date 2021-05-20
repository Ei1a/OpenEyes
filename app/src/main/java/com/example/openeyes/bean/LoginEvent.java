package com.example.openeyes.bean;

public class LoginEvent {
    private boolean isSignIn = false;
    private String count;
    private String password;

    public LoginEvent(boolean isSignIn, String count, String password){
        this.isSignIn = isSignIn;
        this.count = count;
        this.password = password;
    }

    public boolean getStatus(){
        return isSignIn;
    }

    public String getCount() {
        return count;
    }

    public String getPassword() {
        return password;
    }
}
