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
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.anosh.birthdaygiftmanager.Database.DataSource;

import java.util.ArrayList;
import java.util.HashMap;


public class DetailView extends ActionBarActivity {
    String name,id1;
    DataSource datasource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        datasource=MainActivity.datasource;

        ActionBar actionBar = getSupportActionBar();
        name=getIntent().getExtras().getString("name");
        id1=getIntent().getExtras().getString("id");

        actionBar.setTitle(name);
        loadListView();
    }
    public void loadListView(){
        final ArrayList<Gift> values = datasource.getAllGifts(id1);
        ArrayList<HashMap<String, String>> Items = new ArrayList<HashMap<String, String>>();
        int size=values.size();
        if(size==0){
            Toast.makeText(this, "Use the \"+\" button at the top right of the screen to add a gift  ", Toast.LENGTH_SHORT).show();
        }
        else {
            for (int i = 0; i < size; i++) {

                // Writing values to map
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("name", values.get(i).getGiftName());
                String status="Bought";
                if(values.get(i).getStatus().equals("false")){
                    status="Not Bought";
                }
                map.put("bought", status);

                map.put("price", String.valueOf(values.get(i).getPrice()));


                Items.add(map);
            }
            ListView ls = (ListView) findViewById(R.id.listView);

            ListAdapter adapter = new SimpleAdapter(this, Items,
                    R.layout.gift_view, new String[]{"name", "bought", "price"},
                    new int[]{R.id.giftName, R.id.status, R.id.price});

            ls.setAdapter(adapter);
            ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(DetailView.this, UpdateGift.class);
                    i.putExtra("gname", values.get(position).getGiftName());
                    i.putExtra("bought", values.get(position).getStatus());
                    i.putExtra("price", String.valueOf(values.get(position).getPrice()));
                    i.putExtra("gid", values.get(position).getId());
                    i.putExtra("note",values.get(position).getNote());
                    i.putExtra("id",id1);
                    startActivity(i);
                }
            });
            ls.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                               final int position, long arg3) {
                    // TODO Auto-generated method stub
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailView.this);
                    final int p = position;
                    builder.setMessage("Do you want to delete Gift ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    datasource.deleteGift((int) values.get(p).getId());
                                    onResume();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                    builder.show();

                    return true;
                }
            });
        }

    }
    @Override
    protected void onResume() {

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
        getMenuInflater().inflate(R.menu.menu_detail_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent in=new Intent(this,AddGift.class);
            in.putExtra("name", name);
            in.putExtra("id", id1);
            startActivity(in);
        }

        return super.onOptionsItemSelected(item);
    }
}
