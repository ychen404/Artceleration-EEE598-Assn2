
jbyte ArrayOperater(jbyte pixel1,int colorshift, int* piecewiseArray);
void convertToInt(jbyte* b, int w, int h, float** red, float** green, float** blue);
void processOne(float** color, int w, int h, float sigma, int r);
void processTwo(float** color, int w, int h, float sigma,int r);
float gKernel(int k, float t);
float** create2DArray(int w, int h);
void cleanupArray(float** array, int h);



