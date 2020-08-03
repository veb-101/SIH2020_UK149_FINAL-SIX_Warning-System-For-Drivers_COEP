package com.example.finalsih;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity {
    private Button add;
    private TextView textView;
    private static final int REQUEST_IMAGE_CAPTURE = 1888;
    private StorageReference mStorageReference;
    private ProgressDialog mProgress;
    private Uri filePath;
    private Bitmap bitmap;
    private FusedLocationProviderClient fusedLocationProvider;
    private ImageView mImageLabel;
    //  code added by khem
    private static final int CAMERA_REQUEST = 1888;
    //    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

//    firestore ref
    final FirebaseFirestore db = FirebaseFirestore.getInstance();


    String imageUrl = "";
    String userName = "";
    String userAddress = "";
    String userArea = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageLabel = findViewById(R.id.mImageLabel);

        add = (Button) findViewById(R.id.add);
        textView = findViewById(R.id.textView);
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(this);

        Bundle bundle = getIntent().getExtras();
        //        Extracting data
        userName = bundle.getString("Name");
        userAddress = bundle.getString("Address");
        userArea = bundle.getString("Area");


        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
//                Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                }

                //code added by khem

                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }


            }
        });
    }

    //code added by khem

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Log.d("asdsss", "asasdd");
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageLabel.setImageBitmap(imageBitmap);

            FirebaseOptions opts = FirebaseApp.getInstance().getOptions();
            Log.i("TAGbucket", "Bucket = " + opts.getStorageBucket());

            // Create a storage reference from our app
            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://sihlocationadding.appspot.com");

// Create a reference to "mountains.jpg"
            StorageReference mountainsRef = storageRef.child("newPlace.jpg");

// Create a reference to 'images/mountains.jpg'
            StorageReference mountainImagesRef = storageRef.child("images/newPlace.jpg");

// While the file names are the same, the references point to different files
            mountainsRef.getName().equals(mountainImagesRef.getName());    // true
            mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false


            // Get the data from an ImageView as bytes
            mImageLabel.setDrawingCacheEnabled(true);
            mImageLabel.buildDrawingCache();
//            Bitmap bitmap = mImageLabel.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] datanew = baos.toByteArray();

            UploadTask uploadTask = mountainsRef.putBytes(datanew);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                    imageUrl = downloadUrl.toString();
                    Log.d("assdd",imageUrl);
                    sendLocation();
                }
            });

        }
    }

    private void sendLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission.
            return;
        }fusedLocationProvider.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {

                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
//                            Toast.makeText(MainActivity.this, (int) latitude, Toast.LENGTH_SHORT).show();
                            textView.setText("Latitude =" + latitude + "\nLongitude =" + longitude);
                            Map<String, Object> New_Location = new HashMap<>();
                            New_Location.put("Name", userName);
                            New_Location.put("Address", userAddress);
                            New_Location.put("Area", userArea);
                            New_Location.put("lat", latitude);
                            New_Location.put("lng", longitude);
                            New_Location.put("Image", imageUrl);
                            New_Location.put("Yes", 0);
                            New_Location.put("No", 0);


                         db.collection("Location")
                        .add(New_Location)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("ssddf", "DocumentSnapshot successfully written!");
                                startActivity(new Intent(MainActivity.this,Start_Activity.class));
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("sdfdgfg", "Error adding document", e);
                            }
                        });


                        }
                    }
                });
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
//
//            mProgress.setMessage("Uploading Image...");
//            mProgress.show();
//
//            Uri uri = data.getData();
//            StorageReference filepath = mStorageReference.child("Photo").child(uri.getLastPathSegment());
//
//            filepath.putFile(uri)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                    mProgress.dismiss();
//
//                    Toast.makeText(MainActivity.this, "Uploading Finished...", Toast.LENGTH_LONG).show();
//
//                }
//
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle unsuccessful uploads
//                    Toast.makeText(MainActivity.this,"Error occured.",Toast.LENGTH_LONG).show();
//
//                }
//            });
//
//
//        }
//        else
//        {
//            Log.d("aaaa", String.valueOf(requestCode));
//        }
//    }
}