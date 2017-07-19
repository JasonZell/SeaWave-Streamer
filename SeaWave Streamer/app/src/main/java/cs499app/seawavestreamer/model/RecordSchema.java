package cs499app.seawavestreamer.model;

import android.provider.BaseColumns;

/**
 * Created by centa on 7/6/2017.
 */

public final class RecordSchema {

    private RecordSchema(){}

    public static class PlaylistEntry implements BaseColumns{

        public static final String TABLE_NAME = "PlaylistTable";
        public static final String COLUMN_NAME_TITLE="playlistTitle";
    }

    public static class StationEntry implements BaseColumns{

        public static final String TABLE_NAME = "StationTable";
        public static final String COLUMN_NAME_PLAYLISTID = "playlistID";
        public static final String COLUMN_NAME_STATIONTITLE="stationTitle";
        public static final String COLUMN_NAME_URL="url";
        public static final String COLUMN_NAME_HASH="hash";
    }
}
