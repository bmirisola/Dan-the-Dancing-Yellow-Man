package zabbandco.danthedancingyellowman;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import util.Constants;


// <string name="banner_ad_unit_id">ca-app-pub-3940256099942544/6300978111</string> - test
// <string name="banner_ad_unit_id">ca-app-pub-9744674999881322/4453831094</string> - real bottom

public class MainActivity extends Activity {
    private Button danceButton;
    private TypedArray danImgs;
    ImageView dancer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        danceButton = findViewById(R.id.dance_button);
        danImgs = getResources().obtainTypedArray(R.array.dan_imgs);
        dancer = findViewById(R.id.dan);

        danceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dan.changeDan(dancer, danImgs);
            }
        });
    }

    protected void onStart() {
        super.onStart();
        if(Dan.counter != 0){
            dancer.setImageResource(danImgs.getResourceId(Dan.counter, Constants.defaultImageValue));
        }
    }

    protected void onResume() {
        super.onResume();
        if(Dan.counter != 0){
            dancer.setImageResource(danImgs.getResourceId(Dan.counter, Constants.defaultImageValue));
        }    }

    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("Dance Counter", Dan.counter);

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
    }
}
