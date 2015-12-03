package com.example.tenderapp;

import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TrackingActivity extends Activity {

	private GoogleMap map;

	// just so you know
	private static final LatLng MANILA = new LatLng(14.579754, 121.043122);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tracking);
	
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

		// enables the my location button on the upper right
		map.setMyLocationEnabled(true);

		// default to normal view
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		//map.moveCamera(CameraUpdateFactory.newLatLngZoom(MANILA, 17));		
		
		String establishment = (String) getIntent().getStringExtra("establishment");
		System.out.println("WHUUUUUUUUUUUAAAAAAAAAAAAWWWWWWWW" + establishment);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Establishment");
		query.getInBackground(establishment, new GetCallback<ParseObject>() 
		{
			@Override
			public void done(ParseObject establishment, ParseException arg1) {
				// TODO Auto-generated method stub
					TextView establishmentName = (TextView) findViewById(R.id.establishmentName);
					establishmentName.setText(establishment.getString("name"));
				
					ParseGeoPoint estabLocation = (ParseGeoPoint) establishment.getParseGeoPoint("location");
					String name = establishment.get("name").toString();
					Double userLat = estabLocation.getLatitude();
					Double userLong = estabLocation.getLongitude();
					LatLng pos = new LatLng(userLat,userLong);
					
					map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 17));
					
					addMarker(pos, name);
			}});

	}

	private void addMarker(final LatLng pos, final String name)
	{
		// NOTE: in order to update the UI an operation must occur in the UI
		// thread, this will force the action to occur in the UI thread
		runOnUiThread(new Runnable()
		{
			public void run()
			{
				// Use is diff icon to indicate if the data is sent or not
				MarkerOptions markerOptions = new MarkerOptions().position(pos);

				markerOptions.title(name);

				// based on local profile
				markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.red_pin));

				Marker marker = map.addMarker(markerOptions);
			}
		});

	}
	
	public void back (View v)
	{
		finish();
	}
	
	
	
	
	
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tracking, menu);
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
