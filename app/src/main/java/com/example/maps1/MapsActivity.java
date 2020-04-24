package com.example.maps1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.StaticMapsRequest;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;


import java.util.ArrayList;
import java.util.List;

import static com.example.maps1.R.*;
import static com.google.maps.StaticMapsRequest.*;
import static com.google.maps.StaticMapsRequest.Markers.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 2;
    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    String[] Room_Nos = {"Select an option","101","102","103","104","105","106","107","108","109","110","111","112","113","114","115","116","117","119","120","121","122","123","125","126","201","202","204","205","206","207","208","209","210","211","212","213","214","215","216","217","218","219","220","221","222","223","224","225","227","228","229","230","231","232","233","234","235","236","237","301","302","307","308","309","310","311","312","313","314","315","316","317","318","319","320","321","322","401","402","403","407","408","409","410","411","412","413","414","415","416","417","418","419","420","421","422","451 DSH","452","453","454","455","456","457","458","459","460","461","462","463","464","465","466","467","468","469","470","471","472","473","474","475","476","477","501", "502", "503", "504", "505", "506", "507", "508", "509", "510", "511", "512", "513", "514", "515", "516", "517", "518", "519", "520", "521", "601", "602", "603", "604", "605", "606", "607", "608", "609", "610", "611", "612", "613", "614", "615", "616", "617", "618", "619", "620", "621", "622", "623", "624", "625", "626", "701", "702", "703", "704", "705", "706", "707", "708", "709", "710", "711", "712", "717", "718", "719", "720", "721", "722", "723", "724", "725", "726", "727"};
    String[] Buildings_Names = {"Select an option", "CE/IT", "Civil/Mech", "EC/EE", "RPCP", "I2IM", "PDPIS", "BCA/MCA", "GH-1", "GH-2", "GH-3", "GH-4", "HARIOM CANTEEN", "DANNY'S CANTEEN", "ICEBERG CANTEEN", "KRISHNA CHAT"};
    String[] Labs = {"Select an option","118A","118B","118C","124A","124B","124C","203A","203B","226A","226B","303A","303B","304A","304B","305A","305B","306A","306B","404A","404B","405A","405B","406B","523A","523B","524A","524B","627A","627B","628A","628B","713A","713B","714A","714B","714C","714D","715A","715B","715C","715D","716A","716B"};
    String[] option = {"Select an option", "Place_name", "Room_No","Lab_No"};
    FirebaseFirestore db;
    private FusedLocationProviderClient mFusedLocationClient;

    private TextView Placename;
    private TextView description;
    private ImageView BImage;
    private boolean mLocationPermissiongranted = false;
    private GeoPoint location;
    private String Imageref;
    private GeoApiContext mgeoapicontext = null;
    private String optionSelected;
    LatLng curr;
    Polyline polyline1;
