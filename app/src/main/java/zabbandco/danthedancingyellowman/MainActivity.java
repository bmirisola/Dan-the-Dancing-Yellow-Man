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
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.READ_CONTACTS)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.READ_CONTACTS},
                                Constants.REQUEST_CODE_CONTACTS);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    // Permission has already been granted
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, Constants.REQUEST_CODE_CONTACTS);
                }


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
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (Constants.REQUEST_CODE_CONTACTS):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String num = "";
                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers.moveToNext()) {
                                num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                Toast.makeText(MainActivity.this, "Number=" + num, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    break;
                }
        }
    }
}
