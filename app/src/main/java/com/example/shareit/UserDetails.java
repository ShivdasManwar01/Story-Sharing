package com.example.shareit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class UserDetails extends SQLiteOpenHelper {
    private static final String DB_NAME = "user";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "user_details";
    private static final String ID_COL = "user_id";
    private static final String NAME_COL = "user_name";
    private static final String EMAIL_COL = "user_email";
    private static final String PASSWORD = "user_password";

    public UserDetails(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + EMAIL_COL + " TEXT,"
                + PASSWORD + " TEXT)";
        db.execSQL(query);

    }

    public void addNewUser(String userName, String userEmail, String userPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(NAME_COL, userName);
        values.put(EMAIL_COL, userEmail);
        values.put(PASSWORD, userPassword);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    public ArrayList<String> readEmail() {
        // on below line we are creating a database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorEmail = db.rawQuery("SELECT " + EMAIL_COL + " FROM " + TABLE_NAME, null);

        // on below line we are creating a new array list.
        ArrayList<String> emailArraylist = new ArrayList<>();

        // moving our cursor to first position.
        cursorEmail.moveToFirst();
        do {
            // on below line we are adding the data from cursor to our array list.
            emailArraylist.add(cursorEmail.getString(cursorEmail.getColumnIndex(UserDetails.EMAIL_COL)));
        } while (cursorEmail.moveToNext());
        // moving our cursor to next.

        // at last closing our cursor
        // and returning our array list.
        cursorEmail.close();
        return emailArraylist;
    }

    public ArrayList<String> readPassword(String username) {
        // on below line we are creating a database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorPassword = db.rawQuery("SELECT " + PASSWORD + " FROM " + TABLE_NAME + " WHERE " + EMAIL_COL + "=?", new String[]{username});

        // on below line we are creating a new array list.
        ArrayList<String> passwordArraylist = new ArrayList<>();

        // moving our cursor to first position.
        cursorPassword.moveToFirst();
        do {
            // on below line we are adding the data from cursor to our array list.
            passwordArraylist.add(cursorPassword.getString(cursorPassword.getColumnIndex(UserDetails.PASSWORD)));
        } while (cursorPassword.moveToNext());
        // moving our cursor to next.

        // at last closing our cursor
        // and returning our array list.
        cursorPassword.close();
        return passwordArraylist;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
