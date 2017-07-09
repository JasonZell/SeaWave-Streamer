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
import android.widget.Toast;

import cs499app.cs499mobileapp.R;
import cs499app.cs499mobileapp.model.LibraryRecord;
import cs499app.cs499mobileapp.model.PlaylistRecord;
import cs499app.cs499mobileapp.model.StationRecord;
import cs499app.cs499mobileapp.viewadapter.PlaylistAdapter;
import cs499app.cs499mobileapp.viewadapter.StationListAdapter;

/**
 * Created by centa on 7/6/2017.
 */

public class AddStationDialogFragment extends AppCompatDialogFragment {



    private int parentPlaylistID;
    private LibraryRecord libRecord;
    private StationListAdapter stationListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_addstation_dialog,container,false);
        final EditText addStationTitleEditText = (EditText)rootView.findViewById(R.id.addstationtitle_edittext);
        final EditText addStationUrlEditText = (EditText)rootView.findViewById(R.id.addstationurl_edittext);

        // show soft keyboard
        addStationTitleEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setCanceledOnTouchOutside(false);

        Button addbutton = rootView.findViewById(R.id.addstation_addbutton);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addStationTitleEditText.getText().toString().trim().length() == 0)
                {
                    Toast.makeText(getContext(), "Title is empty!", Toast.LENGTH_SHORT).show();
                }
                else {
                    StationRecord sr = new StationRecord(
                            parentPlaylistID,
                            addStationTitleEditText.getText().toString(),
                            addStationUrlEditText.getText().toString());
                    libRecord.insertStationRecord(sr);
                    refreshLibraryRecordUpdateView();
                    dismiss();
                }

                Log.e("addStation","add button clicked");
            }
        });

        Button cancelButton = rootView.findViewById(R.id.addstation_cancelbutton);
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

    public StationListAdapter getStationListAdapter() {
        return stationListAdapter;
    }

    public void setStationListAdapter(StationListAdapter stationListAdapter) {
        this.stationListAdapter = stationListAdapter;
    }

    public int getParentPlaylistID() {
        return parentPlaylistID;
    }

    public void setParentPlaylistID(int parentPlaylistID) {
        this.parentPlaylistID = parentPlaylistID;
    }

    private void refreshLibraryRecordUpdateView()
    {
        libRecord.importStationRecordList(parentPlaylistID);
        stationListAdapter.notifyDataSetChanged();
    }
}
