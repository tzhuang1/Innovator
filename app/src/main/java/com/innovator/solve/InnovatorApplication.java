package com.innovator.solve;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.UUID;

public class InnovatorApplication extends Application {
    static SharedPreferences sp;

    private static UserData currentUser;

    public static void setSP(Context c) {
        sp = c.getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    public static UserData getUser()
    {
        if(currentUser==null){
            String uniqueID;
            if (sp.contains("UUID")) {
                uniqueID = sp.getString("UUID", "DEFAULT");
            }
            else {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.putString("UUID", uniqueID);
                editor.commit();
            }
            currentUser = new UserData(uniqueID, "","");
            return currentUser;
        }
        return currentUser;
    }
    public static void setUser(UserData user)
    {
        currentUser = user;
    }

}
