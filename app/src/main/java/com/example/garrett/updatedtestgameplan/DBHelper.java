package com.example.garrett.updatedtestgameplan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String  DATABASE_NAME = "users.db";
    private static final String  TABLE_NAME = "users";
    private static final String  TASK_TABLE_NAME = "tasks";
    private static final String  COLUMN_ID = "id";
    private static final String  COLUMN_USERNAME = "username";
    private static final String  COLUMN_EMAIL = "email";
    private static final String  COLUMN_PASSWORD = "password";
    private static final String  COLUMN_TASK = "task";
    SQLiteDatabase db;
    private static final String  TASK_TABLE_CREATE = "create table tasks (id integer primary key not null autoincrement , " + "task text not null);";
    private static final String  TABLE_CREATE = "create table users (id integer primary key not null autoincrement , " + "username text not null, email text not null, password text not null);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(TASK_TABLE_CREATE);
        this.db = db;
    }

    public void insertUser(User u) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME,u.getUsername());
        values.put(COLUMN_EMAIL,u.getEmail());
        values.put(COLUMN_PASSWORD,u.getPassword());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public String searchPass(String username){
        db = getReadableDatabase();
        String query = "Select username, password from users";
        Cursor cursor = db.rawQuery(query, null);
        String a, b;
        b = "not found in db";
        if(cursor.moveToFirst()) {
            do {
                a = cursor.getString(0);
                Logger.getLogger(getClass().getName()).log(Level.WARNING, "THIS IS a ");


                if (a.equals(username)) {
                    b = cursor.getString(1);
                    break;

                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return b;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS" + TABLE_NAME;
        String querytwo = "DROP TABLE IF EXISTS" + TASK_TABLE_NAME;
        db.execSQL(query);
        db.execSQL(querytwo);
        this.onCreate(db);
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "goodnight");

    }
    public void insertTask (Tasks t) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK,t.getTask());
        db.insert(TASK_TABLE_NAME,null,values);
        db.close();
    }

}
