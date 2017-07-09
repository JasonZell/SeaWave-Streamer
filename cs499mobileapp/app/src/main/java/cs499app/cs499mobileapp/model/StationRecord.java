package cs499app.cs499mobileapp.model;

/**
 * Created by centa on 7/6/2017.
 */

public class StationRecord {

    private int _ID;
    private int playlistID;
    private String stationTitle;
    private String stationURL;
    private String stationHash;

    public StationRecord(int _ID, int playlistID, String stationTitle, String stationURL, String stationHash) {
        this._ID = _ID;
        this.playlistID = playlistID;
        this.stationTitle = stationTitle;
        this.stationURL = stationURL;
        this.stationHash = stationHash;
    }

    public StationRecord( int playlistID, String stationTitle, String stationURL) {
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


    public int getPlaylistID() {
        return playlistID;
    }

    public void setPlaylistID(int playlistID) {
        this.playlistID = playlistID;
    }

    public String getStationTitle() {
        return stationTitle;
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
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
