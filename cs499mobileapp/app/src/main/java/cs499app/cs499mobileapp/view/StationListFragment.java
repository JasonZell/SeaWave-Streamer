package cs499app.cs499mobileapp.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cs499app.cs499mobileapp.R;
import cs499app.cs499mobileapp.model.LibraryRecord;
import cs499app.cs499mobileapp.model.PlaylistRecord;
import cs499app.cs499mobileapp.model.StationRecord;
import cs499app.cs499mobileapp.viewadapter.PlaylistAdapter;
import cs499app.cs499mobileapp.viewadapter.StationListAdapter;

/**
 * Created by jason on 7/3/2017.
 */

public class StationListFragment extends android.support.v4.app.DialogFragment{


    private StationListAdapter stationlistAdapter;
    private ListView stationlistListview;
    private List<StationRecord> stationRecordList;
    private int parentPlaylistViewID;
    View rootview;
    private int parentPlaylistID;
    private LibraryRecord libRecord;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("LibFragmentOnCreate","OnCreatecalled");
        rootview = inflater.inflate(R.layout.fragment_station_list, container, false);
        parentPlaylistViewID = getArguments().getInt(getString(R.string.PlayListViewPos));
        loadStationList();

        return rootview;
    }


    private void loadStationList()
    {
        AsyncTask<Void, Void, Void> loadDataTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                //libRecord = new LibraryRecord(getContext());
                libRecord.importStationRecordList(parentPlaylistID);
                Log.i("loadDataTask","loading done");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                initViews();

            }
        };

        loadDataTask.execute();

//        stationNames = new ArrayList<>();
//        stationNames.add(new PlaylistRecord("First song"));
//        stationNames.add(new PlaylistRecord("Second song"));
//        stationNames.add(new PlaylistRecord("Third song"));


    }

    public void setStationRecordList(List<StationRecord> srl)
    {
        stationRecordList = srl;
    }

    public LibraryRecord getLibRecord() {
        return libRecord;
    }

    public void setLibRecord(LibraryRecord libRecord) {
        this.libRecord = libRecord;
    }

    public int getParentPlaylistID() {
        return parentPlaylistID;
    }

    public void setParentPlaylistID(int parentPlaylistID) {
        this.parentPlaylistID = parentPlaylistID;
    }

    private void initViews()
    {
        stationlistListview = rootview.findViewById(R.id.stationlist_listview);
        stationlistAdapter = new StationListAdapter(this.getContext(),R.layout.playlist_listview_items,
                libRecord.getStationListRecordsMap().get(parentPlaylistID));//stationRecordList);

        stationlistListview.setAdapter(stationlistAdapter);

        final FragmentManager fm = getFragmentManager();
        FloatingActionButton fab = (FloatingActionButton) rootview.findViewById(R.id.add_station_float_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddStationDialogFragment dialogFm = new AddStationDialogFragment();
                dialogFm.setLibRecord(libRecord);
                dialogFm.setParentPlaylistID(parentPlaylistID);
                dialogFm.setStationListAdapter(stationlistAdapter);
                dialogFm.show(fm,"addstationfragment");
            }
        });

        FloatingActionButton backFab = (FloatingActionButton) rootview.findViewById(R.id.back_arrow_float_button);
        backFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Back button ","pressed");
                getFragmentManager().popBackStack();
            }
        });

    }



}
