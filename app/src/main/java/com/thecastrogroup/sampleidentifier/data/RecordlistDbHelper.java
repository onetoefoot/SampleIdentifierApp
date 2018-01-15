package com.thecastrogroup.sampleidentifier.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.thecastrogroup.sampleidentifier.data.RecordlistContract.RecordlistEntry;

/**
 * Created by jodycastro on 1/12/18.
 */

public class RecordlistDbHelper extends SQLiteOpenHelper {

    private static final String TAG = RecordlistDbHelper.class.getSimpleName();

    SQLiteDatabase sqLiteDatabase;

    private static final String DATABASE_NAME = "recordlist.db";
    private static final int DATABASE_VERSION = 10;

    public RecordlistDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_RECORDLIST_TABLE = "CREATE TABLE " +
                RecordlistEntry.TABLE_NAME + " (" +
                RecordlistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"  +
                RecordlistEntry.COLUMN_IDENTIFIER + " TEXT NOT NULL,"  +
                RecordlistEntry.COLUMN_SAMPLE + " TEXT NOT NULL,"  +
                RecordlistEntry.COLUMN_SESSION + " TEXT NOT NULL,"  +
                RecordlistEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"  +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_RECORDLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d(TAG, "Updating table from " + oldVersion + " to " + newVersion);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecordlistEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
        Log.d(TAG, "Update complete");
    }
}
