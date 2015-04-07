package com.example.cameralocation;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper { 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ImageManager";

    // Contacts table name
    private static final String TABLE_IMAGES = "images";

    private static final String KEY_ID = "id";
    private static final String KEY_FILEPATH = "filepath";  
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUTDE = "longitude";    

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_IMAGES_TABLE = "CREATE TABLE " + TABLE_IMAGES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FILEPATH + " TEXT," 
                + KEY_LATITUDE + " TEXT," + KEY_LONGITUTDE + " TEXT" + ")";
            db.execSQL(CREATE_IMAGES_TABLE);
        }

    // Upgrading database
    @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);

            // Create tables again
            onCreate(db);
        }

    // Adding new contact
    public void addImage(Image newImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //Add image file path
        values.put(KEY_FILEPATH, newImage.getFilePath()); 
        values.put(KEY_LATITUDE, newImage.getLatitude()); 
        values.put(KEY_LONGITUTDE, newImage.getLongitude()); 

        // Inserting Row
        db.insert(TABLE_IMAGES, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    public Image getImage(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_IMAGES, new String[] { KEY_ID,
            KEY_FILEPATH, KEY_LATITUDE, KEY_LONGITUTDE }, KEY_ID + "=?",
            new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        //Returns this image with ID and filepath 
        Image image = new Image(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),  cursor.getString(3));

        //return image
        return image;
    }

    // Getting All Contacts
    public List<Image> getAllImages() {
        List<Image> imageList = new ArrayList<Image>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_IMAGES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Image image = new Image();
                image.setID(Integer.parseInt(cursor.getString(0)));
                image.setFilepath(cursor.getString(1));
                image.setLatitude(cursor.getString(2));
                image.setLongitude(cursor.getString(3));
                // Adding contact to list
                imageList.add(image);
            } while (cursor.moveToNext());
        }

        // return contact list
        return imageList;
    }

    // Getting single contact
    /*public String getImageCoord(String filename) {
      String selectQuery = "SELECT id FROM " + TABLE_IMAGES +  " WHERE " + KEY_FILEPATH + " = '" +
      filename + "'" ;
      SQLiteDatabase db = this.getWritableDatabase();

      Cursor cursor = db.query
      (
          TABLE_IMAGES,
          new String[] { TABLE_ROW_ID, TABLE_ROW_ONE, TABLE_ROW_TWO },
          TABLE_ROW_ID + "=" + rowID,
          null, null, null, null, null
      );

      cursor.moveToFirst();
      return cursor.getString(0);
      }*/
}
