package com.example.cameralocation;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.cameralocation.SessionManagement;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends ActionBarActivity {
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "CameraLocation";
    
    private ImageView imgPreview;
    private Button btnPicture;
    private Uri filePath;
	LocationManager locationM;
 
	SessionManagement session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		session = new SessionManagement(getApplicationContext());
		locationM = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 

		imgPreview = (ImageView) findViewById(R.id.imgPreview);
        btnPicture = (Button) findViewById(R.id.takePicture);
        
        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	// capture picture
            	takePicture();

            	Location currLocation2 = locationM.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Location currLocation = locationM.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            	
        		double currLong = 0;
        		double currLat = 0;
        		boolean usedGPS = true;
        		
        		
        		//If no gps available, use network provider
        		if(currLocation==null){
        			currLong = currLocation2.getLongitude();
        			currLat = currLocation2.getLatitude();
        			usedGPS = false;
        		}else{
        			currLong = currLocation.getLongitude();
        			currLat = currLocation.getLatitude();
        		}
        		
        		session.addPicture(Double.toString(currLat), Double.toString(currLong));
            }
        });
	}
	
	private void takePicture() {
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	 
	    filePath = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
	 
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
	 
	    // start the image capture Intent
	    startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}
	
	private static File getOutputMediaFile(int type) {
		 
	    // External sdcard location
	    File mediaStorageDir = new File(
	            Environment
	                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
	            IMAGE_DIRECTORY_NAME);
	 
	    // Create the storage directory if it does not exist
	    if (!mediaStorageDir.exists()) {
	        if (!mediaStorageDir.mkdirs()) {
	            Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
	                    + IMAGE_DIRECTORY_NAME + " directory");
	            return null;
	        }
	    }
	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
	            Locale.getDefault()).format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator
	                + "IMG_" + timeStamp + ".jpg");
	    } else {
	        return null;
	    }
	 
	    return mediaFile;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	 
	 public void openPictures(View view){
		//Open settings in response to the button being clicked.
		Intent intent = new Intent(this, DisplayPhotos.class);
		startActivity(intent);
	}

}
