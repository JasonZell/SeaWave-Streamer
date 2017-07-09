package cs499app.cs499mobileapp.model;

/**
 * Created by centa on 6/29/2017.
 */

public class PlaylistRecord {

    private String playlistName;
    private int _ID;

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public PlaylistRecord(String playlistName, int _ID) {
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
