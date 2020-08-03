package com.example.finalsih;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Start_Activity extends AppCompatActivity {

    private Button addPlace,verifyPlace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_);
        addPlace = (Button) findViewById(R.id.addPlace);
        verifyPlace = (Button) findViewById(R.id.VerifyPlace);


        addPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //After clicking LoginActivity should start
                // startActivity(new Intent(From which class , to which class));
                startActivity(new Intent(Start_Activity.this, UserDetails.class));
                finish();
            }
        });

        verifyPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //After clicking LoginActivity should start
                // startActivity(new Intent(From which class , to which class));
                startActivity(new Intent(Start_Activity.this, ViewListItem.class));
                finish();
            }
        });
    }
}
