package cs499app.cs499mobileapp.model;

/**
 * Created by centa on 7/6/2017.
 */

public class StationRecord {

    private long _ID;



    private String playlistTitle;
    private String stationTitle;
    private String stationURL;
    private String stationHash;

    public StationRecord(long _ID, String playlistTitle, String stationTitle, String stationURL, String stationHash) {
        this._ID = _ID;
        this.playlistTitle = playlistTitle;
        this.stationTitle = stationTitle;
        this.stationURL = stationURL;
        this.stationHash = stationHash;
    }

    public StationRecord( String playlistTitle, String stationTitle, String stationURL) {
        this._ID = -1;
        this.playlistTitle = playlistTitle;
        this.stationTitle = stationTitle;
        this.stationURL = stationURL;
        this.stationHash = "";
    }

    public StationRecord() {
        this._ID = -1;
        this.playlistTitle = "";
        this.stationTitle = "";
        this.stationURL = "";
        this.stationHash = "";
    }
    public String getPlaylistTitle() {
        return playlistTitle;
    }

    public void setPlaylistTitle(String playlistTitle) {
        this.playlistTitle = playlistTitle;
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
