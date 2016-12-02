package edu.asu.msrs.artcelerationlibrary;

import android.util.Log;

/**
 * Created by yitaochan on 12/1/16.
 */

public class GaussianBlurByte {

    String TAG = "GaussianBlurByte";
    int r = 5;
    float sigma = 1f;
    int height = 1066;
    int width = 1600;
    int[][] result = new int[height][width];
    int[][] red = new int[height][width];
    int[][] green = new int[height][width];
    int[][] blue = new int[height][width];

    final int pixelLength = 4;
    //byte[] processedbyte = new byte[height*width*pixelLength];

    public byte[] gBlurByte(byte[] b) {
        Log.d(TAG, "GaussianBlur Starts");

//        convertToInt(b);
        Log.d(TAG, "The b0 is " + String.valueOf(b[0]));
        Log.d(TAG, "The b1 is " + String.valueOf(b[1]));
        Log.d(TAG, "The b2 is " + String.valueOf(b[2]));
        Log.d(TAG, "The b3 is " + String.valueOf(b[3]));

        Log.d(TAG, "The b4 is " + String.valueOf(b[4]));
        Log.d(TAG, "The b5 is " + String.valueOf(b[5]));
        Log.d(TAG, "The b6 is " + String.valueOf(b[6]));
        Log.d(TAG, "The b7 is " + String.valueOf(b[7]));


        Log.d(TAG, "The b4 is " + String.valueOf(b[4] * 0.5f));
        Log.d(TAG, "The b5 is " + String.valueOf(b[5] * 0.5f));
        Log.d(TAG, "The b6 is " + String.valueOf(b[6] * 0.5f));
        Log.d(TAG, "The b7 is " + String.valueOf(b[7] * 0.5f));

        Log.d(TAG, "The b4 is " + String.valueOf((byte) (b[4] * 0.1f)));
        Log.d(TAG, "The b5 is " + String.valueOf((byte) (b[5] * 0.1f)));
        Log.d(TAG, "The b6 is " + String.valueOf((byte) (b[6] * 0.1f)));
        Log.d(TAG, "The b7 is " + String.valueOf((byte) (b[7] * 0.1f)));

        int num = 200; // 0000 0000 0000 0000 0000 0000 1100 1000 (200)
        byte test = (byte) 200; // 1100 1000 (-56 by Java specification, 200 by convention)

        //transform(b);
        Log.d(TAG, "The b4 after is " + String.valueOf(b[4]));
        Log.d(TAG, "The b5 after is " + String.valueOf(b[5]));
        Log.d(TAG, "The b6 after is " + String.valueOf(b[6]));
        Log.d(TAG, "The b7 after is " + String.valueOf(b[7]));
        convertToInt(b);
        processOne(red);
        processTwo(red);
////
        processOne(green);
        processTwo(green);
////
        processOne(blue);
        processTwo(blue);


        for (int pixel = 0, row = 0, col = 0; pixel < height*width*pixelLength; pixel += pixelLength) {

            b[pixel + 0] = (byte)(red[row][col]);
            b[pixel + 1] = (byte)((green[row][col]));
            b[pixel + 2] = (byte)((blue[row][col]));
            col++;
            if (col == width) {
                col = 0;
                row++;
            }
        }


        Log.d(TAG,"Ends");

        return b;
    }


    public void convertToInt(byte[] b) {
        for (int pixel = 0, row = 0, col = 0; pixel < height*width*pixelLength; pixel += pixelLength) {
            //argb += ((int) b[pixel] & 0xff); // alpha
            red[row][col] = ((int) b[pixel + 0] & 0xff);
            green[row][col] = ((int) b[pixel + 1] & 0xff);
            blue[row][col] = ((int) b[pixel + 2] & 0xff);
            col++;
            if (col == width) {
                col = 0;
                row++;
            }
        }
    }

//    public void processOne(int[][] color) {
//        for (int row = r; row < height; row++ ){
//            for (int col = r; col < width; col++){
//                color[row][col] = (int)(color[row][col]*gKernel(0,3f));
//                for (int k = 1; k<=r; k++){
//                    if ((row < k) || (row + k >= height )){
//                        color[row][col] = 0;
//                    }
//                    else {
////                        color[row][col] += (int) (color[row + k][col] * gKernel(k, 1f));
////                        color[row][col] += (int) (color[row - k][col] * gKernel(-k, 1f));
//                        color[row][col] +=  (color[row + k][col]);
////                        color[row][col] += (int) (color[row + k][col] * gKernel(k, 1f));
//
//                        //  color[row][col] += (int) (color[row - k][col] * gKernel(-k, 1f));
//                     //   Log.d(TAG, "The row and col is " + row + " " + col);
//                    }
//                }
//            }
//        }
//
//    }

//        public void processOne(int[][] color) {
//        for (int row = 0; row < height; row++ ){
//            for (int col = 0; col < width; col++){
//                color[row][col] = (int)(color[row][col]*0.1f);
//
//
//            }
//        }
//
//    }


    public void processOne(int[][] color) {
        for (int row = 0; row < height; row++ ){
            for (int col = 0; col < width; col++){
                color[row][col] = (int)(color[row][col]*gKernel(0,sigma));
                for (int k = 1; k<=r; k++){
                    if((row < k) || ( row + k >= height)){
                        color[row][col] = 0;
                    } else{
                        color[row][col] += (int)(color[row+k][col]*gKernel(k,sigma));
                        color[row][col] += (int)(color[row-k][col]*gKernel(-k,sigma));
                    }


                }

            }
        }

    }



    public void processTwo(int[][] color) {
        for (int row = 0; row < height; row++ ){
            for (int col = 0; col < width; col++){
                color[row][col] = (int)(color[row][col]*gKernel(0,sigma));
                for (int k = 1; k<=r; k++){
                    if((col < k) || ( col + k >= width)){
                        color[row][col] = 0;
                    } else{
                        color[row][col] += (int)(color[row][col+k]*gKernel(k,sigma));
                        color[row][col] += (int)(color[row][col-k]*gKernel(-k,sigma));
                    }


                }

            }
        }

    }


    public float gKernel(int k, float t){

        float g;
        g = (float)Math.exp(-(k*k)/(2*(t*t)));
        g = g*(float)1/(float)Math.sqrt(2*Math.PI*t*t);

        return g;
    }


}
