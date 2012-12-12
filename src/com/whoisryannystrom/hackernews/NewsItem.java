package com.whoisryannystrom.hackernews;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import java.util.ArrayList;

public class NewsItem {
	private int _commentCount = 0;
	private int _id = 0;
	private int _points = 0;
	private String _postedAgo;
	private String _postedBy;
	private String _title;
	private String _url;
	
	public NewsItem(int commentCount, int id, int points, String postedAgo, String postedBy, String title, String url) {
		this._commentCount = commentCount;
		this._id = id;
		this._points = points;
		this._postedAgo = postedAgo;
		this._postedBy = postedBy;
		this._title = title;
		this._url = url;
	}
	
	static NewsItem loadFromJSON(JSONObject jObject) {
		try {
			int commentCount = jObject.getInt("commentCount");
			int id = jObject.getInt("id");
			int points = jObject.getInt("points");
			String postedAgo = jObject.getString("postedAgo");
			String postedBy = jObject.getString("postedBy");
			String title = jObject.getString("title");
			String url = jObject.getString("url");
			return new NewsItem(commentCount, id, points, postedAgo, postedBy, title, url);
		}
		catch (JSONException jsonException) {
			return null;
		}
	}
	
	static ArrayList<NewsItem> loadFromJSONArray(String jsonString) {
		ArrayList<NewsItem> list = new ArrayList<NewsItem>();
		
		/*
		 * JSON format:
		  {
		    "cachedOnUTC": "/Date(1355253416932)/", 
		    "items": [
		        {
		            "commentCount": 72, 
		            "id": 4905684, 
		            "points": 92, 
		            "postedAgo": "1 hour ago", 
		            "postedBy": "joecackler", 
		            "title": "23andMe raises $50M, cuts price to $99, sets goal of 1M genotyped customers", 
		            "url": "http://blog.23andme.com/news/one-million-strong-a-note-from-23andmes-anne-wojcicki/"
		        },
		        ...
	        }
		 */
		
		try {
			JSONObject jRoot = new JSONObject(jsonString);
			JSONArray jArray = jRoot.getJSONArray("items");
			for (int i = 0; i < jArray.length(); i++) {
				NewsItem item = NewsItem.loadFromJSON(jArray.getJSONObject(i));
				if (item != null) {
					list.add(item);
				}
			}
		}
		catch (JSONException exception) {
			// do nothing
		}
		
		return list;
	}
	
	/**********************************************************
	* Getters
	**********************************************************/
	public int getCommentCount() {
		return _commentCount;
	}
	
	public int getId() {
		return _id;
	}
	
	public int getPoints() { 
		return _points;
	}
	
	public String getPostedAgo() {
		return _postedAgo;
	}
	
	public String getPostedBy() {
		return _postedBy;
	}
	
	public String getTitle() {
		return _title;
	}
	
	public String getUrl() {
		return _url;
	}
	
	/**********************************************************
	* Setters
	**********************************************************/
	public void setCommentCount(int commentCount) {
		_commentCount = commentCount;
	}
	
	public void setId(int id) {
		_id = id;
	}
	
	public void setPoints(int points) {
		_points = points;
	}
	
	public void setPostedAgo(String postedAgo) {
		_postedAgo = postedAgo;
	}
	
	public void setPostedBy(String postedBy) {
		_postedBy = postedBy;
	}
	
	public void setTitle(String title) {
		_title = title;
	}
	
	public void setUrl(String url) {
		_url = url;
	}
}
