package edu.asu.msrs.artcelerationlibrary;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.MemoryFile;
import android.os.Message;
import android.os.Messenger;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.LinkedList;


public class ArtLib {
    private TransformHandler artlistener;
    private Activity mActivity;
    String TAG = "ArtLib";


    public ArtLib(Activity activity) {
        mActivity = activity;
        init();
    }

    static {
        System.loadLibrary("my-native-lib");
    }

//    static {
//        System.loadLibrary("hello-neon");
//    }
    // To test NEON
    //public native String StringFromJNI();
    //public native String stringFromJNI();


    private Messenger mMessenger = null;
    private Messenger mService;
    private boolean mBound;

    LinkedList<ReqArgs> mList = new LinkedList<ReqArgs>();
    ReqArgs reqContainer = new ReqArgs();

    ServiceConnection mServiceConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mMessenger = new Messenger(service);
            mBound = true;
            Log.v("test","Connected");


            Message msg  = Message.obtain(null, ArtTransformService.COLOR_FILTER);
            msg.replyTo = mReceive;


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
        String[] transforms = {"Color Filter", "Motion Blur", "ASCII Art", "Gaussian Blur", "Tilt Shift"};
        return transforms;
    }

    public TransformTest[] getTestsArray(){
        TransformTest[] transforms = new TransformTest[5];
        transforms[0]=new TransformTest(0, new int[]{1,2,3}, new float[]{0.1f, 0.2f, 0.3f});
        transforms[1]=new TransformTest(1, new int[]{11,22,33}, new float[]{0.3f, 0.2f, 0.3f});
        transforms[2]=new TransformTest(2, new int[]{51,42,33}, new float[]{0.5f, 0.6f, 0.3f});
        transforms[3]=new TransformTest(3, new int[]{51,42,33}, new float[]{0.5f, 0.6f, 0.3f});
        transforms[4]=new TransformTest(4, new int[]{51,42,33}, new float[]{0.5f, 0.6f, 0.3f});

        return transforms;
    }

    //Function: RegisterHandler to artLib
    //Input: transformHandler
    //Output: null

    public void registerHandler(TransformHandler artlistener){
        this.artlistener=artlistener;

    }

    class ProcessedImgHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG,"Processed img received: "+ msg.what);

            switch (msg.what){
//                case ArtTransformService.MSG_MULT:
//                    int result = msg.what;
//                    Log.d(TAG,"MULT: "+ result);
                case 10:
                    Bundle retBundle = msg.getData();
                    if (msg.getData() == null){
                        Log.d(TAG,"No data bundle");
                        return;
                    }
                    else {
                        ParcelFileDescriptor pfd_ret = (ParcelFileDescriptor) retBundle.get("pfd_ret");
                        FileInputStream fios_ret = new FileInputStream(pfd_ret.getFileDescriptor());

                        Bitmap procImg = toBitmap(readProcessed(fios_ret));
                        artlistener.onTransformProcessed(procImg);
                     //    int result_1 = msg.what;
                     //    Log.d("ArtLib","MULT: "+ result_1);
                    }
                    break;
                    default:
                        break;
            }
        }
    }

    final Messenger mReceive = new Messenger(new ProcessedImgHandler());

    // Function: requestTransform to the activity
    // Input: Bitmap image
    //  Output: Boolean result
    public boolean requestTransform(Bitmap img, int index, int[] intArgs, float[] floatArgs) {

        ReqArgs reqArgs = new ReqArgs();
        reqArgs.index = index;
        reqArgs.intArgs = intArgs;
        reqArgs.floatArgs = floatArgs;
        reqArgs.img = img;
        reqArgs.img_height = img.getHeight();
        reqArgs.img_width = img.getWidth();
        mList.add(reqArgs);



    //    Log.d(TAG, "The size is + " + String.valueOf(mList.size()));



            reqContainer = mList.pollFirst();
            ByteBuffer buffer = ByteBuffer.allocateDirect(reqContainer.img.getByteCount());
            reqContainer.img.copyPixelsToBuffer(buffer);

            byte[] bytes = buffer.array();

            try {
                MemoryFile memFile = new MemoryFile("somename", bytes.length);
                memFile.allowPurging(true); //
                memFile.writeBytes(bytes, 0, 0, bytes.length);

                ParcelFileDescriptor pfd = MemoryFileUtil.getParcelFileDescriptor(memFile);

               // int what = ArtTransformService.MSG_MULT;

                Bundle dataBundle = new Bundle();
                dataBundle.putParcelable("pfd", pfd);
                dataBundle.putInt("index", reqContainer.index);
                dataBundle.putInt("width", reqContainer.img_width );
                dataBundle.putInt("height", reqContainer.img_height );

                Log.d(TAG, "The index is + " + String.valueOf(reqContainer.index));
                int what = reqContainer.index;

                Message msg = Message.obtain(null, what);
                msg.replyTo = mReceive;
                msg.setData(dataBundle);
                memFile.close();


                try {
                    if (mMessenger == null)
                        Log.v("test", "null");
                    mMessenger.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }


    //Function: Convert input file stream from the service to buffer
    //Input: FileInputStream
    //Output: Buffer

    public Buffer readProcessed(FileInputStream input)
    {
        //  Log.d(TAG,StringFromJNI()); // Test cpp

          //Log.d(TAG,stringFromJNI()); // Test neon

        byte[] byteArray = null;
        Buffer buf = null;
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
            buf = ByteBuffer.wrap(byteArray);



        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
       // return byteArray;
        return buf;
    }

    //Function: Convert buffer into bitmap
    //Input: Buffer
    //Output: Bitmap object

    public Bitmap toBitmap(Buffer buf){

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(reqContainer.img_width, reqContainer.img_height, conf);

        bmp.copyPixelsFromBuffer(buf);
        return bmp;
    }

}

