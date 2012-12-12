package com.whoisryannystrom.hackernews;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import java.util.ArrayList;

public class NewsItem {
	private int mCommentCount = 0;
	private int mHnId = 0;
	private int mPoints = 0;
	private String mPostedAgo;
	private String mPostedBy;
	private String mTitle;
	private String mUrl;
	private long mRowId;
	
	public NewsItem(int commentCount, int id, int points, String postedAgo, String postedBy, String title, String url) {
		this.mCommentCount = commentCount;
		this.mHnId = id;
		this.mPoints = points;
		this.mPostedAgo = postedAgo;
		this.mPostedBy = postedBy;
		this.mTitle = title;
		this.mUrl = url;
	}
	
	/**********************************************************
	* Getters
	**********************************************************/
	public int getCommentCount() {
		return mCommentCount;
	}
	
	public int getHnId() {
		return mHnId;
	}
	
	public int getPoints() { 
		return mPoints;
	}
	
	public String getPostedAgo() {
		return mPostedAgo;
	}
	
	public String getPostedBy() {
		return mPostedBy;
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public String getUrl() {
		return mUrl;
	}
	
	public long getRowId() {
		return mRowId;
	}
	
	/**********************************************************
	* Setters
	**********************************************************/
	public void setCommentCount(int commentCount) {
		mCommentCount = commentCount;
	}
	
	public void setId(int id) {
		mHnId = id;
	}
	
	public void setPoints(int points) {
		mPoints = points;
	}
	
	public void setPostedAgo(String postedAgo) {
		mPostedAgo = postedAgo;
	}
	
	public void setPostedBy(String postedBy) {
		mPostedBy = postedBy;
	}
	
	public void setTitle(String title) {
		mTitle = title;
	}
	
	public void setUrl(String url) {
		mUrl = url;
	}

}
