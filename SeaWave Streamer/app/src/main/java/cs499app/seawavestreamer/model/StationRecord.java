package cs499app.seawavestreamer.model;

/**
 * Created by centa on 7/6/2017.
 */

public class StationRecord {

    private long _ID;
    private long playlistID;
    private String stationTitle;
    private String stationURL;
    private String stationHash;

    public StationRecord(long _ID, long playlistID, String stationTitle, String stationURL, String stationHash) {
        this._ID = _ID;
        this.playlistID = playlistID;
        this.stationTitle = stationTitle;
        this.stationURL = stationURL;
        this.stationHash = stationHash;
    }

    public StationRecord( long playlistID, String stationTitle, String stationURL) {
        this._ID = -1;
        this.playlistID = playlistID;
        this.stationTitle = stationTitle;
        this.stationURL = stationURL;
        this.stationHash = "";
    }

    public StationRecord() {
        this._ID = -1;
        this.playlistID = -1;
        this.stationTitle = "";
        this.stationURL = "";
        this.stationHash = "";
    }


    public long getPlaylistID() {
        return playlistID;
    }

    public void setPlaylistID(long playlistID) {
        this.playlistID = playlistID;
    }

    public String getStationTitle() {
        return stationTitle;
    }

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public void setStationTitle(String stationTitle) {
        this.stationTitle = stationTitle;
    }

    public String getStationURL() {
        return stationURL;
    }

    public void setStationURL(String stationURL) {
        this.stationURL = stationURL;
    }

    public String getStationHash() {
        return stationHash;
    }

    public void setStationHash(String stationHash) {
        this.stationHash = stationHash;
    }
}
