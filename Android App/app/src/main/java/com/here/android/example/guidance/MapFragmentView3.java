/*
 * Copyright (c) 2011-2017 HERE Europe B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.here.android.example.guidance;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appindexing.Action;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.Actions;
import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.IconCategory;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.guidance.AudioPlayerDelegate;
import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.guidance.VoiceCatalog;
import com.here.android.mpa.guidance.VoicePackage;
import com.here.android.mpa.guidance.VoiceSkin;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.routing.CoreRouter;
import com.here.android.mpa.routing.Maneuver;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;
import com.here.android.mpa.routing.RouteTta;
import com.here.android.mpa.routing.RouteWaypoint;
import com.here.android.mpa.routing.RoutingError;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MapFragmentView3 extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener{
    //Variable
    private static final String TAG = "TTS-BasicMapActivity";
    private final int EN_US_ID = 206;
    private Map map = null;
    public int limit=50;
    public int flag = 0,flag1 = 0;
    public int speed=80,k=0;
    public   ToneGenerator  toneGen1;
    private MapFragment mapFragment = null;
    private GeoCoordinate pos;
    TextView bluetoothstatus, bluetoothPaired;
    Button  btndisconnect;
    BluetoothAdapter myBluetooth;
    boolean status;
    ArrayList<String> devicesList;
    ArrayList<BluetoothDevice> ListDevices;
    ArrayAdapter<String> adapter;
    InputStream taInput;
    OutputStream taOut;
    BluetoothDevice pairedBluetoothDevice = null;
    BluetoothSocket blsocket = null;
    ListView listt;
    private Button create;
    public Button create1,create2;
    private Button navigation;
    private Button simulation, lastMile;
    private NavigationManager nm;
    private ImageButton navigate, simmulate;
    private boolean firstPositionSet = false;
    private MapRoute currentRoute;
    private Route m_route;
    private GeoBoundingBox m_geoBoundingBox;
    static final Integer LOCATION = 0x1;
    static final Integer GPS_SETTINGS = 0x7;
    LocationRequest mLocationRequest;
    PendingResult<LocationSettingsResult> result;
    public static final int RequestPermissionCode = 1;
    public static final int REQUEST_CHECK_SETTINGS = 123;
    int INTERVAL = 1000;
    int FASTEST_INTERVAL = 500;
    FloatingActionButton floatingActionButton;
//    private BottomSheetBehavior mBottomSheetBehavior1;
    LinearLayout tapactionlayout;
//    View bottomSheet;
    private TextView source,destination;
    private GoogleApiClient client,client1;
    private Maneuver m;

    private Button downloadLanguage;
    private Nuance nuance;

    // using to decide to use Nuance or no, by default TTS engine on device will be used
    private boolean useNuance = false;

    Date current;
    Date dateval;
    long dateDiffBetman;
    long datestored;
    long timeleft;
    long hours;
    long min;
    RouteTta rrta;
    TextView text1;
    double a;
    double b;

    private MapMarker m_map_marker;
    List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME);


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

            if (m != null) {
                if (m.getAction() == Maneuver.Action.END) {

                }
//                TextView textView2 = (TextView) findViewById(R.id.simpleTextView2);
                // textView2.setText("DISTANCE:"+(m.getDistanceToNextManeuver())+"m");

//                textView2.setText("DISTANCE:" + (m.getCoordinate().distanceTo(pos)));

                // WORKING
                double abc = (double) m.getCoordinate().distanceTo(pos);


                float distanceleft=currentRoute.getRoute().getLength()-m.getDistanceFromStart()+m.getDistanceFromPreviousManeuver();
                distanceleft=Math.round(distanceleft/10);
                distanceleft/=100;

                //cal time remaining
                dateval=m.getStartTime();
//                dateDiffBetman= dateval.getTime()-current.getTime();//in millisec
//                datestored+= dateDiffBetman/1000;
                current=dateval;

                rrta=currentRoute.getRoute().getTta(Route.TrafficPenaltyMode.OPTIMAL,Route.WHOLE_ROUTE);
                int getTimesec=rrta.getDuration();
                timeleft=getTimesec-datestored;
                hours=timeleft/3600;
                min=(timeleft-hours*3600)/60;
                String Time="";

                if(hours==0)
                    Time=min+" mins ";
                else if(min==0)
                    Time=timeleft+" secs";
                else
                    Time=hours+" hrs "+min+" mins";


                //  if(!textViewNav.getText().toString().matches(""))
                //        textViewNav.setText("");
//                text1 = (TextView) findViewById(R.id.text1);

//                text1.setText("dist-Left:"+distanceleft+" km\nEstimated Time:"+Time);

//                if(text1.getVisibility()==View.GONE)
//                    text1.setVisibility(View.VISIBLE);
                //  Toast.makeText(m_activity.getApplicationContext(),"dist-Left:"+distanceleft+" Time: "+datestored,Toast.LENGTH_SHORT).show();

                // generate.getText().toString().equals(result.getText().toString())

//                TextView textView = (TextView) findViewById(R.id.simpleTextView);
//                if(abc < 50.0000  ) {
//               ,
//
//                TextView textView = (TextView) findViewById(R.id.simpleTextView);
//                textView.setText(NavigationManager.getInstance().getNextManeuver().getTurn().name() + " " + m.getIcon().value());
//                Log.d("maneuver", "onPositionUpdated: "+NavigationManager.getInstance().getNextManeuver().getTurn().name() + " " + m.getIcon().value());
//                textView.setText(NavigationManager.getInstance().getNextManeuver().getTurn().name() + " " + m.getIcon().value());
//                TextView textView3 = (TextView) findViewById(R.id.simpleTextView3);
//                TextView textView4 = (TextView) findViewById(R.id.simpleTextView4);

//
//                if ((abc < 30.0000) && ((textView.getText().toString().equals("KEEP_LEFT 9")) ||
//                        (textView.getText().toString().equals("UTURN_LEFT 3")) ||
//                        (textView.getText().toString().equals("KEEP_LEFT 7")) ||
//                        (textView.getText().toString().equals("LIGHT_LEFT 8")) ||
//                        (textView.getText().toString().equals("QUITE_LEFT 9")) ||
//                        (textView.getText().toString().equals("HEAVY_LEFT 10")) ||
//                        (textView.getText().toString().equals("LIGHT_LEFT 10")) ||
//                        (textView.getText().toString().equals("QUITE_LEFT 11")) ||
//                        (textView.getText().toString().equals("HEAVY_LEFT 12")) ||
//                        (textView.getText().toString().equals("HIGHWAY_KEEP_LEFT 18")) ||
//                        (textView.getText().toString().equals("LEAVE_HIGHWAY_LEFT_LANE 16")) ||
//                        (textView.getText().toString().equals("ENTER_HIGHWAY_LEFT_LANE 14")))){
//
//                    send2Bluetooth(44, 40);
//
//                }
//                else if(abc<3.00000)
//                {
//                    send2Bluetooth(13, 13);
//                }
//
//
//                else if((abc < 30.0000) && ((textView.getText().toString().equals("UTURN_RIGHT 2")) ||
//                        (textView.getText().toString().equals("KEEP_RIGHT 4")) ||
//                        (textView.getText().toString().equals("LIGHT_RIGHT 5")) ||
//                        (textView.getText().toString().equals("QUITE_RIGHT 6")) ||
//                        (textView.getText().toString().equals("HEAVY_RIGHT 7")) ||
//                        (textView.getText().toString().equals("ENTER_HIGHWAY_RIGHT_LANE 13")) ||
//                        (textView.getText().toString().equals("LEAVE_HIGHWAY_RIGHT_LANE 15")) ||
//                        (textView.getText().toString().equals("HIGHWAY_KEEP_RIGHT 17")) ||
//                        (textView.getText().toString().equals("KEEP_RIGHT 3")) ||
//                        (textView.getText().toString().equals("LIGHT_RIGHT 4")) ||
//                        (textView.getText().toString().equals("QUITE_RIGHT 5")) ||
//                        (textView.getText().toString().equals("KEEP_RIGHT 17")) ||
//                        (textView.getText().toString().equals("HEAVY_RIGHT 6")))) {
//                    send2Bluetooth(44, 45);
//                }
////
//                else {
//                    send2Bluetooth(13, 13);
//                }

            }
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

    private AudioPlayerDelegate player = new AudioPlayerDelegate() {
        @Override
        public boolean playText(final String s) {
            Log.i(TAG, "Text for TTS: " + s);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    nuance.speak(s);
                }
            });
            return true;
        }

        @Override
        public boolean playFiles(String[] files) {
            // we don't want to play audio files
            return false;
        }
    };
    private Image myImage;
    private MapMarker m_map_marker1;
    private MapMarker m_map_marker2;
    private MapMarker m_map_marker3;
    private MapMarker m_map_marker4;


    //

    //    double srcLat, srcLng, desLat, desLng;
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
//
//        txtViewFromAddress = (TextView) findViewById(R.id.txtViewFromAddress);
//        txtViewToAddress = (TextView) findViewById(R.id.txtViewToAddress);

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.mapfragment);

//        srcLat = Double.valueOf(getIntent().getStringExtra("srcLat"));
//        srcLng = Double.valueOf(getIntent().getStringExtra("srcLng"));
//
//        desLat = Double.valueOf(getIntent().getStringExtra("desLat"));
//        desLng = Double.valueOf(getIntent().getStringExtra("desLng"));
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
//        tapactionlayout = (LinearLayout) findViewById(R.id.tap_action_layout);
//        bottomSheet = findViewById(R.id.bottom_sheet1);
//        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);
//        mBottomSheetBehavior1.setPeekHeight(120);
//        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);

//        downloadLanguage = (Button) findViewById(R.id.download);

//        nuance = new Nuance(this);
//        downloadLanguage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                downloadCatalogAndSkin();
//            }
//        });
//lastMile=(Button) findViewById(R.id.lastmile);
//        lastMile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                createMapMarker1();
//                startRouting3();
////                dateval=m.getStartTime();
//////                dateDiffBetman= dateval.getTime()-current.getTime();//in millisec
//////                datestored+= dateDiffBetman/1000;
////                current=dateval;
////
////                rrta=currentRoute.getRoute().getTta(Route.TrafficPenaltyMode.OPTIMAL,Route.WHOLE_ROUTE);
////                int getTimesec=rrta.getDuration();
////                timeleft=getTimesec-datestored;
////                hours=timeleft/3600;
////                min=(timeleft-hours*3600)/60;
////                String Time="";
////
////                if(hours==0)
////                    Time=min+" mins ";
////                else if(min==0)
////                    Time=timeleft+" secs";
////                else
////                    Time=hours+" hrs "+min+" mins";
////
////
////                //  if(!textViewNav.getText().toString().matches(""))
////                //        textViewNav.setText("");
////                text1 = (TextView) findViewById(R.id.text1);
////
////                text1.setText("Estimated Time:"+Time);
////
////                if(text1.getVisibility()==View.GONE)
////                    text1.setVisibility(View.VISIBLE);
////
////
//
//            }
//        });
//


//        myImage =
//                new Image();







//        mBottomSheetBehavior1.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
//                    tapactionlayout.setVisibility(View.VISIBLE);
//                }
//
//                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                    tapactionlayout.setVisibility(View.GONE);
//                }
//
//                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
//                    tapactionlayout.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//
//            }
//        });

//        tapactionlayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mBottomSheetBehavior1.getState()== BottomSheetBehavior.STATE_COLLAPSED)
//                {
//                    mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
//                }
//            }
//        });




        create = (Button) findViewById(R.id.create);
        create1=(Button)findViewById(R.id.create1);
        create2=(Button)findViewById(R.id.create2);

        navigation = (Button) findViewById(R.id.navigation);
        simulation = (Button) findViewById(R.id.simulation);


        source = (TextView) findViewById(R.id.toolbar);


        destination = (TextView) findViewById(R.id.toolbar2);
//        setSupportActionBar(destination);

//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
//        getSupportActionBar().setTitle("Search here");

        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        client1 = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);
        // startNavigation.setEnabled(false);
        // startNavigation.setText(R.string.wait_gps);
        // init Nuance TTS with session


//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        mLocationRequest = new LocationRequest();
//        mLocationRequest.setNumUpdates(1);
//        mLocationRequest.setExpirationTime(6000);
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PositioningManager.getInstance().start(PositioningManager.LocationMethod.GPS_NETWORK_INDOOR);
                GeoPosition lkp = PositioningManager.getInstance().getLastKnownPosition();
                if (lkp != null && lkp.isValid())
                    map.setCenter(lkp.getCoordinate(), Map.Animation.NONE);

            }
        });



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

//        com.here.android.mpa.common.Image myImage =
//                new com.here.android.mpa.common.Image();
//
//
//        try {
//            myImage.setImageResource(R.drawable.marker);
//        } catch (Exception e) {
//            finish();
//        }
//
//        MapMarker myMapMarker =
//                new MapMarker(new GeoCoordinate(srcLat, srcLng), myImage);
//
//        map.addMapObject(myMapMarker);








        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    if (startNavigation.getText().equals(getText(R.string.navigation_start))) {
//
////
////                        if (m_route == null) {
////                            startRouting();
////                        } else {
////                            NavigationManager.stop();
////                            /*
////                             * Restore the map orientation to show entire route on screen
////                             */
////                            map.zoomTo(m_geoBoundingBox, Map.Animation.NONE, 0f);
////                            startNavigation.setText(R.string.start_navi);
////                            m_route = null;
////                        }
//
//
//                        startNavigation.setText(R.string.navigation_stop);
//                    } else if (startNavigation.getText().equals(getText(R.string.navigation_stop))) {
//                        startNavigation.setText(R.string.navigation_start);
//                    }
                updateInstalledVoices();
                createMapMarker();
                startRouting();
            }
        });
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startRouting1();
            }
        });
        simulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startRouting2();
            }
        });

