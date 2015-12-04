package com.example.tenderapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tenderapp.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

	private String usernameStr, passwordStr;
	private EditText username, password;
	private CheckBox rememberMe;
	private SharedPreferences loginPreferences;
	private SharedPreferences.Editor loginPrefsEditor;
	private Boolean saveLogin;
	//Typeface type = Typeface.createFromAsset(getAssets(), "fonts/ChaletLondonNineteenSeventy.ttf");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// get the fields
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		rememberMe = (CheckBox) findViewById(R.id.rememberMe);
		
		loginPreferences = this.getSharedPreferences("loginPrefs", MODE_PRIVATE);
		loginPrefsEditor = loginPreferences.edit();
		
		saveLogin = loginPreferences.getBoolean("saveLogin", false);
		if (saveLogin == true)
		{
			username.setText(loginPreferences.getString("username", ""));
			password.setText(loginPreferences.getString("password", ""));
			rememberMe.setChecked(true);
		}
	}

	//ParseObject foodId;
	
	public void signIn(View v)
	{
		usernameStr = username.getText().toString();
		passwordStr = password.getText().toString();		
		
		if (usernameStr.isEmpty())
        {
			Toast.makeText(getApplicationContext(), "Please enter a username.", Toast.LENGTH_LONG).show();
        	return;
        }
		if (passwordStr.isEmpty())
        {
			Toast.makeText(getApplicationContext(), "Please enter your password.", Toast.LENGTH_LONG).show();
        	return;
        }
		// disable the buttons and enable the spinner
		disableButtonsShowSpinner();

		//foodId = (ParseObject) v.getTag();
		
		// trigger parse signing		
		ParseUser.logInInBackground(usernameStr, 
									passwordStr, 
									new LogInCallback() {
			  public void done(ParseUser user, ParseException e) {
			    if (user != null) 
			    {
			    	if (rememberMe.isChecked())
			    	{
				    	loginPrefsEditor.putBoolean("saveLogin", true);
				    	loginPrefsEditor.putString("username", usernameStr);
				    	loginPrefsEditor.putString("password", passwordStr);
				    	loginPrefsEditor.commit();
			    	}
			    	else
			    	{
			    		loginPrefsEditor.clear();
			    		loginPrefsEditor.commit();
			    	}
			    	 // Hooray! The user is logged in.
			    	
			    	Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
			    	//intent.putExtra("foodId", foodId.getObjectId());
			    	startActivity(intent);
			    } 
			    else 
			    {
			      // Signup failed. Look at the ParseException to see what happened.
			    	e.printStackTrace();
			    	Toast.makeText(getApplicationContext(), "Unable to sign in: "+e.getMessage(), Toast.LENGTH_LONG).show();

			    }
			    
			    // enable the buttons and disable the spinner
			    enableButtonDisableSpinner();
			    
			  }
			});		
		
	}
	
	
	public void register(View v)
	{
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}
	
	
	private void disableButtonsShowSpinner()
	{
		View button1 = findViewById(R.id.signIn);
		View button2 = findViewById(R.id.register);
		View spinner = findViewById(R.id.progressBar1);
		
		spinner.setVisibility(View.VISIBLE);
		button1.setEnabled(false);
		button2.setEnabled(false);
		button1.setVisibility(View.GONE);
		button2.setVisibility(View.GONE);
	}
	
	private void enableButtonDisableSpinner()
	{
		View button1 = findViewById(R.id.signIn);
		View button2 = findViewById(R.id.register);
		View spinner = findViewById(R.id.progressBar1);
		
		button1.setEnabled(true);
		button2.setEnabled(true);
		button1.setVisibility(View.VISIBLE);
		button2.setVisibility(View.VISIBLE);
		spinner.setVisibility(View.GONE);		
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
}
