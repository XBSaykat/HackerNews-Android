package com.whoisryannystrom.hackernews;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.os.Message;
import android.os.Messenger;
import android.os.Handler;

public class FrontPageActivity extends ListActivity {
	Button refresh;
	Handler handler = new Handler() {
		public void handleMessage(Message message) {
			if (message.arg1 == HackerNewsService.RESULT_SUCCESS) {
				fillData();
			}
			else {
				handleError();
			}
		}
	};
	private NewsItemsDbAdapter mDbHelper;
	private final static int ACTIVITY_WEB_VIEW = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.front_page);
		
		mDbHelper = new NewsItemsDbAdapter(this);
        mDbHelper.open();
		
		refresh = (Button)findViewById(R.id.refresh_button);
		View.OnClickListener refreshHandler = (new View.OnClickListener() {
			public void onClick(View v) {
				refreshData();
			}
		});
		refresh.setOnClickListener(refreshHandler);
		
		fillData();
		
		registerForContextMenu(getListView());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.front_page, menu);
		return true;
	}
	
	private void fillData() {
		// Get all of the rows from the database and create the item list
        Cursor itemsCursor = mDbHelper.fetchAllNotes();
        startManagingCursor(itemsCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{
        		NewsItemsDbAdapter.KEY_TITLE,
        		NewsItemsDbAdapter.KEY_POSTEDBY,
        		NewsItemsDbAdapter.KEY_POSTEDAGO
        		};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{
        		R.id.news_title,
        		R.id.posted_by,
        		R.id.posted_ago
        		};

        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter items = new SimpleCursorAdapter(this, R.layout.news_item, itemsCursor, from, to);
        setListAdapter(items);
	}
	
	private void refreshData() {
		Intent intent = new Intent(this, HackerNewsService.class);
		Messenger messenger = new Messenger(handler);
		intent.putExtra(HackerNewsService.MESSENGER_IDENTIFIER, messenger);
		startService(intent);
	}
	
	public void handleError() {
		Toast.makeText(this, "Problem loading news.", Toast.LENGTH_LONG).show();
	}
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, WebViewActivity.class);
        i.putExtra(NewsItemsDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_WEB_VIEW);
    }

}
