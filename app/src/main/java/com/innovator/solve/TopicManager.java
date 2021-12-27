package com.innovator.solve;

import java.io.Serializable;

public class TopicManager implements Serializable {

    //set defaults so qListRef in ReadingQuestions.java doesn't crash
    private static String folderName = "Grade_9";
    private static String picRootFolderName = "Grade_9_Questions";
    private static String picNamePrefix = "grade_9";

    private static String gradeLevel = "9";

    private static String category = "Probability and Statistics";

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
