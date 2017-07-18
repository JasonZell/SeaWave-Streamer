package cs499app.downwavestreamer.view;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import cs499app.downwavestreamer.R;
import cs499app.downwavestreamer.helper.PlayProgressCountDownTimer;

public  class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    int maxDurationInSeconds;
    PlayProgressCountDownTimer timer;
    PlayerFragment playerFragmentRef;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        // Calendar c = Calendar.getInstance();
//        int hour = 0;
//        int minute = 5;

        maxDurationInSeconds = getArguments()
                .getInt(getString(R.string.SETTING_MAX_PLAY_PROGRESS_SECONDS));
        int hours = maxDurationInSeconds / 3600;
        int minutes = (maxDurationInSeconds % 3600) / 60;
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hours, minutes,
                true);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        int totalSecond = hourOfDay *3600;
        totalSecond += minute*60;
        maxDurationInSeconds = totalSecond;
        timer.setMaxTimeMillis(maxDurationInSeconds*1000);
        playerFragmentRef.setMaxPlayDurationInSeconds(maxDurationInSeconds);
        //Toast.makeText(this.getContext(), "Max Seconds set:"+maxDurationInSeconds, Toast.LENGTH_SHORT).show();
    }

    public void setPlayProgressCountDownTimer(PlayProgressCountDownTimer t)
    {
        timer = t;
    }

    public void setPlayerFragmentRef(PlayerFragment playerFragmentRef) {
        this.playerFragmentRef = playerFragmentRef;
    }
}