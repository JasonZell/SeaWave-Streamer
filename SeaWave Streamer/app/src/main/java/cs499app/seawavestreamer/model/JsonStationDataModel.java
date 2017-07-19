package cs499app.seawavestreamer.model;

/**
 * Created by centa on 7/18/2017.
 */

public class JsonStationDataModel {
    String stationName;
    String stationURL;

    public JsonStationDataModel(String stationName, String stationURL) {
        this.stationName = stationName;
        this.stationURL = stationURL;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationURL() {
        return stationURL;
    }

    public void setStationURL(String stationURL) {
        this.stationURL = stationURL;
    }
}
