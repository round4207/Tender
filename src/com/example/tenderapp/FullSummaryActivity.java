package com.example.tenderapp;

import java.util.List;

import com.example.tenderapp.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class FullSummaryActivity extends Activity {

	String foodId;
	String establishmentName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_summary);
		
		// NOTE: you cannot pass the CatPic directly doesn't seem to work
		//		 so pass the object ID and reload the ParseObject
		foodId = (String) getIntent().getStringExtra("foodId");
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Dish"); 
		query.getInBackground(foodId, new GetCallback<ParseObject>()
		{
			@Override
			public void done(ParseObject report, ParseException arg1) {
				// TODO Auto-generated method stub
				if (arg1==null)
				{
					// upon loading, place the ParseFile into the image view
					// then load in background
					ParseImageView imageView = (ParseImageView) findViewById(R.id.imageView1);
					
					imageView.setParseFile(report.getParseFile("thumbnail"));
					imageView.loadInBackground();
					
					TextView dishName = (TextView) findViewById(R.id.dishName);
					dishName.setText(report.getString("name"));
					establishmentName = report.getString("name");
					
					TextView price = (TextView) findViewById(R.id.price);
					price.setText(String.valueOf(report.getDouble("price")));
					
					TextView nameOfEstablishment = (TextView) findViewById(R.id.nameOfEstablishment);
					nameOfEstablishment.setText(report.getString("establishment"));
				}
				else
				{
					arg1.printStackTrace();
				}
			} 
		
		});
		
		ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Establishment"); 
		
		query1.whereEqualTo("name", establishmentName);
		query1.findInBackground(new FindCallback<ParseObject>()
		{
			@Override
			public void done(List<ParseObject> name, ParseException arg1) {
				// TODO Auto-generated method stub
				if (arg1==null)
				{
					TextView contactNumber = (TextView) findViewById(R.id.contactNumber);
					contactNumber.setText(name.get(0).get("contactNumber").toString());
					
					//Button button = (Button) findViewById(R.id.viewClick);
					//button.setTag(report);
				}
				else
				{
					arg1.printStackTrace();
				}
			} 
		});
	}
	
	public void viewClick (View v)
	{
		//ParseObject foodId = (ParseObject) v.getTag();
		
		Intent intent = new Intent(getApplicationContext(), TrackingActivity.class);
		//intent.putExtra("foodId", foodId.getObjectId());
		intent.putExtra("foodId", foodId);
		startActivity(intent);
	}
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.full_report, menu);
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
