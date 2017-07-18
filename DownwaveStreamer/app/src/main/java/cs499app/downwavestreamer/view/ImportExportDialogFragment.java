package cs499app.downwavestreamer.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cs499app.downwavestreamer.R;
import cs499app.downwavestreamer.helper.FileOperationMode;



public class ImportExportDialogFragment extends DialogFragment {

    private static final String OP_TYPE = "OPERATION_TYPE";

    // TODO: Rename and change types of parameters
    private int Param;

    private FileOperationMode mode;


    public ImportExportDialogFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ImportExportDialogFragment newInstance(int OperationType) {
        ImportExportDialogFragment fragment = new ImportExportDialogFragment();
        Bundle args = new Bundle();
        args.putInt(OP_TYPE, OperationType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Param = getArguments().getInt(OP_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        if(Param == 0) {
            view = inflater.inflate(R.layout.fragment_import_dialog_layout, container, false);
        }
        else
            view = inflater.inflate(R.layout.fragment_export_dialog_layout, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
