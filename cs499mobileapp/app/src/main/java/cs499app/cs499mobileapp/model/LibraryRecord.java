package cs499app.cs499mobileapp.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cs499app.cs499mobileapp.helper.DBHelper;

public class LibraryRecord implements  LibraryRecordInterface{

    private static final String LOGTAG = "DATABASE_LOG";
    Context context;
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;
    List<PlaylistRecord> playlistRecords;
    HashMap<String,List<StationRecord>> stationListRecords;

    String[] playlistProjection = {
            RecordSchema.PlaylistEntry._ID,
            RecordSchema.PlaylistEntry.COLUMN_NAME_TITLE
    };

    String[] stationProjection = {
            RecordSchema.StationEntry._ID,
            RecordSchema.StationEntry.COLUMN_NAME_TITLE,
            RecordSchema.StationEntry.COLUMN_NAME_URL,
            RecordSchema.StationEntry.COLUMN_NAME_HASH
    };

    LibraryRecord(Context context){
        this.context = context;
        dbhelper = new DBHelper(context);
    }

    public void openReadableDatabase()
    {
        database = dbhelper.getReadableDatabase();
    }

    public void openWritableDatabase()
    {
        database = dbhelper.getWritableDatabase();
    }

    public void closeDatabase()
    {
        dbhelper.close();
    }

    public void insertPlaylistRecord(PlaylistRecord pr)
    {
        ContentValues values = new ContentValues();
        values.put(RecordSchema.PlaylistEntry.COLUMN_NAME_TITLE, pr.getPlaylistName());
        long result = database.insert(RecordSchema.PlaylistEntry.TABLE_NAME,null,values);
        if(result == -1)
            Log.e("LOGTAG","Error inserting new playlist record");
    }


    // insert one station record 
    public void inserStationRecord(StationRecord sr)
    {
        ContentValues values = new ContentValues();
        values.put(RecordSchema.StationEntry.COLUMN_NAME_TITLE, sr.getStationTitle());
        values.put(RecordSchema.StationEntry.COLUMN_NAME_URL, sr.getStationURL());
        values.put(RecordSchema.StationEntry.COLUMN_NAME_HASH, sr.getStationHash());
        long result = database.insert(RecordSchema.PlaylistEntry.TABLE_NAME,null,values);
        if(result == -1)
            Log.e("LOGTAG","Error inserting new playlist record");
    }

    //import all playlist titles to library record
    public List<PlaylistRecord> importlPlaylistRecordList(){

        List<PlaylistRecord> pr = new ArrayList<>();

        Cursor cursor = database.query(RecordSchema.PlaylistEntry.TABLE_NAME,playlistProjection,
                null,null,null,null,null);
        Log.i(LOGTAG,"Playlist Returned " + cursor.getCount() + " rows.");

        if(cursor.getCount() > 0){
            while(cursor.moveToNext())
            {
                PlaylistRecord pRecord = new PlaylistRecord();
                pRecord.set_ID(cursor.getLong(cursor.getColumnIndex(RecordSchema.PlaylistEntry._ID)));
                pRecord.setPlaylistName(cursor.getString(cursor.getColumnIndex(RecordSchema.PlaylistEntry.COLUMN_NAME_TITLE)));
                pr.add(pRecord);
            }
        }
        playlistRecords = pr; // load into member variable
        return pr;
    }

    //import stations from one playlist into the library record.
    public List<StationRecord> importStationRecordList(String playlistName){

        List<StationRecord> sr = new ArrayList<>();

        String selection = RecordSchema.StationEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = {playlistName};
        Cursor cursor = database.query(RecordSchema.StationEntry.TABLE_NAME,stationProjection,
                selection,selectionArgs,null,null,null);
        Log.i(LOGTAG,"Station Returned " + cursor.getCount() + " rows.");

        if(cursor.getCount() > 0){
            while(cursor.moveToNext())
            {
                StationRecord sRecord = new StationRecord();
                sRecord.set_ID(cursor.getLong(cursor.getColumnIndex(RecordSchema.StationEntry._ID)));
                sRecord.setStationTitle(cursor.getString(cursor.getColumnIndex(RecordSchema.StationEntry.COLUMN_NAME_TITLE)));
                sRecord.setStationURL(cursor.getString(cursor.getColumnIndex(RecordSchema.StationEntry.COLUMN_NAME_URL)));
                sRecord.setStationHash(cursor.getString(cursor.getColumnIndex(RecordSchema.StationEntry.COLUMN_NAME_HASH)));
                sr.add(sRecord);
            }
        }
        stationListRecords = new HashMap<>();
        stationListRecords.put(playlistName,sr);
        return sr;
    }

}