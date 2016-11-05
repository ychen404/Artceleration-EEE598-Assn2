package edu.asu.msrs.artcelerationlibrary;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.FileInputStream;

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
                    Log.d(TAG,String.valueOf(fios) );
                    int result = msg.arg1 * msg.arg2;
                    Log.d(TAG, "MULT " + result);
                    break;
                default:
                    break;
            }


        }

    }

    final Messenger mMessenger = new Messenger(new ArtTransformHandler());
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       // throw new UnsupportedOperationException("Not yet implemented");
        return mMessenger.getBinder();
    }
}
