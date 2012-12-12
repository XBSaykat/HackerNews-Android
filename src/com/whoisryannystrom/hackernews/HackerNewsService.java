package com.whoisryannystrom.hackernews;

import android.app.IntentService;
import android.app.backup.RestoreObserver;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.Bundle;
import android.util.Log;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.StatusLine;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class HackerNewsService extends IntentService {
	static public String MESSENGER_IDENTIFIER = "HACKER_NEWS_SERVICE_MESSENGER";
	static public int RESULT_SUCCESS = 0;
	static public int RESULT_ERROR = 1;
	
	private String HN_URI = "http://api.ihackernews.com/page?format=json";
	private Messenger messenger = null;
	
	private NewsItemsDbAdapter mDbHelper;

	public HackerNewsService() {
		super("HackerNewsService");
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// assign messenger
		Bundle extras = intent.getExtras();
		if (extras != null) {
			messenger = (Messenger) extras.get(MESSENGER_IDENTIFIER);
		}
		
		try {
			HttpClient httpclient = new DefaultHttpClient();
		    HttpResponse response = httpclient.execute(new HttpGet(HN_URI));
		    StatusLine statusLine = response.getStatusLine();
		    
		    Log.d("HTTP Response", Integer.toString(statusLine.getStatusCode()));
		    
		    if(statusLine.getStatusCode() == 200){
		        ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        String responseString = out.toString();
		        
		        Log.d("Raw response", responseString);
		        
		        requestDidComplete(responseString);
		    } else{
		        //Closes the connection.
		        response.getEntity().getContent().close();
		        throw new IOException(statusLine.getReasonPhrase());
		    }
		} catch (ClientProtocolException e) {
			requestDidFail(e);
        } catch (IOException e) {
            requestDidFail(e);
        }
		
	}
	
	private void parseAndSaveItemsFromJSON(String jsonString) {
		try {
			JSONObject jRoot = new JSONObject(jsonString);
			JSONArray jArray = jRoot.getJSONArray("items");
			for (int i = 0; i < jArray.length(); i++) {
				parseAndSaveItemFromJSON(jArray.getJSONObject(i));
			}
		}
		catch (JSONException exception) {
			Log.d("Parsing list error", exception.toString());
		}
	}
	
	private void parseAndSaveItemFromJSON(JSONObject jObject) {
		try {
			long commentCount = jObject.getLong("commentCount");
			long id = jObject.getLong("id");
//			long points = jObject.getLong("points");
			String postedAgo = jObject.getString("postedAgo");
			String postedBy = jObject.getString("postedBy");
			String title = jObject.getString("title");
			String url = jObject.getString("url");
			
			if (mDbHelper != null) {
				mDbHelper.createItem(title, id, commentCount, postedBy, postedAgo, url);
			}
		}
		catch (JSONException jsonException) {
			Log.d("Parsing item error", jsonException.toString());
		}
	}
	
	private void requestDidComplete(String jsonString) {
		mDbHelper = new NewsItemsDbAdapter(this);
        mDbHelper.open();
        
        parseAndSaveItemsFromJSON(jsonString);
        
        mDbHelper.close();
		
		Message message = Message.obtain();
		message.arg1 = RESULT_SUCCESS;
		sendMessage(message);
	}
	
	private void requestDidFail(Exception exception) {
		Message message = Message.obtain();
		message.arg1 = RESULT_ERROR;
		sendMessage(message);
	}
	
	private void sendMessage(Message message) {
		if (messenger != null) {
			try { messenger.send(message); }
			catch (android.os.RemoteException exception) {
				exception.printStackTrace();
			}
		}
	}

}
