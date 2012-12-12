package com.whoisryannystrom.hackernews;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends Activity {
	WebView mWebView;
	private Long mRowId;
	private NewsItemsDbAdapter mDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDbHelper = new NewsItemsDbAdapter(this);
		mDbHelper.open();
		
		setContentView(R.layout.activity_web_view);
		
		mWebView = (WebView)findViewById(R.id.webview);
		
		mRowId = (savedInstanceState == null) ? null : (Long) savedInstanceState.getSerializable(NewsItemsDbAdapter.KEY_ROWID);
        if (mRowId == null) {
        	Bundle extras = getIntent().getExtras();
        	mRowId = extras != null ? extras.getLong(NewsItemsDbAdapter.KEY_ROWID) : null;
        }
        
        loadUrl();
	}
		
	private void loadUrl() {
		if (mRowId != null) {
			Cursor c = mDbHelper.fetchNote(mRowId);
			startManagingCursor(c);
			mWebView.loadUrl(c.getString(c.getColumnIndexOrThrow(NewsItemsDbAdapter.KEY_URL)));
		}
	}
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	outState.putSerializable(NewsItemsDbAdapter.KEY_ROWID, mRowId);
    }
  
    @Override
    protected void onResume() {
        super.onResume();
        loadUrl();
    }

}
