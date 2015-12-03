package com.example.tenderapp;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Dish")
public class Dish extends ParseObject {
	
	
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

	public void setDishName(String name)
	{
		put("name", name);
	}
	
	public String getDishName()
	{
		return getString("name");
	}
	
	public void setEstablishment(String estab)
	{
		put("establishment", estab);
	}
	
	public String getPlace()
	{
		return getString("place");
	}
	
	public void setPrice(double price)
	{
		put("price", price);
	}
	
	public double getPrice()
	{
		return getNumber("price").doubleValue();
	}
	
	public void setLikes(int likes)
	{
		put("likes", likes);
	}
	
	public int getLikes()
	{
		return getNumber("likes").intValue();
	}
	
	public void setDislikes(float dislikes)
	{
		put("dislikes", dislikes);
	}
	
	public double getDislikes()
	{
		return getNumber("dislikes").intValue();
	}

}
