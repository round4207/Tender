package com.example.tenderapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;


public class MyParseApplication extends Application
{
	// set up the Parse.com keys here
	private String applicationId = "QUm8PK9g7jCPcu8mICqP2y755olChOMjCGNoqoMw";
	private String clientKey = "kXfti17TiOLK1RV9JWCz4hgqW4jPv3xZqRj1ZysL";
	
	public void onCreate()
	{
		super.onCreate();
		
		// add any of your subclasses here
		
		// initialize Parse here		
		Parse.initialize(this, applicationId, clientKey);
	}

}