//        navigate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent ia= new Intent(MapFragmentView3.this, SourceDest.class);
//                startActivity(ia);
//            }
//        });
//
//        simmulate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent ib= new Intent(MapFragmentView3.this, SourceDest.class);
//                startActivity(ib);
//            }
//        });

//
//        bluetoothstatus = (TextView) findViewById(R.id.bluetooth_state);
//        bluetoothPaired = (TextView) findViewById(R.id.bluetooth_paired);
////        btndisconnect = (Button) findViewById(R.id.buttondisconnect);
//        listt = (ListView) findViewById(R.id.mylist);
//        btndisconnect = (Button) findViewById(R.id.buttondisconnect);
//        ListDevices = new ArrayList<BluetoothDevice>();
//        devicesList = new ArrayList<String>();
//        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listitem, R.id.txtlist, devicesList);
//        listt.setAdapter(adapter);
////        client = new GoogleApiClient.Builder(this).build();
//        btndisconnect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (blsocket != null && blsocket.isConnected()) {
//                    try {
//                        blsocket.close();
//                        Toast.makeText(getApplicationContext(), "disconnected", Toast.LENGTH_LONG).show();
//                        bluetoothPaired.setText("DISCONNECTED");
//                        bluetoothPaired.setTextColor(getResources().getColor(R.color.red));
//
//                    } catch (IOException ioe) {
//                        Log.e("app>", "Cannot close socket");
//                        pairedBluetoothDevice = null;
//                        Toast.makeText(getApplicationContext(), "Could not disconnect", Toast.LENGTH_LONG).show();
//
//                    }
//
//                }
//            }
//        });
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
//
//        listt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getApplicationContext(), "item with address: " + devicesList.get(i) + " clicked", Toast.LENGTH_LONG).show();
//
//                connect2LED(ListDevices.get(i));
//            }
//        });


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
    private void createMapMarker1() {
        // create an image from cafe.png.
        Image marker_img = new Image();

        try {
            marker_img.setImageResource(R.mipmap.black);

        } catch (IOException e) {
            e.printStackTrace();
        }
        // create a MapMarker centered at current location with png image.
        m_map_marker2 = new MapMarker(new GeoCoordinate(19.0942871,73.0057121), marker_img);
        m_map_marker3 = new MapMarker(new GeoCoordinate(19.0481025,73.0180040), marker_img);
        m_map_marker4 = new MapMarker(new GeoCoordinate(19.0751625,73.0881240), marker_img);

        // add a MapMarker to current active map.
//        m_map_marker.setAnchorPoint(new PointF(marker_img.getWidth()/50, marker_img.getHeight()/50));

        map.addMapObject(m_map_marker2);
        map.addMapObject(m_map_marker3);
        map.addMapObject(m_map_marker4);

    }



    private void updateInstalledVoices() {

        // First get the voice catalog from the backend that contains all available languages (so called voiceskins) for download
        VoiceCatalog.getInstance().downloadCatalog(new VoiceCatalog.OnDownloadDoneListener() {
            @Override
            public void onDownloadDone(VoiceCatalog.Error error) {
                if (error != VoiceCatalog.Error.NONE) {
                    Toast.makeText(getApplicationContext(), "Failed to download catalog", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Catalog downloaded", Toast.LENGTH_LONG).show();

                    boolean result = false;
                    List<VoicePackage> packages = VoiceCatalog.getInstance().getCatalogList();
                    List<VoiceSkin> local = VoiceCatalog.getInstance().getLocalVoiceSkins();

                    // if successful, check for updated version in catalog compared to local installed ones
                    for (VoiceSkin voice : local) {
                        for (VoicePackage pkg : packages) {
                            if (voice.getId() == pkg.getId() && !voice.getVersion().equals(pkg.getVersion())) {
                                Toast.makeText(getApplicationContext(), "New version detected....downloading", Toast.LENGTH_LONG).show();
                                downloadVoice(voice.getId());
                                result = true;
                            }
                        }
                    }

                    if (!result)
                        Toast.makeText(getApplicationContext(), "No updates found", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void downloadCatalogAndSkin() {
        // First get the voice catalog from the backend that contains all available languages (so called voiceskins) for download
        VoiceCatalog.getInstance().downloadCatalog(new VoiceCatalog.OnDownloadDoneListener() {
            @Override
            public void onDownloadDone(VoiceCatalog.Error error) {
                if (error != VoiceCatalog.Error.NONE) {
                    Toast.makeText(getApplicationContext(), "Failed to download catalog", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Catalog downloaded", Toast.LENGTH_LONG).show();

                    // If catalog was successfully downloaded, you can iterate over it / show it to the user / select a skin for download
                    List<VoicePackage> packages = VoiceCatalog.getInstance().getCatalogList();
                    Log.i(TAG, "# of available packages: " + packages.size());

                    // debug print of the voice skins that are available
                    for (VoicePackage lang : packages)
                        Log.d(TAG, "Language name: " + lang.getLocalizedLanguage() + " is TTS: " + lang.isTts() + " ID: " + lang.getId());

                    // Return list of already installed voices on device
                    List<VoiceSkin> localInstalledSkins = VoiceCatalog.getInstance().getLocalVoiceSkins();

                    // debug print of the already locally installed skins
                    Log.d(TAG, "# of local skins: " + localInstalledSkins.size());
                    for (VoiceSkin voice : localInstalledSkins) {
                        Log.d(TAG, "ID: " + voice.getId() + " Language: " + voice.getLanguage());
                    }

                    downloadVoice(EN_US_ID);
                }
            }
        });
    }

    private void downloadVoice(final long skin_id) {
        // kick off the download for a voice skin from the backend
        VoiceCatalog.getInstance().downloadVoice(skin_id, new VoiceCatalog.OnDownloadDoneListener() {
            @Override
            public void onDownloadDone(VoiceCatalog.Error error) {
                if (error != VoiceCatalog.Error.NONE) {
                    Toast.makeText(getApplicationContext(), "Failed downloading voice skin", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Voice skin downloaded and activated", Toast.LENGTH_LONG).show();
                    // set output for Nuance TTS
                    if (useNuance && nuance.isInitialized()) {
                        NavigationManager.getInstance().setTtsOutputFormat(NavigationManager.TtsOutputFormat.NUANCE);
                    }
                    // set usage of downloaded voice
                    NavigationManager.getInstance().setVoiceSkin(VoiceCatalog.getInstance().getLocalVoiceSkin(skin_id));
                }
            }
        });
    }


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MapFragmentView3.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapFragmentView3.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MapFragmentView3.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MapFragmentView3.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            askForGPS();
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                //Location
                case 1:
                    askForGPS();
                    break;
            }

            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }

    }
    private void askForGPS(){
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        result = LocationServices.SettingsApi.checkLocationSettings(client1, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(MapFragmentView3.this, GPS_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    //private String TAG = SourceDest.class.getSimpleName();
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
                new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fields)
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
        client.connect();

//        myBluetooth = BluetoothAdapter.getDefaultAdapter();
//        status = myBluetooth.isEnabled();
//        myBluetooth.startDiscovery();
//        if (status) {
//            //bluetoothstatus.setText("ENABLED");
//            registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
//        } else {
//            bluetoothstatus.setText("NOT READY");
//        }
    }
//    void connect2LED (BluetoothDevice device)
//    {
//        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
//        try {
//            blsocket = device.createInsecureRfcommSocketToServiceRecord(uuid);
//            blsocket.connect();
//            pairedBluetoothDevice = device;
//            bluetoothPaired.setText("PAIRED: " + device.getName());
//            bluetoothPaired.setTextColor(getResources().getColor(R.color.green));
//
//            Toast.makeText(getApplicationContext(), "Device paired successfully!", Toast.LENGTH_LONG).show();
//        } catch (IOException ioe) {
//            Log.e("taha>", "cannot connect to device :( " + ioe);
//            Toast.makeText(getApplicationContext(), "Could not connect", Toast.LENGTH_LONG).show();
//            pairedBluetoothDevice = null;
//        }
//    }

//    void send2Bluetooth ( int led, int brightness)
//    {
//        //make sure there is a paired device
//        if (pairedBluetoothDevice != null && blsocket != null) {
//            try {
//                taOut = blsocket.getOutputStream();
//                taOut.write(led + brightness);
//
//                taOut.flush();
//            } catch (IOException ioe) {
//                Log.e("app>", "Could not open a output stream " + ioe);
//            }
//        }
//    }



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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

//        FirebaseUserActions.getInstance().end(getAction());
//
//        client.disconnect();
    }

//    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//
//        public void onReceive(Context context, Intent intent) {
//
//            Log.i("app>", "broadcast received");
//            String action = intent.getAction();
//
//
//            // When discovery finds a device
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                // Get the BluetoothDevice object from the Intent
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                // Add the name and address to an array adapter to show in a ListView
//                devicesList.add(device.getName() + " @" + device.getAddress());
//                ListDevices.add(device);
//
//                adapter.notifyDataSetChanged();
//            }
//        }
//    };




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

        // set usage of Nuance TTS engine if specified

        // start simulation with speed of 10 m/s
        //NavigationManager.Error e = NavigationManager.getInstance().(currentRoute.getRoute(),0);

        // start real guidance
//        NavigationManager.Error ef = NavigationManager.getInstance().startNavigation(currentRoute.getRoute());
//
//        Log.i(TAG, "Guidance start result : " + ef.name());
    }

    public void startRouting1() {
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

                    startGuidance1();
                }
            }
        });

    }




    private void startGuidance1() {
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
//
        Log.i(TAG, "Guidance start result : " + ef.name());
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
        create2.setText("Speed limit:\n50km/h");

//        create2.setTextColor(Color.rgb(255,0,0));
        create1.setText("Current speed:\n40km/h");

        NavigationManager.Error ef = NavigationManager.getInstance().simulate(currentRoute.getRoute(),speed);
                toneGen1 = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                create1.setText("Current speed:\n60km/h");
                create1.setTextColor(Color.rgb(255,0,0));

                toneGen1.startTone(ToneGenerator.TONE_DTMF_S, 20000);


            }
        }, 4000);




        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

                create1.setText("Current speed:\n40km/h");
                create1.setTextColor(Color.rgb(0,0,255));


                toneGen1.stopTone();
            }
        }, 9000);




