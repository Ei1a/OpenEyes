package com.example.openeyes.bean;

public class LoginEvent {
    private boolean isSignIn = false;

    public LoginEvent(boolean isSignIn){
        this.isSignIn = isSignIn;
    }

    public boolean getStatus(){
        return isSignIn;
    }
}
