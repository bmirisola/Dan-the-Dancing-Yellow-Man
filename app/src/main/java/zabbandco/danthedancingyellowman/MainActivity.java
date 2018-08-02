package zabbandco.danthedancingyellowman;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import util.Constants;


// <string name="banner_ad_unit_id">ca-app-pub-3940256099942544/6300978111</string> - test
// <string name="banner_ad_unit_id">ca-app-pub-9744674999881322/4453831094</string> - real bottom

public class MainActivity extends Activity {
    private Button danceButton;
    private Button danYourFriendsButton;
    private ImageView dancer;
    private TextView danPointsText;
    SmsManager smsManager;
    private int danPoints;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        danceButton = findViewById(R.id.dance_button);
        danYourFriendsButton = findViewById(R.id.dan_friends_button);
        dancer = findViewById(R.id.dan);
        danPointsText = findViewById(R.id.danpoints);
        smsManager = SmsManager.getDefault();

        //Permissions
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        //Shared preferences
        preferences = getPreferences(MODE_PRIVATE);
        editor = preferences.edit();

        danPoints = preferences.getInt(Constants.DAN_POINTS_KEY, 0);
        danPointsText.setText(String.format("Your Dan Points: %d", danPoints));


        Log.d("Oncreate", "On create counter = " + Integer.toString(Dancer.counter));

        danceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dancer.changeDan(dancer);
                danPoints++;
                danPointsText.setText(String.format("Your Dan Points: %d", danPoints));
                Log.d("Button Press", Integer.toString(Dancer.counter));
            }
        });

        danYourFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE}, 1);
                }

                final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
                Intent intentPickContact = new Intent(Intent.ACTION_PICK, uriContact);
                startActivityForResult(intentPickContact, Constants.REQUEST_CODE_CONTACTS);

            }
        });
    }

    protected void onStart() {
        super.onStart();

        Log.d("Onstart", "On Start counter = " + Integer.toString(Dancer.counter));

    }

    protected void onResume() {
        super.onResume();
        Log.d("OnResume", "On Resume counter = " + Integer.toString(Dancer.counter));

    }

    protected void onPause() {
        super.onPause();
        editor.putInt(Constants.DAN_POINTS_KEY, danPoints);
        editor.commit();
        Log.d("OnPause", "On Pause counter = " + Integer.toString(Dancer.counter));

    }

    protected void onStop() {
        super.onStop();
        editor.putInt(Constants.DAN_POINTS_KEY, danPoints);
        editor.commit();
    }

    protected void onDestroy() {
        super.onDestroy();
        editor.putInt(Constants.DAN_POINTS_KEY, danPoints);
        editor.commit();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.DANCE_COUNTER_ID, Dancer.counter);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Dancer.counter = savedInstanceState.getInt(Constants.DANCE_COUNTER_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQUEST_CODE_CONTACTS) {
                Uri returnUri = data.getData();
                Cursor cursor = getContentResolver().query(returnUri, null, null, null, null);

                if (cursor.moveToNext()) {
                    int columnIndex_ID = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                    String contactID = cursor.getString(columnIndex_ID);

                    int columnIndex_HASPHONENUMBER = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                    String stringHasPhoneNumber = cursor.getString(columnIndex_HASPHONENUMBER);

                    if (stringHasPhoneNumber.equalsIgnoreCase("1")) {
                        Cursor cursorNum = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactID, null, null);

                        //Get the first phone number
                        if (cursorNum.moveToNext()) {
                            int columnIndex_number = cursorNum.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            String phoneNumber = cursorNum.getString(columnIndex_number);
                            smsManager.sendTextMessage(String.valueOf(phoneNumber), null, "You just got Danned! Download Dan the Dancing Yellow Man. Available on Android today!", null, null);
                            Toast.makeText(MainActivity.this, "You just Danned your friend " + Contacts.getContactName(phoneNumber, MainActivity.this) + "!", Toast.LENGTH_LONG).show();
                        }

                    } else {

                    }


                } else {
                    Toast.makeText(getApplicationContext(), "NO data!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //Toast.makeText(MainActivity.this, "Permission denied to read your Contacts", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Permission denied to send SMS. Please allow, so that you can Dan people and be cool", Toast.LENGTH_LONG).show();
                }

            }

            return;
        }

        // other 'case' lines to check for other
        // permissions this app might request
    }
}
