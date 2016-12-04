package edu.asu.msrs.artcelerationlibrary;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * Created by yitaochan on 12/4/16.
 */

public class MotionBlur {

    String TAG = "MotionBlur";
    Bitmap bmp_orig;



    public byte[] motionBlur(byte[] bytes, int w, int h, int[] args){

        Log.d(TAG,"Start");
        int r = args[1];
        int redValue;
        int blueValue;
        int greenValue;
        bmp_orig = byteToBmp(bytes,w,h);

        switch (args[0]){

            case 0:
                for(int x = r; x< w - r; x++) {
                    for (int y = r; y < h - r; y++) {
                        redValue = Color.red(bmp_orig.getPixel(x, y));
                        blueValue = Color.blue(bmp_orig.getPixel(x, y));
                        greenValue = Color.green(bmp_orig.getPixel(x, y));
                        for (int k = 1; k <= r; k++) {
                            redValue += ( Color.red(bmp_orig.getPixel((x - k), y)) + Color.red(bmp_orig.getPixel((x + k), y)));
                            blueValue += ( Color.blue(bmp_orig.getPixel((x - k), y)) + Color.blue(bmp_orig.getPixel((x + k), y)));
                            greenValue += ( Color.green(bmp_orig.getPixel((x - k), y)) + Color.green(bmp_orig.getPixel((x + k), y)));
                        }
                        redValue = redValue / (2 * r + 1);
                        blueValue = blueValue / (2 * r + 1);
                        greenValue = greenValue / (2 * r + 1);
                        bmp_orig.setPixel(x, y, Color.argb(255, redValue, greenValue, blueValue));

                    }
                }

                break;
            case 1:
                for(int x = r; x< w - r; x++) {
                    for (int y = r; y < h - r; y++) {
                        redValue = Color.red(bmp_orig.getPixel(x, y));
                        blueValue = Color.blue(bmp_orig.getPixel(x, y));
                        greenValue = Color.green(bmp_orig.getPixel(x, y));
                        for (int k = 1; k <= r; k++) {
                            redValue += ( Color.red(bmp_orig.getPixel(x, (y - k))) + Color.red(bmp_orig.getPixel(x, (y + k))));
                            blueValue += ( Color.blue(bmp_orig.getPixel(x, (y - k))) + Color.blue(bmp_orig.getPixel(x, (y + k))));
                            greenValue += ( Color.green(bmp_orig.getPixel(x, (y - k))) + Color.green(bmp_orig.getPixel(x, (y + k))));
                        }
                        redValue = redValue / (2 * r + 1);
                        blueValue = blueValue / (2 * r + 1);
                        greenValue = greenValue / (2 * r + 1);
                        bmp_orig.setPixel(x, y, Color.argb(255, redValue, greenValue, blueValue));

                    }
                }
                break;
                default:
                    break;

        }


        Log.d(TAG,"END");

        return bmpToByte(bmp_orig);

    }


    public byte[] bmpToByte(Bitmap bitmap){

        ByteBuffer buffer = ByteBuffer.allocateDirect(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(buffer);

        byte[] bytes = buffer.array();

        return bytes;
    }

    public Bitmap byteToBmp (byte[] b, int w, int h){


        Buffer buf = null;
        buf = ByteBuffer.wrap(b);
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(w, h, conf);

        bmp.copyPixelsFromBuffer(buf);

        return bmp;
    }

}
