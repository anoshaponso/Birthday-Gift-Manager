package com.example.anosh.birthdaygiftmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import com.example.anosh.birthdaygiftmanager.Database.DataSource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;


public class MainActivity  extends ActionBarActivity {
    public static DataSource datasource;
    int dayCount,age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);

        datasource = new DataSource(this);
        datasource.open();
        loadListView();
    }

    public void loadListView(){
        final ArrayList<Birthday> values = datasource.getAllBirthdays();
        if(values.size()==0){
            Toast.makeText(this, "Use the \"+\" button at the top right of the screen to add a birthday  ", Toast.LENGTH_SHORT).show();
        }
        else {

            ListView ls = (ListView) findViewById(android.R.id.list);
            CustomAdapter adapter = new CustomAdapter(this, values);

            ls.setAdapter(adapter);
            ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String name = values.get(position).getPersonName();
                    String id1 = String.valueOf(values.get(position).getId());

                    Intent i = new Intent(MainActivity.this, DetailView.class);
                    i.putExtra("name", name);
                    i.putExtra("id", id1);
                    startActivity(i);
                }
            });
            ls.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                               final int position, long arg3) {
                    // TODO Auto-generated method stub
                    String[] array ={"Update","Delete"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Choose a photo")
                            .setItems(array,new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    final int p = position;
                                    int id1= (int) values.get(p).getId();
                                    if(which==0){
                                        Intent in=new Intent(MainActivity.this,UpdateBirthday.class);
                                        in.putExtra("id",String.valueOf(id1));
                                        in.putExtra("name", values.get(p).getPersonName());
                                        in.putExtra("image", values.get(p).getImage());
                                        in.putExtra("dob",values.get(p).getDob());
                                        startActivity(in);
                                    }
                                    else{

                                        deleteBirthday(id1);
                                    }
                                }}).show();

                    return true;
                }
            });
        }
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
        loadListView();
    }

    @Override
    protected void onPause() {
//        datasource.close();
        super.onPause();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent in=new Intent(this,AddBirthday.class);
            startActivity(in);
        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteBirthday(int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage("Do you want to delete this Birthday?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        datasource.deleteBirthday(id);
                        onResume();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        builder.show();
    }
}
