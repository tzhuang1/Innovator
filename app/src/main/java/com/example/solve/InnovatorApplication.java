package com.example.solve;

import android.app.Application;

public class InnovatorApplication extends Application {
    private static UserData currentUser;

    public static UserData getUser()
    {
        return currentUser;
    }
    public static void setUser(UserData user)
    {
        currentUser = user;
    }

}
