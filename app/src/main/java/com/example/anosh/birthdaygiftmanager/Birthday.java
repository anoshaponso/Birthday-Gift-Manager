package com.example.anosh.birthdaygiftmanager;

import java.util.Comparator;

/**
 * Created by Anosh on 5/11/2015.
 */
public class Birthday {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    long id;
    String personName;
    String dob;
    String contact;
    int daysLeft;
    int age;

    public  int getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String image;
    public static Comparator<Birthday> days = new Comparator<Birthday>() {

        public int compare(Birthday b1, Birthday b2) {

            int dayno1 = b1.getDaysLeft();
            int dayno2 = b2.getDaysLeft();

	   /*For ascending order*/
            return dayno1-dayno2;

        }
    };
}
