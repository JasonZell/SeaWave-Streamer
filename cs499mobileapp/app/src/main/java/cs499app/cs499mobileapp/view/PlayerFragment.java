package cs499app.cs499mobileapp.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cs499app.cs499mobileapp.helper.CircularSeekBar.OnCircularSeekBarChangeListener;
import cs499app.cs499mobileapp.R;
import cs499app.cs499mobileapp.helper.CircularSeekBar;
import cs499app.cs499mobileapp.helper.RoundedBitmapDrawableUtility;

/**
 * Created by centa on 6/27/2017.
 */

public class PlayerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("PlayFragmentOnCreate","OnCreatecalled");
        View root = inflater.inflate(R.layout.player_fragment, container, false);
//        ViewPager pager = (ViewPager) root.findViewById(R.id.pager);
//        InnerFragment.MyPagerAdapter adapter = new InnerFragment.MyPagerAdapter(getChildFragmentManager());
//        pager.setAdapter(adapter);

        final CircularSeekBar seekBar = (CircularSeekBar) root.findViewById(R.id.circular_seek_bar);
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

        //fitting image inside circular seekbar

        Bitmap src  = BitmapFactory.decodeResource(root.getResources(), R.drawable.radio_image);
        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(seekBar.getResources(), src);
        dr.setCircular(true);
        ImageView imageView = root.findViewById(R.id.seekbar_image);
        imageView.setMaxHeight(seekBar.getLayoutParams().width - (int)convertDpToPixel(5,root.getContext()));
        imageView.setMaxWidth(seekBar.getLayoutParams().width - (int)convertDpToPixel(5,root.getContext()));



       // imageView.requestLayout();
       // imageView.setLayoutParams(params);
        Log.e("offset","offset"+(int)convertPxToDp(50));
        imageView.setImageDrawable(dr);

        return root;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f); return px;
    }

    public static float convertPxToDp(float px) {
        return px / Resources.getSystem().getDisplayMetrics().density;
    }

    public PlayerFragment() {
    }
    //    private class MyPagerAdapter extends FragmentStatePagerAdapter {
//
//        public MyPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public android.support.v4.app.Fragment getItem(int i) {
//            return PageFragment.newInstance(i+1);
//        }
//
//        @Override
//        public int getCount() {
//            return 3;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return "FRAGMENT " + (position+1) + " TITLE " + (position+1);
//        }
//    }
}
