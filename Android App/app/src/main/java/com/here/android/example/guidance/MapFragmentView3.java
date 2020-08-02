

package com.here.android.example.guidance;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.libraries.places.api.Places;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.FirebaseApp;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPolygon;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.IconCategory;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapCircle;

import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapPolygon;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.routing.CoreRouter;
import com.here.android.mpa.routing.Maneuver;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;
import com.here.android.mpa.routing.RouteWaypoint;
import com.here.android.mpa.routing.RoutingError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

//interfaces and classes for current speed start
interface IBaseGpsListener extends LocationListener, GpsStatus.Listener {

    public void onLocationChanged(Location location);

    public void onProviderDisabled(String provider);

    public void onProviderEnabled(String provider);

    public void onStatusChanged(String provider, int status, Bundle extras);

    public void onGpsStatusChanged(int event);

}


class CLocation extends Location {

    private boolean bUseMetricUnits = false;

    public CLocation(Location location) {
        this(location, true);
    }

    public CLocation(Location location, boolean bUseMetricUnits) {
        // TODO Auto-generated constructor stub
        super(location);
        this.bUseMetricUnits = bUseMetricUnits;
    }


    public boolean getUseMetricUnits() {
        return this.bUseMetricUnits;
    }

    public void setUseMetricunits(boolean bUseMetricUntis) {
        this.bUseMetricUnits = bUseMetricUntis;
    }

    @Override
    public float distanceTo(Location dest) {
        // TODO Auto-generated method stub
        float nDistance = super.distanceTo(dest);
        if (!this.getUseMetricUnits()) {
            //Convert meters to feet
            nDistance = nDistance * 3.28083989501312f;
        }
        return nDistance;
    }

    @Override
    public float getAccuracy() {
        // TODO Auto-generated method stub
        float nAccuracy = super.getAccuracy();
        if (!this.getUseMetricUnits()) {
            //Convert meters to feet
            nAccuracy = nAccuracy * 3.28083989501312f;
        }
        return nAccuracy;
    }

    @Override
    public double getAltitude() {
        // TODO Auto-generated method stub
        double nAltitude = super.getAltitude();
        if (!this.getUseMetricUnits()) {
            //Convert meters to feet
            nAltitude = nAltitude * 3.28083989501312d;
        }
        return nAltitude;
    }

    @Override
    public float getSpeed() {
        // TODO Auto-generated method stub
        float nSpeed = super.getSpeed() * 3.6f;
        if (!this.getUseMetricUnits()) {
            //Convert meters/second to miles/hour
            nSpeed = nSpeed * 3.6f;
        }
        return nSpeed;
    }


}

//interfaces and classes for current speed stop



public class MapFragmentView3 extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        IBaseGpsListener,
        TextToSpeech.OnInitListener {
    //Variable
    private static final String TAG = "TTS-BasicMapActivity";
    private Map map = null;
    public int speed=80,k=0;
    public   ToneGenerator  toneGen1;
    private MapFragment mapFragment = null;
    public Button create1,create2,addplace;
    private Button simulation;
    private Button navigation;
    private Boolean isNavigation=false;
    private boolean firstPositionSet = false;
    private MapRoute currentRoute;
    private GeoBoundingBox m_geoBoundingBox;
    private int MY_DATA_CHECK_CODE = 0;
    LocationRequest mLocationRequest;
    int INTERVAL = 1000;
    int FASTEST_INTERVAL = 500;
    int countspeak=0,countspeak1=0;
    FloatingActionButton floatingActionButton;
    private TextView source,destination;
    private GoogleApiClient client,client1;
    private Maneuver m;
    double a;
    String aaspeed;
    String hospital;
    private TextToSpeech myTTS;
    double b;
    private MapMarker m_map_marker1;
    private MapMarker m_map_marker;
    ProgressDialog pd;
    private   String strCurrentSpeed;
    List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME);
    private static final int LOCATION_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;


    private PositioningManager.OnPositionChangedListener mapPositionHandler = new PositioningManager.OnPositionChangedListener() {
        @Override
        public void onPositionUpdated(PositioningManager.LocationMethod method, GeoPosition position, boolean isMapMatched) {

            if (!position.isValid())
                return;
            if (!firstPositionSet) {
                map.setCenter(new GeoCoordinate(position.getCoordinate().getLatitude(),position.getCoordinate().getLongitude()),
                        Map.Animation.NONE);
                firstPositionSet = true;

            }
            a=position.getCoordinate().getLatitude();
            b=position.getCoordinate().getLongitude();

            GeoCoordinate pos = position.getCoordinate();
            Log.d(TAG, "New position: " + pos.getLatitude() + " / " + pos.getLongitude() + " / " + pos.getAltitude());


            //tvMeters.setText("Current" + pos.distanceTo());
            // ... do something with position ...

            m = NavigationManager.getInstance().getNextManeuver();

        }


        @Override
        public void onPositionFixChanged(PositioningManager.LocationMethod method, PositioningManager.LocationStatus status) {
            Log.i(TAG, "Position fix changed : " + status.name() + " / " + method.name());
            // only allow guidance, when we have a position fix
            if (status == PositioningManager.LocationStatus.AVAILABLE &&
                    (method == PositioningManager.LocationMethod.NETWORK || method == PositioningManager.LocationMethod.GPS)) {
                // we have a fix, so allow start of guidance now
                if (!simulation.isEnabled())
                    simulation.setEnabled(true);
                if (simulation.getText().equals(getText(R.string.wait_gps)))
                    simulation.setText(R.string.navigation_start);
            }
        }
    };



    // listen for new instruction events
    private NavigationManager.NewInstructionEventListener instructionHandler = new NavigationManager.NewInstructionEventListener() {
        @Override
        public void onNewInstructionEvent() {

            super.onNewInstructionEvent();

        }
    };

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        ArrayList<String> ccc=new ArrayList<String>();
        new CountDownTimer(40000, 10000) {

            public void onTick(long millisUntilFinished) {

                //here you can have your logic to set text to edittext


                SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);


                int value = sharedPreferences.getInt("value",0);


                Log.d("pppp", String.valueOf(value));
                String x=String.valueOf(value);
                Log.d("pppp1",x);

//    value=1;



            }

            public void onFinish() {
//        count.setText(ccc);


//        Map<String, Object> city = new HashMap<>();
//        city.put("personCount", String.valueOf(sum40sec));
////
////
//        db.collection("Sum").document("i4Wh7AMjCb8f45SVGyC1")
//                .set(city)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                  @Override
//                  public void onSuccess(Void aVoid) {
//                    Log.d("TAG", "DocumentSnapshot successfully written!");
//                  }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                  @Override
//                  public void onFailure(@NonNull Exception e) {
//                    Log.w("TAG", "Error writing document", e);
//                  }
//                });


            }

        }.start();


        //texttospeech start

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);


        //texttospeech end


        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        create1=(Button)findViewById(R.id.create1);
        create2=(Button)findViewById(R.id.create2);
        simulation = (Button) findViewById(R.id.simulation);
        navigation = (Button) findViewById(R.id.navigation);
