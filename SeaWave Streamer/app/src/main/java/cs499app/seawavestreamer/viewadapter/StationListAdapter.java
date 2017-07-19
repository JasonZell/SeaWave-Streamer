package cs499app.seawavestreamer.viewadapter;

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

import cs499app.seawavestreamer.R;
import cs499app.seawavestreamer.model.StationRecord;

/**
 * Created by centa on 6/29/2017.
 */

public class StationListAdapter extends ArrayAdapter<StationRecord>{

    //LibraryCallbackListener callbackListener;

    private Context context;
    private List<StationRecord> playlistDataArray;

    public StationListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<StationRecord> objects) {
        super(context, resource, objects);
        this.context = context;
        this.playlistDataArray = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.stationlist_listview_items,parent,false);

        TextView stationListnumber = view.findViewById(R.id.statiolist_listview_listnumber);
        stationListnumber.setText(String.valueOf(position+1));

        TextView playlistNameView = view.findViewById(R.id.stationlist_listview_textview);
        playlistNameView.setText(playlistDataArray.get(position).getStationTitle());
        playlistNameView.setHorizontallyScrolling(true);
        playlistNameView.setSelected(true);
        //playlistNameView.setMovementMethod(new ScrollingMovementMethod());


        return view;
    }

    @Nullable
    @Override
    public StationRecord getItem(int position) {

        return super.getItem(position);
    }
//
//    public interface LibraryCallbackListener {
//        public void onPlayStationButtonPressed(
//                long playListViewID, long stationViewID);
//        public void onPlayAllStationButtonPressed(
//                long playListViewID);
//    }


}
