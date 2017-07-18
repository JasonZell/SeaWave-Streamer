package cs499app.downwavestreamer.view;

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

import cs499app.downwavestreamer.R;
import cs499app.downwavestreamer.helper.DialogActionMode;
import cs499app.downwavestreamer.model.LibraryRecord;
import cs499app.downwavestreamer.model.StationRecord;
import cs499app.downwavestreamer.viewadapter.StationListAdapter;

/**
 * Created by centa on 7/6/2017.
 */

public class StationDialogFragment extends AppCompatDialogFragment {



    private Long parentPlaylistID;
    private int parentPlaylistViewID;
    private LibraryRecord libRecord;
    private StationListAdapter stationListAdapter;
    private DialogActionMode DM;
    private String dialogTitle;
    StationDialogCallbackListener dialogCallbackListener;
   // private int listItemPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_station_dialog,container,false);
        final EditText StationTitleEditText = (EditText)rootView.findViewById(R.id.dialog_stationtitle_edittext);
        final EditText StationUrlEditText = (EditText)rootView.findViewById(R.id.dialog_stationurl_edittext);
        final Button confirmButton = rootView.findViewById(R.id.dialog_station_confirm_button);
        parentPlaylistID = getArguments().getLong(getString(R.string.ParentPlaylistID));
        parentPlaylistViewID = getArguments().getInt(getString(R.string.PlayListViewPos));

        //setup callback
        try {
            dialogCallbackListener = (StationDialogCallbackListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement StationDialogCallbackListener Methods");
        }

        TextView tv = rootView.findViewById(R.id.station_dialog_fragment_title);
        tv.setText(dialogTitle);
        initializeView(StationTitleEditText,StationUrlEditText,confirmButton);
        Selection.setSelection(StationUrlEditText.getText(), StationUrlEditText.getText().length());
        StationUrlEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().contains("http://")){
                    StationUrlEditText.setText("http://");
                    Selection.setSelection(StationUrlEditText.getText(), StationUrlEditText.getText().length());
                }
            }
        });

        // show soft keyboard
        StationTitleEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setCanceledOnTouchOutside(false);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StationTitleEditText.getText().toString().trim().length() == 0)
                {
                    Toast.makeText(getContext(), "Title is empty!", Toast.LENGTH_SHORT).show();
                }
                else {

                    if(DM == DialogActionMode.ADD_MODE) {
                        StationRecord sr = new StationRecord(
                                parentPlaylistID,
                                StationTitleEditText.getText().toString(),
                                StationUrlEditText.getText().toString());
                        List<StationRecord> srl = libRecord.getStationListRecordsMap().get(parentPlaylistID);

                        srl.add(libRecord.insertStationRecord(sr));
                        stationListAdapter.notifyDataSetChanged();
                        Log.i("AddStation","station added at location ");

                        dialogCallbackListener.onStationAdded(parentPlaylistID,parentPlaylistViewID);


                        //refreshLibraryRecordUpdateView();
                    }
                    else if(DM == DialogActionMode.MODIFY_MODE)
                    {

                        Log.i("STATIIONVEWPOS :","" + getArguments().getInt(
                                getString(R.string.StationListViewPos)));
                        Log.i("PARENTPLAYID :","" + parentPlaylistID);

                        parentPlaylistID = getArguments().getLong(getString(R.string.ParentPlaylistID));
                            StationRecord sr = libRecord.getStationListRecordsMap()
                                    .get(parentPlaylistID).get(
                                            getArguments().getInt(
                                                    getString(R.string.StationListViewPos)));
                            sr.setStationTitle(StationTitleEditText.getText().toString());
                            sr.setStationURL(StationUrlEditText.getText().toString());
                            libRecord.updateStationRecord(sr);
                        stationListAdapter.notifyDataSetChanged();
                        Log.i("ModifyStation","station modified");
                    }
                    dismiss();
                }

                Log.e("addStation","add button clicked");
            }
        });

        Button cancelButton = rootView.findViewById(R.id.dialog_station_cancel_button);
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

//    public int getListItemPosition() {
//        return listItemPosition;
//    }
//
//    public void setListItemPosition(int listItemPosition) {
//        this.listItemPosition = listItemPosition;
//    }
    private void initializeView(EditText titleEditText, EditText urlEditText,Button confirmButton)
    {
        if(DM == DialogActionMode.ADD_MODE) {
            titleEditText.setHint("Enter New Station Name");
            urlEditText.setText("http://");
            confirmButton.setText("Add");
        }
        else if(DM == DialogActionMode.MODIFY_MODE)
        {
            confirmButton.setText("Modify");
            titleEditText.setText(libRecord.getStationListRecordsMap()
                    .get(getArguments().getLong(getString(R.string.ParentPlaylistID)))
                    .get(getArguments().getInt(
                            getString(R.string.StationListViewPos)))
                    .getStationTitle());
            urlEditText.setText(libRecord.getStationListRecordsMap()
                    .get(getArguments().getLong(getString(R.string.ParentPlaylistID)))
                    .get(getArguments().getInt(
                            getString(R.string.StationListViewPos)))
                    .getStationURL());
        }

    }

    public interface StationDialogCallbackListener {
        public void onStationAdded(long parentPlayListID, int parentPlaylistViewID);
    }

}
