package cs499app.cs499mobileapp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import cs499app.cs499mobileapp.R;

/**
 * Created by centa on 7/15/2017.
 */

public class FileSizeInputDialogFragment  extends AppCompatDialogFragment {


    private View rootview;
    private Button confirmButton;
    private Button cancelButton;
    private EditText inputEditText;
    private Spinner inputSpinner;
    private static int maxLength = 3;
    private PlayerFragment playerFragmentRef;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_fize_size_input_dialog,container,false);

        inputEditText = rootview.findViewById(R.id.filesize_dialog_input_edittext);
        //inputEditText.setText(String.valueOf(playerFragmentRef.getMaxFileSizeInBytes()));
        inputEditText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        inputEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        inputSpinner = (Spinner) rootview.findViewById(R.id.filesize_dialog_input_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.file_size_unit_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        inputSpinner.setAdapter(adapter);

        retrieveSavedValues();

        confirmButton = rootview.findViewById(R.id.filesize_dialog_confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String spinnerString = (String)inputSpinner.getSelectedItem();
                long num = inputSpinner.getSelectedItemId();
                int value = Integer.parseInt(inputEditText.getText().toString());

                if(num == 0)
                {
                    value *= 1000;
                }
                else if(num == 1)
                {
                    value *= 1000000;
                }
                playerFragmentRef.setMaxFileSizeInBytes(value);
                dismiss();
            }
        });

        cancelButton = rootview.findViewById(R.id.filesize_dialog_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return rootview;

    }

    public void setPlayerFragmentRef(PlayerFragment playerFragmentRef) {
        this.playerFragmentRef = playerFragmentRef;
    }

    public void retrieveSavedValues()
    {
        int value = playerFragmentRef.getMaxFileSizeInBytes();
        if(value >= 1000000) //megabyte territory
        {
            value /=1000000;
            inputEditText.setText("");
            inputEditText.append(String.valueOf(value));
            inputSpinner.setSelection(1);
        }
        else //under 1 megabyte
        {
            value /=1000;
            inputEditText.setText("");
            inputEditText.append(String.valueOf(value));

            inputSpinner.setSelection(0);
        }
    }
}
