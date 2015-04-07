package com.example.cameralocation;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class DisplayPhotos extends ActionBarActivity {
    SessionManagement session;
    final DatabaseHandler db = new DatabaseHandler(this);

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_display_photos);
            session = new SessionManagement(getApplicationContext());

            /*
             * Get co-ordinates as string, each is an array[0] = lat, array[1] = longitude
             * convert to doubles
             * pass into getName, update namePlace.setText with the name
             */
            //String pictureCoords = db.getImageCoord("/storage/emulated/0/Pictures/CameraLocation/IMG_20140614_214904.jpg");

            //Get current co-ordinates
            Location currCords = getCoords();
            double currLat = currCords.getLatitude();
            double currLong = currCords.getLongitude();

            TextView namePlace = (TextView) findViewById(R.id.NamePlace);
            TextView distPlace = (TextView) findViewById(R.id.NameDistance);

            //Get picture coordinates
            String[] picCoords = session.retrieveDetails();
            Double[] castCoords = new Double[2];
            castCoords[0] = Double.parseDouble(picCoords[0]);
            castCoords[1] = Double.parseDouble(picCoords[1]);

            //Update with name of place
            String nameOfPlace = getName(castCoords);
            namePlace.setText(nameOfPlace);

            float[] results = new float[10];
            Location.distanceBetween(currLat, currLong, castCoords[0], castCoords[1], results);

            //Update distance to photo
            distPlace.setText(Float.toString(results[0]) + " metres");

            //This is all to do with history of images, currently functionality is not completely fleshed out.	
            LinkedList<String> names = getImages();
            String[] array = names.toArray( new String[0] );

            /*
             * Spinner for history of images
             */
            final Spinner spinnerNames = (Spinner) findViewById(R.id.spinnerNames);

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerNames.setAdapter(spinnerAdapter);

            ImageView image = (ImageView) findViewById(R.id.displayImage);
            //String path = Environment.getExternalStorageDirectory() + "/Pictures/CameraLocation/" + array[array.length-1];

            String path = array[array.length-1];
            Bitmap imageBit = BitmapFactory.decodeFile(path);
            image.setImageBitmap(imageBit);

            //Code to update the image with the one chosen in the file list
            Button btnUpdate = (Button) findViewById(R.id.btnUpdate);
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView image = (ImageView) findViewById(R.id.displayImage);
                    String imageTitle = spinnerNames.getSelectedItem().toString();
                    //String newImagePath = Environment.getExternalStorageDirectory() + "/Pictures/CameraLocation/" + imageTitle;

                    String newImagePath = imageTitle;

                    String updatePath = newImagePath.substring(7, newImagePath.length());
                    Bitmap imageBit = BitmapFactory.decodeFile(updatePath);
                    image.setImageBitmap(imageBit);
                }
            });

        }

    private Double[] getCoords(String filePath) {
        SQLiteDatabase dbInstance = db.getReadableDatabase();

        //Look up file path and place lat/long into a String array from it.
        Cursor c = dbInstance.rawQuery("SELECT * FROM TABLE_IMAGES '", null); 
        //Cursor c = dbInstance.rawQuery("SELECT KEY_LATITUDE FROM TABLE_IMAGES WHERE KEY_FILEPATH = '"+filePath+"'", null); 
        c.moveToNext(); 

        return null;
    }

    private String getName(Double[] picCoords) {
        String name = "";

        // Uses inbuilt location services to parse a name for Lat/Long
        Geocoder geoCoderObj = new Geocoder(getApplicationContext());
        List<Address> addressPerson;
        String firstLine = "";
        try {
            addressPerson = geoCoderObj.getFromLocation(picCoords[0],
                    picCoords[1], 1);
            firstLine = addressPerson.get(0).getAddressLine(0);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return firstLine;
    }

    // Return co-ordinates for given location
    private Location getCoords() {
        LocationManager locationM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Location currLocation2 = locationM
            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location currLocation = locationM
            .getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // If no gps available, use network provider
        if (currLocation == null) {
            return currLocation2;
        } else {
            return currLocation;
        }
    }

    // Gets the full history of images with this app
    LinkedList<String> getImages() {
        LinkedList<String> names = new LinkedList<String>();
        File sdCardRoot = Environment.getExternalStorageDirectory();
        File directory = new File(sdCardRoot, "/Pictures/CameraLocation/");

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<Image> images = db.getAllImages();  

        for (Image imgs : images) {
            String log = "Id: "+ imgs.getID()+" ,Filepath: " + imgs.getFilePath() + " ,Latitude: " + imgs.getLatitude()
                + " ,Longitude: " + imgs.getLongitude();
            // Writing Contacts to log
            Log.d("Name: ", log);
            names.add(imgs.getFilePath());
        }

        /* 
           for (File f : directory.listFiles()) {
           if (f.isFile()) {
           String name = f.getName();
           names.add(name);
           }
           }*/
        return names;
    }

    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.display_photos, menu);
            return true;
        }

    @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();
            if (id == R.id.action_settings) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
}
