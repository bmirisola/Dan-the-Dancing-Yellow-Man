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
import com.google.android.gms.ads.MobileAds;

// <string name="banner_ad_unit_id">ca-app-pub-3940256099942544/6300978111</string> - test
// <string name="banner_ad_unit_id">ca-app-pub-9744674999881322/4453831094</string> - real bottom

public class MainActivity extends Activity {
    Button danceButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //danceButton = new Button(R.id.)
    }

    protected void onStart() {

        super.onStart();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause(){
        super.onPause();
    }

    protected void onStop(){
        super.onStop();
    }

    protected void onDestroy(){
        super.onDestroy();
    }
}
