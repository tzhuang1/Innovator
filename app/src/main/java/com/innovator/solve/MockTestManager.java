package com.innovator.solve;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MockTestManager {

    private static ArrayList<MockTest> allTests;
    public static boolean loaded = false;
    public static void init() {
        DatabaseReference tref = FirebaseDatabase.getInstance().getReference().child("Mocks");
        allTests = new ArrayList<>();
        //Log.d("Database", tref.toString());
        tref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        collectTests((Map<String,Object>) dataSnapshot.getValue());
                    }

                    private void collectTests(Map<String, Object> tests) {
                        for (Map.Entry<String, Object> entry : tests.entrySet()){
                            String name = entry.getKey();
                            //Log.d("Database", name);
                            Map singleTest = (Map) entry.getValue();
                            MockTest tmpTest = new MockTest((Long) singleTest.get("Grade"), (String) singleTest.get("Subject"), (Long) singleTest.get("QuestionCount"), (String) singleTest.get("ID"));
                            //Log.d("Database", tmpTest.toString());
                            allTests.add(tmpTest);
                        }
                        loaded = true;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("DATABASE ERROR", databaseError.toString());
                    }
                });
    }

    public static ArrayList<MockTest> getTestBySubject(String subject) {
        if (!loaded) {
            return null;
        }
        else {
            ArrayList<MockTest> res = new ArrayList<>();
            for (MockTest m : allTests) {
                if (m.getSubject().equals(subject)) {
                    res.add(m);
                }
            }
            return res;
        }
    }
    public static MockTest getTestByID(String ID) {
        for (MockTest m : allTests) {
            if (m.getID().equals(ID)) {
                return m;
            }
        }
        return null;
    }

    public static class MockTest {
        private String ID;
        private Long grade;
        private Long questionCount;
        private String subject;
        public boolean populated = false;
        public ArrayList<Question> questionList = null;

        MockTest(Long grade, String subject, Long questionCount, String ID) {
            this.ID = ID;
            this.grade = grade;
            this.subject = subject;
            this.questionCount = questionCount;
        }
        public void populateQuestions() {
            DatabaseReference tref = FirebaseDatabase.getInstance().getReference().child("Mocks").child(this.ID);
            questionList = new ArrayList<>();
            //goes to template

            tref.addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Log.d("Output", dataSnapshot.getValue().toString());
                        collectTests((Map<String,Object>) dataSnapshot.getValue());
                    }

                    private void collectTests(Map<String,Object>questions) {
                        Log.d("Output2", questions.get("Questions").toString()); //questions.get("Questions") gets forced into an ARrayList
                        ArrayList<Object> list = (ArrayList<Object>) questions.get("Questions");
                        for (Object o:list) {
//                            Log.d("Database", o.toString());
                            Map<String, Object> q = (Map) o;
                            String answer = (String) q.get("Answer");
                            String type = (String) q.get("Type");
                            String category = (String) q.get("Category");
                            String statement = (String) q.get("Question");
                            String explanation = (String) q.get("Explanation");
                            int picNum = Math.toIntExact((Long) q.get("MediaID"));
                            int exPicNum = Math.toIntExact((Long) q.get("ExMediaID"));



                            TreeMap<Character, String> choices = new TreeMap<>();
                            Map choiceMap = (Map) q.get("Choices");
                            for (Object key: (choiceMap).keySet()) {
                                Character k = ((String) key).charAt(0);
                                choices.put(k, (String) choiceMap.get(key));
                            }


                            Question qObj = new Question(true, type, statement, choices, answer, explanation, category, picNum, exPicNum);
                            questionList.add(qObj);
                        }
                        populated = true;
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("DATABASE ERROR", databaseError.toString());
                    }
                });
        }
        public String getSubject() {
            return this.subject;
        }
        public Long getGrade() { return this.grade; }
        public Long getQuestionCount() { return this.questionCount; }
        public String getID() { return this.ID; }
        public String toString() {
            return this.subject + " " + String.valueOf(this.grade);
        }
    }
}
