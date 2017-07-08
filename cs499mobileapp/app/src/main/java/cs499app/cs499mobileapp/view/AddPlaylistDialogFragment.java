package cs499app.cs499mobileapp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import cs499app.cs499mobileapp.R;
import cs499app.cs499mobileapp.model.LibraryRecord;
import cs499app.cs499mobileapp.model.PlaylistRecord;
import cs499app.cs499mobileapp.viewadapter.PlaylistAdapter;

/**
 * Created by centa on 7/6/2017.
 */

public class AddPlaylistDialogFragment extends AppCompatDialogFragment {



    private  LibraryRecord libRecord;



    private PlaylistAdapter playlistAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_addplaylist_dialog,container,false);
        final EditText editText = (EditText)rootView.findViewById(R.id.addplaylist_edittext);

        // show soft keyboard
        editText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setCanceledOnTouchOutside(false);
        Button addbutton = rootView.findViewById(R.id.addplaylist_addbutton);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaylistRecord pr = new PlaylistRecord(editText.getText().toString());
                libRecord.insertPlaylistRecord(pr);
                libRecord.importlPlaylistRecordList();
                playlistAdapter.notifyDataSetInvalidated();
                playlistAdapter.notifyDataSetChanged();
                dismiss();
                Log.e("addPlaylist","add button clicked");

            }
        });

        Button cancelButton = rootView.findViewById(R.id.addplaylist_cancelbutton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return rootView;
    }

    public LibraryRecord getLibRecord() {
        return libRecord;
    }

    public void setLibRecord(LibraryRecord libRecord) {
        this.libRecord = libRecord;
    }

    public PlaylistAdapter getPlaylistAdapter() {
        return playlistAdapter;
    }

    public void setPlaylistAdapter(PlaylistAdapter playlistAdapter) {
        this.playlistAdapter = playlistAdapter;
    }
}
