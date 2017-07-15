package cs499app.cs499mobileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import cs499app.cs499mobileapp.helper.AudioRecorder;
import cs499app.cs499mobileapp.helper.CircularSeekBar;
import cs499app.cs499mobileapp.model.LibraryRecord;
import cs499app.cs499mobileapp.model.StationRecord;
import cs499app.cs499mobileapp.service.MusicService;
import cs499app.cs499mobileapp.view.ContainerFragment;
import cs499app.cs499mobileapp.view.ContextMenuDialogFragment;
import cs499app.cs499mobileapp.view.PlayerFragment;
import cs499app.cs499mobileapp.view.StationDialogFragment;
import cs499app.cs499mobileapp.view.StationListFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        StationListFragment.StationListCallbackListener,
        PlayerFragment.MediaControllerCallbackListener,
        ContextMenuDialogFragment.ContextMenuCallbackListener,
        StationDialogFragment.StationDialogCallbackListener{

    public static final int EXPORT_DB_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    public static final int INITIAL_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    public static final int CREATE_BASE_DIR_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 3;

    private DrawerLayout navigationDrawerLayout;
    private ActionBarDrawerToggle navigationDrawerToggle;
    private int currentFocusedTab;
    private PlayerFragment playerTabFragmentRef;
    private ContainerFragment containerTabFragmentRef;
    LibraryRecord libRecord;
    private boolean usePlayProgressTimer;
    private AudioRecorder audioRecorder;
    private Switch playProgressSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize share preferences
        initSharePref();
        restoreSettings();
        checkReadWritePermission();

        //initialize library record
        setLibRecord(new LibraryRecord(this.getApplicationContext()));

        Toolbar toolbar = (Toolbar) findViewById(R.id.customized_toolbar);
        setSupportActionBar(toolbar);
       // this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationDrawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        navigationDrawerToggle = new ActionBarDrawerToggle(
                this, navigationDrawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        navigationDrawerLayout.addDrawerListener(navigationDrawerToggle);
        navigationDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);



//

//        if (findViewById(R.id.fragment_container) != null) {
//            Log.d("Fm not null","fm not null");
//
//            // However, if we're being restored from a previous state,
//            // then we don't need to do anything and should return or else
//            // we could end up with overlapping fragments.
//            if (savedInstanceState != null) {
//                return;
//            }
//
//            // Create a new Fragment to be placed in the activity layout
//            ContainerFragment firstFragment = new ContainerFragment();
//            firstFragment.setFragmentManager(getSupportFragmentManager()); //important
//
//            // In case this activity was started with special instructions from an
//            // Intent, pass the Intent's extras to the fragment as arguments
//            firstFragment.setArguments(getIntent().getExtras());
//
//            // Add the fragment to the 'fragment_container' FrameLayout
//            final int commit = getSupportFragmentManager().beginTransaction()
//                    .add(R.id.fragment_container, firstFragment)
//                    .addToBackStack(firstFragment.getClass().getName()).commit();
//        }

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.main_pager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.i("Current Tab:",position+"");
            }

            @Override
            public void onPageSelected(int position) {
                Log.i("Current Tab:",position+"");
                if(position == 0)
                    currentFocusedTab = R.string.TAB_ONE;
                else
                    currentFocusedTab = R.string.TAB_TWO;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(adapter);

        adapter.startUpdate(viewPager);
        playerTabFragmentRef = (PlayerFragment) adapter.instantiateItem(viewPager,0);
        containerTabFragmentRef = (ContainerFragment) adapter.instantiateItem(viewPager,1);
        adapter.finishUpdate(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager); //layout will use PagerAdapter's page titles
        tabLayout.getTabAt(0).setIcon(R.drawable.player_icon_selector);
        tabLayout.getTabAt(1).setIcon(R.drawable.library_icon_selector);

        //playbackToggle
        Menu navMenu = navigationView.getMenu();
        playProgressSwitch = navMenu.findItem(R.id.playback_timer_toggle_item).getActionView().findViewById(R.id.play_progress_toggle);
        playProgressSwitch.setChecked(usePlayProgressTimer);
        playProgressSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    usePlayProgressTimer = true;
                    playerTabFragmentRef.setUsePlayProgressTimer(true);
                    playerTabFragmentRef.getPlayProgressTimer().start();
                }
                else
                {
                    usePlayProgressTimer = false;
                    playerTabFragmentRef.getPlayProgressTimer().stop();
                    playerTabFragmentRef.setUsePlayProgressTimer(false);

                    Toast.makeText(MainActivity.this, "SwitchOFF", Toast.LENGTH_SHORT).show();
                }
            }
        });


