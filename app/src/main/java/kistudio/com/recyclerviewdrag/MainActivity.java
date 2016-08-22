package kistudio.com.recyclerviewdrag;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<RecyclerObject> contacts;
    MainRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        RecyclerView rv = (RecyclerView) findViewById(R.id.rvMain);
        assert rv != null;
        rv.setLayoutManager(new LinearLayoutManager(this));
        contacts = new ArrayList<>();
//        ProviderUtils.fillContacts(this, contacts);
        ProviderUtils.getMusic(this, contacts);
        adapter = new MainRecyclerAdapter(this, contacts);
        rv.setAdapter(adapter);
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.listener.onStopActivity();
    }

    //    private void fillArrayList(ArrayList<String> contacts) {
//        for (int i =0; i<25; i++){
//            contacts.add("Position - "+i);
//        }
//    }


}
