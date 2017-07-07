package cs499app.cs499mobileapp.view;

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
import cs499app.cs499mobileapp.model.PlaylistRecord;
import cs499app.cs499mobileapp.viewadapter.PlaylistAdapter;

/**
 * Created by jason on 7/3/2017.
 */

public class StationListFragment extends android.support.v4.app.DialogFragment{


    private PlaylistAdapter stationlistAdapter;
    private ListView stationlistListview;
    private List<PlaylistRecord>stationNames;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("LibFragmentOnCreate","OnCreatecalled");
        View root = inflater.inflate(R.layout.fragment_station_list, container, false);

        loadStationList();
        stationlistListview = root.findViewById(R.id.stationlist_listview);

        stationlistAdapter = new PlaylistAdapter(this.getContext(),R.layout.playlist_listview_items,stationNames);
        stationlistListview.setAdapter(stationlistAdapter);
        final FragmentManager fm = getFragmentManager();

        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.add_station_float_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddStationDialogFragment dialogFm = new AddStationDialogFragment();
                dialogFm.show(fm,"addstationfragment");
            }
        });

        FloatingActionButton backFab = (FloatingActionButton) root.findViewById(R.id.back_arrow_float_button);
        backFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Back button ","pressed");
                getFragmentManager().popBackStack();
            }
        });



        return root;
    }


    private void loadStationList()
    {
        stationNames = new ArrayList<>();
        stationNames.add(new PlaylistRecord("First song"));
        stationNames.add(new PlaylistRecord("Second song"));
        stationNames.add(new PlaylistRecord("Third song"));


    }



}
