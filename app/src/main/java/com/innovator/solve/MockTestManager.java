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

import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

public class MockTestManager {

    private static ArrayList<MockTest> allTests;
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
                        ArrayList<String> titles = new ArrayList<>();
                        int id = 0;
                        for (Map.Entry<String, Object> entry : tests.entrySet()){
                            String name = entry.getKey();
                            //Log.d("Database", name);
                            Map singleTest = (Map) entry.getValue();
                            MockTest tmpTest = new MockTest(id, (Long) singleTest.get("Grade"), (String) singleTest.get("Subject"));
                            //Log.d("Database", tmpTest.toString());
                            allTests.add(tmpTest);
                            id += 1;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("DATABASE ERROR", databaseError.toString());
                    }
                });
    }

    public static ArrayList<MockTest> getTestBySubject(String subject) {
        ArrayList<MockTest> res = new ArrayList<>();
        for (MockTest m:allTests) {
            if (m.getSubject().equals(subject)) {
                res.add(m);
            }
        }
        return res;
    }

    public static class MockTest {
        private int ID;
        private Long grade;
        private String subject;
        MockTest(int id, Long grade, String subject) {
            this.ID = id;
            this.grade = grade;
            this.subject = subject;
        }

        public String getSubject() {
            return subject;
        }

        public String toString() {
            return this.subject + " " + String.valueOf(this.grade);
        }
    }
}
