package com.example.anosh.birthdaygiftmanager;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Anosh on 5/13/2015.
 */
public class CustomAdapter extends ArrayAdapter<Birthday> {
    ArrayList<Birthday> birthday;
    Activity context;

    public CustomAdapter(Activity context,ArrayList<Birthday> birthday) {
        super(context, R.layout.activity_detail_view, birthday);
        this.context = context;
        this.birthday = birthday;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.birthday_view, null, true);

        Collections.sort(birthday, Birthday.days);// to sort by days left

        TextView personName,bday,age1,daysLeft1;
        ImageView personImage;
        personName=(TextView) rowView.findViewById(R.id.personName);
        bday=(TextView) rowView.findViewById(R.id.birthday);
        age1=(TextView) rowView.findViewById(R.id.age);
        daysLeft1=(TextView) rowView.findViewById(R.id.daysLeft);
        personImage=(ImageView) rowView.findViewById(R.id.personImage);

        personName.setText(birthday.get(position).getPersonName());
        bday.setText(birthday.get(position).getDob());
        age1.setText(String.valueOf(birthday.get(position).getAge()));
        daysLeft1.setText(days(birthday.get(position).getDaysLeft()));
        String image=birthday.get(position).getImage();
//
        try {
            personImage.setImageURI(Uri.parse(image));
        }
        catch (OutOfMemoryError e) {

        }
        catch(Exception e){

        }


        return rowView;
    }

    String days(int days){
        String show=""+days +" days left";
        if(days==0){
            show="today";
        }
        else if(days==1){
            show="tomorrow";
        }
        return show;
    }

}
