package com.example.tenderapp;

import com.example.tenderapp.R;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ViewLikes extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_likes);
		
		// create an adapter instance
		ParseQueryAdapter<ParseObject> adapter = createAdapter(null);

		// set adapter into list view
		ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);
	}

	private ParseQueryAdapter<ParseObject> createAdapter(final String filter) {
		// initialize query factory
		ParseQueryAdapter.QueryFactory<ParseObject> factory = new ParseQueryAdapter.QueryFactory<ParseObject>() {
			public ParseQuery<ParseObject> create() {
				// Here we can configure a ParseQuery to our heart's desire.
				ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Dish");

				// NOTE: adjust query here
				if (filter != null) {
					query.whereStartsWith("name", filter);
				}
				return query;
			}
		};

		// initialize adapter
		ParseQueryAdapter<ParseObject> adapter = new ParseQueryAdapter<ParseObject>(this,
				factory) {
			// configure the rows
			@Override
			public View getItemView(final ParseObject object, View v,
					ViewGroup parent) {
				// if (v == null)
			//	{
					v = View.inflate(getContext(), R.layout.row, null);
			//	}
				
				// initialize name
				
				//if (object.getParseFile("thumbnail") != null)
				//{
					ParseImageView imageView = (ParseImageView) v.findViewById(R.id.parseImage);
					imageView.setBackgroundResource(0);
					imageView.setParseFile(object.getParseFile("thumbnail"));
					imageView.loadInBackground();
				//}
				
				TextView dishName = (TextView) v.findViewById(R.id.dishName);
				dishName.setText(object.getString("name"));
				TextView price = (TextView) v.findViewById(R.id.price);
				price.setText(String.valueOf(object.getDouble("price")));
				TextView nameOfEstablishment = (TextView) v.findViewById(R.id.nameOfEstablishment);
				nameOfEstablishment.setText(object.getString("establishment"));
				
				ImageButton button = (ImageButton) v.findViewById(R.id.viewClick);
				button.setTag(object);	
				
				return v;
			}

			// configure the NEXT button at bottom of list
			// this can be any view
			@Override
			public View getNextPageView(View v, ViewGroup parent) {

				if (v == null) {
					v = View.inflate(getContext(), R.layout.morereports, null);
				}

				return v;
			}

		};

		// initialize adapter
		adapter.setPaginationEnabled(true);
		adapter.setObjectsPerPage(3);
		return adapter;
	}
	
	public void viewClick(View v)
	{
		ParseObject foodId = (ParseObject) v.getTag();
		
		Intent intent = new Intent(getApplicationContext(), FullSummaryActivity.class);
		intent.putExtra("foodId", foodId.getObjectId());
		startActivity(intent);
	}
	
	public void back (View v)
	{
		finish();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.summary, menu);
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
