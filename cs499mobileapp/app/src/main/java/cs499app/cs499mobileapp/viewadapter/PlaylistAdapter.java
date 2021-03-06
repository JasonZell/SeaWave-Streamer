package cs499app.cs499mobileapp.viewadapter;

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

import cs499app.cs499mobileapp.R;
import cs499app.cs499mobileapp.model.PlaylistRecord;

/**
 * Created by centa on 6/29/2017.
 */

public class PlaylistAdapter extends ArrayAdapter<PlaylistRecord>{

    private Context context;
    private List<PlaylistRecord> playlistDataArray;

    public PlaylistAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<PlaylistRecord> objects) {
        super(context, resource, objects);
        this.context = context;
        this.playlistDataArray = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.playlist_listview_items,parent,false);

        TextView playlistListnumber = view.findViewById(R.id.playlist_listview_listnumber);
        playlistListnumber.setText(String.valueOf(position+1));

        TextView playlistNameView = view.findViewById(R.id.playlist_listview_textview);
        playlistNameView.setText(playlistDataArray.get(position).getPlaylistName());
        playlistNameView.setHorizontallyScrolling(true);
        playlistNameView.setSelected(true);
        //playlistNameView.setMovementMethod(new ScrollingMovementMethod());

        return view;
    }

    @Nullable
    @Override
    public PlaylistRecord getItem(int position) {

        return super.getItem(position);
    }
}
