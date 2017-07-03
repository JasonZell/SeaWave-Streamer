package cs499app.cs499mobileapp.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cs499app.cs499mobileapp.R;

/**
 * Created by jason on 7/3/2017.
 */

public class MainFragment extends android.support.v4.app.Fragment{


    @Nullable
    FragmentManager fm;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        MyPagerAdapter adapter = new MyPagerAdapter(fm);
        //MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
        viewPager = (ViewPager) view.findViewById(R.id.main_pager);
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager); //layout will use PagerAdapter's page titles
        tabLayout.getTabAt(0).setIcon(R.drawable.player_icon_selector);
        tabLayout.getTabAt(1).setIcon(R.drawable.library_icon_selector);
        return view;
    }

    public MainFragment() {
    }
    public void setFragmentManager(@Nullable FragmentManager fm) {
        this.fm = fm;
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {

            switch (i){
                case 0:
                    Log.d("return PlayerFragment","return playerfrag");
                    return new PlayerFragment();
                case 1:
                    Log.d("return LibFragment","return Libfrag");
                    return new LibraryFragment();
                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return "TITLE " + (position+1);
//        }
    }
}
