package cs499app.cs499mobileapp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cs499app.cs499mobileapp.R;
import cs499app.cs499mobileapp.model.PlaylistRecord;
import cs499app.cs499mobileapp.viewadapter.PlaylistAdapter;

/**
 * Created by centa on 6/27/2017.
 */

public class LibraryFragment extends Fragment{

    private List<PlaylistRecord> playlistRecord;
    private PlaylistAdapter playlistAdapter;
    private ListView playlistListview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("LibFragmentOnCreate","OnCreatecalled");
        View root = inflater.inflate(R.layout.library_fragment, container, false);

        loadPlaylistLibrary();
        playlistListview = root.findViewById(R.id.listview_playlist);
        playlistListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("onItemClick","Spawn list frag");
                Fragment stationListFragment = new StationListFragment();
                Bundle args = new Bundle();
                args.putInt("StationList", i);
                //args.putString("PlayListTitle",);
                stationListFragment.setArguments(args);
//
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, stationListFragment);
                transaction.addToBackStack(stationListFragment.getClass().getName());
                transaction.commit();
//
//        // update selected item and title, then close the drawer
//        mDrawerList.setItemChecked(position, true);
//        setTitle(mPlanetTitles[position]);
//        mDrawerLayout.closeDrawer(mDrawerList);
            }
        });


        playlistAdapter = new PlaylistAdapter(this.getContext(),R.layout.playlist_listview_items, playlistRecord);
        playlistListview.setAdapter(playlistAdapter);

        final FragmentManager fm = getFragmentManager();
        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.add_playlist_float_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddPlaylistDialogFragment dialogFm = new AddPlaylistDialogFragment();
                dialogFm.show(fm,"addStationFragment");
            }
        });

        return root;
    }



    public LibraryFragment() {

    }

    private void loadPlaylistLibrary()
    {
        playlistRecord = new ArrayList<>();
        playlistRecord.add(new PlaylistRecord("First Station"));
        playlistRecord.add(new PlaylistRecord("Second Station"));
        playlistRecord.add(new PlaylistRecord("Third Station"));
        playlistRecord.add(new PlaylistRecord("This is a very very very long station name, scrolling"));

    }

}
