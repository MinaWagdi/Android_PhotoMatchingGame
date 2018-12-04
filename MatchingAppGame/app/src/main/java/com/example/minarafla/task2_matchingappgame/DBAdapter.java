package com.example.minarafla.task2_matchingappgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;




public class DBAdapter  {

    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /////////////////////////////////////////////////////////////////////
    // For logging:
    private static final String TAG = "DBAdapter";

    // DB Fields (Columns)
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;

    //THESE ARE THE DATABASE TABLE COLUMNS by Mina
    public static final String KEY_DATE = "Date";
    public static final int COL_DATE = 1;
    public static final String KEY_SCORE = "Score";
    public static final int COL_SCORE = 2;

    // [TO_DO_A4]
    // Update the ALL-KEYS string array
    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_DATE,KEY_SCORE};

    // [TO_DO_A5]
    // DB info: db name and table name.
    // this is the database name by Mina
    public static final String DATABASE_NAME = "MyDb";
    // this is the database table name by Mina
    public static final String DATABASE_TABLE = "mainTable";

    // [TO_DO_A6]
    // Track DB version
    public static final int DATABASE_VERSION = 1;


    // [TO_DO_A7]
    // DATABASE_CREATE SQL command
    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
                    + " ("
                    + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_DATE  + " text not null, "
                    + KEY_SCORE + " string not null"
                    +");";

    // Context of application who uses us.
    private final Context context;

    DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    // ==================
    //	Public methods:
    // ==================

    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {

        myDBHelper.close();
    }

    // Add a new set of values to the database.
    public long insertRow(String date, String score) {
        ContentValues initialValues = new ContentValues();
        Log.i("READs","the date that will be stored is "+date);
        Log.i("READs","the score that will be stored is "+score);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_SCORE, score);
        // Insert it into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    // Delete all records
    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // Return all rows in the database.
    public Cursor getAllRows() {
        String where = null;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Change an existing row to be equal to new data.
//    public boolean updateRow(long rowId, String name, int studentNum, String favColour) {
//        String where = KEY_ROWID + "=" + rowId;
//
//        // [TO_DO_A8]
//        // Update data in the row with new fields.
//        // Also change the function's arguments to be what you need!
//        // Create row's data:
//        ContentValues newValues = new ContentValues();
//        newValues.put(KEY_NAME, name);
//        newValues.put(KEY_STUDENTNUM, studentNum);
//        newValues.put(KEY_FAVCOLOUR, favColour);
//
//        // Insert it into the database.
//        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
//    }



    // ==================
    //	Private Helper Classes:
    // ==================

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            Log.i("DB","Database helper constructor is called");
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            Log.i("DB","onCreate in the DBAdapter is called");
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            //DATABASE_TABLE is the name of the table by Mina
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }
}