//        }, 2000);
//
//        }





//final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//@Override
//public void run() {

        Log.i(TAG, "pehla k..."+k);
//        for( k = 80000;k>=0;k--){
//            Log.i(TAG, "delay ke bahar..."+k);
////final Handler handler = new Handler();
////        handler.postDelayed(new Runnable() {
////@Override
////public void run() {
//    Log.i(TAG, "Start guidance..."+k);
//
//            if(k>=limit){
//                if(flag==0){
//                    toneGen1.startTone(ToneGenerator.TONE_DTMF_S, 100000);
//                    flag = 1;
//                    flag1=0;
//                }
//            }
//            if(k<limit){
//                if(flag1==0){
//                    toneGen1.stopTone();
//                    flag1 = 1;
//                    flag = 0;
//                }
//            }
//
//}
//        }, 2000);
//
//        }



//
        Log.i(TAG, "Guidance start result : " + ef.name());
    }
    public void startRouting3() {
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
        // ro.setRouteType(RouteOptions.Type.SHORTEST);

        RoutePlan rp = new RoutePlan();
        RoutePlan rp1 = new RoutePlan();
        RoutePlan rp2 = new RoutePlan();


//        RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(19.54,19.45));
//        /* END: Langley BC */
//        RouteWaypoint destination = new RouteWaypoint(new GeoCoordinate(19.34,19.43));
        RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(a,b));
        RouteWaypoint destination = new RouteWaypoint(new GeoCoordinate(19.0942871,73.0057121));
        RouteWaypoint destination1 = new RouteWaypoint(new GeoCoordinate(19.0481025,73.0180040));
        RouteWaypoint destination2=new RouteWaypoint(new GeoCoordinate(19.0751625,73.0881240));

