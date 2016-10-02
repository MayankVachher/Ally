package com.mayank13059.ally;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mayank Vachher (2013059) on 02/10/16.
 */

public class SqlDB {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_FILENAME = "fname";
    public static final String KEY_TYPE = "ftype";

    private static final String DATABASE_NAME = "AllyDB";

    private static final String TABLE_NAME = "fileDetails";
    private static final int DATABASE_VERSION = 1;

    private DbHelper mHelper;
    private final Context mContext;
    private SQLiteDatabase mDatabase_read;
    private SQLiteDatabase mDatabase_write;

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        //Set up database here
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                    //Column name     Type of variable
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_FILENAME + " TEXT NOT NULL, " +
                    KEY_TYPE + " TEXT NOT NULL);"
            );

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

    }

    public SqlDB(Context c) {
        mContext = c;
    }

    public SqlDB open() throws SQLException {
        mHelper = new DbHelper (mContext);
        mDatabase_write = mHelper.getWritableDatabase();
        mDatabase_read = mHelper.getReadableDatabase();
        return this;
    }

    public void close() {
        mHelper.close();
    }

    public long createEntry(String fileName, String type) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_FILENAME, fileName);
        cv.put(KEY_TYPE, type);
        return mDatabase_write.insert(TABLE_NAME, null, cv);
    }

    public List<DBEntry> readAll() {

        List<DBEntry> fileList = new ArrayList<DBEntry>();

        String[] projection = {
                KEY_ROWID,
                KEY_FILENAME,
                KEY_TYPE
        };

        String sortOrder = KEY_ROWID;

        Cursor cursor = mDatabase_read.query(
                TABLE_NAME, // The table to query
                projection, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                sortOrder // The sort order
        );

        if (cursor.moveToFirst()) {
            do {
                DBEntry entry = new DBEntry();
                entry.ROW_ID = Integer.parseInt(cursor.getString(0));
                entry.FILENAME = cursor.getString(1);
                entry.TYPE = cursor.getString(2);
                fileList.add(entry);
            } while (cursor.moveToNext());
        }

        return fileList;
    }

    public void deleteEntry(String fileName) {
        mDatabase_write.delete(TABLE_NAME, KEY_FILENAME + " = ?",
                new String[] { fileName });
    }

    public void renameEntry(String toSearch, String toReplaceWith) {
        ContentValues values = new ContentValues();

        values.put(KEY_FILENAME, toReplaceWith);

        mDatabase_write.update(TABLE_NAME, values, KEY_FILENAME + " = ?",
                new String[] { toSearch });
    }
}
