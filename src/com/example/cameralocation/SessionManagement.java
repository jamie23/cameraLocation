/*
 * Followed a tutorial to save details using android hive.
 */

package com.example.cameralocation;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManagement {
    //Class to share preferences
    SharedPreferences preferences;

    //Need editor for shared preferences
    Editor editor;

    Context currContext;

    //Shared preference mode
    int PRIVATE_MODE = 0;

    //shared preference file name
    private static final String PREF_NAME = "LocationPref";

    //Shared Preference keys
    public static final String LATITUDE = "3";
    public static final String LONGITUDE = "2";

    public SessionManagement(Context context){
        this.currContext = context;
        preferences = currContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    //Storing the data
    public void addPicture(String lat, String longitude){
        //Logged in here means settings have been setup.
        editor.putString(LATITUDE, lat);
        editor.putString(LONGITUDE, longitude);
        editor.commit();
    }	

    //Retrieve user details via an array
    public String[] retrieveDetails(){
        String[] picDetails = new String[2];
        picDetails[0] = preferences.getString(LATITUDE,"Error");
        picDetails[1] = preferences.getString(LONGITUDE,"Error");
        return picDetails;
    }
}
