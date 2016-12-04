#include <jni.h>
#include <string>
#include "native-lib.h"
#define _USE_MATH_DEFINES // for C++
#include <math.h>
#include <android/log.h>


#define  LOG_TAG    "DEBUG"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
extern "C"
//jstring
jbyteArray
//Java_edu_asu_msrs_artcelerationlibrary_ArtLib_StringFromJNI(
Java_edu_asu_msrs_artcelerationlibrary_ArtTransformService_ColorFilterFromJNI(
        JNIEnv *env,
        jobject /* this */,
        jbyteArray array) {
   // std::string hello = "ColorFilter From NDK";
  //  return env->NewStringUTF(hello.c_str());
    //jbyte* pixels = env->GetByteArrayElements(array,NULL); //pass byte array to pointer

    jbyte* pixels = env->GetByteArrayElements(array, 0); //pass byte array to pointer
    int length = env-> GetArrayLength(array);
    //JString TAG = "ColorFilter";
      //      Log.d(TAG,"Start");

            int piecewiseArray[] = {1, 2, 100, 155, 200, 210, 255, 255,
                              1, 5, 101, 130, 201, 240, 254, 254,
                              2, 2, 102, 102, 202, 202, 253, 253};



            for (int i = 0; i < length/4; i++) {
                pixels[4*i+1] = ArrayOperater(pixels[4*i+1],0, piecewiseArray);
                pixels[4*i+2] = ArrayOperater(pixels[4*i+2],8, piecewiseArray);
                pixels[4*i+3] = ArrayOperater(pixels[4*i+3],16, piecewiseArray);
            }
            //Log.d(TAG,"End");

            env->SetByteArrayRegion (array, 0, length, pixels); // c++ to java, return java
            env->ReleaseByteArrayElements(array, pixels, 0);
            return array;


}

jbyte ArrayOperater(jbyte pixel1,int colorshift, int* piecewiseArray) {

        int pixel = pixel1 & 0xFF;

        if (pixel< 0) {
            pixel = 0;
        }else if (pixel >= 0 || pixel < piecewiseArray[0+colorshift]) {
            pixel = (pixel)*(piecewiseArray[1+colorshift])/(piecewiseArray[0+colorshift]);
        }else if (pixel >= piecewiseArray[0+colorshift] || pixel < piecewiseArray[2+colorshift]) {
            pixel= piecewiseArray[0+colorshift]+(pixel-piecewiseArray[0+colorshift])*((piecewiseArray[3+colorshift]-piecewiseArray[1+colorshift])/(piecewiseArray[2+colorshift]-piecewiseArray[0+colorshift]));
        }else if (pixel >= piecewiseArray[2+colorshift] || pixel < piecewiseArray[4+colorshift]) {
            pixel = (piecewiseArray[2+colorshift]+(pixel-piecewiseArray[2+colorshift])*((piecewiseArray[5+colorshift]-piecewiseArray[3+colorshift])/(piecewiseArray[4+colorshift]-piecewiseArray[2+colorshift])));
        }else if (pixel >= piecewiseArray[4+colorshift]|| pixel < piecewiseArray[6+colorshift]) {
            pixel = (piecewiseArray[4+colorshift]+(pixel-piecewiseArray[4+colorshift])*((piecewiseArray[7+colorshift]-piecewiseArray[5+colorshift])/(piecewiseArray[6+colorshift]-piecewiseArray[4+colorshift])));
        }else if (pixel >= piecewiseArray[6+colorshift]|| pixel < 255){
            pixel = (piecewiseArray[6+colorshift]+ (pixel - piecewiseArray[6+colorshift])* (255 - piecewiseArray[7+colorshift])/(255 - piecewiseArray[6+colorshift]));
        } else {
            pixel = 255;
        }


        return  (jbyte)pixel;
    }


