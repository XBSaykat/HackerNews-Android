package com.whoisryannystrom.hackernews;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.Handler;

public class FrontPageActivity extends ListActivity {
	Button refresh;
	ArrayList<NewsItem> newsItems;
	Handler handler = new Handler() {
		public void handleMessage(Message message) {
			if (message.arg1 == HackerNewsService.RESULT_SUCCESS) {
				newsItemsDidLoad((ArrayList<NewsItem>)message.obj);
			}
			else {
				handleError();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.front_page);
		
		refresh = (Button)findViewById(R.id.refresh_button);
		View.OnClickListener refreshHandler = (new View.OnClickListener() {
			public void onClick(View v) {
				loadNewsItems();
			}
		});
		refresh.setOnClickListener(refreshHandler);
		
		loadNewsItems();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.front_page, menu);
		return true;
	}
	
	public void loadNewsItems() {
		if (newsItems == null) {
			newsItems = new ArrayList<NewsItem>();
		}
		else {
			newsItems.clear();
		}
		
		Intent intent = new Intent(this, HackerNewsService.class);
		Messenger messenger = new Messenger(handler);
		intent.putExtra(HackerNewsService.MESSENGER_IDENTIFIER, messenger);
		startService(intent);
	}
	
	public void newsItemsDidLoad(ArrayList<NewsItem> items) {
		newsItems = items;
		
		setListAdapter(new NewsItemAdapter(this,android.R.layout.simple_list_item_1,newsItems));
	}
	
	public void handleError() {
		Toast.makeText(this, "Problem loading news.", Toast.LENGTH_LONG).show();
	}

}
