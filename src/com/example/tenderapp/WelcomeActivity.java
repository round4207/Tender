package com.example.tenderapp;

import java.util.List;

import com.example.tenderapp.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		String foodId = (String) getIntent().getStringExtra("foodId");
		
		ParseQuery<ParseUser> query = ParseUser.getQuery(); 
		query.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> arg0, ParseException arg1) {
				// TODO Auto-generated method stub
				if (arg1==null)
				{
					// upon loading, place the ParseFile into the image view
					// then load in background
					
					ParseImageView imageView = (ParseImageView) findViewById(R.id.profPic);
					imageView.setBackgroundResource(0);
					imageView.setParseFile(ParseUser.getCurrentUser().getParseFile("profPic"));
					imageView.loadInBackground();
					
					TextView name = (TextView) findViewById(R.id.name);
					name.setText(ParseUser.getCurrentUser().getUsername());
				}
				else
				{
					arg1.printStackTrace();
				}
			}
		}); 
		
	}

	
	public void addClick (View v)
	{
		Intent intent = new Intent(getApplicationContext(), AddDish.class);
		startActivity(intent);
	}
	
	public void viewClick (View v)
	{
		Intent intent = new Intent(getApplicationContext(), ViewLikes.class);
		startActivity(intent);
	}
	
	public void viewDishes (View v) {
		Intent intent = new Intent(getApplicationContext(), SwipeActivity.class);
		startActivity(intent);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
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
