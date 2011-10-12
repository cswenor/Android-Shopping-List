package bu.homework.shoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ItemsModel {
	public static final String KEY_NAME = "name";
    public static final String KEY_PRICE = "price";
    public static final String KEY_PRIORITY = "priority";
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "ItemsModel";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
        "create table items (_id integer primary key autoincrement, "
        + "name text not null, price text not null, priority INTEGER not null);";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "items";
    private static final int DATABASE_VERSION = 7;

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
            db.execSQL("DROP TABLE IF EXISTS items");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public ItemsModel(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public ItemsModel open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param title the title of the note
     * @param body the body of the note
     * @return rowId or -1 if failed
     */
    public long createItem(String name, String price, int priority) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_PRICE, price);
        initialValues.put(KEY_PRIORITY, priority);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the note with the given rowId
     * 
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteItem(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all notes in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAllItems() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,
                KEY_PRICE, KEY_PRIORITY}, null, null, null, null, KEY_PRIORITY);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchItem(long rowId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                    KEY_NAME, KEY_PRICE, KEY_PRIORITY}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     * 
     * @param rowId id of note to update
     * @param title value to set note title to
     * @param body value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateItem(long rowId, String name, String price, int priority) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_PRICE, price);
        args.put(KEY_PRIORITY, priority);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public int getHighestPriority(){
    	int highPriority = 0;
    	Cursor mCursor = mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,
                KEY_PRICE, KEY_PRIORITY}, null, null, null, null, KEY_PRIORITY);
    	if (mCursor.getCount() > 0) {
    		mCursor.moveToLast();
    		highPriority = mCursor.getInt(mCursor.getColumnIndexOrThrow(KEY_PRIORITY)) + 1;
    		
        }

    	return highPriority;
    }
    
    public void adjustPriority(int oldPriority, int newPriority) {
    	
    	Cursor itemCursor = this.fetchAllItems();
	    itemCursor.moveToFirst();
	    int cursRowID = -100;
	    int i = 0;
	    int p = 0;
	    String cursName = new String();
	    String cursPrice = new String();
	    
		while (itemCursor.isAfterLast() == false) {
			cursRowID = itemCursor.getInt(itemCursor.getColumnIndexOrThrow(ItemsModel.KEY_ROWID));
			cursName = itemCursor.getString(itemCursor.getColumnIndexOrThrow(ItemsModel.KEY_NAME));
			cursPrice = itemCursor.getString(itemCursor.getColumnIndexOrThrow(ItemsModel.KEY_PRICE));
			
			if( i == oldPriority ) {
				this.updateItem(cursRowID, cursName, cursPrice, newPriority);
			} else {
				if( p == newPriority){
					p++;
				}
				this.updateItem(cursRowID, cursName, cursPrice, p);
				p++;
			}
			
			itemCursor.moveToNext();
			i++;
        }
    	
    }
}
