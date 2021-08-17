package com.example.solve;

import java.io.Serializable;

public class TopicManager implements Serializable {

    private static String folderName;
    private static String picRootFolderName;
    private static String picNamePrefix;

    private static String gradeLevel;

    private static String category="Computation and Estimation";

    public static void setDataLocations(String level){
        gradeLevel=level;

        folderName="Grade_"+gradeLevel;
        picRootFolderName="Grade_"+gradeLevel+"_Questions";
        picNamePrefix="grade_"+gradeLevel;
    }

    public static String getGradeLevel(){
        return gradeLevel;
    }


    public static String getPicRootFolderName(){
        return picRootFolderName;
    }

    public static String getPicNamePrefix(){
        return picNamePrefix;
    }

    public static String getQuestionFolderName(){
        return folderName;
    }

    public static void setCategory(String val){
        category=val;
    }

    public static String getCategory(){
        return category;
    }

}