//    private int PERMISSION_REQUEST_ACCESS_FINE_LOCATION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        db = FirebaseFirestore.getInstance();

        setContentView(layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        Placename = (TextView) findViewById(id.text2);
        description = (TextView) findViewById(id.desc1);
        BImage = (ImageView) findViewById(id.image1);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(MapsActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };

        Spinner spin = (Spinner) findViewById(id.spinner1);
       //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, option);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // ArrayAdapter adapter = ArrayAdapter.createFromResource(this,and) ;

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.color_spinner_layout,option);
        spinnerArrayAdapter.setDropDownViewResource(layout.spinner_dropdown_layout);
        spin.setAdapter(spinnerArrayAdapter);

       // spin.setAdapter(adapter);
       // spin.setAdapter(spinnerArrayAdapter);
        spin.setOnItemSelectedListener(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        LatLng Charusat = new LatLng(22.599589, 72.8205);
        mMap.addMarker(new MarkerOptions().position(Charusat).title("I love Charusat"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Charusat, 18));
        LatLng hospital = new LatLng(22.6024825, 72.8211926);
        mMap.addMarker(new MarkerOptions().position(hospital).title("hospital").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        LatLng Cspit = new LatLng(22.600192, 72.820376);
        mMap.addMarker(new MarkerOptions().position(Cspit).title("CE/IT").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        LatLng Civil = new LatLng(22.5993681, 72.8179032);
        mMap.addMarker(new MarkerOptions().position(Civil).title("Civil/Mech/Cspit").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        LatLng Ec = new LatLng(22.6000753, 72.8192711);
        mMap.addMarker(new MarkerOptions().position(Ec).title("EC/EE").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        LatLng bca = new LatLng(22.6032978, 72.8183886);
        mMap.addMarker(new MarkerOptions().position(bca).title("BCA/MCA").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


        LatLng pdpis = new LatLng(22.6016527, 72.819677);
        mMap.addMarker(new MarkerOptions().position(pdpis).title("PDPIS").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


        LatLng mba = new LatLng(22.6000159, 72.8207731);
        mMap.addMarker(new MarkerOptions().position(mba).title("I2IM").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        LatLng rpcp = new LatLng(22.5992123, 72.81943);
        mMap.addMarker(new MarkerOptions().position(rpcp).title("RPCP").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


        LatLng gh3 = new LatLng(22.6018059, 72.8180815);
        mMap.addMarker(new MarkerOptions().position(gh3).title("GIRLS HOSTEL-3").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

        LatLng gh1 = new LatLng(22.6011987, 72.8189385);
        mMap.addMarker(new MarkerOptions().position(gh1).title("GIRLS HOSTEL-1").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

        LatLng gh2 = new LatLng(22.6018623, 72.8186756);
        mMap.addMarker(new MarkerOptions().position(gh2).title("GIRLS HOSTEL-2").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

        LatLng gh4 = new LatLng(22.6009735, 72.8182371);
        mMap.addMarker(new MarkerOptions().position(gh4).title("GIRLS HOSTEL-4").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

        LatLng hari = new LatLng(22.5988681, 72.8202501);
        mMap.addMarker(new MarkerOptions().position(hari).title("HARIOM CANTEEN").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        LatLng dannys = new LatLng(22.6014691, 72.8205244);
        mMap.addMarker(new MarkerOptions().position(dannys).title("DANNY'S CANTEEN").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        LatLng iceberg = new LatLng(22.6015883, 72.8203923);
        mMap.addMarker(new MarkerOptions().position(iceberg).title("ICEBERG CANTEEN").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        LatLng krishna = new LatLng(22.601726, 72.8203155);
        mMap.addMarker(new MarkerOptions().position(krishna).title("KRISHNA CHAT").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

       // LatLng library = new LatLng()

        getlastlocation();
        if(mgeoapicontext==null){
            mgeoapicontext = new GeoApiContext.Builder()
                    .apiKey(getString(string.google_maps_key))
                    .build();

        }
    }

  //  private void calculateDirections(LatLng marker){
    //    Log.d("this", "calculateDirections: calculating directions.");

      //  com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
        //        marker.latitude,
         //       marker.longitude
        //);
        //DirectionsApiRequest directions = new DirectionsApiRequest(mgeoapicontext);

        //directions.alternatives(true);
        //directions.origin(
          //      new com.google.maps.model.LatLng(
            //            curr.latitude,
              //          curr.longitude
                //)
        //);
        //Log.d("this", "calculateDirections: destination: " + destination.toString());
        //directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
          //  @Override
            //public void onResult(DirectionsResult result) {
              //  Log.d("this", "calculateDirections: routes: " + result.routes[0].toString());
                //Log.d("this", "calculateDirections: duration: " + result.routes[0].legs[0].duration);
                //Log.d("this", "calculateDirections: distance: " + result.routes[0].legs[0].distance);
                //Log.d("this", "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());
                //addPolylinesToMap(result);

            //}

            //@Override
            //public void onFailure(Throwable e) {
              //  Log.e("this", "calculateDirections: Failed to get directions: " + e.getMessage() );

            //}
        //});
   // }


   // private void addPolylinesToMap(final DirectionsResult result){
     //   new Handler(Looper.getMainLooper()).post(new Runnable() {
       //     @Override
         //   public void run() {
           //     Log.d("this", "run: result routes: " + result.routes.length);

             //   for(DirectionsRoute route: result.routes){
               //     Log.d("this", "run: leg: " + route.legs[0].toString());
                 //   List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

  //                  List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
//                    for(com.google.maps.model.LatLng latLng: decodedPath){

//                        Log.d(TAG, "run: latlng: " + latLng.toString());

  //                      newDecodedPath.add(new LatLng(
    //                            latLng.lat,
                           //     latLng.lng
                        //));
                    //}
                    //Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                   // polyline.setColor(ContextCompat.getColor(getActivity(), color.darkGrey));
                    //polyline.setClickable(true);
                    //polyline.setColor(color.colorPrimaryDark);

                //}
            //}
        //});
    //}

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
       // Toast.makeText(this,parent.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
        if (position == 0)
            return;

        Spinner spinn = (Spinner) parent;

        if (spinn.getId() == R.id.spinner1) {
            Spinner spin2 = (Spinner) findViewById(R.id.spinner2);
            ArrayAdapter<String>adapter2;
         //   if(spinn.getId() == R.id.spinner3){
           //     Spinner spin3 = (Spinner) findViewById(R.id.spinner3);
             //   ArrayAdapter<String>adapter3;


            if (position == 1) {
                //adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Buildings_Names);
                adapter2 = new ArrayAdapter<String>(this, layout.color_spinner2_layout, Buildings_Names);
                adapter2.setDropDownViewResource(layout.spinner_dropdown_layout);
                optionSelected = "Buildings";
            }  else if(position==2)  {

                //adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Room_Nos)
                 adapter2 = new ArrayAdapter<String>(this, layout.color_spinner2_layout, Room_Nos);
                adapter2.setDropDownViewResource(layout.spinner_dropdown_layout);
                optionSelected = "Rooms";
            }
             else{
                adapter2 = new ArrayAdapter<String>(this, layout.color_spinner2_layout,Labs);
                adapter2.setDropDownViewResource(layout.spinner_dropdown_layout);
                optionSelected = "Labs";
            }

            //adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin2.setAdapter(adapter2);
            spin2.setOnItemSelectedListener(this);
        } else {
            // Toast.makeText(getApplicationContext(), "Selected User: "+Buildings_Names[position] ,Toast.LENGTH_SHORT).show();

            final ArrayList<Buildings> buildings = new ArrayList<>();
            db.collection("Buildings")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Buildings temp = document.toObject(Buildings.class);
                                    buildings.add(temp);
                                }
                                if (optionSelected == "Buildings")
                                    searchbybuilding_name(buildings, Buildings_Names[position]);
                                //else
                                else if(optionSelected == "Rooms")
                                    searchbyroom_no(buildings, Room_Nos[position]);
                                else{
                                    searchbylab_no(buildings,Labs[position]);
                                }
                            } else {
                                Log.w("err", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }


    }

    private void searchbybuilding_name(ArrayList<Buildings> buildings, String buildings_name) {
        if(polyline1!=null){
            polyline1.remove();
        }
        for (Buildings b : buildings) {

            if (buildings_name.equals(b.getName())) {

                displaybuilding(b);
                break;
            }
        }
    }

    private void searchbylab_no(ArrayList<Buildings>buildings,String labs){
        if(polyline1!=null){
            polyline1.remove();
        }
       int r = convstoi(labs);
        for(Buildings b: buildings){
            if(r>=convstoi(b.getRange_room_no().get(0))&&r<=convstoi(b.getRange_room_no().get(1))){
                displaybuilding(b);
                break;
            }
        }
    }
    private void searchbyroom_no(ArrayList<Buildings> buildings, String rno) {
        if(polyline1!=null){
            polyline1.remove();
        }

        int r = convstoi(rno);
        for (Buildings b : buildings) {

            if (r >= convstoi(b.getRange_room_no().get(0)) && r <= convstoi(b.getRange_room_no().get(1))) {
                displaybuilding(b);
                break;
            }
        }
    }

    private int convstoi(String str) {
        String s = "";
        for (int i = 0; i < 3; i++) {

            s += str.charAt(i);
        }
        int r = Integer.parseInt(s);

        return r;
    }


    public void getlastlocation(){
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            getlocationPermission();
            //return;
        }
        //getlocationPermission();
       // Toast.makeText(getApplicationContext(), "permission?? " ,Toast.LENGTH_SHORT).show();
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(getApplicationContext(), "permission:) " ,Toast.LENGTH_SHORT).show();
                    Location location = task.getResult();
                     curr = new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(curr).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                }
            }
        });

    }
    public void getlocationPermission(){
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            mLocationPermissiongranted=true;
            getlastlocation();
        }
        else{
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    private void displaybuilding(Buildings building)
    {

        description.setText(building.getDescription());
        Placename.setText(building.getName());
        Glide.with(this).load(building.getImage()).into(BImage);
          LatLng destlocation = new LatLng(building.getLocation().getLatitude(),building.getLocation().getLongitude());
       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destlocation, (float) 20));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(destlocation).tilt(20).zoom(17).bearing(90).build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destlocation, (float) 20)
        );
        getlastlocation();
        //TODO: Add path from user-location
     //   calculateDirections(destlocation);
        //showMarker();




         polyline1 = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                //.color(Color.)
                .add(new LatLng(curr.latitude,curr.longitude),
                      new LatLng(destlocation.latitude,destlocation.longitude)));
         mMap.addCircle(
                 new CircleOptions().center(curr).radius(10.0).strokeWidth(3f).strokeColor(Color.RED).fillColor(Color.argb(70,150,50,50))
         );
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
