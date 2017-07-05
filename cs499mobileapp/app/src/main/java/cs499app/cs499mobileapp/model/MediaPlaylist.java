package cs499app.cs499mobileapp.model;

/**
 * Created by centa on 6/29/2017.
 */

public class MediaPlaylist {

    private String playlistName;

    public MediaPlaylist(String playlistName) {
        this.playlistName = playlistName;
    }



    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }
}
