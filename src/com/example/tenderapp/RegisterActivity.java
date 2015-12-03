package com.example.tenderapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tenderapp.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.squareup.picasso.Picasso;

public class RegisterActivity extends Activity {

	ParseUser user = new ParseUser();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}

	private ParseFile file;
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
	
	public void save(View v) throws IOException
	{
		EditText userName = (EditText) findViewById(R.id.username);
		EditText email = (EditText) findViewById(R.id.email);
		EditText password = (EditText) findViewById(R.id.password);
		EditText confirmPassword = (EditText) findViewById(R.id.confirmPassword);
		
		String userNameStr = userName.getText().toString();
		String emailStr = email.getText().toString();
		String passwordStr = password.getText().toString();
		String confirmPasswordStr = confirmPassword.getText().toString();

		String filename = outputFile.getAbsolutePath();
	    String thumbnailFilename = thumbNailFile.getAbsolutePath();
		
	    // send Images via Parse    
        ParseFile thumbnail = new ParseFile(ImageUtils.getFileByte(thumbnailFilename), thumbnailFilename);
        thumbnail.saveInBackground();

        ParseFile fullSize = new ParseFile(ImageUtils.getFileByte(filename), filename);
        fullSize.saveInBackground();
        
       // if ((outputFile.length() == 0) && (thumbNailFile.length() == 0))
        //{
    		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.buffalo_wing);
    		ByteArrayOutputStream stream = new ByteArrayOutputStream();
    		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
    		byte[] image = stream.toByteArray();
    		
    		file = new ParseFile("buffalo_wing.jpg", image);
    		user.put("profPic",file);
      //  }
       // else
		//{
        	//user.put("profPic", thumbnail);
        //}
        
	    // create and save
        if (userNameStr.isEmpty())
        {
        	Toast.makeText(getApplicationContext(), "Please enter a username.", Toast.LENGTH_LONG).show();
        	return;
        }
		user.setUsername(userNameStr);
		
		if (emailStr.isEmpty())
        {
        	Toast.makeText(getApplicationContext(), "Please enter an email.", Toast.LENGTH_LONG).show();
        	return;
        }
		user.setEmail(emailStr);
		
		if (passwordStr.equals(confirmPasswordStr))
		{
			user.setPassword(passwordStr);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Passwords do not match. Try again.", Toast.LENGTH_LONG).show();
			return;
		}
		
		disableButtonsShowProgress();
		file.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				// TODO Auto-generated method stub
				if (e == null)
				{
					signup();
					//Toast.makeText(getApplicationContext(), "Registration successful.  You can now log in", Toast.LENGTH_LONG).show();
			    	//finish();
				}
				else
				{
			    	e.printStackTrace();
			    	Toast.makeText(getApplicationContext(),
                    "Unable to register: " + e.getMessage(), Toast.LENGTH_LONG).show();
				}

			    enableButtonDisableProgress();	
			}
		});
	}
	
	private void signup()
	{
		user.signUpInBackground(new SignUpCallback() {
		  public void done(ParseException e) 
		  {
		  if (e == null) 
		    {
		      // Hooray! Let them use the app now.
		    	Toast.makeText(getApplicationContext(), "Registration successful.  You can now log in", Toast.LENGTH_LONG).show();
		    	finish();
		    } 
		  else
		    {
		      // Sign up didn't succeed. Look at the ParseException
		      // to figure out what went wrong
		    	e.printStackTrace();
		    	Toast.makeText(getApplicationContext(), "Unable to register: "+e.getMessage(), Toast.LENGTH_LONG).show();
		    }
		  }
		});
	}
	
	public void cancel(View v)
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
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
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
