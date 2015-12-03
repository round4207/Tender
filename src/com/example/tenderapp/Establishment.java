package com.example.tenderapp;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@ParseClassName("Establishment")
public class Establishment extends ParseObject {
	
	
	public void setPic(ParseFile pic)
	{
		put("pic", pic);
	}
	
	public ParseFile getPic()
	{
		return getParseFile("pic");
	}

	public void setThumbNail(ParseFile pic)
	{
		put("thumbnail", pic);
	}
	
	public ParseFile getThumbNail()
	{
		return getParseFile("thumbnail");
	}	

	public void setName(String name)
	{
		put("name", name);
	}
	
	public String getName()
	{
		return getString("name");
	}
	
	public void setContact(String contact)
	{
		put("contact", contact);
	}
	
	public String getContact()
	{
		return getString("contact");
	}
	
	public void setLocation(ParseGeoPoint location)
	{
		put("location", location);
	}
	
	public ParseGeoPoint getLocation()
	{
		return getParseGeoPoint("location");
	}
}
