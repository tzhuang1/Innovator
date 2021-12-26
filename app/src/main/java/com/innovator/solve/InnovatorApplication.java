package com.innovator.solve;

import android.app.Application;

public class InnovatorApplication extends Application {
    private static UserData currentUser;

    public static UserData getUser()
    {
        if(currentUser==null){
            return new UserData("", "","");
        }
        return currentUser;
    }
    public static void setUser(UserData user)
    {
        currentUser = user;
    }

}
