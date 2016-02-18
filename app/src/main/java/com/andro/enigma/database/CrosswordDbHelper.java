package com.andro.enigma.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.andro.enigma.database.CrosswordContract.CrosswordEntry;

/**
 * Created by Andrija on 28-Aug-15.
 */
public class CrosswordDbHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENIGMA_TABLE =
            "CREATE TABLE " + CrosswordEntry.TABLE_NAME + " (" +
                    CrosswordEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    CrosswordEntry.COLUMN_NAME_CROSSWORD_NUMBER + " INTEGER " + COMMA_SEP +
                    CrosswordEntry.COLUMN_NAME_PACKAGE_ID + " INTEGER " + COMMA_SEP +
                    CrosswordEntry.COLUMN_NAME_TEXT + TEXT_TYPE + COMMA_SEP +
                    CrosswordEntry.COLUMN_NAME_SOLVED + TEXT_TYPE + COMMA_SEP +
                    CrosswordEntry.COLUMN_NAME_TIME + TEXT_TYPE + COMMA_SEP +
                    CrosswordEntry.COLUMN_NAME_LOCALE +
                    " )";

    private static final String SQL_CREATE_PACKAGE_TABLE =
            "CREATE TABLE " + Package.TABLE_NAME + " ( " +
                    Package.COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " +
                    Package.COLUMN_NAME_TITLE + " TEXT, " +
                    Package.COLUMN_NAME_LANG + " TEXT, " +
                    Package.COLUMN_NAME_ID_TYPE + " TEXT, " +
                    Package.COLUMN_NAME_DATE_CREATED + " TEXT, " +
                    Package.COLUMN_NAME_ENIGMA_COUNT + " INTEGER, " +
                    Package.COLUMN_NAME_SOLVED_COUNT + " INTEGER, " +
                    Package.COLUMN_NAME_PURCHASED + " INTEGER, " +
                    Package.COLUMN_NAME_PRICE + " REAL )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CrosswordEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "CrosswordDB.db";

    public CrosswordDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENIGMA_TABLE);
        db.execSQL(SQL_CREATE_PACKAGE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long addCrossword(Crossword c) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Log.d("ADD", "ID " + c.ID + " packageID " + c.packageId);
        ContentValues values = new ContentValues();
        values.put(CrosswordEntry.COLUMN_NAME_ID, c.ID);
        values.put(CrosswordEntry.COLUMN_NAME_CROSSWORD_NUMBER, c.crosswordNumber);
        values.put(CrosswordEntry.COLUMN_NAME_PACKAGE_ID, c.packageId);
        values.put(CrosswordEntry.COLUMN_NAME_TEXT, c.text);
        values.put(CrosswordEntry.COLUMN_NAME_SOLVED, c.solved);
        values.put(CrosswordEntry.COLUMN_NAME_TIME, c.time);
        values.put(CrosswordEntry.COLUMN_NAME_LOCALE, c.locale);

        long newRowId = -1;

        try {
            newRowId = db.insertOrThrow(
                    CrosswordContract.CrosswordEntry.TABLE_NAME,
                    null,
                    values);
        }catch(SQLException ex){
            Log.d("MYTAG","SQL Insert Exception " + ex.getMessage());
        }

        db.close();
        return newRowId;
    }

    public long addPackage(Package p) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Package.COLUMN_NAME_ID, p.getId());
        values.put(Package.COLUMN_NAME_TITLE, p.getTitle());
        values.put(Package.COLUMN_NAME_LANG, p.getLang());
        values.put(Package.COLUMN_NAME_ID_TYPE, p.getIdType());
        values.put(Package.COLUMN_NAME_DATE_CREATED, p.getDateCreated());
        values.put(Package.COLUMN_NAME_ENIGMA_COUNT, p.getEnigmaCount());
        values.put(Package.COLUMN_NAME_SOLVED_COUNT, p.getSolvedCount());
        values.put(Package.COLUMN_NAME_PURCHASED, p.getPurchased());
        values.put(Package.COLUMN_NAME_PRICE, p.getPrice());

        long newRowId;
        newRowId = db.insert(
                Package.TABLE_NAME,
                null,
                values);

        db.close();
        return newRowId;
    }

    /**
     *
     * @param locale
     * @return returns all localized enigmas
     */
    public Cursor getAllCrosswords(String locale) {

        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                CrosswordEntry.COLUMN_NAME_ID,
                CrosswordEntry.COLUMN_NAME_CROSSWORD_NUMBER,
                CrosswordEntry.COLUMN_NAME_PACKAGE_ID,
                CrosswordEntry.COLUMN_NAME_TEXT,
                CrosswordEntry.COLUMN_NAME_SOLVED,
                CrosswordEntry.COLUMN_NAME_TIME,
                CrosswordEntry.COLUMN_NAME_LOCALE
        };
        String selection = CrosswordEntry.COLUMN_NAME_LOCALE + " LIKE ? ";
        String[] selectionArgs = {
                locale
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                CrosswordEntry.COLUMN_NAME_CROSSWORD_NUMBER + " ASC";

        Cursor c = db.query(
                CrosswordEntry.TABLE_NAME,                          // The table to query
                projection,                                         // The columns to return
                selection,                                          // The columns for the WHERE clause
                selectionArgs,                                      // The values for the WHERE clause
                null,                                               // don't group the rows
                null,                                               // don't filter by row groups
                sortOrder                                           // The sort order
        );

        return c;
    }

    public Cursor getAllCrosswords(int packageId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM crosswords WHERE packageId = " + packageId, null);
        Log.d("MYTAG", "SELECT * FROM crosswords WHERE packageId = " + packageId);

        return c;
    }

    public Cursor getAllPackages(String locale, int type) {

        SQLiteDatabase db = this.getReadableDatabase();

        Log.d("MYTAG", "SELECT * FROM " + Package.TABLE_NAME + " WHERE " + Package.COLUMN_NAME_LANG + " LIKE '" + locale + "' AND " + Package.COLUMN_NAME_ID_TYPE + " = " + type);

        Cursor c = db.rawQuery("SELECT * FROM " + Package.TABLE_NAME + " WHERE " + Package.COLUMN_NAME_LANG + " LIKE '" + locale + "' AND " + Package.COLUMN_NAME_ID_TYPE + " = " + type, null);

        return c;
    }

    public long updateCrosswordTime(int crosswordId, String time, String locale) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CrosswordEntry.COLUMN_NAME_SOLVED, "YES");
        values.put(CrosswordEntry.COLUMN_NAME_TIME, time);

        long newRowId;
        newRowId = db.update(
                CrosswordContract.CrosswordEntry.TABLE_NAME,
                values,
                CrosswordEntry.COLUMN_NAME_ID + " = " + crosswordId,
                null);

        db.close();
        return newRowId;
    }


    public long updatePackageSolved(int packageId, int numberSolved) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Package.COLUMN_NAME_SOLVED_COUNT, numberSolved);

        long newRowId;
        newRowId = db.update(
                Package.TABLE_NAME,
                values,
                Package.COLUMN_NAME_ID + " = " + packageId,
                null);

        db.close();
        return newRowId;
    }

}
