package cs499app.cs499mobileapp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import cs499app.cs499mobileapp.R;
import cs499app.cs499mobileapp.helper.ContextMenuMode;

/**
 * Created by centa on 7/9/2017.
 */

public class ContextMenuDialogFragment extends AppCompatDialogFragment {

    ContextMenuMode CMM;

    public ContextMenuDialogFragment() {
        CMM = ContextMenuMode.NULL_MODE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_context_menu_dialog, container, false);

        TextView modifyActionTextView = rootView.findViewById(R.id.context_menu_modify_action);
        TextView deleteActionTextView = rootView.findViewById(R.id.context_menu_delete_action);

        modifyActionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CMM == ContextMenuMode.PLAYLIST_MODE)
                {
                    Toast.makeText(getContext(), "modify action called for playlist", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), "modify action called for station", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });

        deleteActionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CMM == ContextMenuMode.PLAYLIST_MODE)
                {
                    Toast.makeText(getContext(), "delete action called for playlist", Toast.LENGTH_SHORT).show();


                }
                else
                {
                    Toast.makeText(getContext(), "delete action called for station", Toast.LENGTH_SHORT).show();

                }
                dismiss();

            }
        });

        return rootView;
    }

    public ContextMenuMode getContextMenuMode() {
        return CMM;
    }

    public void setContextMenuMode(ContextMenuMode CMM) {
        this.CMM = CMM;
    }




}
