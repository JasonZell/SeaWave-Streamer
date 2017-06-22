package cs499app.cs499mobileapp;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ObjectAnimator animation = ObjectAnimator.ofInt (progressBar, "progress", 0, 500); // see this max value coming back here, we animale towards that value
        animation.setDuration (5000); //in milliseconds
        animation.setInterpolator (new DecelerateInterpolator());
        animation.start ();
    }
}
