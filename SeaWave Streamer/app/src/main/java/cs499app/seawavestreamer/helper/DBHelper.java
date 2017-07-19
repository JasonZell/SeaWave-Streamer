package cs499app.seawavestreamer.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import cs499app.seawavestreamer.model.RecordSchema;

/**
 * Created by centa on 5/10/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RecordSchema.db";
    private static final int DATABASE_VERSION = 1;
    private static final String LOGTAG = "DATABASE_LOG";


    private static final String TABLE_PLAYLIST_CREATE =
            "CREATE TABLE " + RecordSchema.PlaylistEntry.TABLE_NAME + " (" +
                    RecordSchema.PlaylistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RecordSchema.PlaylistEntry.COLUMN_NAME_TITLE + " TEXT)";
                    ;

    private static final String TABLE_STATION_CREATE =
            "CREATE TABLE " + RecordSchema.StationEntry.TABLE_NAME + " (" +
                    RecordSchema.StationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RecordSchema.StationEntry.COLUMN_NAME_PLAYLISTID + " INTEGER , " +
                    RecordSchema.StationEntry.COLUMN_NAME_STATIONTITLE + " TEXT, " +
                    RecordSchema.StationEntry.COLUMN_NAME_URL + " TEXT, "+
                    RecordSchema.StationEntry.COLUMN_NAME_HASH + " TEXT)";
//                    "PRIMARY KEY ("+RecordSchema.StationEntry._ID + "," +
//                    RecordSchema.StationEntry.COLUMN_NAME_PLAYLISTID+")";
//                    "FOREIGN KEY (" + RecordSchema.StationEntry.COLUMN_NAME_PLAYLISTTITLE + ") REFERENCES " +
//                    RecordSchema.PlaylistEntry.TABLE_NAME + "(" + RecordSchema.PlaylistEntry.COLUMN_NAME_TITLE +") " +
//                    "ON UPDATE CASCADE ON DELETE CASCADE" +
//                    ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // only called by the app
    //
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_PLAYLIST_CREATE);
        db.execSQL(TABLE_STATION_CREATE);

        Log.i(LOGTAG,"Database Created.");
    }

    //only called by the app
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecordSchema.PlaylistEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RecordSchema.StationEntry.TABLE_NAME);
        onCreate(db);

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
        super.onConfigure(db);
    }

    public static String getDbName() {
        return DATABASE_NAME;
    }
}
