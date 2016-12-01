package edu.asu.msrs.artcelerationlibrary;

import android.util.Log;

/**
 * Created by tangmiao on 11/27/2016.
 */
public class ColorFilter {
    public byte[] piecewiseprocess(byte[] pixels){
        String TAG = "ColorFilter";
        Log.d(TAG,"Start");

//        int [] piecewiseArray = new int[]{1, 2, 100, 155, 200, 210, 255, 255,
//                1, 5, 101, 130, 201, 240, 254, 254,
//                2, 2, 102, 102, 202, 202, 253, 253};

        int [] piecewiseArray = new int[]{5, 26, 30, 80, 100, 150, 170,230,
                1, 68, 30, 10, 150, 150, 200,
                30,100, 130, 130, 80, 200, 250, 240, 5};

        for (int i = 0; i < pixels.length/4; i++) {
            pixels[4*i+1] = ArrayOperater(pixels[4*i+1],0, piecewiseArray);
            pixels[4*i+2] = ArrayOperater(pixels[4*i+2],8, piecewiseArray);
            pixels[4*i+3] = ArrayOperater(pixels[4*i+3],16, piecewiseArray);
        }
        Log.d(TAG,"End");
        return pixels;


    }

    public byte ArrayOperater(byte pixel1,int colorshift, int[] piecewiseArray) {

        int pixel = pixel1 & 0xFF;

        if (pixel< 0) {
            pixel = 0;
        }else if (pixel >= 0 || pixel < piecewiseArray[0+colorshift]) {
            pixel = (pixel)*(piecewiseArray[1+colorshift])/(piecewiseArray[0+colorshift]);
        }else if (pixel >= piecewiseArray[0+colorshift] || pixel < piecewiseArray[2+colorshift]) {
            pixel= piecewiseArray[0+colorshift]+(pixel-piecewiseArray[0+colorshift])*((piecewiseArray[3+colorshift]-piecewiseArray[1+colorshift])/(piecewiseArray[2+colorshift]-piecewiseArray[0+colorshift]));
        }else if (pixel >= piecewiseArray[2+colorshift] || pixel < piecewiseArray[4+colorshift]) {
            pixel = (piecewiseArray[2+colorshift]+(pixel-piecewiseArray[2+colorshift])*((piecewiseArray[5+colorshift]-piecewiseArray[3+colorshift])/(piecewiseArray[4+colorshift]-piecewiseArray[2+colorshift])));
        }else if (pixel >= piecewiseArray[4+colorshift]|| pixel < piecewiseArray[6+colorshift]) {
            pixel = (piecewiseArray[4+colorshift]+(pixel-piecewiseArray[4+colorshift])*((piecewiseArray[7+colorshift]-piecewiseArray[5+colorshift])/(piecewiseArray[6+colorshift]-piecewiseArray[4+colorshift])));
        }else if (pixel >= piecewiseArray[6+colorshift]|| pixel < 255){
            pixel = (piecewiseArray[6+colorshift]+ (pixel - piecewiseArray[6+colorshift])* (255 - piecewiseArray[7+colorshift])/(255 - piecewiseArray[6+colorshift]));
        } else {
            pixel = 255;
        }


        return  (byte)pixel;
    }


}
