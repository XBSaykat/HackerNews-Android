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
import java.io.ByteArrayOutputStream;

public class HackerNewsService extends IntentService {
	static public String MESSENGER_IDENTIFIER = "HACKER_NEWS_SERVICE_MESSENGER";
	static public int RESULT_SUCCESS = 0;
	static public int RESULT_ERROR = 1;
	
	private String HN_URI = "http://api.ihackernews.com/page?format=json";
	private Messenger messenger = null;

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
		    if(statusLine.getStatusCode() == 200){
		        ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        String responseString = out.toString();
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
	
	private void requestDidComplete(String jsonString) {
		Message message = Message.obtain();
		message.obj = NewsItem.loadFromJSONArray(jsonString);
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
