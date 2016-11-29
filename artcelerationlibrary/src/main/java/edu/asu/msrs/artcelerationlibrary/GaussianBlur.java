package edu.asu.msrs.artcelerationlibrary;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by yitaochan on 11/27/16.
 */

public class GaussianBlur {

    //ArtTransformService mArt = new ArtTransformService();

    String TAG = "GaussianBlur";


    public Bitmap gblur(Bitmap bmp) {
        int r = 3;
        float t = 0.2f;
        int redValue;
        int blueValue;
        int greenValue;
        Log.d(TAG, "GaussianBlur Starts");
        //Log.d(TAG, "The red value is "+ String.valueOf(redValue));


        for (int x = 0; x < bmp.getWidth(); x++){
            for(int y = 0; y < bmp.getHeight(); y++){

                float temp = gKernel(0,t);
                redValue = (int) (temp * Color.red(bmp.getPixel(x, y)));
                blueValue = (int) (temp * Color.red(bmp.getPixel(x, y)));
                greenValue = (int) (temp * Color.red(bmp.getPixel(x, y)));

                    for (int k = 1; k <=r; k++){
                        redValue =+ (int)(gKernel(-r,t)*Color.red(bmp.getPixel(x-r,y))+gKernel(r,t)*Color.red(bmp.getPixel(x+r,y)));
                        blueValue =+ (int)(gKernel(-r,t)*Color.blue(bmp.getPixel(x-r,y))+gKernel(r,t)*Color.blue(bmp.getPixel(x+r,y)));
                        greenValue =+ (int)(gKernel(-r,t)*Color.green(bmp.getPixel(x-r,y))+gKernel(r,t)*Color.green(bmp.getPixel(x+r,y)));
                    }


                bmp.setPixel(x, y, Color.argb(255, redValue, greenValue, blueValue));
            }
        }

        for (int x = 0; x < bmp.getWidth(); x++){
            for(int y = 0; y < bmp.getHeight(); y++){

                float temp = gKernel(0,t);
                redValue = (int) (temp * Color.red(bmp.getPixel(x, y)));
                blueValue = (int) (temp * Color.red(bmp.getPixel(x, y)));
                greenValue = (int) (temp * Color.red(bmp.getPixel(x, y)));

                for (int k = 1; k <=r; k++){
                    redValue =+ (int)(gKernel(-r,t)*Color.red(bmp.getPixel(x,y-r))+gKernel(r,t)*Color.red(bmp.getPixel(x,y+r)));
                    blueValue =+ (int)(gKernel(-r,t)*Color.blue(bmp.getPixel(x,y-r))+gKernel(r,t)*Color.blue(bmp.getPixel(x,y+r)));
                    greenValue =+ (int)(gKernel(-r,t)*Color.green(bmp.getPixel(x,y-r))+gKernel(r,t)*Color.green(bmp.getPixel(x,y+r)));
                }


                bmp.setPixel(x, y, Color.argb(255, redValue, greenValue, blueValue));
            }
        }



        // blueValue = Color.blue(bmp.getPixel(x, y));
        // greenValue = Color.green(bmp.getPixel(x, y));


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
