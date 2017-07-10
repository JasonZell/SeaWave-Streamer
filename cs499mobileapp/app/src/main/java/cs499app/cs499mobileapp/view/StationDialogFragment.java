package cs499app.cs499mobileapp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cs499app.cs499mobileapp.R;
import cs499app.cs499mobileapp.helper.DialogActionMode;
import cs499app.cs499mobileapp.model.LibraryRecord;
import cs499app.cs499mobileapp.model.StationRecord;
import cs499app.cs499mobileapp.viewadapter.StationListAdapter;

/**
 * Created by centa on 7/6/2017.
 */

public class StationDialogFragment extends AppCompatDialogFragment {



    private Long parentPlaylistID;
    private LibraryRecord libRecord;
    private StationListAdapter stationListAdapter;
    private DialogActionMode DM;
    private String dialogTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_station_dialog,container,false);
        final EditText addStationTitleEditText = (EditText)rootView.findViewById(R.id.addstationtitle_edittext);
        final EditText addStationUrlEditText = (EditText)rootView.findViewById(R.id.addstationurl_edittext);

        TextView tv = rootView.findViewById(R.id.station_dialog_fragment_title);
        tv.setText(dialogTitle);

        addStationUrlEditText.setText("http://");
        Selection.setSelection(addStationUrlEditText.getText(), addStationUrlEditText.getText().length());


        addStationUrlEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().contains("http://")){
                    addStationUrlEditText.setText("http://");
                    Selection.setSelection(addStationUrlEditText.getText(), addStationUrlEditText.getText().length());

                }

            }
        });


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
                    List<StationRecord>  srl = libRecord.getStationListRecordsMap().get(parentPlaylistID);
                    srl.add(libRecord.insertStationRecord(sr));
                    //refreshLibraryRecordUpdateView();
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

    public Long getParentPlaylistID() {
        return parentPlaylistID;
    }

    public void setParentPlaylistID(Long parentPlaylistID) {
        this.parentPlaylistID = parentPlaylistID;
    }

    private void refreshLibraryRecordUpdateView()
    {
//        libRecord.setStationListRecordsMap(libRecord.getStationListRecordsMap());
        libRecord.importStationRecordList(parentPlaylistID);
        stationListAdapter.notifyDataSetChanged();
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