//        addplace=(Button)findViewById(R.id.addplace);
        source = (TextView) findViewById(R.id.toolbar);
        destination = (TextView) findViewById(R.id.toolbar2);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PositioningManager.getInstance().start(PositioningManager.LocationMethod.GPS_NETWORK_INDOOR);
                GeoPosition lkp = PositioningManager.getInstance().getLastKnownPosition();
                if (lkp != null && lkp.isValid())
                    map.setCenter(lkp.getCoordinate(), Map.Animation.NONE);

            }
        });





        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNavigation = true;
                createMapMarker();
                startRoutingNav();

            }
        });

        simulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNavigation = false;
                createMapMarker();
                startRouting();

            }
        });

        //permission start

        checkPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                LOCATION_PERMISSION_CODE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                STORAGE_PERMISSION_CODE
        );


        //permission end


//        addplace.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(MapFragmentView3.this,AddPlace.class);
//                startActivity(i);
//
//            }
//        });

    }



    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode,String permission1, int requestCode1)
    {
        if (ContextCompat.checkSelfPermission(MapFragmentView3.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MapFragmentView3.this,
                    new String[] { permission },
                    requestCode);


        }
        else {
            Toast.makeText(MapFragmentView3.this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();

            if (ContextCompat.checkSelfPermission(MapFragmentView3.this, permission1)
                    == PackageManager.PERMISSION_DENIED) {

                // Requesting the permission
                ActivityCompat.requestPermissions(MapFragmentView3.this,
                        new String[] { permission1 },
                        requestCode1);


            }
            else
            {

                //code for current speed start
                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                this.updateSpeed(null);

                //code for current speed stop



                client = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();

                client1 = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .build();

                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(INTERVAL);
                mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

// Search for the map fragment to finish setup by calling init().

                mapFragment.init(new OnEngineInitListener() {
                    @Override
                    public void onEngineInitializationCompleted(Error error) {
                        if (error == Error.NONE) {
                            map = mapFragment.getMap();

                            // more map settings
                            map.setProjectionMode(Map.Projection.GLOBE);  // globe projection
                            map.setExtrudedBuildingsVisible(true);  // enable 3D building footprints
                            map.setLandmarksVisible(true);  // 3D Landmarks visible
                            map.setCartoMarkersVisible(IconCategory.ALL, true);  // show embedded map markers
                            map.setSafetySpotsVisible(true); // show speed cameras as embedded markers on the map



                            map.setMapScheme(Map.Scheme.NORMAL_DAY);   // normal day mapscheme

                            // traffic options
                            map.setTrafficInfoVisible(true);

//                    MapCircle circle = new MapCircle(400.0, new GeoCoordinate(19.162120, 72.840431));
//                    circle.setLineColor(Color.BLUE);
//                    circle.setFillColor(Color.TRANSPARENT);
//                    circle.setLineWidth(12);
//                    map.addMapObject(circle);

//                    m_circles.add(circle);



                            // set positioning, position indicator and event listener
                            PositioningManager.getInstance().addListener(new WeakReference<>(mapPositionHandler));
                            mapFragment.getPositionIndicator().setVisible(true);
                            mapFragment.getPositionIndicator().setAccuracyIndicatorVisible(true);
                            // use gps plus cell and wifi
                            PositioningManager.getInstance().start(PositioningManager.LocationMethod.GPS_NETWORK_INDOOR);
                            GeoPosition lkp = PositioningManager.getInstance().getLastKnownPosition();
                            if (lkp != null && lkp.isValid())
                                map.setCenter(lkp.getCoordinate(), Map.Animation.NONE);
                            // for Navigation, you need to assign the map instance to navigation manager
                            NavigationManager.getInstance().setMap(map);
                        } else {
                            Log.e(TAG, "ERROR: Cannot initialize Map Fragment " + error.name());
                            Log.e(TAG, error.getDetails());
                            Log.e(TAG, error.getStackTrace());
                        }
                    }
                });


            }

//            checkPermission(
//                    Manifest.permission.CAMERA,
//                    CAMERA_PERMISSION_CODE
//            );

            //

        }


    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MapFragmentView3.this,
                        "Location Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
                checkPermission(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        LOCATION_PERMISSION_CODE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        STORAGE_PERMISSION_CODE

                );
            }
            else {
                Toast.makeText(MapFragmentView3.this,
                        "Location Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MapFragmentView3.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();



                //


                //code for current speed start
                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                this.updateSpeed(null);

                //code for current speed stop



                client = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();

                client1 = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .build();

                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(INTERVAL);
                mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

// Search for the map fragment to finish setup by calling init().

                mapFragment.init(new OnEngineInitListener() {
                    @Override
                    public void onEngineInitializationCompleted(Error error) {
                        if (error == Error.NONE) {
                            map = mapFragment.getMap();

                            // more map settings
                            map.setProjectionMode(Map.Projection.GLOBE);  // globe projection
                            map.setExtrudedBuildingsVisible(true);  // enable 3D building footprints
                            map.setLandmarksVisible(true);  // 3D Landmarks visible
                            map.setCartoMarkersVisible(IconCategory.ALL, true);  // show embedded map markers
                            map.setSafetySpotsVisible(true); // show speed cameras as embedded markers on the map



                            map.setMapScheme(Map.Scheme.NORMAL_DAY);   // normal day mapscheme

                            // traffic options
                            map.setTrafficInfoVisible(true);

//                    MapCircle circle = new MapCircle(400.0, new GeoCoordinate(19.162120, 72.840431));
//                    circle.setLineColor(Color.BLUE);
//                    circle.setFillColor(Color.TRANSPARENT);
//                    circle.setLineWidth(12);
//                    map.addMapObject(circle);

//                    m_circles.add(circle);



                            // set positioning, position indicator and event listener
                            PositioningManager.getInstance().addListener(new WeakReference<>(mapPositionHandler));
                            mapFragment.getPositionIndicator().setVisible(true);
                            mapFragment.getPositionIndicator().setAccuracyIndicatorVisible(true);
                            // use gps plus cell and wifi
                            PositioningManager.getInstance().start(PositioningManager.LocationMethod.GPS_NETWORK_INDOOR);
                            GeoPosition lkp = PositioningManager.getInstance().getLastKnownPosition();
                            if (lkp != null && lkp.isValid())
                                map.setCenter(lkp.getCoordinate(), Map.Animation.NONE);
                            // for Navigation, you need to assign the map instance to navigation manager
                            NavigationManager.getInstance().setMap(map);
                        } else {
                            Log.e(TAG, "ERROR: Cannot initialize Map Fragment " + error.name());
                            Log.e(TAG, error.getDetails());
                            Log.e(TAG, error.getStackTrace());
                        }
                    }
                });


//                checkPermission(
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        LOCATION_PERMISSION_CODE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        STORAGE_PERMISSION_CODE
//
//                );
            }
            else {
                Toast.makeText(MapFragmentView3.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    public void finish()
    {
        super.finish();
        System.exit(0);
    }

    private void updateSpeed(CLocation location) {
        // TODO Auto-generated method stub
        float nCurrentSpeed = 0;

        if(location != null)
        {
//            location.setUseMetricunits(this.useMetricUnits());
            nCurrentSpeed = location.getSpeed();
            nCurrentSpeed=nCurrentSpeed/3;
//            nCurrentSpeed= (float) (nCurrentSpeed-3.0);

        }
        Formatter fmt = new Formatter(new StringBuilder());
        fmt.format(Locale.US, "%5.1f", nCurrentSpeed);
        strCurrentSpeed = fmt.toString();
        strCurrentSpeed = strCurrentSpeed.replace(' ', '0');

        String strUnits = "kms/hour";
//        if(this.useMetricUnits())
//        {
//            strUnits = "meters/second";
//        }

        if(isNavigation == true)
        {
            Button create1=(Button)findViewById(R.id.create1);


            create1.setText("Current speed:\n"+strCurrentSpeed + "\n" + strUnits);
            Log.d("aaujo451",strCurrentSpeed+" aana "+aaspeed);

            if(aaspeed != null)
            {
                float curr=Float.parseFloat(strCurrentSpeed);
                int spee=Integer.parseInt(aaspeed);



                Log.d("aaujo451bhai",strCurrentSpeed+" aana "+spee);




                        if(curr > spee && countspeak == 0)
                        {

//                            String words = "Your Speed Limit is high. Please Slow Down.";
                            countspeak=1;
//                            speakWords(words);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 5s = 5000ms
                                    countspeak=0;

                                   }
                                   }, 5000);


                        }






            }


        }

//        create1.setTextColor(Color.rgb(255,0,0));
        Log.d("float",strCurrentSpeed);
        if( Float.parseFloat(strCurrentSpeed) > 0.0 && countspeak1 == 0)
        {

            double currlat=location.getLatitude();
            double currlng=location.getLongitude();

            new JsonTask1().execute("http://dev.virtualearth.net/REST/v1/locationrecog/"+currlat+","+currlng+"?r=0.12&distanceUnit=mi&type=Hospitals,Schools,Education&key=AtEZXSXwdHgb-TnLu1iqJUEmc4Jwy6u33NRb1f2ItXgx7TxkqMNCNsOm33IZUDhP&output=json");
            Log.d("strcurrspeed",strCurrentSpeed);

            countspeak1=1;
//            speakWords(words);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    countspeak1=0;

                }
            }, 5000);


        }
        if(aaspeed != null)
        {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms


                }
            }, 5000);

        }






    }



    private void createMapMarker() {
        // create an image from cafe.png.
        Image marker_img = new Image();
        Image marker_img1 = new Image();

        try {
            marker_img.setImageResource(R.mipmap.black);
            marker_img1.setImageResource(R.mipmap.redicon1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        // create a MapMarker centered at current location with png image.
        m_map_marker = new MapMarker(new GeoCoordinate(srcLat,srcLng), marker_img);
        m_map_marker1 = new MapMarker(new GeoCoordinate(desLat,desLng), marker_img1);

        // add a MapMarker to current active map.
//        m_map_marker.setAnchorPoint(new PointF(marker_img.getWidth()/50, marker_img.getHeight()/50));

        map.addMapObject(m_map_marker);
        map.addMapObject(m_map_marker1);

    }

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    boolean isSourceAddressClicked, isDestAddressClicked;

    public void getAddress(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                isSourceAddressClicked = true;
                isDestAddressClicked = false;
                break;
            case R.id.toolbar2:
                isSourceAddressClicked = false;
                isDestAddressClicked = true;
                break;
        }

        if (!Places.isInitialized()) {
            FirebaseApp.initializeApp(this);

//            Places.initialize(getApplicationContext(), "AIzaSyD2RIc0ygVt903QDXfiL2LgAZM_jUu8jCw", Locale.US);
            Places.initialize(getApplicationContext(), "AIzaSyA0Xm1WytHx1q9d-rWt-TbDncqvDr9QVJU", Locale.US);

        }



        Intent intent =
                new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,fields)
                        .build(this);
        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);


    }

    com.google.android.libraries.places.api.model.Place sourcePlace;
    Place destPlace;

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                if (isSourceAddressClicked) {
                    sourcePlace = place;
                    srcLat=sourcePlace.getLatLng().latitude;
                    srcLng=sourcePlace.getLatLng().longitude;
                    source.setText(sourcePlace.getName());
                    Log.d("khem", "onActivityResult: "+place.getAddress());

                } else if (isDestAddressClicked) {
                    destPlace = place;

                    desLat=destPlace.getLatLng().latitude;
                    desLng= destPlace.getLatLng().longitude;
                    destination.setText(destPlace.getName());
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                myTTS = new TextToSpeech(this, this);
            }
            else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }

    }

    double srcLat,srcLng,desLat,desLng;


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    protected void onStart () {
        super.onStart();
//        client.connect();
    }
    @Override
    protected void onDestroy () {
        super.onDestroy();
//        unregisterReceiver(mReceiver);
    }

