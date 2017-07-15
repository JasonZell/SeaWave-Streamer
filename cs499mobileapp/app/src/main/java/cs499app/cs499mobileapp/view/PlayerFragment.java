package cs499app.cs499mobileapp.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import cs499app.cs499mobileapp.helper.CircularSeekBar.OnCircularSeekBarChangeListener;
import cs499app.cs499mobileapp.R;
import cs499app.cs499mobileapp.helper.CircularSeekBar;
import cs499app.cs499mobileapp.helper.PlayProgressCountDownTimer;
import cs499app.cs499mobileapp.model.LibraryRecord;
import cs499app.cs499mobileapp.model.PlayQueue;
import cs499app.cs499mobileapp.model.StationRecord;

/**
 * Created by centa on 6/27/2017.
 */

public class PlayerFragment extends Fragment {

    enum playOrPause {PLAY_STATE, PAUSE_STATE};

    private PlayQueue playQueue;
    private boolean isShuffle; //value set by settings only
    private boolean isRepeat; //value set by settings only
    private boolean isRecord;
    private boolean usePlayProgressTimer;
    private LibraryRecord libRecord;
    private String currentStationTitle;
    private String currentPlaylistTitle;
    private String currentStationURL;
    private int currentPlaylistViewID;

    private TextView stationTitleView;
    private TextView playlistTitleView;

    MediaControllerCallbackListener controllerCallbackListener;
    private View rootView;
    private ImageButton playPauseButton;
    private ImageButton skipForwardButton;
    private ImageButton skipPrevButton;
    private ImageButton shuffleButton;
    private ImageButton repeatButton;
    private FloatingActionButton recordButton;
    private playOrPause playOrPauseState;
    private ProgressBar playProgressBar;
    private int maxPlayDurationInSeconds;
    private CircularSeekBar seekBar;
    private PlayProgressCountDownTimer playProgressTimer;



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
        playProgressBar = rootView.findViewById(R.id.controller_progress_bar);
        playQueue = new PlayQueue();


        //DEBUG ONLY
        maxPlayDurationInSeconds = 10;
        //usePlayProgressTimer = true;

        restoreSettings();
        setupProgressBar();
        setupProgressTimer();
        //setup callback
        try {
            controllerCallbackListener = (PlayerFragment.MediaControllerCallbackListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement MediaControllerCallbackListener Methods");
        }

        setupMediaControlButtons();
        resetMediaPlayer();


        final int max = 30;
        int progress = 20;
        seekBar = (CircularSeekBar) rootView.findViewById(R.id.circular_seek_bar);
        seekBar.setIsTouchEnabled(false);
        seekBar.setMax(max);
        seekBar.setProgress(progress);

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
        public boolean onSkipForwardButtonPressed();
        public boolean onSkipPrevButtonPressed();
        public void onShuffleButtonPressed(boolean shuffleState);
        public void onRecordButtonPressed(boolean recordState);
        public void onRepeatButtonPressed(boolean repeatState);

    }

