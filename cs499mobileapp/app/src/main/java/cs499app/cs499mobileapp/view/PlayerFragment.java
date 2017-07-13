package cs499app.cs499mobileapp.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import cs499app.cs499mobileapp.helper.CircularSeekBar.OnCircularSeekBarChangeListener;
import cs499app.cs499mobileapp.R;
import cs499app.cs499mobileapp.helper.CircularSeekBar;
import cs499app.cs499mobileapp.helper.RoundedBitmapDrawableUtility;
import cs499app.cs499mobileapp.model.LibraryRecord;

/**
 * Created by centa on 6/27/2017.
 */

public class PlayerFragment extends Fragment {

    enum playOrPause {PLAY_STATE, PAUSE_STATE};

    private LibraryRecord libRecord;
    private String currentStationTitle;
    private String currentPlaylistTitle;
    private String currentStationURL;

    private TextView stationTitleView;
    private TextView playlistTitleView;

    MediaControllerCallbackListener controllerCallbackListener;
    private View rootView;
    private ImageButton playPauseButton;
    private ImageButton skipForwardButton;
    private ImageButton skipPrevButton;
    private playOrPause playOrPauseState;



    public PlayerFragment() {
        playOrPauseState = playOrPause.PAUSE_STATE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("PlayFragmentOnCreate","OnCreatecalled");
        rootView = inflater.inflate(R.layout.player_fragment, container, false);
        playlistTitleView = rootView.findViewById(R.id.controller_playlist_name);
        stationTitleView = rootView.findViewById(R.id.controller_station_name);

        restoreSettings();

        //setup callback
        try {
            controllerCallbackListener = (PlayerFragment.MediaControllerCallbackListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement MediaControllerCallbackListener Methods");
        }
        setupMediaControlButtons();



        final int max = 30;
         int progress = 20;
        final CircularSeekBar seekBar = (CircularSeekBar) rootView.findViewById(R.id.circular_seek_bar);
        seekBar.setIsTouchEnabled(false);
        seekBar.setMax(max);
        seekBar.setProgress(progress);

//        CountDownTimer cd= new CountDownTimer(max*1000,1000){
//            @Override
//            public void onTick(long lefttime) {
//            seekBar.setProgress(((int)lefttime/1000)-1);
//                Log.i("timer:", String.valueOf(lefttime));
//            seekBar.invalidate();
//
//            }
//
//            @Override
//            public void onFinish() {
//                //seekBar.setProgress(0);
//
//            }
//        }.start();

        seekBar.setOnSeekBarChangeListener(new OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
//                AppCompatTextView text = (AppCompatTextView) circularSeekBar.findViewById(R.id.controller_station_name);
//                text.setText("Progress:" +progress);
            }
            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
//
//        //fitting image inside circular seekbar
//
//        Bitmap src  = BitmapFactory.decodeResource(rootView.getResources(), R.drawable.radio_image);
//        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(seekBar.getResources(), src);
//        dr.setCircular(true);
//        ImageView imageView = rootView.findViewById(R.id.seekbar_image);
//        imageView.setMaxHeight(seekBar.getLayoutParams().width - (int)convertDpToPixel(5,rootView.getContext()));
//        imageView.setMaxWidth(seekBar.getLayoutParams().width - (int)convertDpToPixel(5,rootView.getContext()));
//
//       // imageView.requestLayout();
//       // imageView.setLayoutParams(params);
//        Log.e("offset","offset"+(int)convertPxToDp(50));
//        imageView.setImageDrawable(dr);

        return rootView;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f); return px;
    }

    public static float convertPxToDp(float px) {
        return px / Resources.getSystem().getDisplayMetrics().density;
    }


    public LibraryRecord getLibRecord() {
        return libRecord;
    }

    public void setLibRecord(LibraryRecord libRecord) {
        this.libRecord = libRecord;
    }

    public String getCurrentStationTitle() {
        return currentStationTitle;
    }

    public void setCurrentStationTitle(String currentStationTitle) {
        this.currentStationTitle = currentStationTitle;
    }

    public String getCurrentPlaylistTitle() {
        return currentPlaylistTitle;
    }

    public void setCurrentPlaylistTItle(String currentPlaylistTitle) {
        this.currentPlaylistTitle = currentPlaylistTitle;
    }

    public String getCurrentStationURL() {
        return currentStationURL;
    }

    public void setCurrentStationURL(String currentStationURL) {
        this.currentStationURL = currentStationURL;
    }

    public void updateDisplayTitles()
    {
        playlistTitleView.setText(currentPlaylistTitle);
        stationTitleView.setText(currentStationTitle);

    }

    public interface MediaControllerCallbackListener {
        public void onPlayButtonPressed();
        public void onPauseButtonPressed();
        public void onSkipForwardButtonPressed();
        public void onSkipPrevButtonPressed();
    }

    public void setupMediaControlButtons()
    {
        playPauseButton = rootView.findViewById(R.id.controller_playpause_button);
        skipForwardButton = rootView.findViewById(R.id.controller_skipnext_button);
        skipPrevButton = rootView.findViewById(R.id.controller_skipprev_button);

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(playOrPauseState == playOrPause.PAUSE_STATE) {
                    controllerCallbackListener.onPlayButtonPressed();
                    playPauseButton.setImageResource(R.drawable.pause_icon);
                    playOrPauseState = playOrPause.PLAY_STATE;
                }
                else
                {
                    controllerCallbackListener.onPauseButtonPressed();
                    playPauseButton.setImageResource(R.drawable.play_arrow);
                    //view.setBackgroundResource(R.drawable.pause_icon);
                    playOrPauseState = playOrPause.PAUSE_STATE;

                }

            }
        });

        skipForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controllerCallbackListener.onSkipForwardButtonPressed();

            }
        });

        skipPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controllerCallbackListener.onSkipPrevButtonPressed();

            }
        });
    }

    public void setStateToPlay()
    {
        controllerCallbackListener.onPlayButtonPressed();
        playPauseButton.setImageResource(R.drawable.pause_icon);
        playOrPauseState = playOrPause.PLAY_STATE;
    }

    private void restoreSettings()
    {

        Log.d("Restore player"," settings in player fragment");
        SharedPreferences settings = getContext().getSharedPreferences(
                getString(R.string.SETTING_PREFERENCES), 0);

        currentPlaylistTitle = settings.getString(
                getString(R.string.SETTING_LAST_PLAYLIST_TITLE),"No Playlist");

        currentStationTitle = settings.getString(
                getString(R.string.SETTING_LAST_STATION_TITLE),"No Station");

        currentStationURL = settings.getString(
                getString(R.string.SETTING_LAST_STATION_URL),"");
        updateDisplayTitles();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences settings = getContext().getSharedPreferences(
                getString(R.string.SETTING_PREFERENCES), 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(getString(R.string.SETTING_LAST_PLAYLIST_TITLE), currentPlaylistTitle);
        editor.putString(getString(R.string.SETTING_LAST_STATION_TITLE), currentStationTitle);
        editor.putString(getString(R.string.SETTING_LAST_STATION_URL), currentStationURL);
        editor.commit();
        Log.d("Destoryed player ","Fragment, saving settings");

    }
}
