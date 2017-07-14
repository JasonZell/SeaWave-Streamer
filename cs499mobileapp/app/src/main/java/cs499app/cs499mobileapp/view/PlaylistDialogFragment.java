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
   // private int listItemPosition;


    public PlaylistDialogFragment() {
        DM = DialogActionMode.NULL_MODE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playlist_dialog,container,false);
        final EditText dialogPlaylistTitleEditText = (EditText)rootView.findViewById(R.id.dialog_playlist_title_edittext);
        final Button confirmButton = rootView.findViewById(R.id.dialog_playlist_confirm_button);


        TextView tv = rootView.findViewById(R.id.playlist_dialog_fragment_title);
        tv.setText(dialogTitle);

        initializeView(dialogPlaylistTitleEditText,confirmButton);
        // show soft keyboard
        dialogPlaylistTitleEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setCanceledOnTouchOutside(false);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialogPlaylistTitleEditText.getText().toString().trim().length() == 0)
                {
                    Toast.makeText(getContext(), "Title is empty!", Toast.LENGTH_SHORT).show();
                }
                else {

                    if(DM == DialogActionMode.ADD_MODE) {
                        PlaylistRecord pr = new PlaylistRecord(dialogPlaylistTitleEditText.getText().toString());
                        libRecord.getPlaylistRecords().add(
                                libRecord.insertPlaylistRecord(pr)); //insert into database then add into libRecord object.
                        playlistAdapter.notifyDataSetChanged();
                        Log.i("AddPlaylist","playlist added");
                    }
                    else if(DM == DialogActionMode.MODIFY_MODE)
                    {
                       // PlaylistRecord pr = new PlaylistRecord(dialogPlaylistTitleEditText.getText().toString());
                        PlaylistRecord pr = libRecord.getPlaylistRecords().get(getArguments().getInt(getString(R.string.PlayListViewPos)));
                        pr.setPlaylistName(dialogPlaylistTitleEditText.getText().toString());
                        libRecord.updatePlaylistRecord(pr);
                        playlistAdapter.notifyDataSetChanged();
                        Log.i("ModifyPlaylist","playlist updated");
                    }
                    else //NULL mode
                    {
                        Log.i("ModifyPlaylist","NULL MODE!");

                    }
                    //refreshLibraryRecordUpdateView();
                    dismiss();
                }
                Log.e("addPlaylist","add button clicked");
            }
        });

        Button cancelButton = rootView.findViewById(R.id.dialog_playlist_cancelbutton);
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

    public void initializeView(EditText et, Button confirmButton)
    {
        if(DM == DialogActionMode.ADD_MODE)
        {
            et.setHint("Enter New Playlist Name");
            confirmButton.setText("Add");
        }
        else if (DM == DialogActionMode.MODIFY_MODE)
        {
            confirmButton.setText("Modify");
            et.setText(libRecord.getPlaylistRecords().get(
                    getArguments().getInt(getString(R.string.PlayListViewPos))
            ).getPlaylistName());
        }
    }


}
