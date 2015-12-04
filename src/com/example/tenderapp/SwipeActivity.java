package com.example.tenderapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class SwipeActivity extends Activity {
	int windowwidth;
	int screenCenter;
	int x_cord, y_cord, x, y;
	int Likes = 0;
	RelativeLayout parentView;
	float alphaValue = 0;
	private Context m_context;
	//private GoogleMap map;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swipe);
		
		m_context = SwipeActivity.this;
        
        parentView = (RelativeLayout) findViewById(R.id.relativeLayout);
		windowwidth = getWindowManager().getDefaultDisplay().getWidth();
		screenCenter = windowwidth / 2;
		final ProgressBar spinny = (ProgressBar) findViewById(R.id.loadingSpinner);
		final TextView loading = (TextView) findViewById(R.id.loadingLabel);

//		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
//
//		// enables the my location button on the upper right
//		map.setMyLocationEnabled(true);
//		// default to normal view
//		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//		//map.moveCamera(CameraUpdateFactory.newLatLngZoom(MANILA, 17));		
//		ParseGeoPoint userLoc = new ParseGeoPoint(map.getMyLocation().getLatitude(),map.getMyLocation().getLongitude());
		
		ParseQuery<Establishment> placeQuery = new ParseQuery<Establishment>(Establishment.class);
		//placeQuery.whereNear("location", userLoc);
		placeQuery.findInBackground(new FindCallback<Establishment>() {
			@Override
			public void done(List<Establishment> placeResults, ParseException arg2) {

				ParseQuery<Dish> foodQuery = new ParseQuery<Dish>(Dish.class);
				ArrayList<String> likes = (ArrayList<String>) ParseUser.getCurrentUser().get("dishesLiked");
				ArrayList<String> places = new ArrayList<String>();
				for (int i = 0; i < placeResults.size(); i++) {
					places.add(placeResults.get(i).getName());
				}
				foodQuery.whereNotContainedIn("objectId", likes);
				foodQuery.whereContainedIn("establishment", places);
				foodQuery.findInBackground(new FindCallback<Dish>() { 
					@Override
					public void done(List<Dish> foodResults, ParseException arg1) {
						if (arg1 == null) {
							spinny.setVisibility(View.INVISIBLE);
							//loading.setVisibility(View.GONE);
							loading.setText("You've finished\n   all the food!");
							System.out.println("Did it reach?");
							for (int i = 0; i < foodResults.size(); i++) {		
								
								final Dish bruh = foodResults.get(i);
								
								LayoutInflater inflate = (LayoutInflater) m_context
										.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
								final View m_view = inflate.inflate(R.layout.card, null);
								//ImageView m_image = (ImageView) m_view.findViewById(R.id.imageView);
								//LinearLayout m_topLayout = (LinearLayout) m_view.findViewById(R.id.sp_color);
								//LinearLayout m_bottomLayout = (LinearLayout) m_view.findViewById(R.id.sp_linh);
								// final RelativeLayout myRelView = new RelativeLayout(this);
								LinearLayout picContainer = (LinearLayout) m_view.findViewById(R.id.picContainer);
								LinearLayout mainLayout = (LinearLayout) m_view.findViewById(R.id.MainLayout);
								//m_view.setLayoutParams(new LayoutParams((windowwidth - 80), 450));
								m_view.setLayoutParams(new LayoutParams(windowwidth - 90, android.app.ActionBar.LayoutParams.MATCH_PARENT));
								m_view.setX(40);
								System.out.println("initial x" + m_view.getX());
								m_view.setY(40);
								m_view.setTag(i);
								
								// initialize image
								ParseImageView image = (ParseImageView) m_view.findViewById(R.id.imageView);
								TextView dishName = (TextView) m_view.findViewById(R.id.dishName);
								TextView price = (TextView) m_view.findViewById(R.id.price);
								image.setParseFile(bruh.getPic());
								image.loadInBackground();
								dishName.setText(bruh.getDishName());
								price.setText(String.valueOf(bruh.getPrice()));				
			
								// ADD dynamically like button on image.
			//					final Button imageLike = new Button(m_context);
			//					imageLike.setLayoutParams(new LayoutParams(100, 50));
			//					imageLike.setBackground(getResources().getDrawable(R.drawable.like));
			//					imageLike.setX(20);
			//					imageLike.setY(-250);
			//					imageLike.setAlpha(alphaValue);
			//					m_topLayout.addView(imageLike);
					//
			//					// ADD dynamically dislike button on image.
			//					final Button imagePass = new Button(m_context);
			//					imagePass.setLayoutParams(new LayoutParams(100, 50));
			//					imagePass.setBackground(getResources().getDrawable(R.drawable.dislike));
			//					imagePass.setX(260);
			//					imagePass.setY(-300);
			//					imagePass.setAlpha(alphaValue);
			//					m_topLayout.addView(imagePass);
			
								// Click listener on the bottom layout to open the details of the
								// image.
			//					m_bottomLayout.setOnClickListener(new OnClickListener() {
			//
			//						@Override
			//						public void onClick(View v) {
			//							startActivity(new Intent(m_context, DetailsActivity.class));
			//
			//						}
			//					});
			
								// Touch listener on the image layout to swipe image right or left.
								//used to be m_topLayout, ~ picLayout
								mainLayout.setOnTouchListener(new OnTouchListener() {
			
									@Override
									public boolean onTouch(View v, MotionEvent event) {
										x_cord = (int) event.getRawX();
										y_cord = (int) event.getRawY();
			
										//the auto moving of pics upon contact
										//m_view.setX(x_cord - screenCenter + 40);
										//m_view.setY(y_cord - 150);
										switch (event.getAction()) {
										case MotionEvent.ACTION_DOWN:
											x = (int) event.getX();
											//y = (int) event.getY();
											Log.v("On touch", x + " " + y);
											break;
										case MotionEvent.ACTION_MOVE:
											x_cord = (int) event.getRawX(); // Updated for more
																			// smoother animation.
											//y_cord = (int) event.getRawY();
											m_view.setX(x_cord - x);
											//m_view.setY(y_cord - y);									
											// m_view.setY(y_cord-y);﻿
											// y_cord = (int) event.getRawY();
											// m_view.setX(x_cord - screenCenter + 40);
											// m_view.setY(y_cord - 150);
											if (x_cord >= screenCenter) {
												//rotate
												//m_view.setRotation((float) ((x_cord - screenCenter) * (Math.PI / 128)));
												//like icon overlay
												if (x_cord > (screenCenter + (screenCenter / 2))) {
													//imageLike.setAlpha(1);
													if (x_cord > (windowwidth - (screenCenter / 4))) {
														Likes = 2;
													} else {
														Likes = 0;
													}
												} else {
													Likes = 0;
													//imageLike.setAlpha(0);
												}
												//imagePass.setAlpha(0);
											} else {
												// rotate
												//m_view.setRotation((float) ((x_cord - screenCenter) * (Math.PI / 128)));
												//dislike icon overlay
												if (x_cord < (screenCenter / 2)) {
													//imagePass.setAlpha(1);
													if (x_cord < screenCenter / 4) {
														Likes = 1;
													} else {
														Likes = 0;
													}
												} else {
													Likes = 0;
													//imagePass.setAlpha(0);
												}
												//imageLike.setAlpha(0);
											}
			
											break;
										case MotionEvent.ACTION_UP:
											x_cord = (int) event.getRawX();
											//y_cord = (int) event.getRawY();
			
											Log.e("X Point", "" + x_cord + " , Y " + y_cord);
			//								imagePass.setAlpha(0);
			//								imageLike.setAlpha(0);
			
											if (Likes == 0) {
												 Log.e("Event Status", "Nothing");
												//set layout params was added
												//m_view.setLayoutParams(new LayoutParams((windowwidth - 80), 450));
												m_view.setX(40);
												//m_view.setY(40);
												System.out.println("repsisiotned x: " + m_view.getX());
												//m_view.setRotation(0);
											} else if (Likes == 1) {
												 Log.e("Event Status", "Rejected");	
												 bruh.increment("dislikes");
												 bruh.saveInBackground();
												 parentView.removeView(m_view);
											} else if (Likes == 2) {
												 Log.e("Event Status", "Liked");
												 ParseUser.getCurrentUser().add("dishesLiked", bruh.getObjectId());
												 System.out.println(bruh.getObjectId());
												 ParseUser.getCurrentUser().saveInBackground();
												 bruh.increment("likes");
												 bruh.saveInBackground();
												 parentView.removeView(m_view);
											}
											break;
										default:
											break;
										}
										return true;
									}
								});
			
								parentView.addView(m_view);
			
							}
						} else {
							 System.out.println("parse error, probably: " + arg1.getMessage());
							 spinny.setVisibility(View.GONE);
							 loading.setText("Could not load foodz :(");
						}
					}
				});	
					
			}
		});
		System.out.println("Did it get out?");
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.swipe, menu);
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
