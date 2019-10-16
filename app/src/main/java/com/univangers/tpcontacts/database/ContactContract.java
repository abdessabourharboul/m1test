package com.univangers.tpcontacts.database;

import android.provider.BaseColumns;

public final class ContactContract {

    private ContactContract() {
    }

    public static class ContactEntry implements BaseColumns {
        public static final String TABLE_NAME = "Contact";
        public static final String COLUMN_FIRST_NAME = "nom";
        public static final String COLUMN_LAST_NAME = "prenom";
        public static final String COLUMN_TEL = "numTel";
    }
}
