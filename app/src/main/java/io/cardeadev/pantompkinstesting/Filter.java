package io.cardeadev.pantompkinstesting;

import android.util.Log;

/**
 * Created by abhinav on 28/06/16.
 */
public class Filter {

    private static final String TAG = "Debugging TAG";
    private double[] currentECGReading;
    private double currentECGVal ;
    private int samplingRate = 250 ;
    private static final double[] lowPassCoeff = { 1.0000,    2.0000,     1.0000,     1.0000,     -1.4755,    0.5869} ;
    private static final double[] highPassCoeff = {  1.0000,   -2.0000,    1.0000,    1.0000,   -1.8227,    0.8372} ;
    private static final double[] diffCoeff = {-0.1250,	-0.2500,	0,	0.2500,	0.1250} ;
    private static final int windowWidth = 38 ;
    private FixedQueue<Double> movingWindowQueue = new FixedQueue<>(windowWidth) ;
    private double movingWindowSum = 0;

    private double[] tempArrayX = new double[diffCoeff.length] ;





    public Filter(double[] ecg_vals) {
        currentECGReading = ecg_vals ;
    }

    public Filter() {
        System.arraycopy(diffCoeff, 0, tempArrayX, 1, diffCoeff.length-1);
    }



    public void setCurrentECGReading(double[] currentECGReading) {
        this.currentECGReading = currentECGReading;

    }

    public void staticFilter(){
        double tempVar ;
        for (int i = 0; i < currentECGReading.length; i++) {
            tempVar = 0 ;

            tempVar = lowPassNext(currentECGReading[i]) ;
            tempVar = highPassNext(tempVar) ;
            tempVar = diffFilterNext(tempVar) ;
            tempVar = squareNext(tempVar) ;

            //or//
            //tempVar = squareNext(diffFilterNext(highPassNext(lowPassNext(currentECGReading[i])))) ;
        }

    }

    public void movingWindowhandler(){


    }

    public void queueInit(){

    }

    public double lowPassNext(double upVal){
        double mod = 0 ;
        for (int i = 0; i < lowPassCoeff.length; i++) {
            mod = mod + upVal* lowPassCoeff[i] ;
        }
        return mod ;
    }

    public double lowPassNext(String upValString){

        double mod = 0, upVal ;
        upVal = Double.parseDouble(upValString) ;
        for (int i = 0; i < lowPassCoeff.length; i++) {
            mod = mod + upVal* lowPassCoeff[i] ;
        }
        return mod ;
    }



    public double highPassNext(double upVal){
        double mod = 0 ;
        for (int i = 0; i < highPassCoeff.length; i++) {
            mod = mod + upVal* highPassCoeff[i] ;
        }
        return mod ;
    }

    public double diffFilterNext(double upVal){
        tempArrayX[0] = upVal ;

        double mod = 0 ;
        for (int i = 0; i < diffCoeff.length; i++) {
            mod = mod + tempArrayX[i] * diffCoeff[i] ;
        }
        return mod ;
    }

    public double squareNext(double upVal){
        return Math.pow(upVal, 2) ;
    }



    public double movingWindowNext(double upVal){
        double mod ;
        movingWindowQueue.add(upVal) ;
        movingWindowSum = 0 ;
        for (int i = 0; i < movingWindowQueue.size(); i++) {
            movingWindowSum = movingWindowSum + movingWindowQueue.get(i) ;
        }
        mod = movingWindowSum/movingWindowQueue.size() ;
        return mod;
    }



}
