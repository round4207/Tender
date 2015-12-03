package com.example.tenderapp;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.example.tenderapp.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class AddDish extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_dish);
	}
	
	private File outputFile;
	private File thumbNailFile;
	
	public void addPicture (View v)
	{
		File sdCard = Environment.getExternalStorageDirectory();
		File directory = new File (sdCard.getAbsolutePath() + "/CameraTest/");
		directory.mkdirs();
		
		// unique filename based on the time
		String name = String.valueOf(System.currentTimeMillis());
		outputFile = new File(directory, name+".jpg");
		thumbNailFile = new File(directory, name+"_tn.jpg");

        Uri outputFileUri = Uri.fromFile(outputFile);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) 
		{
			// rescale the picture from the full size output file which is assumed to now be
			// in outputFile
			try
			{
				// create rescale 640
				// create thumbnail rescale 50

				ImageUtils.resizeSavedBitmap(outputFile.getAbsolutePath(), 100, thumbNailFile.getAbsolutePath());
				ImageUtils.resizeSavedBitmap(outputFile.getAbsolutePath(), 640, outputFile.getAbsolutePath());
				updateImageView();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				//textTargetUri.setText(e.getClass().getName());				
			}
		}
	}
	
	private void updateImageView()
	{
		// place ImageView with Picasso
		ImageView imageView = (ImageView) findViewById(R.id.targetImage);
		Picasso.with(this).load(outputFile).fit().into(imageView);
		//textTargetUri.setText(outputFile.getAbsolutePath());

	}
	
	public void saveClicked (View v) throws IOException
	{
		EditText dishName = (EditText) findViewById(R.id.dishName);
		EditText price = (EditText) findViewById(R.id.price);
		EditText nameOfEstablishment = (EditText) findViewById(R.id.nameOfEstablishment);
		EditText contactNumber = (EditText) findViewById(R.id.contactNumber);
		EditText latitude = (EditText) findViewById(R.id.latitude);
		EditText longitude = (EditText) findViewById(R.id.longitude);
		
		String dishNameStr = dishName.getText().toString();
		String priceStr = price.getText().toString();
		
		if (priceStr.isEmpty())
        {
        	Toast.makeText(getApplicationContext(), "Please enter the price of the dish.", Toast.LENGTH_LONG).show();
        	return;
        }
		double priceDbl = Double.parseDouble(priceStr);
		String nameOfEstablishmentStr = nameOfEstablishment.getText().toString();
		String contactNumberStr = contactNumber.getText().toString();
		
		String latitudeStr = latitude.getText().toString();
		if (latitudeStr.isEmpty())
        {
        	Toast.makeText(getApplicationContext(), "Please enter establishment latitude.", Toast.LENGTH_LONG).show();
        	return;
        }
		double latitudeDbl = Double.parseDouble(latitudeStr);

		String longitudeStr = longitude.getText().toString();
		if (longitudeStr.isEmpty())
        {
        	Toast.makeText(getApplicationContext(), "Please enter establishment longitude.", Toast.LENGTH_LONG).show();
        	return;
        }
		double longitudeDbl = Double.parseDouble(longitudeStr);
		
		ParseGeoPoint point = new ParseGeoPoint(latitudeDbl, longitudeDbl);
		
		String filename = outputFile.getAbsolutePath();
	    String thumbnailFilename = thumbNailFile.getAbsolutePath();
	    Date dateTaken = new Date();
	    
	    // send Images via Parse    
        ParseFile thumbnail = new ParseFile(ImageUtils.getFileByte(thumbnailFilename), thumbnailFilename);
        thumbnail.saveInBackground();

        ParseFile fullSize = new ParseFile(ImageUtils.getFileByte(filename), filename);
        fullSize.saveInBackground();
		
		// create and save
		ParseObject addDish = new ParseObject("Dish");
		
		if (dishNameStr.isEmpty())
        {
        	Toast.makeText(getApplicationContext(), "Please enter a dish name.", Toast.LENGTH_LONG).show();
        	return;
        }
		addDish.put("name", dishNameStr);
		
		addDish.put("establishment", nameOfEstablishmentStr);
		
		addDish.put("price", priceDbl);
		
		//if (addDish.getParseFile("thumbnail") == null)
		//{
		//		Toast.makeText(getApplicationContext(), "Please add a photo of the dish.", Toast.LENGTH_LONG).show();
		//		return;
		//}
			addDish.put("pic", fullSize);
		    addDish.put("thumbnail", thumbnail);
		
		ParseObject addEstablishment = new ParseObject("Establishment");
		if (nameOfEstablishmentStr.isEmpty())
        {
        	Toast.makeText(getApplicationContext(), "Please enter the establishment name.", Toast.LENGTH_LONG).show();
        	return;
        }
		addEstablishment.put("name", nameOfEstablishmentStr);
		
		if (contactNumberStr.isEmpty())
        {
        	Toast.makeText(getApplicationContext(), "Please enter the establishment contact number.", Toast.LENGTH_LONG).show();
        	return;
        }
		addEstablishment.put("contactNumber", contactNumberStr);
		
		addEstablishment.put("location", point);

		addEstablishment.put("pic", fullSize);
	    addEstablishment.put("thumbnail", thumbnail);
		
		// should put some kind of Progress and disable the buttons while loading
		disableButtonsShowProgress();
		addDish.saveInBackground(new SaveCallback() {
		  public void done(ParseException e) 
		  {
		    if (e == null) 
		    {
		      // Hooray! Let them use the app now.
		    	Toast.makeText(getApplicationContext(), "Thanks for adding a dish!", Toast.LENGTH_LONG).show();
		    	//finish();
		    } 
		    else 
		    {
		      // Sign up didn't succeed. Look at the ParseException
		      // to figure out what went wrong
		    	e.printStackTrace();
		    	Toast.makeText(getApplicationContext(), "Unable to add dish: "+e.getMessage(), Toast.LENGTH_LONG).show();
		    }
		  }
		});
		addEstablishment.saveInBackground(new SaveCallback() {
			  public void done(ParseException e) 
			  {
			    if (e == null) 
			    {
			      // Hooray! Let them use the app now.
			    } 
			    else 
			    {
			      // Sign up didn't succeed. Look at the ParseException
			      // to figure out what went wrong
			    	e.printStackTrace();
			    	Toast.makeText(getApplicationContext(), "Unable to add dish: "+e.getMessage(), Toast.LENGTH_LONG).show();
			    }
			    
			    enableButtonDisableProgress();
			  }
			});
	}
	
	public void cancelClicked (View v)
	{
		finish();
	}
	
	private void disableButtonsShowProgress()
	{
		View button1 = findViewById(R.id.save);
		View button2 = findViewById(R.id.cancel);
		View progress = findViewById(R.id.progressBar1);
		
		button1.setEnabled(false);
		button2.setEnabled(false);
		progress.setVisibility(View.VISIBLE);

	}
	
	private void enableButtonDisableProgress()
	{
		View button1 = findViewById(R.id.save);
		View button2 = findViewById(R.id.cancel);
		View progress = findViewById(R.id.progressBar1);
		
		button1.setEnabled(true);
		button2.setEnabled(true);
		progress.setVisibility(View.INVISIBLE);		
	}	
	
	
}
