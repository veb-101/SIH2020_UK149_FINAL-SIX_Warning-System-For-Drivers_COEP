package com.example.finalsih;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class ListItemDetail extends AppCompatActivity {

   public String idd = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item_detail);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        final String Name = intent.getStringExtra("Name");
        db.collection("Location")
                .whereEqualTo("Name", Name)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }
                        for (QueryDocumentSnapshot doc : value) {
                            Log.d("data", "data");
//
                            idd = doc.getId();
                                String area= doc.getData().get("Area").toString();
                                String address = doc.getData().get("Address").toString();
                                TextView nam = (TextView) findViewById(R.id.names);
                                nam.setText("Name: "+Name); //set text for text view
                                TextView add = (TextView) findViewById(R.id.address);
                                add.setText("Area: "+area); //set text for text view
                                TextView areaa = (TextView) findViewById(R.id.area);
                                areaa.setText("Address: "+address); //set text for text view
                            ImageView img;
                            img = (ImageView) findViewById(R.id.img);
                            Picasso.get().load(doc.getData().get("Image").toString()).into(img);
//                            Bitmap bmp = null;
//                            try {

//                                Log.d("Error", doc.getData().get("Image").toString());
//                                InputStream in = new java.net.URL((String) doc.getData().get("Image")).openStream();
//                                bmp = BitmapFactory.decodeStream(in);
//                                Log.d("Error", doc.getData().get("Image").toString());
//                                Log.d("bmp", bmp.toString());
//                            } catch (Exception ee) {
////                                Log.e("Error", ee.getMessage());
//                                Log.d("Error", "error hai");
//                                ee.printStackTrace();
//                            }
//                            img.setImageBitmap(bmp);

                        }

                    }
                });


//        mPostReference.addValueEventListener(postListener);
//        TextView textView = (TextView) findViewById(R.id.names);
//        textView.setText("Name: "+Name); //set text for text view
    }
  public void Yes(View view)
{
    Log.d("data", idd);
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference washingtonRef = db.collection("Location").document(idd);
//// Atomically increment the population of the city by 50.
    washingtonRef.update("Yes", FieldValue.increment(1));
    Toast.makeText(this, "Your input has been recorded successfully.", Toast.LENGTH_SHORT).show();
    Intent i = new Intent(ListItemDetail.this,Start_Activity.class);


    startActivity(i);




}
    public void No(View view)
    {
        Log.d("dataa", idd);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference washingtonRef = db.collection("Location").document(idd);

//// Atomically increment the population of the city by 50.
        washingtonRef.update("No", FieldValue.increment(1));
        Toast.makeText(this, "Your input has been recorded successfully.", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(ListItemDetail.this,MainActivity.class);
        startActivity(i);

    }

//
}
