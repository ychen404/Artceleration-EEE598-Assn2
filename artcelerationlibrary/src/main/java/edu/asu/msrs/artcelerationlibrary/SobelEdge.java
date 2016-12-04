package edu.asu.msrs.artcelerationlibrary;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by yitaochan on 11/30/16.
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
    int[][] sy;
    String TAG = "SobelEdge";

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
//        gry = new int[w][h];
//        gr = new int[w][h];

        Log.d(TAG, "Starts");
        getColorValue(bmp);
        grayScale();
        setGrayScale(bmp);
        gradient(bmp, args1[0]);

        Log.d(TAG, "Ends");

        return bmp;
    }

    public void getColorValue(Bitmap bmp){
        for ( int x = 0; x < w; x++){
            for ( int y = 0; y < h; y++){
                redcontainer [x][y] = Color.red(bmp.getPixel(x, y));
                greencontainer [x][y] = Color.green(bmp.getPixel(x, y));
                bluecontainer [x][y] = Color.blue(bmp.getPixel(x, y));
              //  Log.d(TAG, "RGB"+String.valueOf(redcontainer[x][y])+String.valueOf(greencontainer[x][y])+ String.valueOf(bluecontainer[x][y]));

            }
        }
    }

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

    public void setGrayScale(Bitmap bmp){
        for (int x = 0; x < w; x++){
            for (int y = 0; y < h; y++){
               // bmp.setPixel(x, y, Color.argb(255, redcontainer[x][y], greencontainer[x][y], bluecontainer[x][y]));
                bmp.setPixel(x, y, Color.argb(255, gray[x][y], gray[x][y], gray[x][y]));

            }

        }

    }


    public void gradient(Bitmap bmp, int args){
        switch (args){
            case 0:
                for (int x = 1; x < w-1; x ++){
                    for (int y = 1; y < h-1; y++){
                        grx[x][y] = (-1)*gray[x-1][y-1] + (0)*gray[x][y-1] + (1)*gray[x+1][y-1]+
                                (-2)*gray[x-1][y] + (0)*gray[x][y] + (2)*gray[x+1][y]+
                                (-1)*gray[x-1][y+1] + (0)*gray[x][y+1]+ (1)*gray[x+1][y+1];
                        if(grx[x][y] < 0) {
                            grx[x][y] = 0;
                        } else {

                        }
                        bmp.setPixel(x, y, Color.argb(255, grx[x][y], grx[x][y], grx[x][y]));

                    }
                }
                break;
            case 1:
                for (int x = 1; x < w-1; x ++){
                    for (int y = 1; y < h-1; y++){
                        grx[x][y] = (-1)*gray[x-1][y-1] + (-2)*gray[x][y-1] + (1)*gray[x+1][y-1]+
                                (0)*gray[x-1][y] + (0)*gray[x][y] + (0)*gray[x+1][y]+
                                (1)*gray[x-1][y+1] + (2)*gray[x][y+1]+ (1)*gray[x+1][y+1];
                        if(grx[x][y] < 0) {
                            grx[x][y] = 0;
                        } else {

                        }
                        bmp.setPixel(x, y, Color.argb(255, grx[x][y], grx[x][y], grx[x][y]));

                    }
                }
                break;
            case 2:

                break;
            default:
                break;
        }


    }





    }

