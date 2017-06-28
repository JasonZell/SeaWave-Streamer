package cs499app.cs499mobileapp.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cs499app.cs499mobileapp.R;

/**
 * Created by centa on 6/27/2017.
 */

public class LibraryFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.library_fragment, container, false);
//        ViewPager pager = (ViewPager) root.findViewById(R.id.pager);
//        InnerFragment.MyPagerAdapter adapter = new InnerFragment.MyPagerAdapter(getChildFragmentManager());
//        pager.setAdapter(adapter);
        return root;
    }

    public LibraryFragment() {
    }
}
