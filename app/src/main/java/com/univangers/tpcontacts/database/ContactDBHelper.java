package com.univangers.tpcontacts.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Contact_DB.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ContactContract.ContactEntry.TABLE_NAME + " (" +
                    ContactContract.ContactEntry.COLUMN_FIRST_NAME + " TEXT," +
                    ContactContract.ContactEntry.COLUMN_LAST_NAME + " TEXT," +
                    ContactContract.ContactEntry.COLUMN_TEL + " TEXT"
                    + " )";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ContactContract.ContactEntry.TABLE_NAME;

    public ContactDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void getContactsByNomOrPrenom(SQLiteDatabase db , String wanted) {
        Cursor cursor = db.query(ContactContract.ContactEntry.TABLE_NAME, null, "instr(nom, ?) != 0 OR instr(prenom, ?) != 0", new String[] { wanted }, null, null, null);
    }
}