//    public Action getAction() {
//        return Actions.newView("My Page", "http://host/path");
//    }

    @Override
    public void onStop () {
        super.onStop();
    }




    public void startRouting() {
        // if currently a guidance session is running, stop it

        if (NavigationManager.getInstance().getRunningState() == NavigationManager.NavigationState.RUNNING) {
            Log.d(TAG, "Stop guidance guidance...");
            NavigationManager.getInstance().stop();
            return;
        }

        Log.i(TAG, "Calculating new route...");

        //
        // calculate a route first that we can use for guidance later
        //
        RouteOptions ro = new RouteOptions();
        ro.setTransportMode(RouteOptions.TransportMode.CAR);
        ro.setRouteType(RouteOptions.Type.SHORTEST);

        RoutePlan rp = new RoutePlan();

//
//        RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(19.54,19.45));
//        /* END: Langley BC */
//        RouteWaypoint destination = new RouteWaypoint(new GeoCoordinate(19.34,19.43));
        RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(srcLat,srcLng ));
        RouteWaypoint destination = new RouteWaypoint(new GeoCoordinate(desLat,desLng));


        //calculate speed limit
        new JsonTask().execute("https://dev.virtualearth.net/REST/v1/Routes/SnapToRoad?points="+srcLat+","+srcLng+";"+desLat+","+desLng+"&includeTruckSpeedLimit=true&IncludeSpeedLimit=true&speedUnit=KPH&travelMode=driving&output=json&key=AtEZXSXwdHgb-TnLu1iqJUEmc4Jwy6u33NRb1f2ItXgx7TxkqMNCNsOm33IZUDhP");
        Log.d("aaspeed", "srclat"+srcLat+"srcLong"+srcLng+"Destlat"+desLat+"DestLng"+desLng);





