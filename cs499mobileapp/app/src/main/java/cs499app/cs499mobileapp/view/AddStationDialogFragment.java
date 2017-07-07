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

import cs499app.cs499mobileapp.R;

/**
 * Created by centa on 7/6/2017.
 */

public class AddStationDialogFragment extends AppCompatDialogFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_addstation_dialog,container,false);
        EditText editText = (EditText)rootView.findViewById(R.id.addstationtitle_edittext);

        // show soft keyboard
        editText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setCanceledOnTouchOutside(false);

        Button addbutton = rootView.findViewById(R.id.addstation_addbutton);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
}