    public void setupMediaControlButtons()
    {
        //get references
        playPauseButton = rootView.findViewById(R.id.controller_playpause_button);
        skipForwardButton = rootView.findViewById(R.id.controller_skipnext_button);
        skipPrevButton = rootView.findViewById(R.id.controller_skipprev_button);
        shuffleButton = rootView.findViewById(R.id.controller_shuffle_button);
        recordButton  = rootView.findViewById(R.id.controller_record_floatingButton);
        repeatButton = rootView.findViewById(R.id.controller_repeat_button);

        //base on the state, choose icon image
        refreshRecordIconState();
        refreshShuffleIconState();
        refreshRepeatIconState();

        //setup button listeners
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(playOrPauseState == playOrPause.PAUSE_STATE) {
                    controllerCallbackListener.onPlayButtonPressed();
                    playPauseButton.setImageResource(R.drawable.pause_icon);
                    playOrPauseState = playOrPause.PLAY_STATE;
                }
                else {
                    controllerCallbackListener.onPauseButtonPressed();
                    playPauseButton.setImageResource(R.drawable.play_arrow);
                    playOrPauseState = playOrPause.PAUSE_STATE;
                }
            }
        });

        skipForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPlaylistViewID != -1)
                    controllerCallbackListener.onSkipForwardButtonPressed();
            }
        });

        skipPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPlaylistViewID != -1)
                    controllerCallbackListener.onSkipPrevButtonPressed();
            }
        });

        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controllerCallbackListener.onShuffleButtonPressed(isShuffle = !isShuffle);
                refreshShuffleIconState();
            }
        });

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentStationURL != "")
                    controllerCallbackListener.onRecordButtonPressed(isRecord = !isRecord);
                refreshRecordIconState();


            }
        });

        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controllerCallbackListener.onRepeatButtonPressed(isRepeat = !isRepeat);
                refreshRepeatIconState();

            }
        });
    }

    public void setStateToPlay()
    {
        controllerCallbackListener.onPlayButtonPressed();
        playPauseButton.setImageResource(R.drawable.pause_icon);
        playOrPauseState = playOrPause.PLAY_STATE;
    }

    public void refreshRepeatIconState()
    {
        if(isRepeat)
            setRepeatIconOn();
        else
            setRepeatIconOff();
    }

    public void refreshRecordIconState()
    {
        if(isRecord)
            setRecordIconOn();
        else
            setRecordIconOff();
    }

    public void refreshShuffleIconState()
    {
        if(isShuffle)
            setShuffleIconOn();
        else
            setShuffleIconOff();
    }

    public void setRepeatIconOn()
    {
        repeatButton.setImageResource(R.drawable.repeat_on_icon);
    }

    public void setRepeatIconOff()
    {
        repeatButton.setImageResource(R.drawable.repeat_off_icon);

    }

    public void setShuffleIconOn()
    {
        shuffleButton.setImageResource(R.drawable.shuffle_on_icon);
    }

    public void setShuffleIconOff()
    {
        shuffleButton.setImageResource(R.drawable.shuffle_off_icon);

    }

    public void setRecordIconOff()
    {
        recordButton.setImageResource(R.drawable.record_off_word_icon);
    }

    public void setRecordIconOn()
    {
        recordButton.setImageResource(R.drawable.record_word_icon);

    }

    private void restoreSettings()
    {
        Log.d("Restore player"," settings in player fragment");
        SharedPreferences settings = getContext().getSharedPreferences(
                getString(R.string.SETTING_PREFERENCES), 0);
        isShuffle = settings.getBoolean(
                getString(R.string.SETTING_IS_SHUFFLE),false);

        isRepeat = settings.getBoolean(getString(R.string.SETTING_IS_REPEAT),false);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences settings = getContext().getSharedPreferences(
                getString(R.string.SETTING_PREFERENCES), 0);
        SharedPreferences.Editor editor = settings.edit();
//        editor.putString(getString(R.string.SETTING_LAST_PLAYLIST_TITLE), currentPlaylistTitle);
//        editor.putString(getString(R.string.SETTING_LAST_STATION_TITLE), currentStationTitle);
//        editor.putString(getString(R.string.SETTING_LAST_STATION_URL), currentStationURL);
        editor.putBoolean(getString(R.string.SETTING_IS_REPEAT),isRepeat);
        editor.putBoolean(getString(R.string.SETTING_IS_SHUFFLE),isShuffle);
        editor.putBoolean(getString(R.string.SETTING_USE_PLAY_PROGRESS),usePlayProgressTimer);
        editor.commit();
        Log.d("Destoryed player ","Fragment, saving settings");

    }

    public PlayQueue getPlayQueue() {
        return playQueue;
    }

    public boolean isShuffle() {
        return isShuffle;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setShuffle(boolean shuffle) {
        isShuffle = shuffle;
        playQueue.setShuffle(shuffle);
    }

    public void notifyPlayQueue(List<StationRecord> record, long currentPlaylistID,
                                int currentStationViewID, boolean newShuffleState, boolean newRepeatState)
    {
        playQueue.notifyPlayQueue(record,currentPlaylistID,currentStationViewID,newShuffleState,newRepeatState);
    }

    public int getCurrentPlaylistViewID() {
        return currentPlaylistViewID;
    }

    public void setCurrentPlaylistViewID(int currentPlaylistViewID) {
        this.currentPlaylistViewID = currentPlaylistViewID;
    }

    public void resetMediaPlayer()
    {
        currentPlaylistTitle = "No Playlist Selected";
        currentStationTitle = "No Station Selected";
        currentStationURL = "";
        currentPlaylistViewID = -1;
        updateDisplayTitles();
        playPauseButton.setImageResource(R.drawable.play_arrow);
        playOrPauseState = playOrPause.PAUSE_STATE;

    }
    public void setupProgressBar()
    {
        playProgressBar.setMax(maxPlayDurationInSeconds);
    }

    public void setupProgressTimer()
    {
        playProgressTimer = new PlayProgressCountDownTimer(
                this,playProgressBar,maxPlayDurationInSeconds*1000,usePlayProgressTimer);
    }

    public CircularSeekBar getSeekBar() {
        return seekBar;
    }

    public PlayProgressCountDownTimer getPlayProgressTimer()
    {
        return playProgressTimer;
    }

    public ImageButton getSkipForwardButton() {
        return skipForwardButton;
    }

    public boolean isUsePlayProgressTimer() {
        return usePlayProgressTimer;
    }

    public void setUsePlayProgressTimer(boolean usePlayProgressTimer) {
        playProgressTimer.setEnabled(usePlayProgressTimer);
        this.usePlayProgressTimer = usePlayProgressTimer;
    }
}
