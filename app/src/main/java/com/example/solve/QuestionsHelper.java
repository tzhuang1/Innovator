package com.example.solve;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


class QuestionsHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DB_NAME = "TQuiz.db";

    //If you want to add more questions or wanna update table values
    //or any kind of modification in db just increment version no.
    private static final int DB_VERSION = 4;
    //Table name
    private static final String TABLE_NAME = "TQ";
    //Id of question
    private static final String UID = "_UID";
    //Question
    private static final String QUESTION = "QUESTION";
    //Option A
    private static final String OPTA = "OPTA";
    //Option B
    private static final String OPTB = "OPTB";
    //Option C
    private static final String OPTC = "OPTC";
    //Option D
    private static final String OPTD = "OPTD";
    //Answer
    private static final String ANSWER = "ANSWER";
    //Explanation
    private static final String EXPLANATION = "EXPLANATION";
    //So basically we are now creating table with first column-id , sec column-question , third column -option A, fourth column -option B , Fifth column -option C , sixth column -option D , seventh column - answer(i.e ans of  question), eighth column - explanation
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + UID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + QUESTION + " VARCHAR(255), " + OPTA + " VARCHAR(255), " + OPTB + " VARCHAR(255), " + OPTC + " VARCHAR(255), " + OPTD + " VARCHAR(255), " + ANSWER + " VARCHAR(255), " + EXPLANATION + " VARCHAR(255));";
    //Drop table query
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    QuestionsHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //OnCreate is called only once
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //OnUpgrade is called when ever we upgrade or increment our database version no
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    void allQuestion() {
        ArrayList<Questions> arraylist = new ArrayList<>();

        arraylist.add(new Questions("Sample Question 1", "Option A", "Option B", "Option C", "Option D", "Option A", "Explanation"));
        arraylist.add(new Questions("Sample Question 2", "Option A", "Option B", "Option C", "Option D", "Option B", "Explanation"));
        arraylist.add(new Questions("Sample Question 3", "Option A", "Option B", "Option C", "Option D", "Option A", "Explanation"));
        arraylist.add(new Questions("Sample Question 4", "Option A", "Option B", "Option C", "Option D", "Option D", "Explanation"));
        arraylist.add(new Questions("Sample Question 5", "Option A", "Option B", "Option C", "Option D", "Option A", "Explanation"));
        arraylist.add(new Questions("Sample Question 6", "Option A", "Option B", "Option C", "Option D", "Option C", "Explanation"));
        arraylist.add(new Questions("Sample Question 7", "Option A", "Option B", "Option C", "Option D", "Option A", "Explanation"));
        arraylist.add(new Questions("Sample Question 8", "Option A", "Option B", "Option C", "Option D", "Option B", "Explanation"));
        arraylist.add(new Questions("Sample Question 9", "Option A", "Option B", "Option C", "Option D", "Option B", "Explanation"));
        arraylist.add(new Questions("Sample Question 10", "Option A", "Option B", "Option C", "Option D", "Option C", "Explanation"));
        arraylist.add(new Questions("Sample Question 11", "Option A", "Option B", "Option C", "Option D", "Option A", "Explanation"));
        arraylist.add(new Questions("Sample Question 12", "Option A", "Option B", "Option C", "Option D", "Option D", "Explanation"));
        arraylist.add(new Questions("Sample Question 13", "Option A", "Option B", "Option C", "Option D", "Option D", "Explanation"));
        arraylist.add(new Questions("Sample Question 14", "Option A", "Option B", "Option C", "Option D", "Option B", "Explanation"));
        arraylist.add(new Questions("Sample Question 15", "Option A", "Option B", "Option C", "Option D", "Option C", "Explanation"));

        this.addAllQuestions(arraylist);

    }


    private void addAllQuestions(ArrayList<Questions> allQuestions) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (Questions question : allQuestions) {
                values.put(QUESTION, question.getQuestion());
                values.put(OPTA, question.getOptA());
                values.put(OPTB, question.getOptB());
                values.put(OPTC, question.getOptC());
                values.put(OPTD, question.getOptD());
                values.put(ANSWER, question.getAnswer());
                values.put(EXPLANATION, question.getExplanation());
                db.insert(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }


    List<Questions> getAllOfTheQuestions() {

        List<Questions> questionsList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String column[] = {UID, QUESTION, OPTA, OPTB, OPTC, OPTD, ANSWER, EXPLANATION};
        Cursor cursor = db.query(TABLE_NAME, column, null, null, null, null, null);


        while (cursor.moveToNext()) {
            Questions question = new Questions();
            question.setId(cursor.getInt(0));
            question.setQuestion(cursor.getString(1));
            question.setOptA(cursor.getString(2));
            question.setOptB(cursor.getString(3));
            question.setOptC(cursor.getString(4));
            question.setOptD(cursor.getString(5));
            question.setAnswer(cursor.getString(6));
            question.setExplanation(cursor.getString(7));
            questionsList.add(question);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        db.close();
        return questionsList;
    }
}
