package com.example.finalsih;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewListItem extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ArrayList<String> mobileArray = new ArrayList<String>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_item);

//        mobileArray.add("Hello");

        db.collection("Location")

                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("", "listen:error", e);
                            return;
                        }
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d("1", "New city: " + dc.getDocument().getData());
                                    Log.d("1111", "id: " + dc.getDocument().getId());
                                    Log.d("1", "New city: " + dc.getDocument().getData().get("Name"));
                                    mobileArray.add(dc.getDocument().getData().get("Name").toString());
                                    Log.d("1234","ADDED");
                                    assign();
                                    break;
                                case MODIFIED:
                                    Log.d("1", "Modified city: " + dc.getDocument().getData());
                                    Log.d("123", "Removed city: "+ mobileArray);
                                    mobileArray.remove(dc.getDocument().getData().get("Name").toString());
                                    mobileArray.add(dc.getDocument().getData().get("Name").toString());
                                    Log.d("123", "Removed city: "+ mobileArray);
                                    Log.d("1234","MODIFIED");
                                    assign();
                                    break;
                                case REMOVED:
                                    Log.d("12", "Removed city: " + dc.getDocument().getData());
                                    Log.d("123", "Removed city: "+ mobileArray);
                                    mobileArray.remove(dc.getDocument().getData().get("Name").toString());
                                    Log.d("123", "Removed city: "+ mobileArray);
                                    Log.d("1234","REMOVED");
                                    assign();
                                    break;
                            }
                        }

                    }
                });





    }
    public void assign(){
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, mobileArray);
        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);

    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
        // Then you start a new Activity via Intent
        Intent intent = new Intent();
        intent.setClass(this, ListItemDetail.class);
        intent.putExtra("position", position);
        intent.putExtra("Name", mobileArray.get(position));
        Log.d("abcd",mobileArray.get(position));
        // Or / And
        intent.putExtra("id", id);
        startActivity(intent);
    }
}