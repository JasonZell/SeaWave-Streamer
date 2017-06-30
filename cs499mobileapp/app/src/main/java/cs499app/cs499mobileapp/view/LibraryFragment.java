package cs499app.cs499mobileapp.view;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cs499app.cs499mobileapp.R;
import cs499app.cs499mobileapp.model.MediaPlaylist;
import cs499app.cs499mobileapp.viewadapter.PlaylistAdapter;

/**
 * Created by centa on 6/27/2017.
 */

public class LibraryFragment extends Fragment{

    private List<MediaPlaylist> mediaPlaylist;
    private PlaylistAdapter playlistAdapter;
    private ListView playlistListview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.library_fragment, container, false);

        loadPlaylistLibrary();
        playlistListview = root.findViewById(R.id.listview_playlist);
        playlistAdapter = new PlaylistAdapter(this.getContext(),R.layout.playlist_listview_items,mediaPlaylist);
        playlistListview.setAdapter(playlistAdapter);

//        ViewPager pager = (ViewPager) root.findViewById(R.id.pager);
//        InnerFragment.MyPagerAdapter adapter = new InnerFragment.MyPagerAdapter(getChildFragmentManager());
//        pager.setAdapter(adapter);
        return root;
    }

    public LibraryFragment() {

    }

    private void loadPlaylistLibrary()
    {
        mediaPlaylist = new ArrayList<>();
        mediaPlaylist.add(new MediaPlaylist("First Station", R.drawable.radio_demo));
        mediaPlaylist.add(new MediaPlaylist("Second Station", R.drawable.radio_demo));
        mediaPlaylist.add(new MediaPlaylist("Third Station", R.drawable.radio_demo));
        mediaPlaylist.add(new MediaPlaylist("This is a very very very long station name, scrolling", R.drawable.radio_demo));

    }
}
