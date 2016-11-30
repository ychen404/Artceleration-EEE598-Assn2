package edu.asu.msrs.artcelerationlibrary;

import android.content.Loader;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by yitaochan on 11/29/16.
 */

public class TiltShift {
    int a0 = 200;
    int a1 = 400;
    int a2 = 600;
    int a3 = 800;
    String TAG = "Tilt-Shift";

    int[] arg1 = {3};
    float[] arg2 = {1f};
    public Bitmap tShift (Bitmap bmp){

        Log.d(TAG,"Starts");

        GaussianBlur mgb = new GaussianBlur();

        for (int x = 0; x < 100;x++)
            for (int y = a0; y < a1; y++ ){
                arg2[0] = arg2[0]*factor(y);
                mgb.gblur(bmp, arg1,arg2);
            }

        Log.d(TAG,"Ends");
        return bmp;
    }

    public int factor (int y){

        return (a1-y)/(a1-a0);

    }







}
