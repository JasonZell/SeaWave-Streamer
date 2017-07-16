package cs499app.cs499mobileapp.helper;

import android.app.Activity;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;

import cs499app.cs499mobileapp.view.PlayerFragment;

/**
 * Created by centa on 7/14/2017.
 */

public class PlayProgressCountDownTimer {

    CountDownTimer cd;
    ProgressBar pB;
    PlayerFragment fragment;
    long totalCountDownMillis;
    long leftOverMillis;
    long tickIntervalMillis;
    boolean isPaused;
    boolean isEnabled;


    public PlayProgressCountDownTimer(PlayerFragment fragment, ProgressBar pB, long totalCountDownMillis, boolean isEnabled) {
        this.fragment = fragment;
        this.pB = pB;
        this.totalCountDownMillis = totalCountDownMillis;
        leftOverMillis = totalCountDownMillis;
        tickIntervalMillis = 15;
        isPaused = false;
        this.isEnabled = isEnabled;
        pB.setMax((int)((totalCountDownMillis)/tickIntervalMillis));
    }


    public void setIntervalMillis(long interval)
    {
        tickIntervalMillis = interval;
    }
    public void setMaxTimeMillis(long totalCountDownMillis)
    {
        this.totalCountDownMillis = totalCountDownMillis;
        pB.setMax((int)((totalCountDownMillis)/tickIntervalMillis));
        if(isEnabled) {
            //if timer is still going from previous run
            if (cd != null)
                cd.cancel();
            if (isPaused == true) {
                isPaused = false;
            } else {
                leftOverMillis = totalCountDownMillis; //reset time
            }
            getNewCountDownTimer();
            //cd.start();
        }

    }

    public void start()
    {
        if(isEnabled) {
            //if timer is still going from previous run
            if (cd != null)
                cd.cancel();
            if (isPaused == true) {
                isPaused = false;
            } else {
                leftOverMillis = totalCountDownMillis; //reset time
            }
            getNewCountDownTimer();
            cd.start();
        }
    }

    public void pause()
    {
        if(isEnabled) {
            cd.cancel();
            isPaused = true;
        }

    }

    public void resume()
    {
        if(isEnabled && isPaused == true)
        {
            getNewCountDownTimer();
            cd.start();
            isPaused = false;
        }
    }

    public void stop()
    {
        if(isEnabled) {
            pB.setProgress(0);
            if (cd != null)
                cd.cancel();
            leftOverMillis = 0;
            isPaused = false;
        }
    }


    private void getNewCountDownTimer()
    {
        long totalTimeMillis = leftOverMillis+1000; //padding

        cd = new CountDownTimer(totalTimeMillis,tickIntervalMillis) {
            @Override
            public void onTick(long lefttime) {
                //pB.setProgress(((int)(lefttime/tickIntervalMillis)-1));
                pB.setProgress(((int)((leftOverMillis/tickIntervalMillis)-tickIntervalMillis)));
                //Log.i("timer:", String.valueOf(lefttime));
                pB.invalidate();
                leftOverMillis -= tickIntervalMillis;
                //leftOverMillis = lefttime;
            }

            @Override
            public void onFinish() {
                pB.setProgress(0);
                fragment.getSkipForwardButton().callOnClick();
            }
        };
    }


    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
