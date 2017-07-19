package cs499app.downwavestreamer.model;

/**
 * Created by centa on 6/29/2017.
 */

public class PlaylistRecord {

    private String playlistName;
    private long _ID;

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public PlaylistRecord(String playlistName, long _ID) {
        this.playlistName = playlistName;
        this._ID = _ID;
    }

    public PlaylistRecord(){
//        playlistName = "";
//        _ID = -1;
    }
    public PlaylistRecord(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }
}
