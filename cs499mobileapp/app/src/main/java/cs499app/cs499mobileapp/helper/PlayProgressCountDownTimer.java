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
    long updateIntervalMillis;
    boolean isPaused;


    public PlayProgressCountDownTimer(PlayerFragment fragment, ProgressBar pB, long totalCountDownMillis) {
        this.fragment = fragment;
        this.pB = pB;
        this.totalCountDownMillis = totalCountDownMillis;
        leftOverMillis = totalCountDownMillis;
        tickIntervalMillis = 15;
        //updateIntervalMillis = 50;
        isPaused = false;
        pB.setMax((int)(totalCountDownMillis/tickIntervalMillis));

    }


    public void setIntervalMillis(long interval)
    {
        tickIntervalMillis = interval;
    }
    public void setMaxTimeMillis(long totalCountDownMillis)
    {
        this.totalCountDownMillis = totalCountDownMillis;
    }

    public void start()
    {
        //if timer is still going from previous run
        if(cd != null)
            cd.cancel();
        if(isPaused == true) {
            isPaused = false;
        }
        else
        {
            leftOverMillis = totalCountDownMillis; //reset time
        }
        getNewCountDownTimer();
        cd.start();
    }

    public void pause()
    {
        cd.cancel();
        isPaused = true;

    }

    public void resume()
    {
        if(isPaused == true)
        {
            getNewCountDownTimer();
            cd.start();
            isPaused = false;
        }
    }

    public void stop()
    {
        pB.setProgress(0);
        cd.cancel();
        leftOverMillis = 0;
        isPaused = false;
    }


    public void getNewCountDownTimer()
    {
        long totalTimeMillis = leftOverMillis;

        cd = new CountDownTimer(totalTimeMillis,tickIntervalMillis) {
            @Override
            public void onTick(long lefttime) {
                //pB.setProgress(((int)(lefttime/tickIntervalMillis)-1));
                pB.setProgress((int)(leftOverMillis/tickIntervalMillis)-1);
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

}
