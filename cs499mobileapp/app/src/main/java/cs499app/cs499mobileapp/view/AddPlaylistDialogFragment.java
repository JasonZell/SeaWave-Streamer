package cs499app.cs499mobileapp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cs499app.cs499mobileapp.R;

/**
 * Created by centa on 7/6/2017.
 */

public class AddPlaylistDialogFragment extends AppCompatDialogFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_addplaylist_dialog,container,false);
        return rootView;
    }
}