//        RouteWaypoint startPoint =new RouteWaypoint(new GeoCoordinate(src));
//        RouteWaypoint destination =new RouteWaypoint();
        /* Add both waypoints to the route plan */
        rp.addWaypoint(startPoint);
        rp.addWaypoint(destination);
        //RouteManager rm = new RouteManager();
        CoreRouter rm = new CoreRouter();

        rm.calculateRoute(rp, new CoreRouter.Listener() {
            @Override
            public void onProgress(int i) {

            }

            @Override
            public void onCalculateRouteFinished(List<RouteResult> list, RoutingError routingError) {
                if (routingError != RoutingError.NONE) {
                    Log.e(TAG, "Could not calculate route: " + routingError);
                    return;
                }

                Log.i(TAG, "Route calculated successful!");

                if (list != null && list.size() > 0) {
                    if (currentRoute != null)
                        map.removeMapObject(currentRoute); // remove old route before showing new one

                    currentRoute = new MapRoute(list.get(0).getRoute());
                    map.addMapObject(currentRoute);
                    m_geoBoundingBox = list.get(0).getRoute().getBoundingBox();
                    map.zoomTo(m_geoBoundingBox, Map.Animation.LINEAR,
                            Map.MOVE_PRESERVE_ORIENTATION);

                    startGuidance();
                }
            }
        });

    }

    private void startGuidance() {
        Log.i(TAG, "Start guidance...");
        // better visuals when switching to special car navigation map scheme
        map.setMapScheme(Map.Scheme.CARNAV_DAY);
        map.setTilt(45);
        map.setZoomLevel(18);

        // set guidance view to position with road ahead, tilt and zoomlevel was setup before manually
        // choose other update modes for different position and zoom behavior
        NavigationManager.getInstance().setMapUpdateMode(NavigationManager.MapUpdateMode.POSITION_ANIMATION);

        // get new guidance instructions
        NavigationManager.getInstance().addNewInstructionEventListener(new WeakReference<>(instructionHandler));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                startRouting2();

            }
        }, 1000);

        // set usage of Nuance TTS engine if specified

        // start simulation with speed of 10 m/s
        //NavigationManager.Error e = NavigationManager.getInstance().(currentRoute.getRoute(),0);

        // start real guidance
