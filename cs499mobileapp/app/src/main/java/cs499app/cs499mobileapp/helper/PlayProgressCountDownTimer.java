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


    public PlayProgressCountDownTimer(PlayerFragment fragment, ProgressBar pB, long totalCountDownMillis) {
        this.fragment = fragment;
        this.pB = pB;
        this.totalCountDownMillis = totalCountDownMillis;
        leftOverMillis = 0;
        tickIntervalMillis = 1000;
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
        if(leftOverMillis > 0)
            cd.cancel();
        leftOverMillis = 0;

        pB.setMax((int)totalCountDownMillis/1000);
        getNewCountDownTimer();
        cd.start();
    }

    public void pause()
    {
        cd.cancel();

    }

    public void resume()
    {
        if(leftOverMillis > 0)
        {
            getNewCountDownTimer();
        }
    }

    public void stop()
    {
        pB.setProgress(0);
        cd.cancel();
        leftOverMillis = 0;
    }

    public void restart()
    {
        pB.setMax((int)totalCountDownMillis/1000);
        getNewCountDownTimer();
        cd.start();
    }

    public void getNewCountDownTimer()
    {
        long totalTimeMillis = totalCountDownMillis;
        if( leftOverMillis > 0)
            totalTimeMillis = leftOverMillis;

        cd = new CountDownTimer(totalTimeMillis,tickIntervalMillis) {
            @Override
            public void onTick(long lefttime) {
                pB.setProgress(((int)(lefttime/tickIntervalMillis)-1));
                //Log.i("timer:", String.valueOf(lefttime));
                pB.invalidate();
                leftOverMillis = lefttime;
            }

            @Override
            public void onFinish() {
                pB.setProgress(0);
                fragment.getSkipForwardButton().callOnClick();
            }
        };
    }

}
