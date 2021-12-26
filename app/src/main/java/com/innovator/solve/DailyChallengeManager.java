package com.innovator.solve;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.util.*;

public class DailyChallengeManager {

    private static Map<String, Object> currentQuestionData=new HashMap<String, Object>();
    private static String currentType;

    public static Map<String, Object> getCurrentQuestionData(){
        return currentQuestionData;
    }

    public static void setCurrentType(String tag){
        currentType=tag;
    }

    public static void setCurrentQuestionData(Map<String, Object> newData){
        currentQuestionData=newData;



    }

    public static void addQuestionData(String tag, String value){
        currentQuestionData.put(tag, value);
    }
}
