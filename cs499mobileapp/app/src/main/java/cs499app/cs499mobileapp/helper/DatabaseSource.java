package cs499app.cs499mobileapp.helper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cs499app.cs499mobileapp.model.LibraryRecord;

/**
 * Created by centa on 5/10/2017.
 */

public class DatabaseSource {

    private static final String LOGTAG = "DATABASE_LOG";
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;


    public DatabaseSource(Context context)
    {
        dbhelper = new DBHelper(context);
    }

    public void open(){
        Log.i(LOGTAG, "Database opened.");
        database = dbhelper.getWritableDatabase();
    }

    public void close()
    {
        Log.i(LOGTAG, "Database closed.");
        dbhelper.close();
    }

    public LibraryRecord create(LibraryRecord libraryRecord)
    {
//        ContentValues values = new ContentValues();
//        values.put(DBHelper.COLUMN_BETNUMBER, libraryRecord.getNumber());
//        values.put(DBHelper.COLUMN_DATETIME, libraryRecord.getDateTime());
//        values.put(DBHelper.COLUMN_NAME, libraryRecord.getName());
//        values.put(DBHelper.COLUMN_MULTIPLIER, libraryRecord.getMultiplier());
//        values.put(DBHelper.COLUMN_NOTES, libraryRecord.getNote());

       // long insertid = database.insert(DBHelper.TABLE_NAME,null,values);
    //    libraryRecord.setId(insertid);
        return libraryRecord;
    }

//    public List<LibraryRecord> findAll(){
//
//        List<LibraryRecord> libraryRecords = new ArrayList<LibraryRecord>();
//
//        Cursor cursor = database.query(DBHelper.TABLE_NAME,allColumns,null,null,null,null,null);
//        Log.i(LOGTAG,"Returned " + cursor.getCount() + " rows.");
//
//        if(cursor.getCount() > 0){
//            while(cursor.moveToNext())
//            {
//                LibraryRecord libraryRecord = new LibraryRecord();
//                libraryRecord.setId(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ID)));
//                libraryRecord.setName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME)));
//                libraryRecord.setDateTime(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DATETIME)));
//                libraryRecord.setNumber(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BETNUMBER)));
//                libraryRecord.setMultiplier(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_MULTIPLIER)));
//                libraryRecord.setNote(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NOTES)));
//                libraryRecords.add(libraryRecord);
//            }
//        }
//        return libraryRecords;
//    }
}
