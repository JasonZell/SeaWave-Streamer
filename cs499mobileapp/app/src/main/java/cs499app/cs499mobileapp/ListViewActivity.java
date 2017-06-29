package cs499app.cs499mobileapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cs499app.cs499mobileapp.helper.DataAdapter;

/**
 * Created by jason on 6/28/2017.
 */

public class ListViewActivity  extends AppCompatActivity{

    List<String> data;
    private DataAdapter dataAdapter;
    private ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        data = new ArrayList<>();
        data.add("one");
        data.add("two");
        data.add("three");
        data.add("four");

        dataAdapter = new DataAdapter(this,R.layout.activity_listview,data);

        listview = (ListView)findViewById(R.id.listview_view);


        listview.setAdapter(dataAdapter);


    }
}
