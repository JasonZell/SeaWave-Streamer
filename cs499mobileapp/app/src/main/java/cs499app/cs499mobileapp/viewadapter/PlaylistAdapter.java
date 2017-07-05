package cs499app.cs499mobileapp.viewadapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import cs499app.cs499mobileapp.R;
import cs499app.cs499mobileapp.model.MediaPlaylist;

/**
 * Created by centa on 6/29/2017.
 */

public class PlaylistAdapter extends ArrayAdapter<MediaPlaylist>{

    private Context context;
    private List<MediaPlaylist> playlistDataArray;

    public PlaylistAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<MediaPlaylist> objects) {
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
        playlistNameView.setSelected(true);
        //playlistNameView.setMovementMethod(new ScrollingMovementMethod());

        return view;
    }

    @Nullable
    @Override
    public MediaPlaylist getItem(int position) {

        return super.getItem(position);
    }
}
