package com.example.garrett.updatedtestgameplan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBHelper extends SQLiteOpenHelper {
    private static final String LOG = DBHelper.class.getName();
    private static final int DATABASE_VERSION = 1;
    private static final String  DATABASE_NAME = "users.db";
    private static final String  TABLE_NAME = "users";
    private static final String  TASK_TABLE_NAME = "tasks";
    private static final String  INTERSECTION_TABLE_NAME = "intersection";
    private static final String  COLUMN_ID = "id";
    private static final String  COLUMN_USERNAME = "username";
    private static final String  COLUMN_EMAIL = "email";
    private static final String  COLUMN_PASSWORD = "password";
    private static final String  COLUMN_TASK = "task";
    private static final String  USERID_COLUMN = "userid";
    private static final String  TASKID_COLUMN = "taskid";
    SQLiteDatabase db;
    private static final String  TASK_TABLE_CREATE = "create table tasks (id integer primary key not null  , " + "task text not null);";
    private static final String  TABLE_CREATE = "create table users (id integer primary key not null  , " + "username text not null, email text not null, password text not null);";
    private static final String  INTERSECTION_TABLE_CREATE = "create table intersection (id integer primary key not null  , " + "userid integer not null, taskid integer not null);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(TASK_TABLE_CREATE);
        db.execSQL(INTERSECTION_TABLE_CREATE);
        this.db = db;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       String query = "DROP TABLE IF EXISTS" + TABLE_NAME;
       String querytwo = "DROP TABLE IF EXISTS" + TASK_TABLE_NAME;
        String querythree = "DROP TABLE IF EXISTS" + INTERSECTION_TABLE_NAME;
        db.execSQL(query);
        db.execSQL(querytwo);
        db.execSQL(querythree);
       this.onCreate(db);
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "goodnight");

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

    public User searchPass(String username){
        db = getReadableDatabase();
        String query = "Select * from users WHERE username = '" + username + "'" ;
        Cursor cursor = db.rawQuery(query, null);
        String a, b;
        b = "not found in db";
        User user = new User();
        if(cursor.moveToFirst()) {
            user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            user.setUsername((cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME))));
            user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
//            do {
//                a = cursor.getString(0);
//
//
//                if (a.equals(username)) {
//                    b = cursor.getString(1);
//                    break;
//
//                }
//            }while (cursor.moveToNext());
        }
        cursor.close();
        return user;

    }

    public long insertTask (Tasks t) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK,t.getTask());
        long id = db.insert(TASK_TABLE_NAME,null,values);
        db.close();
        return id;
    }
    public User getUser(long user_id) {
        db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_ID + " = " + user_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        User td = new User();
        td.setId(c.getInt(c.getColumnIndex(COLUMN_ID)));
        td.setUsername((c.getString(c.getColumnIndex(COLUMN_USERNAME))));
        return td;
    }
    //getting all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                User td = new User();
                td.setId(c.getInt((c.getColumnIndex(COLUMN_ID))));
                td.setUsername((c.getString(c.getColumnIndex(COLUMN_USERNAME))));

                // adding to todo list
                users.add(td);
            } while (c.moveToNext());
        }
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "THIS worked PART 1 ");

        return users;
    }
    //getting users by tasks
    public List<Tasks> getAllTasksByUser(int userId) {
        List<Tasks> tasks = new ArrayList<>();

        String selectQuery = "SELECT tasks.id as taskid, tasks.task as task  FROM " + TASK_TABLE_NAME + " tasks, "
                + TABLE_NAME + " users, " + INTERSECTION_TABLE_NAME + " intersection WHERE users.id = '" + userId
                + "'" + " AND intersection.userid "
                + " = " + "users." + COLUMN_ID + " AND tasks." + COLUMN_ID + " = "
                + "intersection." + TASKID_COLUMN;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Tasks alltasks = new Tasks();
                alltasks.setId(c.getInt((c.getColumnIndex("taskid"))));
                alltasks.setTask((c.getString(c.getColumnIndex(COLUMN_TASK))));

                tasks.add(alltasks);
            } while (c.moveToNext());
        }
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "THIS worked PART 2 ");

        return tasks;

    }

    //getting all tasks
    public List<Tasks> getAllTasks() {
        List<Tasks> tasks = new ArrayList<Tasks>();
        String selectQuery = "SELECT  * FROM " + TASK_TABLE_NAME;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Tasks t = new Tasks();
                t.setId(c.getInt((c.getColumnIndex(COLUMN_ID))));
                t.setTask(c.getString(c.getColumnIndex(COLUMN_TASK)));

                // adding to tags list
                tasks.add(t);
            } while (c.moveToNext());
        }
        return tasks;
    }
    //tasks that link up with the users
    public long createIntersection(long userid, long taskid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USERID_COLUMN, userid);
        values.put(TASKID_COLUMN, taskid);

        long id = db.insert(INTERSECTION_TABLE_NAME, null, values);

        Logger.getLogger(getClass().getName()).log(Level.WARNING, "THIS worked PART 3 ");

        return id;
    }
    public int updateIntersection(long id, long tag_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TASKID_COLUMN, tag_id);

        // updating row
        return db.update(INTERSECTION_TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[] { String.valueOf(id) });
    }
    public void deleteIntersection(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(INTERSECTION_TABLE_NAME, COLUMN_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public void deleteTask(long task) {
        db = this.getWritableDatabase();
        db.execSQL("DELETE from intersection WHERE taskid = " + task );
        db.execSQL("DELETE from tasks WHERE id = " + task);
    }

    public void closeDB() {
         db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "THIS worked PART 4 ");

    }
}
