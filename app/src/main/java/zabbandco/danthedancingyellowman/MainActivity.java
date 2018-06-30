package zabbandco.danthedancingyellowman;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

// <string name="banner_ad_unit_id">ca-app-pub-3940256099942544/6300978111</string> - test
// <string name="banner_ad_unit_id">ca-app-pub-9744674999881322/4453831094</string> - real bottom

public class MainActivity extends Activity {
    TextView textPhone;
    final int RQS_PICKCONTACT = 1;
    Button dancebutton;
    Button danbutton;
    int wd = 0;
    SmsManager smsManager = SmsManager.getDefault();
    MediaPlayer umph;
    public static final int Stream_Music = 3;
    ImageView dan;
    TextView danpoints;
    String Tag = MainActivity.class.getSimpleName();
    int dp;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.SEND_SMS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        umph = MediaPlayer.create(this, R.raw.cutumph);
        dancebutton = (Button) (findViewById(R.id.button));
        danbutton = (Button) (findViewById(R.id.danbutton));
        textPhone = (TextView)findViewById(R.id.textView);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setVolumeControlStream(Stream_Music);
        danpoints = (TextView) (findViewById(R.id.danpoints));
        danpoints.setText(String.format("Your Dan Points %d", dp));
        SharedPreferences sp = getSharedPreferences("Dan_Points", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("DanPoi", dp);
        editor.commit();
        dp = sp.getInt("Dan_Points", 0);
        dancebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dan = (ImageView) (findViewById(R.id.Dan));
                wd++;
                dp++;
                umph.start();
                danpoints.setText(String.format("Your Dan Points %d", dp));
                Log.d(Tag, "Not Danned");
                if (wd == 1) {
                    dan.setImageResource(R.drawable.danthemandancingleft);
                } else if (wd == 2) {
                    dan.setImageResource(R.drawable.danthemanstanding);
                } else if (wd == 3) {
                    dan.setImageResource(R.drawable.danthemandancingright);
                } else if (wd == 4) {
                    dan.setImageResource(R.drawable.danthemanstanding);
                } else if (wd > 4) {
                    dan.setImageResource(R.drawable.danthemandancingleft);
                    wd = 1;
                }


            }
        });

        danbutton.setOnClickListener(new View.OnClickListener() {

                                         public void onClick(View v) {
                                             Log.d(Tag, "Yes Danned");
                                             final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
                                             Intent intentPickContact = new Intent(Intent.ACTION_PICK, uriContact);
                                             startActivityForResult(intentPickContact, RQS_PICKCONTACT);
                                         }
                                     }
        );

    }



    @Override
    protected void onPause() {
        super.onPause();
        umph.release();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
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
                    Toast.makeText(MainActivity.this, "Permission denied to read your Contacts", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Permission denied to send SMS. Please allow, so that you can Dan people and be cool", Toast.LENGTH_LONG).show();
                }

            }

                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if(resultCode == RESULT_OK){
            if(requestCode == RQS_PICKCONTACT){
                Uri returnUri = data.getData();
                Cursor cursor = getContentResolver().query(returnUri, null, null, null, null);

                if(cursor.moveToNext()){
                    int columnIndex_ID = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                    String contactID = cursor.getString(columnIndex_ID);

                    int columnIndex_HASPHONENUMBER = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                    String stringHasPhoneNumber = cursor.getString(columnIndex_HASPHONENUMBER);

                    if(stringHasPhoneNumber.equalsIgnoreCase("1")){
                        Cursor cursorNum = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactID, null, null);

                        //Get the first phone number
                        if(cursorNum.moveToNext()){
                            int columnIndex_number = cursorNum.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            String stringNumber = cursorNum.getString(columnIndex_number);
                            textPhone.setText(stringNumber);
                            smsManager.sendTextMessage(String.valueOf(textPhone.getText()), null, "You just got Danned! Download Dan the Dancing Yellow Man. Available on Android today!", null, null);
                        }

                    }else{
                        textPhone.setText("NO Phone Number");
                    }


                }else{
                    Toast.makeText(getApplicationContext(), "NO data!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

   @Override
    public void onResume ()
    {
        super.onResume();
        setContentView(R.layout.activity_main);
        umph = MediaPlayer.create(this, R.raw.cutumph);
        dancebutton = (Button) (findViewById(R.id.button));
        danbutton = (Button) (findViewById(R.id.danbutton));
        textPhone = (TextView)findViewById(R.id.textView);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setVolumeControlStream(Stream_Music);
        danpoints = (TextView) (findViewById(R.id.danpoints));
        danpoints.setText(String.format("Your Dan Points %d", dp));
        SharedPreferences sp = getSharedPreferences("Dan_Points", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("DanPoi", dp);
        editor.commit();
        dp = sp.getInt("Dan_Points", 0);
        dancebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dan = (ImageView) (findViewById(R.id.Dan));
                wd++;
                dp++;
                umph.start();
                danpoints.setText(String.format("Your Dan Points %d", dp));
                Log.d(Tag, "Not Danned");
                if (wd == 1) {
                    dan.setImageResource(R.drawable.danthemandancingleft);
                } else if (wd == 2) {
                    dan.setImageResource(R.drawable.danthemanstanding);
                } else if (wd == 3) {
                    dan.setImageResource(R.drawable.danthemandancingright);
                } else if (wd == 4) {
                    dan.setImageResource(R.drawable.danthemanstanding);
                } else if (wd > 4) {
                    dan.setImageResource(R.drawable.danthemandancingleft);
                    wd = 1;
                }


            }
        });

        danbutton.setOnClickListener(new View.OnClickListener() {

                                         public void onClick(View v) {
                                             Log.d(Tag, "Yes Danned");
                                             final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
                                             Intent intentPickContact = new Intent(Intent.ACTION_PICK, uriContact);
                                             startActivityForResult(intentPickContact, RQS_PICKCONTACT);
                                         }
                                     }
        );
    }

}
