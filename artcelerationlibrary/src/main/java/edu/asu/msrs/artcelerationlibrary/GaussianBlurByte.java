package edu.asu.msrs.artcelerationlibrary;

import android.util.Log;

/**
 * Created by yitaochan on 12/1/16.
 */

public class GaussianBlurByte {

//    int r = 3;
    //float sigma = 3f;
    int height = 1066;
    int width = 1600;
//    int[][] red = new int[height][width];
//    int[][] green = new int[height][width];
//    int[][] blue = new int[height][width];
    //byte[] processedbyte = new byte[height*width*pixelLength];

 static public byte[] gBlurByte(byte[] b, int w, int h) {
        String TAG = "GaussianBlurByte";
        Log.d(TAG, "GaussianBlur Starts");
        float sigma = 5f;
        int r = 20;
//        int[][] red = new int[h][w];
//        int[][] green = new int[h][w];
//        int[][] blue = new int[h][w];

     float[][] red = new float[h][w];
     float[][] green = new float[h][w];
     float[][] blue = new float[h][w];

        convertToInt(b,w,h,red,green,blue);

        processOne(red,w,h,sigma,r);
        processTwo(red,w,h,sigma,r);

        processOne(green,w,h,sigma,r);
        processTwo(green,w,h,sigma,r);

        processOne(blue,w,h,sigma,r);
        processTwo(blue,w,h,sigma,r);


        for (int pixel = 0, row = 0, col = 0; pixel < h*w*4; pixel += 4) {

            b[pixel + 0] = (byte)(red[row][col]);
            b[pixel + 1] = (byte)((green[row][col]));
            b[pixel + 2] = (byte)((blue[row][col]));
            b[pixel + 3] = (byte)255;
            col++;
            if (col == w) {
                col = 0;
                row++;
            }
        }

        Log.d(TAG,"Ends");

        return b;
    }

    static public void convertToInt(byte[] b, int w, int h, float[][] red, float[][] green, float[][] blue) {
  // static public void convertToInt(byte[] b, int w, int h, int[][] red, int[][] green, int[][] blue) {
        for (int pixel = 0, row = 0, col = 0; pixel < h*w*4; pixel += 4) {
            //argb += ((int) b[pixel] & 0xff); // alpha
            red[row][col] = (b[pixel + 0] & 0xff);
            green[row][col] = (b[pixel + 1] & 0xff);
            blue[row][col] = (b[pixel + 2] & 0xff);
            col++;
            if (col == w) {
                col = 0;
                row++;
            }
        }
    }


    static public void processOne(float[][] color, int w, int h, float sigma, int r) {
       // float temp;
        for (int row = 0; row < h; row++ ){
            for (int col = 0; col < w; col++){
 //               color[row][col] = (int)(color[row][col]*gKernel(0,sigma)/unify(r,sigma));
                color[row][col] = (color[row][col]*gKernel(0,sigma));

                //       temp = color[row][col]*(gKernel(0,sigma)/unify(r,sigma));
                for (int k = 1; k<=r; k++){
//                    if(row+k<h){
//                        color[row][col] += (color[row+k][col]*(gKernel(k,sigma)));
//
//                    }
//                    if(row>=h){
//                        color[row][col] += (color[row-k][col]*(gKernel(-k,sigma)));
//
//                    }

                    if((row < k) || ( row + k >= h)){
                        color[row][col] += 0;
                    } else{
                        color[row][col] += (color[row+k][col]*(gKernel(k,sigma)));
                        color[row][col] += (color[row-k][col]*(gKernel(-k,sigma)));

//                        temp += (temp*(gKernel(k,sigma)/unify(r,sigma)));
//                        temp += (temp*(gKernel(-k,sigma)/unify(r,sigma)));
//                        color[row][col] = (int)temp;
//                        temp = 0;
                    }

                }

            }
        }

    }



    static public void processTwo(float[][] color, int w, int h, float sigma,int r) {
    //    float temp ;
        for (int row = 0; row < h; row++ ){
            for (int col = 0; col < w; col++){
               color[row][col] = color[row][col]*(gKernel(0,sigma));
//                temp = color[row][col]*(gKernel(0,sigma));

                for (int k = 1; k<=r; k++){

                    if((col < k) || ( col + k >= w)){
                        color[row][col] += 0;
                    } else{
                        color[row][col] += (color[row][col+k]*(gKernel(k,sigma)));
                        color[row][col] += (color[row][col-k]*(gKernel(-k,sigma)));
//                        temp += (color[row][col]*(gKernel(k,sigma)));
//                        temp += (color[row][col]*(gKernel(-k,sigma)));
//                        color[row][col] = (int)temp;
//                        temp = 0;
                    }

                }

            }
        }

    }


   static public float gKernel(int k, float t){

        float g;
        g = (float)Math.exp(-(k*k)/(2*(t*t)));
        g = g*(float)1/(float)Math.sqrt(2*Math.PI*t*t);

        return g;
    }

//    public float unify(int r, float sigma){
//        float sum = gKernel(0,sigma);
//        for(int i = 1; i < 2*r+1; i++){
//            sum += gKernel(-r,sigma) + gKernel(r,sigma);
//        }
//
//        return sum;
//    }
}