//     RouteWaypoint startPoint =new RouteWaypoint(new GeoCoordinate(src));
//        RouteWaypoint destination =new RouteWaypoint();
        /* Add both waypoints to the route plan */
        rp.addWaypoint(startPoint);
        rp.addWaypoint(destination);
        rp1.addWaypoint(startPoint);

        rp1.addWaypoint(destination1);
        rp2.addWaypoint(startPoint);
        rp2.addWaypoint(destination2);

        //RouteManager rm = new RouteManager();
        CoreRouter rm = new CoreRouter();
        CoreRouter rm1 = new CoreRouter();
        CoreRouter rm2 = new CoreRouter();

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
                    // if (currentRoute != null)
                    // map.removeMapObject(currentRoute); // remove old route before showing new one

                    currentRoute = new MapRoute(list.get(0).getRoute());
                    map.addMapObject(currentRoute);
                    m_geoBoundingBox = list.get(0).getRoute().getBoundingBox();
                    map.zoomTo(m_geoBoundingBox, Map.Animation.LINEAR,
                            Map.MOVE_PRESERVE_ORIENTATION);

                    startGuidance3();
                }
            }
        });
        rm1.calculateRoute(rp1, new CoreRouter.Listener() {
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
                    // if (currentRoute != null)
                    //   map.removeMapObject(currentRoute); // remove old route before showing new one

                    currentRoute = new MapRoute(list.get(0).getRoute());
                    map.addMapObject(currentRoute);
                    m_geoBoundingBox = list.get(0).getRoute().getBoundingBox();
                    map.zoomTo(m_geoBoundingBox, Map.Animation.LINEAR,
                            Map.MOVE_PRESERVE_ORIENTATION);

                    startGuidance4();
                }
            }
        });

        rm2.calculateRoute(rp2, new CoreRouter.Listener() {
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
                    // if (currentRoute != null)
                    // map.removeMapObject(currentRoute); // remove old route before showing new one

                    currentRoute = new MapRoute(list.get(0).getRoute());
                    map.addMapObject(currentRoute);
                    m_geoBoundingBox = list.get(0).getRoute().getBoundingBox();
                    map.zoomTo(m_geoBoundingBox, Map.Animation.LINEAR,
                            Map.MOVE_PRESERVE_ORIENTATION);

                    startGuidance5();
                }
            }
        });

    }




    private void startGuidance3() {
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
        NavigationManager.Error ef = NavigationManager.getInstance().simulate(currentRoute.getRoute(),600);
//
        Log.i(TAG, "Guidance start result : " + ef.name());

    }
    public void startRouting4() {
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
        RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(a,b));
        RouteWaypoint destination = new RouteWaypoint(new GeoCoordinate(19.0481025,73.0180040));
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

                    startGuidance4();
                }
            }
        });

    }




    private void startGuidance4() {
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
        NavigationManager.Error ef = NavigationManager.getInstance().simulate(currentRoute.getRoute(),100);
//
        Log.i(TAG, "Guidance start result : " + ef.name());

    }
    public void startRouting5() {
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
        RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(a,b));
        RouteWaypoint destination = new RouteWaypoint(new GeoCoordinate(19.0751625,73.0881240));
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

                    startGuidance5();
                }
            }
        });

    }




    private void startGuidance5() {
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
        NavigationManager.Error ef = NavigationManager.getInstance().simulate(currentRoute.getRoute(),100);

        // start real guidance
//        NavigationManager.Error ef = NavigationManager.getInstance().startNavigation(currentRoute.getRoute());
//
        Log.i(TAG, "Guidance start result : " + ef.name());
    }



    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

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
}


