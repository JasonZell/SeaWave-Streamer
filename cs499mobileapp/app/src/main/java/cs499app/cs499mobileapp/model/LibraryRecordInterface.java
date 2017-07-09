package cs499app.cs499mobileapp.model;

import java.util.List;

/**
 * Created by centa on 7/6/2017.
 */

public interface LibraryRecordInterface {

    public List<PlaylistRecord> importlPlaylistRecordList();
    public List<StationRecord> importStationRecordList(int playlistName);
    public void insertStationRecord(StationRecord sr);
    public void insertPlaylistRecord(PlaylistRecord pr);


}
