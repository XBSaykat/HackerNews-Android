package com.whoisryannystrom.hackernews;

import android.widget.ArrayAdapter;
import android.content.Context;
import java.util.ArrayList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NewsItemAdapter extends ArrayAdapter<NewsItem> {
	public NewsItemAdapter(Context context, int textViewResourceId, ArrayList<NewsItem> items) {
		super(context, textViewResourceId, items);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		
		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(this.getContext());
            
            row = inflater.inflate(R.layout.news_item,null);
		}
		
		TextView label = (TextView)row.findViewById(R.id.news_title);
		NewsItem item = (NewsItem)this.getItem(position);
		label.setText(item.getTitle());
		
		return row;
	}
}
