package edu.asu.msrs.artcelerationlibrary;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by yitaochan on 11/27/16.
 */

public class GaussianBlur {

    String TAG = "GaussianBlur";


    public Bitmap gblur(Bitmap bmp, int[] r, float[] sigma) {
//        int r = 5;
//        float sigma = 3f;
        int redValue;
        int greenValue;
        int blueValue;
        float red_f, green_f, blue_f;
        Log.d(TAG, "GaussianBlur Starts");


        //Log.d(TAG, "The red value is "+ String.valueOf(redValue));

        float k1 = gKernel(1,3f);
        float k2 = gKernel(0,3f);
        float k3 = gKernel(-1,3f);
        float tot = 2*k1+2*k2+2*k3;
        float round = Math.round(tot);
        Log.d(TAG, "The k1 value is "+ String.valueOf(k1));
        Log.d(TAG, "The k2 value is "+ String.valueOf(k2));
        Log.d(TAG, "The k3 value is "+ String.valueOf(k3));
        Log.d(TAG, "The total value is "+ String.valueOf(tot));
        Log.d(TAG, "The total value is "+ String.valueOf((int)tot));
        Log.d(TAG, "The round value is "+ String.valueOf((int) round));


        // Reminder: need to round instead of just int to convert the gaussian kernel;



        for (int x = 300; x < 600; x++){
            for(int y = 300; y < 600; y++){
                float temp = gKernel(0,sigma[0]);
                //Log.d(TAG,"temp "+String.valueOf(temp));
                redValue = (int) (temp * Color.red(bmp.getPixel(x, y)));
                greenValue = (int) (temp * Color.green(bmp.getPixel(x, y)));
                blueValue = (int) (temp * Color.blue(bmp.getPixel(x, y)));
                //Log.d(TAG,"Red blue green "+String.valueOf(redValue)+String.valueOf(blueValue)+String.valueOf(greenValue));

                for (int k = 1; k <=r[0]; k++) {
                        redValue += (int)(gKernel(-k,sigma[0])*Color.red(bmp.getPixel(x-k,y))+gKernel(k,sigma[0])*Color.red(bmp.getPixel(x+k,y)));
                        greenValue += (int)(gKernel(-k,sigma[0])*Color.green(bmp.getPixel(x-k,y))+gKernel(k,sigma[0])*Color.green(bmp.getPixel(x+k,y)));
                        blueValue += (int)(gKernel(-k,sigma[0])*Color.blue(bmp.getPixel(x-k,y))+gKernel(k,sigma[0])*Color.blue(bmp.getPixel(x+k,y)));
                     //    Log.d(TAG,"RGB "+String.valueOf(redValue)+String.valueOf(greenValue)+String.valueOf(blueValue));
                    }
                bmp.setPixel(x, y, Color.argb(255, redValue, greenValue, blueValue));
             }



            }

        for (int x = 300; x < 600; x++){
            for(int y = 300; y < 600; y++){

                float temp = gKernel(0,sigma[0]);
                redValue = (int) (temp * Color.red(bmp.getPixel(x, y)));
                greenValue = (int) (temp * Color.green(bmp.getPixel(x, y)));
                blueValue = (int) (temp * Color.blue(bmp.getPixel(x, y)));

                for (int k = 1; k <=r[0]; k++){
                    redValue += (int)(gKernel(-k,sigma[0])*Color.red(bmp.getPixel(x,y-k))+gKernel(k,sigma[0])*Color.red(bmp.getPixel(x,y+k)));
                    greenValue += (int)(gKernel(-k,sigma[0])*Color.green(bmp.getPixel(x,y-k))+gKernel(k,sigma[0])*Color.green(bmp.getPixel(x,y+k)));
                    blueValue += (int)(gKernel(-k,sigma[0])*Color.blue(bmp.getPixel(x,y-k))+gKernel(k,sigma[0])*Color.blue(bmp.getPixel(x,y+k)));
                }


                bmp.setPixel(x, y, Color.argb(255, redValue, greenValue, blueValue));
            }
        }

        Log.d(TAG, "GaussianBlur Ends");

        return bmp;
    }

    public float gKernel(int k, float t){

        float g;
        g = (float)Math.exp(-(k*k)/(2*(t*t)));
        g = g*(float)1/(float)Math.sqrt(2*Math.PI*t*t);

        return g;
    }


}
