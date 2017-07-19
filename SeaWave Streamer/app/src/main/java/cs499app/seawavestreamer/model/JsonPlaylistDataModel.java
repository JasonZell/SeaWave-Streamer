package cs499app.seawavestreamer.model;

import java.util.List;

/**
 * Created by centa on 7/18/2017.
 */

public class JsonPlaylistDataModel {

    String playlistTitle;

    List<JsonStationDataModel> stationList;
    //Collection stationList;


    public JsonPlaylistDataModel(String playlistName, List<JsonStationDataModel> dataModels) {
        this.playlistTitle = playlistName;
        this.stationList = dataModels;
    }

    public String getPlaylistName() {
        return playlistTitle;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistTitle = playlistName;
    }

    public List<JsonStationDataModel> getDataModels() {
        return stationList;
    }

    public void setDataModels(List<JsonStationDataModel> dataModels) {
        this.stationList = dataModels;
    }

}
