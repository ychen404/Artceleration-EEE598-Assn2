package edu.asu.msrs.artcelerationlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Region;
import android.util.Log;

import java.nio.ByteBuffer;

/**
 * Created by tangmiao on 11/27/2016.
 */
public class AsciiArt{



    public byte[] bmpToByte(Bitmap bitmap){

        ByteBuffer buffer = ByteBuffer.allocateDirect(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(buffer);

        byte[] bytes = buffer.array();

        return bytes;
    }
    Context mContext;

    public AsciiArt(Context context) {

        mContext = context;
    }



    public byte[] ascii(byte[] pixels){

        Bitmap imgbmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.char0);
        byte[] asciipixel = bmpToByte(imgbmp);
        //int pixelswidth = imgbmp.getWidth();
        ReqArgs mReq = new ReqArgs();
        int pixelswidth = 1600;
        Log.d("AsciiArt","the width obtained"+ String.valueOf(pixelswidth));
        for (int j = 0; j< 15; j++){

            for (int i =0;i< 8*4;i++ ){

                int indexpixel = j * pixelswidth * 4 + i;
                int indexascii = j * 32 + i;
                pixels[indexpixel] = asciipixel[indexascii];

            }
        }



    return pixels;
    }

}
