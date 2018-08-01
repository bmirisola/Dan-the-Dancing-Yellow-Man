package zabbandco.danthedancingyellowman;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import util.Constants;


// <string name="banner_ad_unit_id">ca-app-pub-3940256099942544/6300978111</string> - test
// <string name="banner_ad_unit_id">ca-app-pub-9744674999881322/4453831094</string> - real bottom

public class MainActivity extends Activity {
    private Button danceButton;
    private ImageView dancer;
    private TextView danPointsText;
    private int danPoints;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        danceButton = findViewById(R.id.dance_button);
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


        Log.d("Oncreate", "On create counter = " + Integer.toString(Dancer.counter) );

        danceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dancer.changeDan(dancer);
                danPoints++;
                danPointsText.setText(String.format("Your Dan Points: %d", danPoints));
                Log.d("Button Press", Integer.toString(Dancer.counter));
            }
        });
    }

    protected void onStart() {
        super.onStart();

        Log.d("Onstart", "On Start counter = " + Integer.toString(Dancer.counter) );

    }

    protected void onResume() {
        super.onResume();
        Log.d("OnResume", "On Resume counter = " + Integer.toString(Dancer.counter) );

    }

    protected void onPause() {
        super.onPause();
        editor.putInt(Constants.DAN_POINTS_KEY, danPoints);
        editor.commit();
        Log.d("OnPause", "On Pause counter = " + Integer.toString(Dancer.counter) );

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

    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.DANCE_COUNTER_ID, Dancer.counter);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        Dancer.counter = savedInstanceState.getInt(Constants.DANCE_COUNTER_ID);
    }
}
