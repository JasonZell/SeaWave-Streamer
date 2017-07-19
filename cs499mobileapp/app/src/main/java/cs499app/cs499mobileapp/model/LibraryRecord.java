package cs499app.cs499mobileapp.model;
import cs499app.cs499mobileapp.MainActivity;
import cs499app.cs499mobileapp.Manifest;
import cs499app.cs499mobileapp.R;
import cs499app.cs499mobileapp.helper.DBHelper;
import cs499app.cs499mobileapp.helper.FileUtils;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.AlphabeticIndex;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;



public class LibraryRecord implements LibraryRecordInterface{

    private static final String LOGTAG = "DATABASE_LOG";
    private  File DB_FILEPATH;
    Context context;
    DBHelper dbhelper;
    SQLiteDatabase database;
    Gson gson;
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
        DB_FILEPATH = context.getDatabasePath(DBHelper.getDbName());
        gson = new Gson();
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
        insertPlaylistRecordQuery(pr);
        dbhelper.close();
        return pr;
    }

    public void insertPlaylistRecordQuery(PlaylistRecord pr)
    {
        ContentValues values = new ContentValues();
        values.put(RecordSchema.PlaylistEntry.COLUMN_NAME_TITLE, pr.getPlaylistName());
        long result = database.insert(RecordSchema.PlaylistEntry.TABLE_NAME,null,values);
        if(result == -1)
            Log.e("LOGTAG","Error inserting new playlist record");
        else
        {
            pr.set_ID(result);
        }
    }

    // insert one station record
    public StationRecord insertStationRecord(StationRecord sr)
    {
        openWritableDatabase();
        insertStationRecordQuery(sr);
        dbhelper.close();
        return sr;
    }

    public void insertStationRecordQuery(StationRecord sr)
    {
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
    }


    //import all playlist titles to library record
    public List<PlaylistRecord> importPlaylistRecordList(){

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
        importStationRecordQuery(sr,playlistID);
        dbhelper.close();
        return sr;
    }

    public List<StationRecord> importStationRecordQuery(List<StationRecord> sr,Long playlistID)
    {
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

        getStationRecord(sr,playlistID);
        stationListRecordsMap.put(playlistID,sr);
        return sr;
    }

    public void getStationRecord(List<StationRecord> sr, long playlistID)
    {
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
    }


    @Override
    public void updateStationRecord(StationRecord sr) {
        //only need to update database
        openReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(RecordSchema.StationEntry.COLUMN_NAME_STATIONTITLE,sr.getStationTitle());
        values.put(RecordSchema.StationEntry.COLUMN_NAME_URL,sr.getStationURL());
        String selection = RecordSchema.StationEntry._ID + " = ?";
        String[] selectionArg = {String.valueOf(sr.get_ID())};
        int count = database.update(
                RecordSchema.StationEntry.TABLE_NAME,
                values,
                selection,
                selectionArg);
        Log.i("Database","Updated "+count+"station column");
        dbhelper.close();
    }


    @Override
    public void updatePlaylistRecord(PlaylistRecord pr) {
        // update database ONLY
        openReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(RecordSchema.PlaylistEntry.COLUMN_NAME_TITLE,pr.getPlaylistName());
        String selection = RecordSchema.PlaylistEntry._ID + " = ?";
        String[] selectionArg = {String.valueOf(pr.get_ID())};
        int playlistcount = database.update(
                RecordSchema.PlaylistEntry.TABLE_NAME,
                values,
                selection,
                selectionArg);

//        values.clear();
//        values.put(RecordSchema.StationEntry.COLUMN_NAME_PLAYLISTID,pr.get_ID());
//        selection = RecordSchema.StationEntry.COLUMN_NAME_PLAYLISTID + " = ?";
//        selectionArg[0] = String.valueOf(pr.get_ID());
//        int stationCount = database.update(
//                RecordSchema.StationEntry.TABLE_NAME,
//                values,
//                selection,
//                selectionArg);

        Log.i("Database","Updated "+playlistcount+"playlist column");
        //Log.i("Database","Updated "+stationCount+"station column");

        dbhelper.close();

    }

    @Override
    public void deleteStationRecord(StationRecord sr) {
        openWritableDatabase();
        String selection = RecordSchema.StationEntry._ID + " = ?";
        String[] selectionArg = { String.valueOf(sr.get_ID())};
        int count = database.delete(RecordSchema.StationEntry.TABLE_NAME,selection,selectionArg);
        Log.i("Delete Station",count +" rows deleted");
        dbhelper.close();
    }

    @Override
    public void deletePlaylistRecord(PlaylistRecord pr) {
        //delete from database
        openWritableDatabase();
        String selection = RecordSchema.PlaylistEntry._ID + " = ?";
        String[] selectionArg = { String.valueOf(pr.get_ID())};
        int playlistCount = database.delete(RecordSchema.PlaylistEntry.TABLE_NAME,selection,selectionArg);

        selection = RecordSchema.StationEntry.COLUMN_NAME_PLAYLISTID + " = ?";
        selectionArg[0] = String.valueOf(pr.get_ID());
        int stationCount = database.delete(RecordSchema.StationEntry.TABLE_NAME,selection,selectionArg);
        Log.i("Delete playlist",playlistCount +" rows deleted");
        Log.i("Delete Stations",stationCount +" rows deleted");

        dbhelper.close();
        //delete from libRecord object
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


    //not tested yet
    public boolean importDatabase(String dbPath) throws IOException {

        // Close the SQLiteOpenHelper so it will commit the created empty
        // database to internal storage.
        dbhelper.close();
        File newDb = new File(dbPath);
        File oldDb = DB_FILEPATH;
        if (newDb.exists()) {
            FileUtils.copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
            // Access the copied database so SQLiteHelper will cache it and mark
            // it as created.
            dbhelper.getWritableDatabase().close();
            return true;
        }
        return false;
    }

    public static void exportDatabase(Context context, AppCompatActivity activity)
    {
        String DATABASE_NAME = context.getString(R.string.DATABASE_NAME);
        String databasePath = context.getDatabasePath(DATABASE_NAME).getPath();
        String inFileName = databasePath;

//
//        int permissionCheck = ContextCompat.checkSelfPermission(context,
//                "android.permission.WRITE_EXTERNAL_STORAGE");

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MainActivity.EXPORT_DB_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
        else {


            try {
                File dbFile = new File(inFileName);
                FileInputStream fis = new FileInputStream(dbFile);

                File f = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.ROOT_DIRECTORY_NAME)+"/Database");
                if (!f.exists()) {
                    f.mkdirs();
                }

                String outFileName = Environment.getExternalStorageDirectory()+"/"
                        + context.getString(R.string.ROOT_DIRECTORY_NAME)
                        + "/Database/" + context.getString(R.string.app_name_no_space) + ".db";

                OutputStream output = new FileOutputStream(outFileName);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }

                //Close the streams
                output.flush();
                output.close();
                fis.close();
                Toast.makeText(context, "Database exported Successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Error! Database export failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public String backupRecordToJSON() {

        //Collection playlistDMArray;
        List<JsonPlaylistDataModel> playlistDMArray;
        List<JsonStationDataModel>  stationDMArray;
        //Collection stationDMArray;
        List<StationRecord> sr = null;

        JsonStationDataModel sDM = null;
        int stationSize = 0;
        long playlistID;
        String JSONString = null;

        openReadableDatabase();

        int playlistSize = playlistRecords.size();
        playlistDMArray = new ArrayList<>();
        for(int i = 0; i < playlistSize;++i)
        {
            playlistID =  playlistRecords.get(i).get_ID();
            sr = importStationRecordQuery(sr,playlistID);

            stationDMArray = new ArrayList<>();

            if(sr != null) {
                Log.i("NOTNULLSR","INBACKUPRECORDJSON");
                for (int j = 0; j < sr.size(); ++j)  // for each stations in playlist
                {
                    stationDMArray.add(new JsonStationDataModel(
                            sr.get(j).getStationTitle(),
                            sr.get(j).getStationURL()));
                }
            }

            playlistDMArray.add(new JsonPlaylistDataModel(
                    playlistRecords.get(i).getPlaylistName(),
                    stationDMArray));
        }

        JSONString = gson.toJson(playlistDMArray);
        dbhelper.close();
        return JSONString;
    }

    @Override
    public void restoreRecordFromJSON(String JSONString, boolean appendRecord) {

        openWritableDatabase();

        if(appendRecord == false) // OVERRIDE EVERYTHING
        {
            database.execSQL("delete from "+ RecordSchema.PlaylistEntry.TABLE_NAME);
            database.execSQL("delete from "+ RecordSchema.StationEntry.TABLE_NAME);
            playlistRecords.clear();
            stationListRecordsMap.clear();
        }
        Type playlistmodelType = new TypeToken<List<JsonPlaylistDataModel>>() {}.getType();
        List<JsonPlaylistDataModel> jpDM = gson.fromJson(JSONString,playlistmodelType);

        for(int i = 0; i < jpDM.size();++i)
        {
            PlaylistRecord pr = new PlaylistRecord();
            pr.setPlaylistName(jpDM.get(i).getPlaylistName());
            insertPlaylistRecord(pr);
            playlistRecords.add(pr);

            List<JsonStationDataModel> jsonStationDM = jpDM.get(i).getDataModels();
            int stationSize = jsonStationDM.size();
            List<StationRecord> srl= new ArrayList<>();

            for(int sIndex = 0; sIndex < stationSize; ++sIndex)
            {
                StationRecord sr = new StationRecord(pr.get_ID(),
                        jsonStationDM.get(sIndex).getStationName(),
                        jsonStationDM.get(sIndex).getStationURL());

                insertStationRecord(sr);
                srl.add(sr);
            }
            stationListRecordsMap.put(pr.get_ID(),srl);
        }

        dbhelper.close();
    }


    public void importLibraryRecord(Context context, AppCompatActivity activity, String importFileName)
    {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MainActivity.IMPORT_LIBRARY_RECORD_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
        else {

            File f = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.ROOT_DIRECTORY_NAME)
                    +"/Library/"+importFileName+".json");

            if (!f.exists()) {
                Toast.makeText(context, "File Does Not Exists!", Toast.LENGTH_SHORT).show();
            }
            else
            {
               // Toast.makeText(context, "File Exists!", Toast.LENGTH_SHORT).show();
                int length = (int) f.length();

                byte[] bytes = new byte[length];

                FileInputStream in = null;
                try {
                    in = new FileInputStream(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    in.read(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                String contents = new String(bytes);
                //restoreRecordFromJSON(contents,true);
                Log.i("IMPORTJSON:",contents);

                restoreRecordFromJSON(contents,false);

            }

        }
    }

    public void exportLibraryRecord(Context context, AppCompatActivity activity, String exportFilename)
    {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MainActivity.EXPORT_LIBRARY_RECORD_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
        else {

            try {
                // File dbFile = new File(exportFilename);
                //FileInputStream fis = new FileInputStream(dbFile);

                File f = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.ROOT_DIRECTORY_NAME)+"/Library");
                if (!f.exists()) {
                    f.mkdirs();
                }

                String outFileName = Environment.getExternalStorageDirectory()+"/"
                        + context.getString(R.string.ROOT_DIRECTORY_NAME)
                        + "/Library/" + exportFilename + ".json";

                OutputStream output = new FileOutputStream(outFileName);
                output.write(backupRecordToJSON().getBytes());
//                byte[] buffer = new byte[1024];
//                int length;
//                while ((length = fis.read(buffer)) > 0) {
//                    output.write(buffer, 0, length);
//                }

                //Close the streams
                output.flush();
                output.close();
                //  fis.close();
                Toast.makeText(context, "Library exported Successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Error! Library export failed", Toast.LENGTH_SHORT).show();
            }
        }
    }


}