package cs499app.cs499mobileapp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import cs499app.cs499mobileapp.R;
import cs499app.cs499mobileapp.helper.ContextMenuMode;
import cs499app.cs499mobileapp.helper.DialogActionMode;
import cs499app.cs499mobileapp.model.LibraryRecord;
import cs499app.cs499mobileapp.viewadapter.PlaylistAdapter;
import cs499app.cs499mobileapp.viewadapter.StationListAdapter;

/**
 * Created by centa on 7/9/2017.
 */

public class ContextMenuDialogFragment extends AppCompatDialogFragment {

    ContextMenuMode CMM;
    LibraryRecord libRecord;
    private PlaylistAdapter playlistAdapter;
    private StationListAdapter stationlistAdapter;
    //private int listItemPosition;
    public ContextMenuDialogFragment() {
        CMM = ContextMenuMode.NULL_MODE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_context_menu_dialog, container, false);

        TextView modifyActionTextView = rootView.findViewById(R.id.context_menu_modify_action);
        TextView deleteActionTextView = rootView.findViewById(R.id.context_menu_delete_action);

        final FragmentManager fm = getFragmentManager();

        modifyActionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CMM == ContextMenuMode.PLAYLIST_MODE)
                {
                    Toast.makeText(getContext(), "modify action called for playlist", Toast.LENGTH_SHORT).show();
                    PlaylistDialogFragment dialogFm = new PlaylistDialogFragment();
                    Bundle args = new Bundle();
                    args.putInt(getString(R.string.PlayListViewPos),
                            getArguments().getInt(getString(R.string.PlayListViewPos)));
                    dialogFm.setArguments(args);

                    dialogFm.setLibRecord(libRecord);
                    dialogFm.setPlaylistAdapter(playlistAdapter);
                    dialogFm.setDialogActionMode(DialogActionMode.MODIFY_MODE);
                    dialogFm.setDialogTitle("MODIFY PLAYLIST");
                    dialogFm.show(fm,"modifyplaylistFragment");

                }
                else // station view is the one calling the context menu
                {
                    Toast.makeText(getContext(), "modify action called for station", Toast.LENGTH_SHORT).show();
                    StationDialogFragment dialogFm = new StationDialogFragment();
                    Bundle args = new Bundle();
                    args.putInt(getString(R.string.StationListViewPos),
                            getArguments().getInt(getString(R.string.StationListViewPos)));
                    args.putLong(getString(R.string.ParentPlaylistID),
                            getArguments().getLong(getString(R.string.ParentPlaylistID)));
                    dialogFm.setArguments(args);

                    dialogFm.setLibRecord(libRecord);
                    dialogFm.setStationListAdapter(stationlistAdapter);
                    dialogFm.setDialogActionMode(DialogActionMode.MODIFY_MODE);
                    dialogFm.setDialogTitle("MODIFY STATION");
                    dialogFm.show(fm,"modifyStationFragment");

                }
                dismiss();
            }
        });

        deleteActionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CMM == ContextMenuMode.PLAYLIST_MODE)
                {
                    Toast.makeText(getContext(), "delete action called for playlist", Toast.LENGTH_SHORT).show();


                }
                else
                {
                    Toast.makeText(getContext(), "delete action called for station", Toast.LENGTH_SHORT).show();

                }
                dismiss();

            }
        });

        return rootView;
    }

    public ContextMenuMode getContextMenuMode() {
        return CMM;
    }

    public void setContextMenuMode(ContextMenuMode CMM) {
        this.CMM = CMM;
    }

    public LibraryRecord getLibRecord()
    {
        return libRecord;
    }
    public void setLibRecord(LibraryRecord lr)
    {
        this.libRecord = lr;
    }


    public PlaylistAdapter getPlaylistAdapter() {
        return playlistAdapter;
    }

    public void setPlaylistAdapter(PlaylistAdapter playlistAdapter) {
        this.playlistAdapter = playlistAdapter;
    }

    public StationListAdapter getStationlistAdapter() {
        return stationlistAdapter;
    }

    public void setStationlistAdapter(StationListAdapter stationlistAdapter) {
        this.stationlistAdapter = stationlistAdapter;
    }
//
//    public int getListItemPosition() {
//        return listItemPosition;
//    }
//
//    public void setListItemPosition(int listItemPosition) {
//        this.listItemPosition = listItemPosition;
//    }



}