//        NavigationManager.Error ef = NavigationManager.getInstance().startNavigation(currentRoute.getRoute());
//
//        Log.i(TAG, "Guidance start result : " + ef.name());
    }

    public void startRouting2() {
        // if currently a guidance session is running, stop it

        if (NavigationManager.getInstance().getRunningState() == NavigationManager.NavigationState.RUNNING) {
            Log.d(TAG, "Stop guidance guidance...");
            NavigationManager.getInstance().stop();
            return;
        }

        Log.i(TAG, "Calculating new route...");

        //
        // calculate a route first that we can use for guidance later
        //
        RouteOptions ro = new RouteOptions();
        ro.setTransportMode(RouteOptions.TransportMode.CAR);
        ro.setRouteType(RouteOptions.Type.SHORTEST);

        RoutePlan rp = new RoutePlan();
//
//        RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(19.54,19.45));
//        /* END: Langley BC */
//        RouteWaypoint destination = new RouteWaypoint(new GeoCoordinate(19.34,19.43));
        RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(srcLat,srcLng));
        RouteWaypoint destination = new RouteWaypoint(new GeoCoordinate(desLat,desLng));
//        RouteWaypoint startPoint =new RouteWaypoint(new GeoCoordinate(src));
//        RouteWaypoint destination =new RouteWaypoint();
        /* Add both waypoints to the route plan */
        rp.addWaypoint(startPoint);
        rp.addWaypoint(destination);
        //RouteManager rm = new RouteManager();
        CoreRouter rm = new CoreRouter();

        rm.calculateRoute(rp, new CoreRouter.Listener() {
            @Override
            public void onProgress(int i) {

            }

            @Override
            public void onCalculateRouteFinished(List<RouteResult> list, RoutingError routingError) {
                if (routingError != RoutingError.NONE) {
                    Log.e(TAG, "Could not calculate route: " + routingError);
                    return;
                }

                Log.i(TAG, "Route calculated successful!");

                if (list != null && list.size() > 0) {
                    if (currentRoute != null)
                        map.removeMapObject(currentRoute); // remove old route before showing new one

                    currentRoute = new MapRoute(list.get(0).getRoute());
                    map.addMapObject(currentRoute);
                    m_geoBoundingBox = list.get(0).getRoute().getBoundingBox();
                    map.zoomTo(m_geoBoundingBox, Map.Animation.LINEAR,
                            Map.MOVE_PRESERVE_ORIENTATION);

                    startGuidance2();
                }
            }
        });

    }


    private void startGuidance2() {
        Log.i(TAG, "Start guidance...");
        // better visuals when switching to special car navigation map scheme
        map.setMapScheme(Map.Scheme.CARNAV_DAY);

        map.setTilt(45);
        map.setZoomLevel(48);

        // set guidance view to position with road ahead, tilt and zoomlevel was setup before manually
        // choose other update modes for different position and zoom behavior
        NavigationManager.getInstance().setMapUpdateMode(NavigationManager.MapUpdateMode.POSITION_ANIMATION);

        // get new guidance instructions
        NavigationManager.getInstance().addNewInstructionEventListener(new WeakReference<>(instructionHandler));

        // set usage of Nuance TTS engine if specified

        // start simulation with speed of 10 m/s
        //NavigationManager.Error e = NavigationManager.getInstance().(currentRoute.getRoute(),0);

        // start real guidance
//        create2.setText("Speed limit:\n50km/h");

//        create2.setTextColor(Color.rgb(255,0,0));
       final Button create1=(Button)findViewById(R.id.create1);

//        create1.setText("Current speed:\n40km/h");

        NavigationManager.Error ef = NavigationManager.getInstance().simulate(currentRoute.getRoute(),speed);
                toneGen1 = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        final int[] currentspeedvar = {0};
        final int[] currentspeedvar1 = {44};

        new CountDownTimer(9000, 200) {

            public void onTick(long millisUntilFinished) {

                currentspeedvar[0] = currentspeedvar[0] + 2;

                create1.setText("Current speed:\n"+ currentspeedvar[0]+" KM/H");


            }

            public void onFinish() {

//                create1.setText("Current speed:\n");




            }

        }.start();



        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
//                create1.setText("Current speed:\n60km/h");
                create1.setTextColor(Color.rgb(255,0,0));

//                toneGen1.startTone(ToneGenerator.TONE_DTMF_S, 20000);

                String words = "Your Current speed is high. Please Slow Down.";
                speakWords(words);


            }
        }, 4000);




        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

