package cs499app.cs499mobileapp.model;

import java.util.List;

/**
 * Created by centa on 7/6/2017.
 */

public interface LibraryRecordInterface {
    public List<PlaylistRecord> importPlaylistRecordList();
    public List<StationRecord> importStationRecordList(Long playlistID);
    public StationRecord insertStationRecord(StationRecord sr);
    public PlaylistRecord insertPlaylistRecord(PlaylistRecord pr);
    public void updateStationRecord(StationRecord sr);
    public void updatePlaylistRecord(PlaylistRecord sr);
    public void deleteStationRecord(StationRecord sr);
    public void deletePlaylistRecord(PlaylistRecord sr);
    public String backupRecordToJSON();
    public void restoreRecordFromJSON(String JSONString,boolean appendRecord);

}
