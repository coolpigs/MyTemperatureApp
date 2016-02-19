package com.whb.temp;

/**
 * Created by hongbo on 2016/2/17.
 */
public class Conversion {
    static {
        System.loadLibrary("utility");
    }

    /**
     * convert Celsius temperature to Fahrenheit temperature
     * @param temp  Celsius temperature array
     * @return  Fahrenheit temperature array
     */
    public static native float[] nativeCelsius2Fahrenheit(float[] temp);

    /**
     * convert Fahrenheit temperature to Celsius temperature
     * @param temp  Fahrenheit temperature array
     * @return  Celsius temperature array
     */
    public static native float[] nativeFahrenheit2Celsius(float[] temp);

}