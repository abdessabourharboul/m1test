package com.univangers.tpcontacts;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.univangers.tpcontacts.database.ContactContract;

import java.util.ArrayList;
import java.util.List;

public final class FakeData {

    /**
     * Cette méthode retourne la liste des contacts
     *
     * @return Une liste de tâches
     */
    public static Element[] get_contacts() {
        return new Element[]{
                new Element("Seguin", "Gérard", "+33612121212"),
                new Element("Dupré", "Tyler", "+33612121213"),
                new Element("Lawton", "Diane", "+32612121214"),
                new Element("Carter", "Lydia", "+32612121215"),
                new Element("Leduc", "Nicolas", "+33612121216"),
                new Element("Garnier", "Amélie", "+33612121217"),
                new Element("Doe", "John", "+31612121218"),
        };

    }

    public static void insert_fake_data(SQLiteDatabase db) {
        if (db == null) {
            return;
        }

        Element[] contacts = get_contacts();
        //create a list of fake guests
        List<ContentValues> list = new ArrayList<ContentValues>();
        ContentValues cv;
        for (Element e : contacts) {
            cv = new ContentValues();
            cv.put(ContactContract.ContactEntry.COLUMN_FIRST_NAME, e.firstname);
            cv.put(ContactContract.ContactEntry.COLUMN_LAST_NAME, e.name);
            cv.put(ContactContract.ContactEntry.COLUMN_TEL, e.phonenumber);
            list.add(cv);
        }

        //insert all guests in one transaction
        try {
            db.beginTransaction();
            //clear the table first
            db.delete(ContactContract.ContactEntry.TABLE_NAME, null, null);
            //go through the list and add one by one
            System.out.println("size "+ list.size());
            for (ContentValues c : list) {
                db.insert(ContactContract.ContactEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            //too bad :(
        } finally {
            db.endTransaction();
        }

    }

    public static class Element {
        public String name;
        public String firstname;
        public String phonenumber;

        Element(String _name, String _firstname, String _phonenumber) {
            name = _name;
            firstname = _firstname;
            phonenumber = _phonenumber;
        }
    }
}
