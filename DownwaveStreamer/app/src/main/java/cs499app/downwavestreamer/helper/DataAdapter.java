package cs499app.downwavestreamer.helper;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cs499app.downwavestreamer.R;

/**
 * Created by jason on 6/28/2017.
 */

public class DataAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> data;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.playlist_listview_items, parent,false);

        TextView textview = view.findViewById(R.id.playlist_listview_textview);
        textview.setText(data.get(position));

        return view;
    }

    public DataAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.data = objects;
    }
}
