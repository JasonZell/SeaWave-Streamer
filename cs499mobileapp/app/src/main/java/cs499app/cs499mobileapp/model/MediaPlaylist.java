package cs499app.cs499mobileapp.model;

/**
 * Created by centa on 6/29/2017.
 */

public class MediaPlaylist {

    private String playlistName;
    private int playlistImageRes;

    public MediaPlaylist(String playlistName, int playlistImageRes) {
        this.playlistName = playlistName;
        this.playlistImageRes = playlistImageRes;
    }

    public int getPlaylistImageRes() {
        return playlistImageRes;
    }

    public void setPlaylistImageRes(int playlistImageRes) {
        this.playlistImageRes = playlistImageRes;
    }



    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }
}
