package com.axegurnov.android.earthshaker;

/**
 * Created by alexe on 02.03.2018.
 */

public class EarthDate {
    private String mCoordinate;

    private String mPlace;

    private String mDate;

    private String mTime;

    private String mMag;

    private String mUrl;

    public EarthDate(String coordinate, String place, String date, String time, String mag,String url) {
        mCoordinate = coordinate;
        mPlace = place;
        mDate = date;
        mTime = time;
        mMag = mag;
        mUrl = url;
    }

    public String getmCoordinate(){
        return mCoordinate;
    }

    public String getmUrl(){
        return mUrl;
    }

    public String getmPlace(){
        return mPlace;
    }
    public String getmDate(){
        return mDate;
    }
    public String getmTime(){
        return mTime;
    }

    public String getmMag(){
        return mMag;
    }

}
