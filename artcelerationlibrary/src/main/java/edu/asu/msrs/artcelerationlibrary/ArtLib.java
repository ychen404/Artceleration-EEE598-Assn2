package edu.asu.msrs.artcelerationlibrary;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.os.MemoryFile;
import android.os.Message;
import android.os.Messenger;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;

/**
 * Created by rlikamwa on 10/2/2016.
 */

public class ArtLib {
    private TransformHandler artlistener;
    private Activity mActivity;

    public ArtLib(Activity activity) {
        mActivity = activity;
        init();
    }



    private Messenger mMessenger;
    private boolean mBound;
    ServiceConnection mServiceConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mMessenger = new Messenger(service);
            mBound = true;
            Log.v("test","vonnected");

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mMessenger = null;
            mBound = false;
        }
    };
    public void init(){
        mActivity.bindService (new Intent(mActivity, ArtTransformService.class),mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public String[] getTransformsArray(){
        String[] transforms = {"Gaussian Blur", "Neon edges", "Color Filter"};
        return transforms;
    }

    public TransformTest[] getTestsArray(){
        TransformTest[] transforms = new TransformTest[3];
        transforms[0]=new TransformTest(0, new int[]{1,2,3}, new float[]{0.1f, 0.2f, 0.3f});
        transforms[1]=new TransformTest(1, new int[]{11,22,33}, new float[]{0.3f, 0.2f, 0.3f});
        transforms[2]=new TransformTest(2, new int[]{51,42,33}, new float[]{0.5f, 0.6f, 0.3f});

        return transforms;
    }

    public void registerHandler(TransformHandler artlistener){this.artlistener=artlistener;}

    public boolean requestTransform(Bitmap img, int index, int[] intArgs, float[] floatArgs){

        byte[] bytes = "testString".getBytes();
        try {
            MemoryFile memFile = new MemoryFile("somename", bytes.length);
            memFile.allowPurging(true); //
            memFile.writeBytes(bytes, 0, 0, bytes.length);

            ParcelFileDescriptor pfd = MemoryFileUtil.getParcelFileDescriptor(memFile);

            int what = ArtTransformService.MSG_MULT;
            Bundle dataBundle = new Bundle();
            dataBundle.putParcelable("pfd", pfd);

            Message msg = Message.obtain(null,what,2,3);
            msg.setData(dataBundle);

        try {
            if(mMessenger == null)
                Log.v("test","null");
            mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

}