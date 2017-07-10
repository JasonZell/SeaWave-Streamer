package cs499app.cs499mobileapp.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cs499app.cs499mobileapp.helper.DBHelper;

public class LibraryRecord implements LibraryRecordInterface{

    private static final String LOGTAG = "DATABASE_LOG";
    Context context;
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    List<PlaylistRecord> playlistRecords;
    HashMap<Long,List<StationRecord>> stationListRecordsMap;

    String[] playlistProjection = {
            RecordSchema.PlaylistEntry._ID,
            RecordSchema.PlaylistEntry.COLUMN_NAME_TITLE
    };

    String[] stationProjection = {
            RecordSchema.StationEntry._ID,
            RecordSchema.StationEntry.COLUMN_NAME_PLAYLISTID,
            RecordSchema.StationEntry.COLUMN_NAME_STATIONTITLE,
            RecordSchema.StationEntry.COLUMN_NAME_URL,
            RecordSchema.StationEntry.COLUMN_NAME_HASH
    };

    public LibraryRecord(Context context){
        this.context = context;
        dbhelper = new DBHelper(context);
        stationListRecordsMap = new HashMap<>();
        playlistRecords = new ArrayList<>();
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

    // Insert the playlistRecord into database, and return the playlistRecord with unique ROWID
    // from database, for that data entry.
    @Override
    public PlaylistRecord insertPlaylistRecord(PlaylistRecord pr)
    {
        openWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RecordSchema.PlaylistEntry.COLUMN_NAME_TITLE, pr.getPlaylistName());
        long result = database.insert(RecordSchema.PlaylistEntry.TABLE_NAME,null,values);
        if(result == -1)
            Log.e("LOGTAG","Error inserting new playlist record");
        else
        {
            pr.set_ID(result);
        }

        dbhelper.close();
        return pr;
    }



    // insert one station record 
    public StationRecord insertStationRecord(StationRecord sr)
    {
        openWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RecordSchema.StationEntry.COLUMN_NAME_PLAYLISTID, sr.getPlaylistID());
        values.put(RecordSchema.StationEntry.COLUMN_NAME_STATIONTITLE, sr.getStationTitle());
        values.put(RecordSchema.StationEntry.COLUMN_NAME_URL, sr.getStationURL());
        values.put(RecordSchema.StationEntry.COLUMN_NAME_HASH, sr.getStationHash());
        long result = database.insert(RecordSchema.StationEntry.TABLE_NAME, null, values);
        if(result == -1)
            Log.e("LOGTAG","Error inserting new station record");
        else
        {
            sr.set_ID(result);
        }
        dbhelper.close();
        return sr;
    }


    //import all playlist titles to library record
    public List<PlaylistRecord> importlPlaylistRecordList(){

        openReadableDatabase();
        List<PlaylistRecord> pr;
        if(playlistRecords == null)
            pr = playlistRecords = new ArrayList<>();
        else {
            playlistRecords.clear();
            pr = playlistRecords;
        }

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
        //playlistRecords = pr; // load into member variable
        dbhelper.close();
        return pr;
    }

    //import stations from one playlist into the library record.
    public List<StationRecord> importStationRecordList(Long playlistID){

        openReadableDatabase();
        List<StationRecord> sr = null;

        if(stationListRecordsMap == null) //should happen only during initialization of this class
        {
            Log.e("StationListRM","IS NULL, creating..");
            stationListRecordsMap = new HashMap<>();
        }

        sr = stationListRecordsMap.get(playlistID);

        if(sr == null) // if the station list is not previously loaded / not exist.
        {
            Log.e("SR is NULL","NULL");
            sr = new ArrayList<>();
        }
        else // if the list have some previous data, clear it.
        {
            sr.clear();
        }


        String selection = RecordSchema.StationEntry.COLUMN_NAME_PLAYLISTID + " = ?";
        String[] selectionArgs = {String.valueOf(playlistID)};
        Cursor cursor = database.query(RecordSchema.StationEntry.TABLE_NAME,stationProjection,
                selection,selectionArgs,null,null,null);
        Log.i(LOGTAG,"Station Returned " + cursor.getCount() + " rows.");

        if(cursor.getCount() > 0){
            while(cursor.moveToNext())
            {
                StationRecord sRecord = new StationRecord();
                sRecord.set_ID(cursor.getLong(cursor.getColumnIndex(RecordSchema.StationEntry._ID)));
                sRecord.setPlaylistID(cursor.getLong(cursor.getColumnIndex(RecordSchema.StationEntry.COLUMN_NAME_PLAYLISTID)));
                sRecord.setStationTitle(cursor.getString(cursor.getColumnIndex(RecordSchema.StationEntry.COLUMN_NAME_STATIONTITLE)));
                sRecord.setStationURL(cursor.getString(cursor.getColumnIndex(RecordSchema.StationEntry.COLUMN_NAME_URL)));
                sRecord.setStationHash(cursor.getString(cursor.getColumnIndex(RecordSchema.StationEntry.COLUMN_NAME_HASH)));
                sr.add(sRecord);
            }
        }
        stationListRecordsMap.put(playlistID,sr);
        dbhelper.close();
        return sr;
    }


    @Override
    public void updateStationRecord(StationRecord sr) {

    }

    @Override
    public void updatePlaylistRecord(PlaylistRecord sr) {

    }

    @Override
    public void deleteStationRecord(StationRecord sr) {

    }

    @Override
    public void deletePlaylistRecord(PlaylistRecord sr) {

    }

    public List<PlaylistRecord> getPlaylistRecords() {
        return playlistRecords;
    }

    public void setPlaylistRecords(List<PlaylistRecord> playlistRecords) {
        this.playlistRecords = playlistRecords;
    }

    public HashMap<Long, List<StationRecord>> getStationListRecordsMap() {
        return stationListRecordsMap;
    }

    public void setStationListRecordsMap(HashMap<Long, List<StationRecord>> stationListRecordsMap) {
        this.stationListRecordsMap = stationListRecordsMap;
    }

}