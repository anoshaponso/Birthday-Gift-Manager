package com.example.anosh.birthdaygiftmanager.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.anosh.birthdaygiftmanager.Birthday;
import com.example.anosh.birthdaygiftmanager.Gift;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Anosh on 5/11/2015.
 */
public class DataSource {
    private SQLiteDatabase database;
    private DBClass dbHelper;

    private String[] allColumnsBirthday = { DBClass.COLUMN_ID_BIRTHDAY,DBClass.COLUMN_PERSON_NAME,DBClass.COLUMN_PERSON_DOB ,DBClass.COLUMN_PERSON_IMAGE};

    private String[] allColumnsGifts = { DBClass.COLUMN_ID_GIFT,DBClass.COLUMN_GIFT_NAME,DBClass.COLUMN_GIFT_ID_BIRTHDAY,
            DBClass.COLUMN_GIFT_PRICE,DBClass.COLUMN_GIFT_NOTE , DBClass.COLUMN_GIFT_STATUS};

    int age, dayCount;

    public DataSource(Context context) {
        dbHelper = new DBClass(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    //Birthday Table
    public Birthday createBirthday(String personName,String bday, String imagepath) {
        ContentValues values = new ContentValues();
        values.put(DBClass.COLUMN_PERSON_DOB, bday);
        values.put(DBClass.COLUMN_PERSON_NAME, personName);
        values.put(DBClass.COLUMN_PERSON_IMAGE,imagepath);
        long insertId = database.insert(DBClass.TABLE_BIRTHDAY, null,
                values);
        Cursor cursor = database.query(DBClass.TABLE_BIRTHDAY,
                allColumnsBirthday, DBClass.COLUMN_ID_BIRTHDAY + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Birthday newComment = cursorToBirthday(cursor);
        cursor.close();
        return newComment;
    }

    public void deleteBirthday(int id) {

        System.out.println("Comment deleted with id: " + id);
        database.delete(DBClass.TABLE_BIRTHDAY, DBClass.COLUMN_ID_BIRTHDAY
                + " = " + id, null);
    }
    public void updateBirthday(int id,String personName,String bday, String imagepath) {
        ContentValues values = new ContentValues();
        values.put(DBClass.COLUMN_PERSON_DOB, bday);
        values.put(DBClass.COLUMN_PERSON_NAME, personName);
        values.put(DBClass.COLUMN_PERSON_IMAGE,imagepath);

        System.out.println("Comment deleted with id: " + id);
        database.update(DBClass.TABLE_BIRTHDAY, values, DBClass.COLUMN_ID_BIRTHDAY
                + " = " + id, null);
    }

    public ArrayList<Birthday> getAllBirthdays() {
        ArrayList<Birthday> birthdays = new ArrayList<Birthday>();

        Cursor cursor = database.query(DBClass.TABLE_BIRTHDAY,
                allColumnsBirthday, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Birthday birthday = cursorToBirthday(cursor);
            birthdays.add(birthday);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return birthdays;
    }

    private Birthday cursorToBirthday(Cursor cursor) {
        Birthday birthday = new Birthday();
        birthday.setId(cursor.getLong(0));
        birthday.setPersonName(cursor.getString(1));
        String bday=cursor.getString(2);
        calculateAge(bday);
        birthday.setDob(bday);
        birthday.setImage(cursor.getString(3));
        birthday.setAge(age);
        birthday.setDaysLeft(dayCount);
        return birthday;
    }

    //Gift Table

    //to add values into Gift Table
    public Gift createGift(String giftName,String bdayId,double price,String note,String isBought) {
        ContentValues values = new ContentValues();
        values.put(DBClass.COLUMN_GIFT_NAME, giftName);
        values.put(DBClass.COLUMN_GIFT_ID_BIRTHDAY, bdayId);
        values.put(DBClass.COLUMN_GIFT_PRICE, price);
        values.put(DBClass.COLUMN_GIFT_STATUS, isBought);
        values.put(DBClass.COLUMN_GIFT_NOTE, note);
        long insertId = database.insert(DBClass.TABLE_GIFT, null, values);
        Cursor cursor = database.query(DBClass.TABLE_GIFT,
                allColumnsGifts, DBClass.COLUMN_ID_GIFT + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Gift newGift = cursorToGift(cursor);
        cursor.close();
        return newGift;
    }

    //to delete a value in gift table
    public void deleteGift(int id) {

        System.out.println("Comment deleted with id: " + id);
        database.delete(DBClass.TABLE_GIFT, DBClass.COLUMN_ID_GIFT
                + " = " + id, null);
    }
    public void updateGift(int id,String giftName,double price,String note,String isBought) {
        ContentValues values = new ContentValues();
        values.put(DBClass.COLUMN_GIFT_NAME, giftName);

        values.put(DBClass.COLUMN_GIFT_PRICE, price);
        values.put(DBClass.COLUMN_GIFT_STATUS, isBought);
        values.put(DBClass.COLUMN_GIFT_NOTE, note);

        System.out.println("Comment deleted with id: " + id);
        database.update(DBClass.TABLE_GIFT,values, DBClass.COLUMN_ID_GIFT
                + " = " + id, null);
    }
    //to get all Gifts data to a list
    public ArrayList<Gift> getAllGifts(String id) {
        ArrayList<Gift> gifts = new ArrayList<Gift>();

        Cursor cursor = database.query(DBClass.TABLE_GIFT,
                allColumnsGifts, DBClass.COLUMN_GIFT_ID_BIRTHDAY + "=" + id, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Gift gift = cursorToGift(cursor);
            gifts.add(gift);
            cursor.moveToNext();
        }

        cursor.close();
        return gifts;
    }


    private Gift cursorToGift(Cursor cursor) {
        Gift gift = new Gift();
        gift.setId(cursor.getLong(0));
        gift.setGiftName(cursor.getString(1));
        gift.setBirthdayId(cursor.getString(2));
        gift.setPrice(cursor.getDouble(3));
        gift.setNote(cursor.getString(4));
        gift.setStatus(cursor.getString(5));

        return gift;
    }

    public void calculateAge(String birthday){
        String[] arr=birthday.split("/");
        int month = Integer.parseInt(arr[0])-1;
        int date = Integer.parseInt(arr[1]);
        int year = Integer.parseInt(arr[2].replace(" ",""));

        Calendar now = Calendar.getInstance();
        Calendar dob = Calendar.getInstance();
        Calendar thisBirthday=dob;
        dob.set(year, month, date);
        if (dob.after(now)) {
            age=0;
        }
        int year1 = now.get(Calendar.YEAR);
        int year2 = dob.get(Calendar.YEAR);
        age= year1 - year2;
        int month1 = now.get(Calendar.MONTH);
        int month2 = dob.get(Calendar.MONTH);

        if (month2 < month1) {
            thisBirthday.set(Calendar.YEAR,now.get(Calendar.YEAR)+1);
            dayCount= (int) ((thisBirthday.getTimeInMillis() - now.getTimeInMillis())/ (24 * 60 * 60 * 1000));
            age++;
        }
        else if (month1 == month2) {
            int day1 = now.get(Calendar.DAY_OF_MONTH);
            int day2 = dob.get(Calendar.DAY_OF_MONTH);
            if (day2 < day1) {
                thisBirthday.set(Calendar.YEAR,now.get(Calendar.YEAR)+1);
                age++;
                dayCount= (int) ((thisBirthday.getTimeInMillis() - now.getTimeInMillis())/ (24 * 60 * 60 * 1000));
            }
            else{
                thisBirthday.set(Calendar.YEAR,now.get(Calendar.YEAR));
                dayCount= (int) ((thisBirthday.getTimeInMillis() - now.getTimeInMillis())/ (24 * 60 * 60 * 1000));
            }
        }
        else{
            thisBirthday.set(Calendar.YEAR,now.get(Calendar.YEAR)+1);
            dayCount= (int) ((thisBirthday.getTimeInMillis() - now.getTimeInMillis())/ (24 * 60 * 60 * 1000));
        }
    }
}
