package com.example.anosh.birthdaygiftmanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anosh.birthdaygiftmanager.Database.DataSource;

import java.util.Calendar;


public class UpdateBirthday extends ActionBarActivity {

    private static final int CONTACT_REQUEST_CODE=123;
    private static final int CAMERA_REQUEST_CODE=456;
    private static final int GALLERY_REQUEST_CODE=789;

    private String contactId=null;

    EditText personName, dateOfBirth;
    ImageView personImage, getContact, getDate;

    Uri photoUri;
    String pId;

    private DataSource datasource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_birthday);


        personName=(EditText) findViewById(R.id.personNameView);
        personImage=(ImageView) findViewById(R.id.personImage);
        dateOfBirth=(EditText) findViewById(R.id.dateOfBirthView);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        String pname=getIntent().getExtras().getString("name");
        actionBar.setTitle(pname);

        personName.setText(pname);
        personImage.setImageURI(Uri.parse(getIntent().getExtras().getString("image")));
        dateOfBirth.setText(getIntent().getExtras().getString("dob"));
        pId=getIntent().getExtras().getString("id");

        dateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");

            }
        });
        getContact=(ImageView) findViewById(R.id.contact);
        getDate=(ImageView) findViewById(R.id.date);

        datasource = MainActivity.datasource;
    }
    public void onAddBirthday(View v){
        String pname=personName.getText().toString();
        String dob=dateOfBirth.getText().toString();
        if(pname.equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.title_activity_add_birthday)
                    .setPositiveButton("You must fill Person name.", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    }).show();
        }else if(dob.equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.title_activity_add_birthday)
                    .setPositiveButton("You must select the date of birth.", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    }).show();
        }
        else {
            datasource.updateBirthday(Integer.parseInt(pId),pname, dob, String.valueOf(photoUri));
            Intent in = new Intent(this, MainActivity.class);
            startActivity(in);
        }

    }
    public String getPath(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }
    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_birthday, menu);
        return true;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        {
            Bitmap bitmap;
            if(requestCode==CONTACT_REQUEST_CODE && resultCode==RESULT_OK) {
                Uri contactData = data.getData();
                Cursor c = getContentResolver().query(contactData, null, null, null, null);
                if (c.moveToFirst()) {
                    contactId = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone._ID));


                    Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));
                    photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                    personName.setText(c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                    personImage.setImageURI(photoUri);

                }
            }
            else if(requestCode==CAMERA_REQUEST_CODE && resultCode==RESULT_OK){
                photoUri= data.getData();
                if(String.valueOf(photoUri).substring(0, 21).equals("content://com.android")){
                    String image=String.valueOf(photoUri).substring(0, 21);
                    String[] photo_split = image.split("%3A");
                    image = "content://media/external/images/media/" + photo_split[1];
                    personImage.setImageURI(Uri.parse(image));
                }
                else {
                    personImage.setImageURI(photoUri);
                }

            }
            else if(requestCode==GALLERY_REQUEST_CODE && resultCode==RESULT_OK){
                photoUri = data.getData();


                personImage.setImageURI(photoUri);

            }

        }
    }
    public void onImageChange(View v){
        String[] array ={"Take photo","Choose photo from Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a photo")
                .setItems(array,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==1){
                            Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, GALLERY_REQUEST_CODE);
                        }
                        else{
                            Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent,CAMERA_REQUEST_CODE);
                        }
                    }});

        builder.show();
    }
    public void onDateSelect(View v){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
    public void onContactLoad(View v){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, CONTACT_REQUEST_CODE);
    }


    @SuppressLint("ValidFragment")
    public class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        private TextView pDisplayDate;
        private Button pPickDate;
        private int pYear;
        private int pMonth;
        private int pDay;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            pYear = year;
            pMonth = monthOfYear;
            pDay = dayOfMonth;
            updateDisplayActbud();
        }
        private void updateDisplayActbud() {
            String date=(
                    new StringBuilder()
                            // Month is 0 based so add 1
                            .append(pMonth + 1).append("/")
                            .append(pDay).append("/")
                            .append(pYear).append(" ")).toString();


            dateOfBirth.setText(date);
        }

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
