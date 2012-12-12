package com.whoisryannystrom.hackernews;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NewsItemsDbAdapter {
	public static final String KEY_TITLE = "title";
	public static final String KEY_HNID = "hnid";
	public static final String KEY_COMMENTCOUNT = "comment_count";
	public static final String KEY_POSTEDBY = "posted_by";
	public static final String KEY_POSTEDAGO = "posted_ago";
	public static final String KEY_URL = "url";
	public static final String KEY_ROWID = "_id";
	public static final String[] PROJECTIONS = {
		KEY_ROWID, 
		KEY_TITLE,
        KEY_URL,
        KEY_POSTEDAGO,
        KEY_POSTEDBY,
        KEY_COMMENTCOUNT,
        KEY_HNID
	};
	
	private static final String TAG = "NewsItemsDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	
	private static final String DATABASE_NAME = "hackernews";
    private static final String DATABASE_TABLE = "newsitems";
    private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE =
			"CREATE TABLE " + DATABASE_TABLE + 
			"(" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			KEY_TITLE + " TEXT NOT NULL, " +
			KEY_HNID + " INTEGER NOT NULL, " +
			KEY_COMMENTCOUNT + " INTEGER NOT NULL, " +
			KEY_POSTEDBY + " TEXT NOT NULL, " +
			KEY_POSTEDAGO + " TEXT NOT NULL, " +
			KEY_URL + " TEXT NOT NULL)";

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }
    
    public NewsItemsDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public NewsItemsDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public long createItem(String title, long hnid, long commentCount, String postedBy, String postedAgo, String url) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_HNID, hnid);
        initialValues.put(KEY_COMMENTCOUNT, commentCount);
        initialValues.put(KEY_POSTEDBY, postedBy);
        initialValues.put(KEY_POSTEDAGO, postedAgo);
        initialValues.put(KEY_URL, url);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteNote(long rowId) {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor fetchAllNotes() {

        return mDb.query(DATABASE_TABLE, PROJECTIONS, null, null, null, null, "_id ASC", "30");
    }

    public Cursor fetchNote(long rowId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE, PROJECTIONS, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public boolean updateNote(long rowId, String title, long hnid, long commentCount, String postedBy, String postedAgo, String url) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_HNID, hnid);
        args.put(KEY_COMMENTCOUNT, commentCount);
        args.put(KEY_POSTEDBY, postedBy);
        args.put(KEY_POSTEDAGO, postedAgo);
        args.put(KEY_URL, url);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
