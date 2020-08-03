package com.example.finalsih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserDetails extends AppCompatActivity {

    private Button add;
    private EditText name;
    private EditText address;
    private  EditText area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        add = (Button) findViewById(R.id.addPlace);
        name = (EditText) findViewById(R.id.name);
        address = (EditText) findViewById(R.id.address);
        area = (EditText) findViewById(R.id.area);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserDetails.this, MainActivity.class);
                String userName = name.getText().toString();
                String userAddress = address.getText().toString();
                String userArea = area.getText().toString();

                //Create the bundle
                Bundle bundle = new Bundle();

                //Add your data to bundle
                bundle.putString("Name", userName);
                bundle.putString("Address", userAddress);
                bundle.putString("Area", userArea);


                //Add the bundle to the intent
                i.putExtras(bundle);

                //After clicking LoginActivity should start
                startActivity(i);
                finish();


            }
        });

    }
}
