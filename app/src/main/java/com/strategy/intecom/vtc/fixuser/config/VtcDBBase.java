package com.strategy.intecom.vtc.fixuser.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Mr. Ha on 25/05/2016.
 */

public class VtcDBBase {
	public static final String TAG = "VtcDBBase";

	public static final String DATABASE_NAME = "fixuser.db";
	public static final int DATABASE_VERSION = 1;

	private static SQLiteDatabase mDatabase = null;
	private static DatabaseHelper myDataHelPer = null;

	// 1-----------------------Table Search Address-------------------//

	public static final String id = "id";
	public static final String Search_distance = "distance";
	public static final String Search_address = "address";
	public static final String Search_description = "description";
	public static final String Search_longitude = "longitude";
	public static final String Search_latitude = "latitude";
	public static final String Search_isSave = "isSave";

    // favourite order save all json string of that order
    public static final String Fav_row_order_detail = "order_detail";
    public static final String Fav_row_order_id = "order_id";

	public static final String TABLE_NAME_HISTORY_SEARCH_ADDRESS = "search_address";
	public static final String TABLE_NAME_FAVOURITE = "favourite";

	private static final String DB_CREATE_TABLE_SEARCH_ADDRESS = "CREATE TABLE ["
			+ TABLE_NAME_HISTORY_SEARCH_ADDRESS + "](["
			+ id + "] INTEGER PRIMARY KEY AUTOINCREMENT, ["
			+ Search_distance + "] TEXT , ["
			+ Search_address + "] TEXT, ["
			+ Search_description + "] TEXT, ["
			+ Search_longitude + "] TEXT, ["
			+ Search_latitude + "] TEXT, ["
			+ Search_isSave + "] INTEGER );";

	public static class DatabaseHelper extends SQLiteOpenHelper {

		@Override
		public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_TABLE_SEARCH_ADDRESS);

			databaseManager.onCreate(db);
		}

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if(oldVersion < 0){
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_HISTORY_SEARCH_ADDRESS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FAVOURITE);
                AppCore.showLog("------------------- Version Database : " + oldVersion);

			}
			onCreate(db);

			databaseManager.onUpgrade(db, oldVersion, newVersion);
		}

		VtcDBBase databaseManager;
		private AtomicInteger counter = new AtomicInteger(0);

		public DatabaseHelper(Context context, String name, int version, VtcDBBase databaseManager) {
			super(context, name, null, version);
			this.databaseManager = databaseManager;
		}

		public void addConnection(){
			counter.incrementAndGet();
		}

		public void removeConnection(){
			counter.decrementAndGet();
		}

		public int getCounter() {
			return counter.get();
		}

		@Override
		public void onOpen(SQLiteDatabase db) {
			databaseManager.onOpen(db);
		}

		@Override
		public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			databaseManager.onDowngrade(db, oldVersion, newVersion);
		}

		@Override
		public void onConfigure(SQLiteDatabase db) {
			databaseManager.onConfigure(db);
		}
	}


	/**See SQLiteOpenHelper documentation
	 */
	public void onCreate(SQLiteDatabase db){

	}
	/**
	 * See SQLiteOpenHelper documentation
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/**Optional.
	 * *
	 */
	public void onOpen(SQLiteDatabase db){}
	/**Optional.
	 *
	 */
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
	/**Optional
	 *
	 */
	public void onConfigure(SQLiteDatabase db){}

	public synchronized SQLiteDatabase OpenDB() {
		synchronized (this){
			if(!isOpen()){
				myDataHelPer.addConnection();
				if (mDatabase == null|| !mDatabase.isOpen()){
					synchronized (lockObject){
						mDatabase = myDataHelPer.getWritableDatabase();
					}
				}
			}
			return getMyDatabase();
		}
	}

	private final ConcurrentHashMap<String,DatabaseHelper> dbMap = new ConcurrentHashMap<String, DatabaseHelper>();

	private final Object lockObject = new Object();

	public VtcDBBase(Context context) {

//		String dbPath = context.getApplicationContext().getDatabasePath(DATABASE_NAME).getPath();
		synchronized (lockObject) {
			myDataHelPer = dbMap.get(DATABASE_NAME);

			if (myDataHelPer == null) {

				myDataHelPer = new DatabaseHelper(context, DATABASE_NAME, DATABASE_VERSION, this);

				dbMap.put(DATABASE_NAME, myDataHelPer);
			}

			if (!isOpen())
				mDatabase = myDataHelPer.getWritableDatabase();
		}
	}

	public SQLiteDatabase getMyDatabase(){
		return mDatabase;
	}

	public boolean isOpen(){
		return (mDatabase != null && mDatabase.isOpen());
	}

	public boolean close(){
		myDataHelPer.removeConnection();

		if (myDataHelPer.getCounter() == 0){

			synchronized (lockObject){

				if (mDatabase.inTransaction())mDatabase.endTransaction();

				if (mDatabase.isOpen())mDatabase.close();

				mDatabase = null;

			}

			return true;
		}else {
			dbMap.remove(DATABASE_NAME);
		}
		return false;
	}

	@SuppressWarnings("serial")
	public static class SQLiteAssetException extends SQLiteException {

		public SQLiteAssetException() {}

		public SQLiteAssetException(String error) {
			super(error);
		}
	}
}