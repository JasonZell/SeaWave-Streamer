package cs499app.cs499mobileapp.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cs499app.cs499mobileapp.R;
import cs499app.cs499mobileapp.model.LibraryRecord;

/**
 * Created by jason on 7/3/2017.
 */

public class ContainerFragment extends android.support.v4.app.Fragment{


    @Nullable
    FragmentManager fm;
    ViewPager viewPager;
    TabLayout tabLayout;
    LibraryRecord libRecord;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);
//        MyPagerAdapter adapter = new MyPagerAdapter(fm);
//        //MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
//        viewPager = (ViewPager) view.findViewById(R.id.main_pager);
//        viewPager.setAdapter(adapter);
//        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager); //layout will use PagerAdapter's page titles
//        tabLayout.getTabAt(0).setIcon(R.drawable.player_icon_selector);
//        tabLayout.getTabAt(1).setIcon(R.drawable.library_icon_selector);


        LibraryFragment libFragment = new LibraryFragment();
        libFragment.setLibRecord(libRecord);

//        Bundle args = new Bundle();
//        args.putInt("StationList", i);
//       libraryFragment.setArguments(args);
//
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container, libFragment,getString(R.string.LibraryFragmentTag));
        transaction.addToBackStack(libFragment.getClass().getName());
        transaction.commit();

        return view;
    }

    public ContainerFragment() {
    }
    public void setFragmentManager(@Nullable FragmentManager fm) {
        this.fm = fm;
    }

    public LibraryRecord getLibRecord() {
        return libRecord;
    }

    public void setLibRecord(LibraryRecord libRecord) {
        this.libRecord = libRecord;
    }
}
