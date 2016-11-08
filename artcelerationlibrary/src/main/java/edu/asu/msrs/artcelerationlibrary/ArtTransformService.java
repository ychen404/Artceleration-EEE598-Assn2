package edu.asu.msrs.artcelerationlibrary;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.MemoryFile;
import android.os.Message;
import android.os.Messenger;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ArtTransformService extends Service {

    @Override
    public void onCreate(){
        super.onCreate();
        Log.v("test","create");
    }

    public ArtTransformService() {
    }
    String TAG = "ArtTransformService";
    static final int MSG_HELLO = 1;
    static final int MSG_MULT = 2;
  //  private Messenger mClients;


    //
  //  ArrayList<Messenger> mClients = new ArrayList<Messenger>();


    class ArtTransformHandler extends Handler{
        @Override

        public void handleMessage(Message msg){
            Log.d(TAG, "handleMessage(msg)"+ msg.what);
            switch (msg.what) {
                case MSG_HELLO:
                    Log.d(TAG, "HELLO");
                    break;
                case MSG_MULT:
                    Bundle dataBundle = msg.getData();
                    ParcelFileDescriptor pfd = (ParcelFileDescriptor) dataBundle.get("pfd");
                    FileInputStream fios = new FileInputStream(pfd.getFileDescriptor());
                    String testPfd = null;

                    try {
                        testPfd = String.valueOf(fios.read());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                        Log.d(TAG,testPfd );
             //           int result = msg.arg1 * msg.arg2;
             //           Log.d(TAG, "MULT " + result);

                    break;
                default:
                    break;
            }

        }

    }


    class ServiceToClient extends Handler{
        @Override

        public void handleMessage(Message msg){
    //        Log.d(TAG, "ToServicehandleMessage(msg)"+ msg.what);


                    byte[] ser = "Service_return".getBytes();
                    MemoryFile memFile_1;
                    try {
                        memFile_1 = new MemoryFile("service", ser.length);
                        memFile_1.allowPurging(true); //
                        memFile_1.writeBytes(ser, 0, 0, ser.length);
                        ParcelFileDescriptor pfd_from_ser = null;
                        pfd_from_ser = MemoryFileUtil.getParcelFileDescriptor(memFile_1);
                        Bundle toClient = new Bundle();
                        toClient.putParcelable("pfd_from_ser", pfd_from_ser);
                        msg = Message.obtain(null,1);
                        msg.setData(toClient);
                        memFile_1.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }

        }


    final Messenger mMessenger = new Messenger(new ArtTransformHandler());
    final Messenger mClients = new Messenger(new ServiceToClient());
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       // throw new UnsupportedOperationException("Not yet implemented");
        return mMessenger.getBinder();
    }
}
