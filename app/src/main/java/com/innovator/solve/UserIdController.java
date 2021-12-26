package com.innovator.solve;

import java.util.Set;

public class UserIdController {

    private static int idLength=21;
    private static Set<String> allUsers;

    public static String generateID(){
        String generatedID ="";

        for(int i=0;i<idLength;i++){
            generatedID+=(int)(Math.random()*10);
        }

        return generatedID;

    }

    public static void setAllUsers(Set<String> allUsersSet){
        allUsers=allUsersSet;
    }

    public static boolean checkIdDuplicates(String id){
        return allUsers.contains(id);
    }

    public static Set<String> getAllUsers(){
        return allUsers;
    }
}
