package com.example.android.quakereport;

/**
 * Created by GBEMILEKE on 15/08/2017.
 */
public class Earthquake {
    // storge the magnitude of the earthquake
    private double mMagnitude;
    // storge the loction of the earthquake
    private String mLocation;
    // storge the magnitude of the earthquake
    private long mTimeInMilliseconds;
    // store the url to the site about the eartquake
    private  String mUrl;

    /**
     * constructor
     * @param magnitude
     * @param location
     * @param timeInMilliseconds
     */
    public Earthquake(double magnitude, String location, long timeInMilliseconds, String url){
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;
    }

    /**
     *
     * @return the magnitude of the earthquake
     */
    public double getMagnitude(){return mMagnitude;}

    /**
     *
     * @return the location of the earthquake
     */
    public String getLocation(){return mLocation;}

    /**
     *
     * @return the date of the earthquake
     */
    public long getTimeInMilliseconds(){return mTimeInMilliseconds;}

    /**
     *
     * @return the url to the site about the earthquake
     */
    public String getUrl(){return mUrl;}
}
