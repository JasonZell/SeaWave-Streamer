package cs499app.downwavestreamer.view;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import cs499app.downwavestreamer.R;
import cs499app.downwavestreamer.helper.FileOperationMode;



public class ImportExportDialogFragment extends DialogFragment {

    private static final String OP_TYPE = "OPERATION_TYPE";

    // TODO: Rename and change types of parameters
    private int Param;

    private FileOperationMode mode;
    private View rootView;
    private EditText fileNameEditText;
    private ImportExportDialogCallbackListener callbackListener;

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
            if(Param == 0)
                mode = FileOperationMode.IMPORT_MODE;
            else
                mode = FileOperationMode.EXPORT_MODE;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //setup callback
        try {
            callbackListener = (ImportExportDialogCallbackListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement ImportExportDialogCallbackListener Methods");
        }


        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_importexport_dialog_layout, container, false);
        TextView titleView= rootView.findViewById(R.id.dialog_importexport_title_textview);
        fileNameEditText = rootView.findViewById(R.id.dialog_importexport_edittext);
        Button confirmButton = rootView.findViewById(R.id.dialog_importexport_confirm_button);
        Button cancelButton = rootView.findViewById(R.id.dialog_importexport_cancelbutton);

        fileNameEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setCanceledOnTouchOutside(false);

        if(mode == FileOperationMode.IMPORT_MODE)
        {
            titleView.setText("Restore Library");
        }
        else
        {
            titleView.setText("Backup Library");
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fileName = fileNameEditText.getText().toString();
                if(fileName == "")
                {
                    Toast.makeText(getContext(), "Empty file name, please try again", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (mode == FileOperationMode.IMPORT_MODE) {
                        if(checkFileNameExistence(fileName)) {
                            callbackListener.onImportCalled(fileName);
                            dismiss();
                        }
                        else
                            Toast.makeText(getContext(), "File name does not exist", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(checkFileNameExistence(fileName))
                            Toast.makeText(getContext(), "File name exist, try another name", Toast.LENGTH_SHORT).show();
                        else
                        {
                            callbackListener.onExportCalled(fileName);
                            dismiss();
                        }

                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
//        Dialog dialog = getDialog();
//        if (dialog != null)
//        {
//            int width = ViewGroup.LayoutParams.MATCH_PARENT;
//            int height = ViewGroup.LayoutParams.MATCH_PARENT;
//            dialog.getWindow().setLayout(width, height);
//        }
    }


    public interface ImportExportDialogCallbackListener {

        public void onImportCalled(String fileName);
        public void onExportCalled(String fileName);
    }


    public boolean checkFileNameExistence(String fileName)
    {
        boolean result = false;
        File f = new File(Environment.getExternalStorageDirectory(), getContext().getString(R.string.ROOT_DIRECTORY_NAME)
                +"/Library/"+fileName+".json");
        if (f.exists()) {
            result = true;
        }
        return result;
    }
}
