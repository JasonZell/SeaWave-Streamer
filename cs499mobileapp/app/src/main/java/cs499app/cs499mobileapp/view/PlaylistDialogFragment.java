package cs499app.cs499mobileapp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cs499app.cs499mobileapp.R;
import cs499app.cs499mobileapp.helper.DialogActionMode;
import cs499app.cs499mobileapp.model.LibraryRecord;
import cs499app.cs499mobileapp.model.PlaylistRecord;
import cs499app.cs499mobileapp.viewadapter.PlaylistAdapter;

/**
 * Created by centa on 7/6/2017.
 */

public class PlaylistDialogFragment extends AppCompatDialogFragment {

    private  LibraryRecord libRecord;
    private PlaylistAdapter playlistAdapter;
    private DialogActionMode DM;
    private String dialogTitle;
    public PlaylistDialogFragment() {
        DM = DialogActionMode.NULL_MODE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playlist_dialog,container,false);
        final EditText editText = (EditText)rootView.findViewById(R.id.addplaylist_edittext);

        TextView tv = rootView.findViewById(R.id.playlist_dialog_fragment_title);
        tv.setText(dialogTitle);

        // show soft keyboard
        editText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setCanceledOnTouchOutside(false);
        Button addbutton = rootView.findViewById(R.id.addplaylist_addbutton);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().trim().length() == 0)
                {
                    Toast.makeText(getContext(), "Title is empty!", Toast.LENGTH_SHORT).show();
                }
                else {
                    PlaylistRecord pr = new PlaylistRecord(editText.getText().toString());
                    libRecord.insertPlaylistRecord(pr);
                    refreshLibraryRecordUpdateView();
                    dismiss();
                }
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

    public void refreshLibraryRecordUpdateView()
    {
        libRecord.importlPlaylistRecordList();
        playlistAdapter.notifyDataSetChanged();
    }

    public DialogActionMode getDialogActionMode() {
        return DM;
    }

    public void setDialogActionMode(DialogActionMode DM) {
        this.DM = DM;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }
}