//         AsyncTask<Void, Void, Void> loadDataTask = new AsyncTask<Void, Void, Void>() {
//
//
//             @Override
//             protected Void doInBackground(Void... voids) {
//
//                 libRecord.importlPlaylistRecordList();
//                 Log.i("loadDataTask","loading done");
//                 return null;
//             }
//
//             @Override
//             protected void onPostExecute(Void aVoid) {
//                 LibraryFragment libFrag = (LibraryFragment)
//                         getSupportFragmentManager().findFragmentByTag(getString(R.string.LIB_FRAG_TAG));
//
//                 libFrag.setPlaylistRecord(libRecord.getPlaylistRecords());
//                 libFrag.notifyFragmentOnDataChange();
//             }
//         };
//         loadDataTask.execute();

        //initialize and retrieve settings from shared preferences.

        //initialize audioRecorder
        audioRecorder = new AudioRecorder(this.getApplicationContext(),MainActivity.this,playerTabFragmentRef.getSeekBar());

        //Start Music Service
        Intent startServiceIntent = new Intent(MainActivity.this, MusicService.class);
        startServiceIntent.setAction("MUSIC_ACTION_CREATE");
        startService(startServiceIntent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_playlist) {
            return true;
        } else if(id == R.id.action_add_station)
        {
            return true;

        } else if(id == R.id.action_import_list)
        {
            return true;

        }else if(id == R.id.action_export_list)
        {
            return true;
        }
        else if(id == R.id.action_export_database)
        {
            libRecord.exportDatabase(getApplicationContext(),this);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Method: onPause
    //Purpose: calls when the activity is paused
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.e("MENU ACTIVITY","ACTIVITY PAUSED");
        //sendBroadcast(new Intent(getString(R.string.MUSIC_ACTION_PAUSE)), getString(R.string.BROADCAST_PRIVATE));

//           Log.e("MENU ACTIVITY", "TRANSITION: " + isTransition);
//        if(!isTransition) {
//            sendBroadcast(new Intent(getString(R.string.MUSIC_ACTION_PAUSE)), getString(R.string.BROADCAST_PRIVATE));
//        }
    }
    //method: onResume
    //Purpose: override method, called when activity goes into resumed state
    //calls music service to restart the music.
    @Override
    protected void onResume()
    {
        super.onResume();
        //updateMusicIcon();
        Log.e("MENU ACTIVITY","ACTIVITY RESUMED");

        //sendBroadcast(new Intent(getString(R.string.MUSIC_ACTION_PLAY)), getString(R.string.BROADCAST_PRIVATE));
        //isTransition = false;

    }

    //method: onStop
    //Purpose: override method, called when activity goes into stopped state
    @Override
    protected void onStop()
    {
        super.onStop();
        Log.e("MENU ACTIVITY","ACTIVITY STOPPED");
    }

    //method: onDestroy
    //purpose: override method, called when activity is destroyed
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        super.onDestroy();
        SharedPreferences settings = this.getSharedPreferences(
                getString(R.string.SETTING_PREFERENCES), 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(getString(R.string.SETTING_USE_PLAY_PROGRESS),usePlayProgressTimer);
        editor.commit();
        Log.d("Destoryed Activity ",", Saving Settings");
        Log.e("MENU ACTIVITY","ACTIVITY DESTROYED");

    }

    //Method: onStart
    //Purpose: override method, called when activity is started
    //send signal to music service to start playing music
    @Override
    protected void onStart() {
        super.onStart();
        //isTransition = false; //very important when transitioning back via back button
        Log.e("MENU ACTIVITY", "ACTIVITY STARTED");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        int count = getSupportFragmentManager().getBackStackEntryCount();
        Log.i("fragment count:",""+count);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {

            if (currentFocusedTab == R.string.TAB_TWO) {
                if (count >= 2) {
                    Log.i("backPressed", "pop fragment");
                    getSupportFragmentManager().popBackStack();
                } else if (count == 1) {
                    moveTaskToBack(false);
                } else {
                    Log.i("Backpressed","super.backpressed()");
                    super.onBackPressed();
                }
            } else {
                moveTaskToBack(false);
                Log.i("backpressed", "while in tab 0");
                //super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);


        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.playback_timer_toggle_item) {
            playProgressSwitch.setChecked(usePlayProgressTimer = !usePlayProgressTimer);


        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }


        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    private class DrawerItemClickListener implements ListView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            //selectItem(position);
//        }
//    }
//      FOR onItemClick of Navigation Drawer items
//    private void selectItem(int position) {
//        // update the main content by replacing fragments
//        Fragment fragment = new PlanetFragment();
//        Bundle args = new Bundle();
//        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
//        fragment.setArguments(args);
//
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
//
//        // update selected item and title, then close the drawer
//        mDrawerList.setItemChecked(position, true);
//        setTitle(mPlanetTitles[position]);
//        mDrawerLayout.closeDrawer(mDrawerList);
//    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        navigationDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        navigationDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {

            switch (i){
                case 0:
                    PlayerFragment pf = new PlayerFragment();
                    pf.setLibRecord(libRecord);
                    return pf;
                case 1:
                    ContainerFragment cf = new ContainerFragment();
                    cf.setLibRecord(libRecord);
                    return cf;

                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return "TITLE " + (position+1);
//        }
    }

    public LibraryRecord getLibRecord() {
        return libRecord;
    }

    public void setLibRecord(LibraryRecord libRecord) {
        this.libRecord = libRecord;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case EXPORT_DB_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    libRecord.exportDatabase(getApplicationContext(),this);

                } else {
                    Toast.makeText(this, "Write External Permission Required To Export Database!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case CREATE_BASE_DIR_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    audioRecorder.setBaseDirectory();
                else
                {
                    Toast.makeText(this, "Write External Permission Required To Record Audio!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public void onPlayButtonPressed() {
        //Toast.makeText(this, "PlayButton event Catch in Activity", Toast.LENGTH_SHORT).show();
        sendBroadcast(new Intent(getString(R.string.MUSIC_ACTION_PLAY)), getString(R.string.BROADCAST_PRIVATE));
        playerTabFragmentRef.getPlayProgressTimer().resume();
    }

    @Override
    public void onPauseButtonPressed() {
        //Toast.makeText(this, "PauseButton event Catch in Activity", Toast.LENGTH_SHORT).show();
        sendBroadcast(new Intent(getString(R.string.MUSIC_ACTION_PAUSE)), getString(R.string.BROADCAST_PRIVATE));
        playerTabFragmentRef.getPlayProgressTimer().pause();


    }

    //return true if there is next station to go to, otherwise, return false;
    @Override
    public boolean onSkipForwardButtonPressed() {
        boolean returnVal = false;
        // Toast.makeText(this, "Skip Forward Button event Catch in Activity", Toast.LENGTH_SHORT).show();
        int nextStationIndex = playerTabFragmentRef.getPlayQueue().getNextStation();
        Log.d("curPlaylistIndex",playerTabFragmentRef.getPlayQueue().getCurrentPlayListID()+"");
        Log.d("NextStationIndex",nextStationIndex+"");
        if(nextStationIndex != -1) {
            returnVal = true;
            long curPlaylistID = playerTabFragmentRef.getPlayQueue().getCurrentPlayListID();
            List<StationRecord> srl = libRecord.getStationListRecordsMap()
                    .get(curPlaylistID);
            StationRecord record = srl.get(nextStationIndex);
            int CurPlaylistViewID = playerTabFragmentRef.getCurrentPlaylistViewID();


            Intent intent = new Intent(getString(R.string.MUSIC_ACTION_PLAY_URL));
            intent.putExtra(getString(R.string.MUSIC_URL_TO_PLAY), record.getStationURL());
            sendBroadcast(intent, getString(R.string.BROADCAST_PRIVATE));

            playerTabFragmentRef.setCurrentPlaylistTItle(
                    libRecord.getPlaylistRecords().get(CurPlaylistViewID).getPlaylistName());
            playerTabFragmentRef.setCurrentStationTitle(record.getStationTitle());
            playerTabFragmentRef.setCurrentStationURL(record.getStationURL());
            playerTabFragmentRef.setStateToPlay();
            playerTabFragmentRef.updateDisplayTitles();

            playerTabFragmentRef.getPlayProgressTimer().start();

        }

        return returnVal;
    }

    //return true if there is previous station to go to, otherwise, return false;
    @Override
    public boolean onSkipPrevButtonPressed() {
       // Toast.makeText(this, "Skip Prev Button event Catch in Activity", Toast.LENGTH_SHORT).show();
        boolean returnVal = false;
        int prevStationIndex = playerTabFragmentRef.getPlayQueue().getPrevStation();
        Log.d("curPlaylistIndex",playerTabFragmentRef.getPlayQueue().getCurrentPlayListID()+"");
        Log.d("PrevStationIndex",prevStationIndex+"");
        if(prevStationIndex != -1) {
            returnVal = true;

            int curPlaylistViewID = playerTabFragmentRef.getCurrentPlaylistViewID();
            long curPlaylistID = playerTabFragmentRef.getPlayQueue().getCurrentPlayListID();
            List<StationRecord> srl = libRecord.getStationListRecordsMap()
                    .get(curPlaylistID);
            StationRecord record = srl.get(prevStationIndex);


            Intent intent = new Intent(getString(R.string.MUSIC_ACTION_PLAY_URL));
            intent.putExtra(getString(R.string.MUSIC_URL_TO_PLAY), record.getStationURL());
            sendBroadcast(intent, getString(R.string.BROADCAST_PRIVATE));

            playerTabFragmentRef.setCurrentPlaylistTItle(
                    libRecord.getPlaylistRecords().get(curPlaylistViewID).getPlaylistName());
            playerTabFragmentRef.setCurrentStationTitle(record.getStationTitle());
            playerTabFragmentRef.setCurrentStationURL(record.getStationURL());
            playerTabFragmentRef.setStateToPlay();
            playerTabFragmentRef.updateDisplayTitles();

            playerTabFragmentRef.getPlayProgressTimer().start();



        }
        return returnVal;
    }

    private void initSharePref()
    {
        String appPackageName = getApplicationInfo().packageName;
        String prefFileName = getString(R.string.SETTING_PREFERENCES);
        File f = new File(
                "/data/data/"+appPackageName+"/shared_prefs/"+prefFileName+".xml");
        if (f.exists()) {

            Log.d("TAG", "SharedPreferences "+prefFileName+" : exist");
        }
        else
        {
            Log.d("TAG", "Setup default preferences");
            SharedPreferences settings = getSharedPreferences(prefFileName, MODE_PRIVATE);
            settings.edit().commit();

        }
    }

    @Override
    public void onPlayStationButtonPressed(long parentPlaylistID, int parentPlaylistViewID,int stationViewID) {
        Log.i("StationClicked","Playlistviewid: "+parentPlaylistID+" stationviewID: "+stationViewID);

        List<StationRecord> srl= libRecord.getStationListRecordsMap()
                .get(parentPlaylistID);
        StationRecord record = srl.get(stationViewID);

        Log.i("callback","received URL:" +record.getStationURL());

        Intent intent = new Intent(getString(R.string.MUSIC_ACTION_PLAY_URL));
        intent.putExtra(getString(R.string.MUSIC_URL_TO_PLAY),record.getStationURL());
        sendBroadcast(intent,getString(R.string.BROADCAST_PRIVATE));

        playerTabFragmentRef.setCurrentPlaylistViewID(parentPlaylistViewID);
        playerTabFragmentRef.setCurrentPlaylistTItle(
                libRecord.getPlaylistRecords().get(parentPlaylistViewID).getPlaylistName());
        playerTabFragmentRef.setCurrentStationTitle(record.getStationTitle());
        playerTabFragmentRef.setCurrentStationURL(record.getStationURL());
        playerTabFragmentRef.updateDisplayTitles();
        playerTabFragmentRef.setStateToPlay();
        playerTabFragmentRef.notifyPlayQueue(
                srl,parentPlaylistID,stationViewID,playerTabFragmentRef.isShuffle(),
                playerTabFragmentRef.isRepeat());

        playerTabFragmentRef.getPlayProgressTimer().start();

    }

    @Override
    public void onPlayAllStationButtonPressed(long playListViewID) {

    }

    @Override
    public void onShuffleButtonPressed(boolean shuffleState) {
        Toast.makeText(this, "Callback: shuffle: "+ shuffleState, Toast.LENGTH_SHORT).show();
        playerTabFragmentRef.setShuffle(shuffleState);
    }


    @Override
    public void onStationDeleted(long parentPlayListID, int parentPlaylistViewID, int stationViewID) {
        if(parentPlayListID == playerTabFragmentRef.getPlayQueue().getCurrentPlayListID())
        {
            if(playerTabFragmentRef.getPlayQueue().getCurrentStationIndex() == stationViewID)
            {
                resetMediaPlayer();
            }
            playerTabFragmentRef.getPlayQueue().removeIndex(stationViewID);
        }
    }

    @Override
    public void onPlaylistDeleted(long parentPlayListID) {
        Log.e("PARENTPLAYID"," "+parentPlayListID);
        Log.e("currentPLAYID"," "+playerTabFragmentRef.getPlayQueue().getCurrentPlayListID());

        if(parentPlayListID == playerTabFragmentRef.getPlayQueue().getCurrentPlayListID())
        {
            playerTabFragmentRef.getPlayQueue().resetPlayQueue();
            resetMediaPlayer();
        }
    }

    @Override
    public void onStationAdded(long parentPlayListID, int parentPlaylistViewID) {
        if(parentPlayListID == playerTabFragmentRef.getPlayQueue().getCurrentPlayListID())
            playerTabFragmentRef.getPlayQueue().incrementStationEntry();
    }

    @Override
    public void onRecordButtonPressed(boolean recordState) {
        if(recordState == true)
        {
            Toast.makeText(this, "START RECORDING", Toast.LENGTH_SHORT).show();
            audioRecorder.startRecording(
                    playerTabFragmentRef.getCurrentPlaylistTitle(),
                    playerTabFragmentRef.getCurrentStationTitle(),
                    playerTabFragmentRef.getCurrentStationURL());
        }
        else
        {
            Toast.makeText(this, "STOP RECORDING", Toast.LENGTH_SHORT).show();
            audioRecorder.stopRecording();
        }
    }

    @Override
    public void onRepeatButtonPressed(boolean repeatState) {
        Toast.makeText(this, "Repeat "+repeatState, Toast.LENGTH_SHORT).show();
        playerTabFragmentRef.getPlayQueue().setRepeat(repeatState);
    }

    public void resetMediaPlayer()
    {
        playerTabFragmentRef.resetMediaPlayer();

        Intent intent = new Intent(getString(R.string.MUSIC_ACTION_RESET));
        sendBroadcast(intent, getString(R.string.BROADCAST_PRIVATE));
    }


    public void checkReadWritePermission()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MainActivity.INITIAL_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    private void restoreSettings()
    {
        Log.d("Restore player"," settings in player fragment");
        SharedPreferences settings = this.getSharedPreferences(
                getString(R.string.SETTING_PREFERENCES), 0);
        usePlayProgressTimer = settings.getBoolean(
                getString(R.string.SETTING_USE_PLAY_PROGRESS),false);

//        currentPlaylistTitle = settings.getString(
//                getString(R.string.SETTING_LAST_PLAYLIST_TITLE),"No Playlist");
//
//        currentStationTitle = settings.getString(
//                getString(R.string.SETTING_LAST_STATION_TITLE),"No Station");
//
//        currentStationURL = settings.getString(
//                getString(R.string.SETTING_LAST_STATION_URL),"");
        //updateDisplayTitles();

    }

}
