package com.example.tenderapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.tenderapp.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddDish extends Activity {
	
	ArrayList<String> estabNames;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_dish);
		
		estabNames = new ArrayList<String>(Arrays.asList("Loading..."));
		final ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, estabNames);
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		spinner.setAdapter(adapter);
		
		ParseQuery<Establishment> placeQuery = new ParseQuery<Establishment>(Establishment.class);
		//TODO:filter the results
		placeQuery.findInBackground(new FindCallback<Establishment>() {
			@Override
			public void done(List<Establishment> placeResults, ParseException arg1) {
				if (arg1==null)
				{
					adapter.clear();
					for (int i = 0; i < placeResults.size(); i++) {
						adapter.add(placeResults.get(i).getName());
					}
					adapter.notifyDataSetChanged();
				}
				else
				{
					arg1.printStackTrace();
				}
			} 
		});
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
		else
		{
			outputFile = null;
			thumbNailFile = null;
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
		Spinner nameOfEstablishment = (Spinner) findViewById(R.id.spinner);
		
		String dishNameStr = dishName.getText().toString();
		String priceStr = price.getText().toString();
		
		if (priceStr.isEmpty())
        {
        	Toast.makeText(getApplicationContext(), "Please enter the price of the dish.", Toast.LENGTH_LONG).show();
        	return;
        }
		double priceDbl = Double.parseDouble(priceStr);
		//String nameOfEstablishmentStr = nameOfEstablishment.getText().toString();
		String nameOfEstablishmentStr = nameOfEstablishment.getSelectedItem().toString();
		
		if (nameOfEstablishmentStr.equals("Loading...")) {
			Toast.makeText(getApplicationContext(), "Please enter a valid establishment name.", Toast.LENGTH_LONG).show();
        	return;
		}
		
		Dish addDish = new Dish();
		
		//ParseObject addDish = new ParseObject("Dish");
		
	    
		if (dishNameStr.isEmpty())
        {
        	Toast.makeText(getApplicationContext(), "Please enter a dish name.", Toast.LENGTH_LONG).show();
        	return;
        }
		addDish.setDishName(dishNameStr);
		
		addDish.setEstablishment(nameOfEstablishmentStr);
		
		addDish.setPrice(priceDbl);
		
		if (thumbNailFile!=null)
	    {
	    	 String thumbnailFilename = thumbNailFile.getAbsolutePath();
	    	 ParseFile thumbnail = new ParseFile(ImageUtils.getFileByte(thumbnailFilename), thumbnailFilename);
	         thumbnail.saveInBackground();
			 addDish.setThumbNail(thumbnail);
	    }
	    else
	    {
	    	Toast.makeText(getApplicationContext(), "Please add a photo of the dish.", Toast.LENGTH_LONG).show();
			return;
	    }
	    if (outputFile != null)
	    {
			String filename = outputFile.getAbsolutePath();
	        ParseFile fullSize = new ParseFile(ImageUtils.getFileByte(filename), filename);
	        fullSize.saveInBackground();
			addDish.setPic(fullSize);
	    }		
		
		// should put some kind of Progress and disable the buttons while loading
		disableButtonsShowProgress();
		addDish.saveInBackground(new SaveCallback() {
		  public void done(ParseException e) 
		  {
		    if (e == null) 
		    {
		      // Hooray! Let them use the app now.
		    	Toast.makeText(getApplicationContext(), "Thanks for adding a dish!", Toast.LENGTH_LONG).show();
		    	finish();
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
