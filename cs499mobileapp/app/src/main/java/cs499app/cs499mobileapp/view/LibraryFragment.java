package cs499app.cs499mobileapp.view;

import android.os.AsyncTask;
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
import cs499app.cs499mobileapp.model.LibraryRecord;
import cs499app.cs499mobileapp.model.PlaylistRecord;
import cs499app.cs499mobileapp.viewadapter.PlaylistAdapter;

/**
 * Created by centa on 6/27/2017.
 */

public class LibraryFragment extends Fragment{



    private List<PlaylistRecord> playlistRecord;
    private PlaylistAdapter playlistAdapter;
    private ListView playlistListview;
    private LibraryRecord libRecord;
    private View root;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("LibFragmentOnCreate","OnCreatecalled");
         root = inflater.inflate(R.layout.library_fragment, container, false);
        loadPlaylistLibrary();
//        playlistListview = root.findViewById(R.id.listview_playlist);
//        playlistListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.i("onItemClick","Spawn list frag");
//                Fragment stationListFragment = new StationListFragment();
//                Bundle args = new Bundle();
//                args.putInt("StationList", i);
//                //args.putString("PlayListTitle",);
//                stationListFragment.setArguments(args);
////
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(R.id.fragment_container, stationListFragment);
//                transaction.addToBackStack(stationListFragment.getClass().getName());
//                transaction.commit();
////
////        // update selected item and title, then close the drawer
////        mDrawerList.setItemChecked(position, true);
////        setTitle(mPlanetTitles[position]);
////        mDrawerLayout.closeDrawer(mDrawerList);
//            }
//        });
//
//
//        playlistAdapter = new PlaylistAdapter(this.getContext(),R.layout.playlist_listview_items, playlistRecord);
//        playlistListview.setAdapter(playlistAdapter);
//
//        final FragmentManager fm = getFragmentManager();
//        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.add_playlist_float_button);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                AddPlaylistDialogFragment dialogFm = new AddPlaylistDialogFragment();
//                dialogFm.show(fm,"addplaylistFragment");
//            }
//        });



        return root;
    }



    public LibraryFragment() {

    }

    private void loadPlaylistLibrary()
    {
        AsyncTask<Void, Void, Void> loadDataTask = new AsyncTask<Void, Void, Void>() {


            @Override
            protected Void doInBackground(Void... voids) {
                libRecord = new LibraryRecord(getContext());
                libRecord.importlPlaylistRecordList();
               // playlistRecord = libRecord.getPlaylistRecords();
                Log.i("loadDataTask","loading done");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                initViews();

            }
        };

        loadDataTask.execute();


        //playlistRecord = new ArrayList<>();
////        playlistRecord = new ArrayList<>();
//        playlistRecord.add(new PlaylistRecord("First Station"));
//        playlistRecord.add(new PlaylistRecord("Second Station"));
//        playlistRecord.add(new PlaylistRecord("Third Station"));
//        playlistRecord.add(new PlaylistRecord("This is a very very very long station name, scrolling"));

    }

    public void setLibraryRecord(LibraryRecord lr){
        libRecord = lr;

    }
    public LibraryRecord getLibraryRecord()
    {
        return libRecord;
    }


    public void notifyFragmentOnDataChange()
    {
        playlistAdapter.notifyDataSetChanged();
    }

    private void initViews()
    {
        playlistListview = root.findViewById(R.id.listview_playlist);
        playlistListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("onItemClick","Spawn list frag");
                Log.i("playlist Item pos:",i+"");
                StationListFragment stationListFragment = new StationListFragment();
                Bundle args = new Bundle();
                args.putInt(getString(R.string.PlayListViewPos), i);
                //args.putString("PlayListTitle",);
                stationListFragment.setArguments(args);
//                stationListFragment.setStationRecordList(libRecord.getStationListRecordsMap()
//                        .get(playlistRecord.get(i).getPlaylistName()));
                stationListFragment.setLibRecord(libRecord);
                stationListFragment.setParentPlaylistID(
                        libRecord.getPlaylistRecords().get(i).get_ID());
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


        playlistAdapter = new PlaylistAdapter(root.getContext(),R.layout.playlist_listview_items, libRecord.getPlaylistRecords());//playlistRecord);
        playlistListview.setAdapter(playlistAdapter);

        final FragmentManager fm = getFragmentManager();
        FloatingActionButton fab =  root.findViewById(R.id.add_playlist_float_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddPlaylistDialogFragment dialogFm = new AddPlaylistDialogFragment();
                dialogFm.setLibRecord(libRecord);
                dialogFm.setPlaylistAdapter(playlistAdapter);
                dialogFm.show(fm,"addplaylistFragment");
            }
        });
    }

    public void reloadData()
    {
        libRecord.importlPlaylistRecordList();
    }
}
