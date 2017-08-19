package com.example.anosh.birthdaygiftmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.anosh.birthdaygiftmanager.Database.DataSource;


public class AddGift extends ActionBarActivity {
    String name,id;
    DataSource datasource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gift);
        name=getIntent().getExtras().getString("name");
        id=getIntent().getExtras().getString("id");

        datasource=MainActivity.datasource;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void onAddGift(View v){
        EditText giftName, price, note;
        CheckBox isBought;

        giftName=(EditText) findViewById(R.id.giftName_giftAdd);
        price=(EditText) findViewById(R.id.price_giftAdd);
        note=(EditText) findViewById(R.id.note_giftAdd);
        isBought =(CheckBox) findViewById(R.id.isBought);

        String status="false";
        if(isBought.isChecked()){
            status="true";
        }
        String gname = giftName.getText().toString();
        double pri=0;
        if(!(price.getText().toString()).equals("")){
            pri=Double.parseDouble(price.getText().toString());
        }
        if(!gname.equals("")) {
            datasource.createGift(giftName.getText().toString(), id, pri, note.getText().toString(), status);
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.title_activity_add_gift)
                    .setPositiveButton("You must fill at least Gift name.", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    }).show();
        }
        Intent in=new Intent(this,DetailView.class);
        startActivity(in);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_gift, menu);
        return true;
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();

    }

    @Override
    protected void onPause() {
//        datasource.close();
        super.onPause();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
