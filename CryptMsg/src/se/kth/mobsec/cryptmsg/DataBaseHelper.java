/**
 * 
 */
package se.kth.mobsec.cryptmsg;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Jasper
 * 
 */
public class DataBaseHelper extends SQLiteOpenHelper {

	private static DataBaseHelper helper = null;

	private static final String DATABASE_NAME = "CaaSMessages.db";

	private static final int DATABASE_VERSION = 2;
	private static final String DECRYPTEDMSG_TABLE_NAME = "decryptedMsgs";
	private static final String SENTMSG_TABLE_NAME = "sentMsgs";
	private static final String DECRYPTEDMSG_TABLE_CREATE = "CREATE TABLE "
			+ DECRYPTEDMSG_TABLE_NAME
			+ " ( id INTEGER PRIMARY KEY AUTOINCREMENT, phoneNo TEXT NOT NULL, msg TEXT NOT NULL);";

	private static final String SENTMSG_TABLE_CREATE = "CREATE TABLE "
			+ SENTMSG_TABLE_NAME
			+ " ( id INTEGER PRIMARY KEY AUTOINCREMENT, phoneNo TEXT NOT NULL, msg TEXT NOT NULL);";

	DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		helper = this;
	}

	/**
	 * Called on creation of the data base. Creates a Table for received
	 * (decrypted) messages and one for sent messages.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DECRYPTEDMSG_TABLE_CREATE);
		db.execSQL(SENTMSG_TABLE_CREATE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public void addRcvdMsg(String phoneNo, String msg) {
		getWritableDatabase().execSQL(
				"INSERT into " + DECRYPTEDMSG_TABLE_NAME
						+ " (phoneNo, msg) VALUES ('" + phoneNo + "', '" + msg
						+ ");");
	}

	public void addSentMsg(String phoneNo, String msg) {
		getWritableDatabase().execSQL(
				"INSERT into " + SENTMSG_TABLE_NAME
						+ " (phoneNo, msg) VALUES ('" + phoneNo + "', '" + msg
						+ ");");
		close();
	}

	/**
	 * Retirves all stored CaaS messages. Currently that is all received
	 * messages in chronological order followed by sent messages in
	 * chronological order.
	 * 
	 * @return
	 */
	public ArrayList<String> getAllMsgs() {
		ArrayList<String> allMsgs = new ArrayList<String>();
		Cursor cursor = getReadableDatabase().query(DECRYPTEDMSG_TABLE_NAME,
				null, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String msg = cursorToString(cursor);
			allMsgs.add("decrypted from " + msg);
			cursor.moveToNext();
		}

		cursor = getReadableDatabase().query(SENTMSG_TABLE_NAME, null, null,
				null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String msg = cursorToString(cursor);
			allMsgs.add("Sent to " + msg);
			cursor.moveToNext();
		}

		// close the cursor and the data base
		cursor.close();
		close();
		return allMsgs;

	}

	/**
	 * Formats a Stored SMS
	 * 
	 * @param cursor
	 * @return formatted SMS as a Sttring
	 */
	private String cursorToString(Cursor cursor) {
		String msg = cursor.getString(1) + ":\n";
		msg += cursor.getString(2);
		return msg;
	}

	/**
	 * Retrieves the DataBaseHelper Object. This allows for the database to be
	 * used within any method of CaaSdroid without passing the object itself on
	 * every call.
	 * 
	 * @return
	 */
	public static DataBaseHelper getInstance() {
		return helper;
	}

}
