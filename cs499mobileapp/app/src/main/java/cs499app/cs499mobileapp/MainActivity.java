package cs499app.cs499mobileapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TableLayout;

import cs499app.cs499mobileapp.service.MusicService;
import cs499app.cs499mobileapp.view.InnerFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager_activity);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager); //layout will use PagerAdapter's page titles

        //Start Music Service
        Intent startServiceIntent = new Intent(MainActivity.this, MusicService.class);
        startServiceIntent.setAction("MUSIC_ACTION_CREATE");
        startService(startServiceIntent);

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
        sendBroadcast(new Intent(getString(R.string.MUSIC_ACTION_PAUSE)), getString(R.string.BROADCAST_PIVATE));

//           Log.e("MENU ACTIVITY", "TRANSITION: " + isTransition);
//        if(!isTransition) {
//            sendBroadcast(new Intent(getString(R.string.MUSIC_ACTION_PAUSE)), getString(R.string.BROADCAST_PIVATE));
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

        sendBroadcast(new Intent(getString(R.string.MUSIC_ACTION_PLAY)), getString(R.string.BROADCAST_PIVATE));
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

    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            return new InnerFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "TITLE " + (position+1);
        }
    }




}