//                create1.setText("Current speed:\n40km/h");
                create1.setTextColor(Color.rgb(0,0,255));


//                toneGen1.stopTone();

                new CountDownTimer(5000, 1000) {

                    public void onTick(long millisUntilFinished) {

                        currentspeedvar1[0] = currentspeedvar1[0] - 1;

                        create1.setText("Current speed:\n"+ currentspeedvar1[0]+" KM/H");


                    }

                    public void onFinish() {

//                create1.setText("Current speed:\n");


                    }

                }.start();


            }
        }, 9000);

    }
    private void speakWords(String speech) {
//implement TTS here
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);

    }

    //navigation functions

    public void startRoutingNav() {
        // if currently a guidance session is running, stop it

        if (NavigationManager.getInstance().getRunningState() == NavigationManager.NavigationState.RUNNING) {
            Log.d(TAG, "Stop guidance guidance...");
            NavigationManager.getInstance().stop();
            return;
        }

        Log.i(TAG, "Calculating new route...");

        //
        // calculate a route first that we can use for guidance later
        //
        RouteOptions ro = new RouteOptions();
        ro.setTransportMode(RouteOptions.TransportMode.CAR);
        ro.setRouteType(RouteOptions.Type.SHORTEST);

        RoutePlan rp = new RoutePlan();

//
//        RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(19.54,19.45));
//        /* END: Langley BC */
//        RouteWaypoint destination = new RouteWaypoint(new GeoCoordinate(19.34,19.43));
        RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(srcLat,srcLng ));
        RouteWaypoint destination = new RouteWaypoint(new GeoCoordinate(desLat,desLng));
