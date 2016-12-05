package edu.asu.msrs.artcelerationlibrary;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by yitaochan on 11/30/16.The  Sobel  Edge  filter  transforms  the  input  image  first
 * into  a  grayscale  brightnessimage.  Then use different edge filter to high light edges in the image.
 * By applyingdifferent edge filters, we can obtain the gradients in horizontal and vertical direction.
 * Once the pixel values are set, we can find the result
 */

public class SobelEdge {


    int w ;
    int h ;
    int[][] redcontainer;
    int[][] greencontainer;
    int[][] bluecontainer;
    int[][] gray;
    int[][] sx;
    int[][] grx;
    int[][] gry;
    int[][] gr;
    int[][] sy;
    String TAG = "SobelEdge";

    //Function: main method in this class which calls all the other methods to implement the sobel edge transform
    //Input: bitmap object
    //Output: bitmap object
    public Bitmap sEdge (Bitmap bmp, int[] args1){

        h = bmp.getHeight();
        w = bmp.getWidth();
        redcontainer = new int[w][h];
        greencontainer = new int[w][h];
        bluecontainer = new int[w][h];
        gray = new int[w][h];
        sx = new int[][]{{-1,0,1},{-2,0,2},{-1,0,1}};
        sy = new int[][]{{-1,-2,-1},{0,0,0},{1,2,1}};

        grx = new int[w][h];
        gry = new int[w][h];
        gr = new int[w][h];

        Log.d(TAG, "Starts");
        getColorValue(bmp);
        grayScale();
        setGrayScale(bmp);
        gradient(bmp, args1[0]);

        Log.d(TAG, "Ends");

        return bmp;
    }

    //Function: extract color values from the bitmap
    //Input: bitmap object
    //Output: bitmap object

    public void getColorValue(Bitmap bmp){
        for ( int x = 0; x < w; x++){
            for ( int y = 0; y < h; y++){
                redcontainer [x][y] = Color.red(bmp.getPixel(x, y));
                greencontainer [x][y] = Color.green(bmp.getPixel(x, y));
                bluecontainer [x][y] = Color.blue(bmp.getPixel(x, y));

            }
        }
    }

    //Function: convert the color array into grayscale values
    //Input: void
    //Output: null
    public void grayScale(){

        for (int x = 0; x < w; x++){
            for (int y = 0; y < h; y++){
                redcontainer[x][y] = (int)(0.2989*redcontainer[x][y]);
                greencontainer[x][y] = (int)(0.5870*greencontainer[x][y]);
                bluecontainer[x][y] = (int)(0.1140*bluecontainer[x][y]);
                gray[x][y] = redcontainer[x][y] + greencontainer[x][y] + bluecontainer[x][y];

            }
        }

    }


    //Function: sets the grayscale values into the image bitmap object
    //Input: bitmap object
    //Output: null

    public void setGrayScale(Bitmap bmp){
        for (int x = 0; x < w; x++){
            for (int y = 0; y < h; y++){
               // bmp.setPixel(x, y, Color.argb(255, redcontainer[x][y], greencontainer[x][y], bluecontainer[x][y]));
                bmp.setPixel(x, y, Color.argb(255, gray[x][y], gray[x][y], gray[x][y]));

            }

        }

    }

    //Function: sets the grx values into the image bitmap object
    //Input: bitmap object, input args (0: Sx, 1: Sy)
    //Output: null


    public void gradient(Bitmap bmp, int args){
        switch (args){
            case 0:
                getGrx();
                for (int x = 1; x < w-1; x ++){
                    for (int y = 1; y < h-1; y++){

                        bmp.setPixel(x, y, Color.argb(255, grx[x][y], grx[x][y], grx[x][y]));
//
                    }
                }

                break;
            case 1:
                getGry();
                for (int x = 1; x < w-1; x ++){
                    for (int y = 1; y < h-1; y++){

                        bmp.setPixel(x, y, Color.argb(255, gry[x][y], gry[x][y], gry[x][y]));
                    }
                }
                break;
            case 2:
                getGrx();
                getGry();
                for (int x = 1; x < w-1; x ++) {
                    for (int y = 1; y < h - 1; y++) {

                        bmp.setPixel(x, y, Color.argb(255, (int)Math.sqrt(grx[x][y]*grx[x][y]),
                                (int)Math.sqrt(grx[x][y]*grx[x][y]),
                                (int)Math.sqrt(grx[x][y]*grx[x][y])));

                    }

                }

                break;
            default:
                break;
        }


    }

    //Function: finds the grx values
    //Input: void
    //Output: null

    public void getGrx(){
        for (int x = 1; x < w-1; x ++){
            for (int y = 1; y < h-1; y++){
                grx[x][y] = (-1)*gray[x-1][y-1] + (0)*gray[x][y-1] + (1)*gray[x+1][y-1]+
                        (-2)*gray[x-1][y] + (0)*gray[x][y] + (2)*gray[x+1][y]+
                        (-1)*gray[x-1][y+1] + (0)*gray[x][y+1]+ (1)*gray[x+1][y+1];
                if(grx[x][y] < 0) {
                    grx[x][y] = 0;
                } else {

                }
            }
        }


    }

    //Function: finds the gry values
    //Input: void
    //Output: null

    public void getGry(){

        for (int x = 1; x < w-1; x ++){
            for (int y = 1; y < h-1; y++){
                        grx[x][y] = (-1)*gray[x-1][y-1] + (-2)*gray[x][y-1] + (1)*gray[x+1][y-1]+
                                (0)*gray[x-1][y] + (0)*gray[x][y] + (0)*gray[x+1][y]+
                                (1)*gray[x-1][y+1] + (2)*gray[x][y+1]+ (1)*gray[x+1][y+1];
                        if(grx[x][y] < 0) {
                            grx[x][y] = 0;
                        } else {

                        }
            }
        }




    }
 }

