package zabbandco.danthedancingyellowman;

import android.content.res.TypedArray;
import android.widget.ImageView;

import util.Constants;

public class Dancer {
    static int counter = 0;

    //Cycles through pictures of Dan
    public static void changeDan(ImageView dancer){
        counter++;
        if(counter % Constants.DAN_CYCLE_DIVISOR == 1){
            dancer.setImageResource(R.drawable.danthemandancingleft);
        }
        else if (counter % Constants.DAN_CYCLE_DIVISOR == 3){
            dancer.setImageResource(R.drawable.danthemandancingright);
        }
        else{
            dancer.setImageResource(R.drawable.danthemanstanding);
        }

    }
}
