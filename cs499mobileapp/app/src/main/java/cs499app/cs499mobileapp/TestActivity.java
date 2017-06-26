package cs499app.cs499mobileapp;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.github.lzyzsd.circleprogress.ArcProgress;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ArcProgress arcProgress = (ArcProgress)findViewById(R.id.arc_progress);
        arcProgress.setBottomText("Test");
        arcProgress.setProgress(70);

//        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        ObjectAnimator animation = ObjectAnimator.ofInt (progressBar, "progress", 0, 100); // see this max value coming back here, we animale towards that value
//        animation.setDuration (10000); //in milliseconds
//        animation.setInterpolator (new DecelerateInterpolator());
//        animation.start ();
    }
}
