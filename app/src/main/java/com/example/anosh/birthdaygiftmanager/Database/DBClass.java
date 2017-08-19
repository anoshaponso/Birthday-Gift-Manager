package com.example.anosh.birthdaygiftmanager.Database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Anosh on 5/10/2015.
 */
public class DBClass extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BirthdayGifts";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_BIRTHDAY = "birthday";
    public static final String COLUMN_ID_BIRTHDAY = "birthday_id";
    public static final String COLUMN_PERSON_NAME="name";
    public static final String COLUMN_PERSON_DOB="dob";
    public static final String COLUMN_PERSON_IMAGE="image";

    public static final String TABLE_GIFT = "gift";
    public static final String COLUMN_ID_GIFT = "gift_id";
    public static final String COLUMN_GIFT_NAME="gift_name";
    public static final String COLUMN_GIFT_ID_BIRTHDAY="birthday_id";
    public static final String COLUMN_GIFT_PRICE="price";
    public static final String COLUMN_GIFT_STATUS="status";
    public static final String COLUMN_GIFT_NOTE="note";

    public DBClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String TABLE_BIRTHDAY_CREATE = "create table "
            + TABLE_BIRTHDAY + "(" + COLUMN_ID_BIRTHDAY
            + " integer primary key autoincrement, " + COLUMN_PERSON_NAME
            + " text not null,"+COLUMN_PERSON_DOB + " text not null,"
            +COLUMN_PERSON_IMAGE+ " text);";

    private static final String TABLE_GIFT_CREATE = "create table "
            + TABLE_GIFT + "(" + COLUMN_ID_GIFT
            + " integer primary key autoincrement, " + COLUMN_GIFT_NAME
            + " text not null,"+COLUMN_GIFT_ID_BIRTHDAY + " integer not null,"
            + COLUMN_GIFT_PRICE+" real not null,"
            + COLUMN_GIFT_STATUS+" text,"
            +COLUMN_GIFT_NOTE+" text);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_BIRTHDAY_CREATE);
        db.execSQL(TABLE_GIFT_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BIRTHDAY_CREATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GIFT_CREATE);
        onCreate(db);
    }
}
