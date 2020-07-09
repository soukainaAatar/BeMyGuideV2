package com.example.bemyguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bemyguide.controllers.SessionManager;
import com.example.bemyguide.controllers.SqlManager;
import com.example.bemyguide.models.Place;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MyMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    final  static String TAG="lifeCycle";
    SqlManager dbHelper;
    public SessionManager mysession;
    Context context;
    private static final String key="AIzaSyCUG-YOYnfBQ1GicjaPWkAc1NESXrl7nD4";
    public static final String MAP_TYPE = "com.example.bemyguide.map_type";
    //AIzaSyBPLovRAtnltVZ7zIQ5Zmw-BNH8dEnV8MI
    //AIzaSyCyFi0XzVU_EapWGRx6W_Xd58tyDIPDt2M
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_map);
        context = MyMap.this;
        dbHelper=new SqlManager(this);
        mysession = new SessionManager(getApplicationContext());
        getLocationPermission();

      //   getDeviceLocation();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

       /* Places.initialize(getApplicationContext(), key);
// Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

// Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

// Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });*/
    }

    public void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(context,
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(context,
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(MyMap.this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(MyMap.this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses = null;
        String add = "rien";
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            Log.d("Adresses", ""+addresses.toString());
            Address obj = addresses.get(0);
            Log.d("Adresse 1", ""+obj.toString());
            add = obj.getAddressLine(0);
            add = add + "\n" +obj.getFeatureName();
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();

            Log.v("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return add;
    }

    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    public static final String DATA_SAVED_BROADCAST = "com.oudersamir.datasaved";
    private BroadcastReceiver broadcastReceiver;



    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient  mFusedLocationProviderClient;
    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        try{
            if(mLocationPermissionsGranted ){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if(task.isSuccessful() && isGPSEnabled(getApplicationContext())){
                            Log.d(TAG, "onComplete: found location!");

                            Location currentLocation = (Location) task.getResult();
                            while(currentLocation==null){
                                currentLocation = (Location) task.getResult();
                            }

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    10);
                            mMap.clear();
                            mapMarker=new MarkerOptions();
                            mapMarker.position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                            mMap.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(context, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }
    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MyMap.this);




    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }


    private Boolean mLocationPermissionsGranted = false;
    // private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    // private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private void drawMarker(LatLng point) {
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);
        // Adding marker on the Google Map
        mMap.addMarker(markerOptions);
        mapMarker=markerOptions;
        Toast.makeText(getBaseContext(), "Marker is added to the Map", Toast.LENGTH_SHORT).show();
        Toast.makeText(context,getAddress(point.latitude,point.longitude),Toast.LENGTH_LONG).show();

    }

    private void drawSavedMarkers() {
        Intent in = getIntent();
        String  type = in.getStringExtra(MAP_TYPE);

        if (type.equals("all")) {
            List<Place> places = dbHelper.getAllPlaces(mysession.getUserId());
            for (int i = 0; i < places.size(); i++) {
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting latitude and longitude for the marker
                markerOptions.position(new LatLng(places.get(i).getLatitud(), places.get(i).getLongtitud()));
                markerOptions.title(places.get(i).getNom());
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                // Adding marker on the Google Map
                mMap.addMarker(markerOptions);
                Toast.makeText(getBaseContext(), "Marker is ", Toast.LENGTH_SHORT).show();
            }
        }
        if (type.equals("one")){



                MarkerOptions markerOptions = new MarkerOptions();
                Place place = dbHelper.getPlace(in.getIntExtra("place_id",0),mysession.getUserId());
                // Setting latitude and longitude for the marker
                markerOptions.position(new LatLng(place.getLatitud(),place.getLongtitud()));
                markerOptions.title(place.getNom());
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                // Adding marker on the Google Map
                mMap.addMarker(markerOptions);
                moveCamera(new LatLng(place.getLatitud(),place.getLongtitud()),15);
                Toast.makeText(getBaseContext(), "Marker is ", Toast.LENGTH_SHORT).show();




        }


    }


   /* private  void saveNameToServer(final Place place) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving Name...");
        progressDialog.show();*/

     /*   StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.URL_SAVE_NAME2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
                                saveNameToLocalStorage(place, NAME_SYNCED_WITH_SERVER);
                                Intent ii = new Intent(context, MainActivity.class);
                                startActivity(ii);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                saveNameToLocalStorage(place, NAME_NOT_SYNCED_WITH_SERVER);
                                Intent ii = new Intent(context, MainActivity.class);
                                startActivity(ii);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },*/
             /*   new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "hna fin kayn lmochkillllllllllllllllllll", Toast.LENGTH_LONG).show();


                        //on error storing the name to sqlite with status unsynced
                        saveNameToLocalStorage(place, NAME_NOT_SYNCED_WITH_SERVER);
                        progressDialog.dismiss();

                        Intent ii = new Intent(context, MainActivity.class);
                        startActivity(ii);
                    }
                }) {*/
        //    @Override
     /*       protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("place", place.getPlace());
                params.put("lat", place.getLat()+"");
                params.put("lng", place.getLng()+"");
                params.put("zoom", place.getZoom()+"");

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }/*/
   /* private  void saveNameToLocalStorage(place p, int status) {
        dbHelper.addPlace(p, status);
    }*/





    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private MarkerOptions mapMarker;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        drawSavedMarkers();


        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

                Toast.makeText(context,getAddress(marker.getPosition().latitude,marker.getPosition().longitude),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Toast.makeText(context,getAddress(marker.getPosition().latitude,marker.getPosition().longitude),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(context,getAddress(marker.getPosition().latitude,marker.getPosition().longitude),Toast.LENGTH_LONG).show();
            }

        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng point) {
                if (mapMarker != null) {

                    Toast.makeText(context, getAddress(mapMarker.getPosition().latitude, mapMarker.getPosition().longitude), Toast.LENGTH_LONG).show();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("ajouter une place")
                            .setMessage("voulez vous ajouter cette place")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {

                                    mMap.clear();
                                    dialoginterface.cancel();
                                    drawSavedMarkers();
                                }
                            })
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                    if (mapMarker != null) {

                                        Log.e("hhhhhhhhhhhhhhhhhhhhhhh", getAddress(mapMarker.getPosition().latitude, mapMarker.getPosition().longitude));

                                        String aa = getAddress(mapMarker.getPosition().latitude, mapMarker.getPosition().longitude);
                                    //    saveNameToServer(new Place(mapMarker.getPosition().longitude, mapMarker.getPosition().latitude, aa, (int) mMap.getCameraPosition().zoom));


                                        Toast.makeText(getBaseContext(), "  added to database " + point.latitude + "", Toast.LENGTH_SHORT).show();


                                    }
                                }
                            }).show();

                } else {
                    Toast.makeText(getBaseContext(), "  No Marker Selected" ,Toast.LENGTH_SHORT).show();


                }


            }
        });
        if (mLocationPermissionsGranted) {


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);




        }
    }
    public boolean isGPSEnabled (Context mContext){
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }



}
