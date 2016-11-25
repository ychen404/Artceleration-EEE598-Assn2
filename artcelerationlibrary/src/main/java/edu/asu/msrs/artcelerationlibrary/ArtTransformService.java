package edu.asu.msrs.artcelerationlibrary;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.MemoryFile;
import android.os.Message;
import android.os.Messenger;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;


public class ArtTransformService extends Service {

    @Override
    public void onCreate(){
        super.onCreate();
        Log.v("test","create");
    }

    public ArtTransformService() {
    }
    String TAG = "ArtTransformService";
  //  static final int MSG_HELLO = 1;
  //  static final int MSG_MULT = 2;
    static final int COLOR_FILTER   = 0;
    static final int MOTION_BLUR    = 1;
    static final int ASCII_ART      = 2;
    static final int GAUSSIAN_BLUR  = 3;
    static final int TILT_SHIFT     = 4;


    private Messenger messenger_2;



    class ArtTransformHandler extends Handler{
        @Override

        // Function: handleMessage sent from ArtLib
        // Input: Message.
        //  Output: receive data from library
        public void handleMessage(Message msg){

            Log.d(TAG, "handleMessage(msg)"+ msg.what);
            Bundle dataBundle = msg.getData();
            ParcelFileDescriptor pfd = (ParcelFileDescriptor) dataBundle.get("pfd");
            FileInputStream fios = new FileInputStream(pfd.getFileDescriptor());
            int ind = dataBundle.getInt("index");
            Log.d(TAG, "The index is " + String.valueOf(ind));
            byte[] bytes = readFully(fios);
            byte[] processed_bytes = null;

            messenger_2 = msg.replyTo;
            switch (msg.what) {

                case COLOR_FILTER:
                    processed_bytes = colorFilter(bytes);
                    break;
                case MOTION_BLUR:
                    motionBlur(bytes);
                    break;
                case ASCII_ART:
                    asciiArt(bytes);
                    break;
                case GAUSSIAN_BLUR:
                    gaussianBlur(bytes);
                    break;
                case TILT_SHIFT:
                    tiltShift(bytes);
                    break;

                default:
                    break;
            }

            try {

                Log.d(TAG,"The byte array is " + String.valueOf(bytes));
                MemoryFile memFile_ret = null;
                memFile_ret = new MemoryFile("processed", bytes.length);
                memFile_ret.allowPurging(true); //
                memFile_ret.writeBytes(bytes, 0, 0, bytes.length);

                ParcelFileDescriptor pfd_ret = MemoryFileUtil.getParcelFileDescriptor(memFile_ret);
                Bundle processedBundle = new Bundle();
                processedBundle.putParcelable("pfd_ret", pfd_ret);

                try {

                    msg.setData(processedBundle);
                    msg.what = 10;
                    messenger_2.send(msg);
                    if(msg == null)
                        Log.d("msg is null", "null");



                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }



    }


    public byte[] readFully(FileInputStream input)
    {
        byte[] byteArray = null;
        try
        {
            //InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024*8];
            int bytesRead =0;

            while ((bytesRead = input.read(b)) != -1)
            {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        long futureTime = System.currentTimeMillis()+10000;
        while (System.currentTimeMillis() < futureTime){
            synchronized (this){
                try{
                    wait(futureTime-System.currentTimeMillis());
                } catch (Exception e){

                }
            }
        }

        return byteArray;
    }



    public byte[] colorFilter(byte[] b){

        for(int i = 0;i<(b.length-3)/4;i++){
            b[4*i+1] = 0;
            b[4*i+2] = 0;
        }

        return b;

    }

    public byte[] motionBlur(byte[] b){

        return b;

    }
    public byte[] asciiArt(byte[] b){

        return b;

    }
    public byte[] gaussianBlur(byte[] b){

        return b;

    }
    public byte[] tiltShift(byte[] b){

        return b;

    }





    final Messenger mMessenger = new Messenger(new ArtTransformHandler());
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mMessenger.getBinder();
    }
}
