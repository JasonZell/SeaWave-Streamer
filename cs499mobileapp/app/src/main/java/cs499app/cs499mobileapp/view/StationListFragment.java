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
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import cs499app.cs499mobileapp.R;
import cs499app.cs499mobileapp.helper.ContextMenuMode;
import cs499app.cs499mobileapp.helper.DialogActionMode;
import cs499app.cs499mobileapp.model.LibraryRecord;
import cs499app.cs499mobileapp.model.StationRecord;
import cs499app.cs499mobileapp.viewadapter.StationListAdapter;

/**
 * Created by jason on 7/3/2017.
 */

public class StationListFragment extends android.support.v4.app.DialogFragment{


    private StationListAdapter stationlistAdapter;
    private ListView stationlistListview;
    private List<StationRecord> stationRecordList;
    View rootview;
    private Long parentPlaylistID;
    private LibraryRecord libRecord;
    private  int parentPlaylistViewID;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("LibFragmentOnCreate","OnCreatecalled");
        rootview = inflater.inflate(R.layout.fragment_station_list, container, false);
        parentPlaylistViewID = getArguments().getInt(getString(R.string.PlayListViewPos));
        parentPlaylistID = getArguments().getLong(getString(R.string.ParentPlaylistID));
        loadStationList();

        return rootview;
    }


    private void loadStationList()
    {
        AsyncTask<Void, Void, Void> loadDataTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                //libRecord = new LibraryRecord(getContext());

                if(libRecord.getStationListRecordsMap().get(parentPlaylistID) == null) {
                    libRecord.importStationRecordList(parentPlaylistID);
                    Log.i("loadDataTask", "loading done");
                }

                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {

                initViews();

            }
        };

        loadDataTask.execute();
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

//    public Long getParentPlaylistID() {
//        return parentPlaylistID;
//    }
//
//    public void setParentPlaylistID(Long parentPlaylistID) {
//        this.parentPlaylistID = parentPlaylistID;
//    }

    private void initViews()
    {
        final FragmentManager fm = getFragmentManager();

        stationlistListview = rootview.findViewById(R.id.stationlist_listview);
        stationlistAdapter = new StationListAdapter(this.getContext(),R.layout.playlist_listview_items,
                libRecord.getStationListRecordsMap().get(parentPlaylistID));//stationRecordList);

        stationlistListview.setAdapter(stationlistAdapter);


        stationlistListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                ContextMenuDialogFragment cmdf = new ContextMenuDialogFragment();
                Bundle args = new Bundle();
                args.putInt(getString(R.string.StationListViewPos), i);
                args.putLong(getString(R.string.ParentPlaylistID),parentPlaylistID);
                cmdf.setArguments(args);

                cmdf.setContextMenuMode(ContextMenuMode.STATION_MODE);
                cmdf.setLibRecord(libRecord);
                cmdf.setStationlistAdapter(stationlistAdapter);
                cmdf.show(fm,"contextMenuFragment");

                return true; // return true prevents calling of onItemClickListener
            }
        });

        FloatingActionButton fab = (FloatingActionButton) rootview.findViewById(R.id.add_station_float_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StationDialogFragment dialogFm = new StationDialogFragment();

                Bundle args = new Bundle();
                args.putInt(getString(R.string.PlayListViewPos),
                        getArguments().getInt(getString(R.string.PlayListViewPos)));
                args.putLong(getString(R.string.ParentPlaylistID),parentPlaylistID);
                dialogFm.setArguments(args);

                dialogFm.setLibRecord(libRecord);
                dialogFm.setParentPlaylistID(parentPlaylistID);
                dialogFm.setStationListAdapter(stationlistAdapter);
                dialogFm.setDialogActionMode(DialogActionMode.ADD_MODE);
                dialogFm.setDialogTitle("ADD STATION");
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