extern "C"
jbyteArray Java_edu_asu_msrs_artcelerationlibrary_ArtTransformService_GaussianBlurFromJNI(
            JNIEnv *env,
            jobject /* this */,
            jbyteArray array,
            int w,
            int h,
            jintArray args1,
            jfloatArray args2) {

        jbyte* b = env->GetByteArrayElements(array, 0); //pass byte array to pointer
        int length = env-> GetArrayLength(array);
        int *intArray = env->GetIntArrayElements(args1, NULL);
        float *floatArray = env->GetFloatArrayElements(args2, NULL);


        //float sigma = 3.0;
        //int r = 20;
        LOGD("line 83");
        float** red = create2DArray(w,h);
        float** green = create2DArray(w,h);
        float** blue = create2DArray(w,h);
                LOGD("line 87");



        convertToInt(b,w,h,red,green,blue);


        processOne(red,w,h,floatArray[0],intArray[0]);
                LOGD("line 95");

        processTwo(red,w,h,floatArray[0],intArray[0]);
                LOGD("line 98");

        processOne(green,w,h,floatArray[0],intArray[0]);
        processTwo(green,w,h,floatArray[0],intArray[0]);

        processOne(blue,w,h,floatArray[0],intArray[0]);
        processTwo(blue,w,h,floatArray[0],intArray[0]);




        for (int pixel = 0, row = 0, col = 0; pixel < h*w*4; pixel += 4) {

            b[pixel + 0] = (jbyte)(red[row][col]);
            b[pixel + 1] = (jbyte)((green[row][col]));
            b[pixel + 2] = (jbyte)((blue[row][col]));
            b[pixel + 3] = (jbyte)255;
            col++;
            if (col == w) {
                col = 0;
                row++;
            }
        }
                LOGD("line 121");


        cleanupArray(red, h);
        cleanupArray(green, h);
        cleanupArray(blue, h);

                LOGD("line 127");


         env->SetByteArrayRegion (array, 0, length, b); // c++ to java, return java
         env->ReleaseByteArrayElements(array, b, 0);
         return array;
    }


void convertToInt(jbyte* b, int w, int h, float** red, float** green, float** blue) {
        for (int pixel = 0, row = 0, col = 0; pixel < h*w*4; pixel += 4) {
            //argb += ((int) b[pixel] & 0xff); // alpha
            red[row][col] = b[pixel + 0] & 0xff;
            green[row][col] = b[pixel + 1] & 0xff;
            blue[row][col] = b[pixel + 2] & 0xff;
            col++;
            if (col == w) {
                col = 0;
                row++;
            }
        }
    }

float** create2DArray(int w, int h){

    float** color = new float*[h];
    for(int i = 0; i < w; ++i)
        color[i] = new float[w];
    return color;
}


void cleanupArray(float** array, int h){

for(int i = 0; i < h; ++i) {
//    for(int j=0; j<h; j++)
    delete[]  array[i];
}
delete [] array;

}

void processOne(float** color, int w, int h, float sigma, int r) {
       // float temp;
        for (int row = 0; row < h; row++ ){
            for (int col = 0; col < w; col++){
 //               color[row][col] = (int)(color[row][col]*gKernel(0,sigma)/unify(r,sigma));
                color[row][col] = color[row][col]*gKernel(0,sigma);

                //       temp = color[row][col]*(gKernel(0,sigma)/unify(r,sigma));
                for (int k = 1; k<=r; k++){
                    if((row < k) || ( row + k >= h)){
                        color[row][col] += 0;
                    } else{
                        color[row][col] += color[row+k][col]*(gKernel(k,sigma));
                        color[row][col] += color[row-k][col]*(gKernel(-k,sigma));

//                        temp += (temp*(gKernel(k,sigma)/unify(r,sigma)));
//                        temp += (temp*(gKernel(-k,sigma)/unify(r,sigma)));
//                        color[row][col] = (int)temp;
//                        temp = 0;
                    }

                }
            }
        }

    }


     void processTwo(float** color, int w, int h, float sigma,int r) {
    //    float temp ;
        for (int row = 0; row < h; row++ ){
            for (int col = 0; col < w; col++){
               color[row][col] = color[row][col]*(gKernel(0,sigma));
//                temp = color[row][col]*(gKernel(0,sigma));

                for (int k = 1; k<=r; k++){
                    if((col < k) || ( col + k >= w)){
                        color[row][col] += 0;
                    } else{
                        color[row][col] += color[row][col+k]*(gKernel(k,sigma));
                        color[row][col] += color[row][col-k]*(gKernel(-k,sigma));
//                        temp += (color[row][col]*(gKernel(k,sigma)));
//                        temp += (color[row][col]*(gKernel(-k,sigma)));
//                        color[row][col] = (int)temp;
//                        temp = 0;
                    }

                }

            }
        }

    }

  float gKernel(int k, float t){

        float g;
        g = (float)exp(-(k*k)/(2*(t*t)));
        g = g*(float)1/(float)sqrt(2*M_PI*t*t);

        return g;
    }
