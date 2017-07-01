package cs499app.cs499mobileapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.support.v4.widget.DrawerLayout;

import cs499app.cs499mobileapp.service.MusicService;
import cs499app.cs499mobileapp.view.InnerFragment;
import cs499app.cs499mobileapp.view.LibraryFragment;
import cs499app.cs499mobileapp.view.PlayerFragment;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout navigationDrawerLayout;

    private ActionBarDrawerToggle navigationDrawerToggle;
    private String[] navigationItemTitleArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.customized_toolbar);
        setSupportActionBar(toolbar);
       // this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);




//        //Start Music Service
//        Intent startServiceIntent = new Intent(MainActivity.this, MusicService.class);
//        startServiceIntent.setAction("MUSIC_ACTION_CREATE");
//        startService(startServiceIntent);

        navigationDrawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        navigationDrawerToggle = new ActionBarDrawerToggle(
                this, navigationDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        navigationDrawerLayout.addDrawerListener(navigationDrawerToggle);
        navigationDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);



        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.main_pager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager); //layout will use PagerAdapter's page titles
        tabLayout.getTabAt(0).setIcon(R.drawable.player_icon_selector);
        tabLayout.getTabAt(1).setIcon(R.drawable.library_icon_selector);

        Log.d("PASSED ","passed");
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
        if (id == R.id.action_settings) {
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
        sendBroadcast(new Intent(getString(R.string.MUSIC_ACTION_PAUSE)), getString(R.string.BROADCAST_PRIVATE));

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

        sendBroadcast(new Intent(getString(R.string.MUSIC_ACTION_PLAY)), getString(R.string.BROADCAST_PRIVATE));
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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
//            return new InnerFragment();

            switch (i){
                case 0:
                    return new PlayerFragment();

                case 1:
                    return new LibraryFragment();
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


}
