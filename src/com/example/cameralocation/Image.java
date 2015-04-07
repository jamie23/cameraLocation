package com.example.cameralocation;

public class Image {
    //private vars	
    int _id;
    String filepath;
    String latitude;
    String longitude;

    public Image(String filepath, String newLatitude, String newLongitude){
        this.filepath = filepath;
        this.latitude = newLatitude;
        this.longitude = newLongitude;
    }

    //Used when creating the new image which returned when you look for it
    public Image(int id, String filepath, String newLatitude, String newLongitude){
        this._id = id;
        this.filepath = filepath;
        this.latitude = newLatitude;
        this.longitude = newLongitude;
    }

    /*
     * Getters
     */
    //get ID
    public int getID(){
        return this._id;
    }

    //get filepath
    public String getFilePath(){
        return this.filepath;
    }

    //get latitude
    public String getLatitude(){
        return this.latitude;
    }

    //get longitude
    public String getLongitude(){
        return this.longitude;
    }

    /*
     * Setters
     */
    //Set ID
    public void setID(int id){
        this._id = id;
    }

    //Set filepath
    public void setFilepath(String path){
        this.filepath = path;
    }

    //Set latitude
    public void setLatitude(String latitude){
        this.latitude = latitude;
    }

    //Set longitude
    public void setLongitude(String longitude){
        this.longitude = longitude;
    }
}
