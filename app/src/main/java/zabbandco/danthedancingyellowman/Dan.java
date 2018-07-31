package zabbandco.danthedancingyellowman;

import android.content.res.TypedArray;
import android.widget.ImageView;

import util.Constants;

public class Dan {
    static int counter = 0;
    public static void changeDan(ImageView dancer, TypedArray imgs){
        dancer.setImageResource(imgs.getResourceId(counter++, Constants.defaultImageValue));
        if(counter == 4){
            counter = 0;
        }

    }
}
