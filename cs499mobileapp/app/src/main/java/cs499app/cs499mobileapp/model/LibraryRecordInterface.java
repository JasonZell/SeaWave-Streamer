package cs499app.cs499mobileapp.model;

import java.util.List;

/**
 * Created by centa on 7/6/2017.
 */

public interface LibraryRecordInterface {

    public List<PlaylistRecord> importlPlaylistRecordList();
    public List<StationRecord> importStationRecordList(Long playlistID);
    public StationRecord insertStationRecord(StationRecord sr);
    public PlaylistRecord insertPlaylistRecord(PlaylistRecord pr);


}