//        RouteWaypoint startPoint =new RouteWaypoint(new GeoCoordinate(src));
//        RouteWaypoint destination =new RouteWaypoint();
        /* Add both waypoints to the route plan */
        rp.addWaypoint(startPoint);
        rp.addWaypoint(destination);
        //RouteManager rm = new RouteManager();

        new JsonTask().execute("https://dev.virtualearth.net/REST/v1/Routes/SnapToRoad?points="+srcLat+","+srcLng+";"+desLat+","+desLng+"&includeTruckSpeedLimit=true&IncludeSpeedLimit=true&speedUnit=KPH&travelMode=driving&output=json&key=AtEZXSXwdHgb-TnLu1iqJUEmc4Jwy6u33NRb1f2ItXgx7TxkqMNCNsOm33IZUDhP");

        CoreRouter rm = new CoreRouter();

        rm.calculateRoute(rp, new CoreRouter.Listener() {
            @Override
            public void onProgress(int i) {

            }

            @Override
            public void onCalculateRouteFinished(List<RouteResult> list, RoutingError routingError) {
                if (routingError != RoutingError.NONE) {
                    Log.e(TAG, "Could not calculate route: " + routingError);
                    return;
                }

                Log.i(TAG, "Route calculated successful!");

                if (list != null && list.size() > 0) {
                    if (currentRoute != null)
                        map.removeMapObject(currentRoute); // remove old route before showing new one

                    currentRoute = new MapRoute(list.get(0).getRoute());
                    map.addMapObject(currentRoute);
                    m_geoBoundingBox = list.get(0).getRoute().getBoundingBox();
                    map.zoomTo(m_geoBoundingBox, Map.Animation.LINEAR,
                            Map.MOVE_PRESERVE_ORIENTATION);

                    startGuidanceNav();
                }
            }
        });

    }

    private void startGuidanceNav() {
        Log.i(TAG, "Start guidance...");
        // better visuals when switching to special car navigation map scheme
        map.setMapScheme(Map.Scheme.CARNAV_DAY);
        map.setTilt(45);
        map.setZoomLevel(18);

        // set guidance view to position with road ahead, tilt and zoomlevel was setup before manually
        // choose other update modes for different position and zoom behavior
        NavigationManager.getInstance().setMapUpdateMode(NavigationManager.MapUpdateMode.POSITION_ANIMATION);

        // get new guidance instructions
        NavigationManager.getInstance().addNewInstructionEventListener(new WeakReference<>(instructionHandler));

        startRoutingNav1();

        // set usage of Nuance TTS engine if specified

        // start simulation with speed of 10 m/s
        //NavigationManager.Error e = NavigationManager.getInstance().(currentRoute.getRoute(),0);

        // start real guidance
//        NavigationManager.Error ef = NavigationManager.getInstance().startNavigation(currentRoute.getRoute());
//
//        Log.i(TAG, "Guidance start result : " + ef.name());
    }


    public void startRoutingNav1() {
        // if currently a guidance session is running, stop it

        if (NavigationManager.getInstance().getRunningState() == NavigationManager.NavigationState.RUNNING) {
            Log.d(TAG, "Stop guidance guidance...");
            NavigationManager.getInstance().stop();
            return;
        }

        Log.i(TAG, "Calculating new route...");

        //
        // calculate a route first that we can use for guidance later
        //
        RouteOptions ro = new RouteOptions();
        ro.setTransportMode(RouteOptions.TransportMode.CAR);
        ro.setRouteType(RouteOptions.Type.SHORTEST);

        RoutePlan rp = new RoutePlan();
//
//        RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(19.54,19.45));
//        /* END: Langley BC */
//        RouteWaypoint destination = new RouteWaypoint(new GeoCoordinate(19.34,19.43));
        RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(srcLat,srcLng));
        RouteWaypoint destination = new RouteWaypoint(new GeoCoordinate(desLat,desLng));
//        RouteWaypoint startPoint =new RouteWaypoint(new GeoCoordinate(src));
//        RouteWaypoint destination =new RouteWaypoint();
        /* Add both waypoints to the route plan */
        rp.addWaypoint(startPoint);
        rp.addWaypoint(destination);
        //RouteManager rm = new RouteManager();
        CoreRouter rm = new CoreRouter();

        rm.calculateRoute(rp, new CoreRouter.Listener() {
            @Override
            public void onProgress(int i) {

            }

            @Override
            public void onCalculateRouteFinished(List<RouteResult> list, RoutingError routingError) {
                if (routingError != RoutingError.NONE) {
                    Log.e(TAG, "Could not calculate route: " + routingError);
                    return;
                }

                Log.i(TAG, "Route calculated successful!");

                if (list != null && list.size() > 0) {
                    if (currentRoute != null)
                        map.removeMapObject(currentRoute); // remove old route before showing new one

                    currentRoute = new MapRoute(list.get(0).getRoute());
                    map.addMapObject(currentRoute);
                    m_geoBoundingBox = list.get(0).getRoute().getBoundingBox();
                    map.zoomTo(m_geoBoundingBox, Map.Animation.LINEAR,
                            Map.MOVE_PRESERVE_ORIENTATION);

                    startGuidanceNav1();
                }
            }
        });

    }




    private void startGuidanceNav1() {
        Log.i(TAG, "Start guidance...");






        // better visuals when switching to special car navigation map scheme
        map.setMapScheme(Map.Scheme.CARNAV_DAY);
        map.setTilt(45);
        map.setZoomLevel(18);

        // set guidance view to position with road ahead, tilt and zoomlevel was setup before manually
        // choose other update modes for different position and zoom behavior
        NavigationManager.getInstance().setMapUpdateMode(NavigationManager.MapUpdateMode.POSITION_ANIMATION);

        // get new guidance instructions
        NavigationManager.getInstance().addNewInstructionEventListener(new WeakReference<>(instructionHandler));

        // set usage of Nuance TTS engine if specified

        // start simulation with speed of 10 m/s
        //NavigationManager.Error e = NavigationManager.getInstance().(currentRoute.getRoute(),0);

        // start real guidance
        NavigationManager.Error ef = NavigationManager.getInstance().startNavigation(currentRoute.getRoute());

//        float curr=Float.parseFloat(strCurrentSpeed);
//        float spee=Float.parseFloat(aaspeed);

        Log.d("aaujo",strCurrentSpeed+" aana "+aaspeed);

//        if( curr > spee)
//        {
//            String words = "Your Speed Limit is high. Please Slow Down. Navvvvv";
//            speakWords(words);
//
//        }
//
        Log.i(TAG, "Guidance start result : " + ef.name());
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null)
        {
            CLocation myLocation = new CLocation(location, false);
            this.updateSpeed(myLocation);




        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onGpsStatusChanged(int event) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            myTTS.setLanguage(Locale.US);
        }
        else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MapFragmentView3.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                    try {
                        // get JSONObject from JSON file
                        JSONObject obj = new JSONObject(line);

                        JSONArray userArray = obj.getJSONArray("resourceSets");
                        // fetch JSONObject named employee
                        JSONObject employee = userArray.getJSONObject(0);
                        aaspeed=employee.getJSONArray("resources").getJSONObject(0).getJSONArray("snappedPoints").getJSONObject(0).get("speedLimit").toString();
                        // get employee name and salary
//                        aaspeed = employee.getString("speedLimit");
                        Log.d("aaspeed","hgfdfgh");
//                salary = employee.getString("salary");
                        // set employee name and salary in TextView's

//                employeeSalary.setText("Salary: "+salary);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
//            txtJson.setText(result);
//            speed.setText(aaspeed);
            create2.setText("SPEED LIMIT \n"+aaspeed+" KM/H");
            Log.d("aaujo1262",strCurrentSpeed+" aana "+aaspeed);


        }
    }

    private class JsonTask1 extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
//
//            pd = new ProgressDialog(MapFragmentView3.this);
//            pd.setMessage("Please wait");
//            pd.setCancelable(false);
//            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                    try {
                        // get JSONObject from JSON file
                        JSONObject obj = new JSONObject(line);

                        JSONArray userArray = obj.getJSONArray("resourceSets");

                        Log.d("arraylength", String.valueOf(userArray.length()));

                        // fetch JSONObject named employee

                        for(int i=0;i<userArray.length();i++)
                        {
                            JSONObject employee = userArray.getJSONObject(i);
                            hospital=employee.getJSONArray("resources").getJSONObject(0).getJSONArray("businessesAtLocation").getJSONObject(0).getJSONObject("businessInfo").get("entityName").toString();
                            Log.d("qwertyuiop",hospital+aaspeed+strCurrentSpeed);
                        }

                        if(userArray.length() > 0)
                        {


                            if(Float.parseFloat(strCurrentSpeed) > Float.parseFloat(aaspeed) )
                            {
                                String words = "You are in a Hospital or School area. Please slow down.";
                                speakWords(words);

                            }
                            else
                            {
                                String words = "Hospital or School is nearby. Please drive slowly.";
                                speakWords(words);

                            }



                        }
                        else if(Float.parseFloat(strCurrentSpeed) > Float.parseFloat(aaspeed) )
                            {
                                String words = "Your Speed is high please slow down.";
                                speakWords(words);

                            }



                        // get employee name and salary
//                        aaspeed = employee.getString("speedLimit");
                        Log.d("aaspeed","hgfdfgh");
//                salary = employee.getString("salary");
                        // set employee name and salary in TextView's

//                employeeSalary.setText("Salary: "+salary);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
//            txtJson.setText(result);
//            speed.setText(aaspeed);

//            create2.setText("SPEED LIMIT \n"+aaspeed+" KM/H");
//            Log.d("aaujo1262",strCurrentSpeed+" aana "+aaspeed);


        }
    }
}





