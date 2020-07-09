package com.example.bemyguide.controllers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bemyguide.R;
import com.example.bemyguide.models.Comment;
import com.example.bemyguide.models.Model;
import com.example.bemyguide.models.Photo;
import com.example.bemyguide.models.Place;
import com.example.bemyguide.models.Reaction;
import com.example.bemyguide.models.User;
import com.example.bemyguide.models.Ville;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class SyncroTask extends AsyncTask<String, Void, Boolean> {

    private String jsonResponse;
    private SqlManager dbHelper;
    public Context appContext;
    private String url_all;
    private String url_add;


    public SyncroTask(Context appContext) {
        super();
        this.appContext = appContext;
        this.dbHelper = new SqlManager(appContext);
        this.url_all  = appContext.getString(R.string.url_all);
        this.url_add  = appContext.getString(R.string.url_add);

    }

    protected void onPreExecute() {


        List<Model> villes = dbHelper.getNonSyncro("villes");
        List<Model> comments = dbHelper.getNonSyncro("comments");
        List<Model> places = dbHelper.getNonSyncro("places");
        List<Model> photos = dbHelper.getNonSyncro("photos");
        List<Model> users = dbHelper.getNonSyncro("users");
        List<Model> reactions = dbHelper.getNonSyncro("place_user");


     /*   savePlace(villes, "villes");
        savePlace(users, "users");
        savePlace(places, "places");
        savePlace(photos, "photos");*/
        savePlace(comments, "comments");
     //   savePlace(reactions, "place_user");

    }

    protected Boolean doInBackground(String... urls) {
        if (isURLReachable(appContext)) {


            try {


                // STEP1. Create a HttpURLConnection object releasing REQUEST to given site
                URL url = new URL(urls[0]); //argument supplied in the call to AsyncTask
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("User-Agent", "");
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();

                // STEP2. wait for incoming RESPONSE stream, place data in a buffer
                InputStream isResponse = urlConnection.getInputStream();
                BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(isResponse));
                // STEP3. Arriving JSON fragments are concatenate into a StringBuilder
                Log.e("LifeStyle", "reponse to string " + responseBuffer.toString());
                String myLine = "";
                StringBuilder strBuilder = new StringBuilder();
                while ((myLine = responseBuffer.readLine()) != null) {
                    strBuilder.append(myLine);
                }
//show response (JSON encoded data)
                jsonResponse = strBuilder.toString();
                Log.e("RESPONSE", jsonResponse);

            } catch (Exception e) {
                Log.e("RESPONSE Error", e.getMessage());
            }
            return true; // needed to gracefully terminate Void method
        } else {

            Log.e("RESPONSE Error", "not accessible");
            return false; // needed to gracefully terminate Void method
        }

    }

    protected void onPostExecute(Boolean check) {
        if (check) {
            try {
// update GUI with JSON Response
// Step4. Convert JSON list into a Java collection of Person objects
// prepare to decode JSON response and create Java list
                Gson gson = new Gson();
                Log.e("PostExecute", "content: " + jsonResponse);
                Toast.makeText(appContext, jsonResponse, Toast.LENGTH_LONG).show();
// set (host) Java type of encoded JSON response


// decode JSON string into appropriate Java container
                ArrayList<ArrayList<Model>> all = new ArrayList<>();
                try {
                    JSONObject obj1 = new JSONObject(jsonResponse);


                    // Log.e("PostExecute", "arrayType: " + obj1.getJSONArray("comments").toString());
                    all.add(Place.parseData(new JsonParser().parse(obj1.getJSONArray("places").toString()).getAsJsonArray()));
                    all.add(Ville.parseData(new JsonParser().parse(obj1.getJSONArray("villes").toString()).getAsJsonArray()));
                    all.add(Comment.parseData(new JsonParser().parse(obj1.getJSONArray("comments").toString()).getAsJsonArray()));
                    all.add(Photo.parseData(new JsonParser().parse(obj1.getJSONArray("photos").toString()).getAsJsonArray()));
                    all.add(User.parseData(new JsonParser().parse(obj1.getJSONArray("users").toString()).getAsJsonArray()));
                    all.add(Reaction.parseData(new JsonParser().parse(obj1.getJSONArray("reactions").toString()).getAsJsonArray()));
                    dbHelper.resetAll();

                    for (int i = 0; i < all.size(); i++) {
                        for (int j = 0; j < all.get(i).size(); j++) {
                            dbHelper.addModel(all.get(i).get(j));
                        }
                    }

                    //  loadPlace();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (JsonSyntaxException e) {
                Log.e("POST-Execute", e.getMessage());
            }



        }else{
            Log.e("POST-Execute", "not syncronized");
        }

        dbHelper.close();



    }

    private void savePlace(final List<Model> list, final String tabName) {

        Log.e("Update", list.toString() + " 00000000000000000000000000" + tabName);
        JsonArray jsonArray2 = new Gson().toJsonTree(list).getAsJsonArray();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, this.url_add+"/"+tabName+"/"+jsonArray2.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("hrtrue", response + " " + tabName);
                        Toast.makeText(appContext, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("hr", error.getMessage() + " " + tabName);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                //Log.e("hhh", jsonArray2.toString() + " hhhh" + tabName);
                //Log.e("hhh", jsonArray2.toString() + " hhhh" + tabName);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(appContext);
        Log.e("Update-request", stringRequest.getCacheKey() + "  " + tabName);
        requestQueue.add(stringRequest);

      /*  StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.1.5/retrieve_data",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        // Hiding the progress dialog after all task complete.


                        // Showing response message coming from server.
                        Toast.makeText(appContext, ServerResponse, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.


                        // Showing error message if something goes wrong.
                        Toast.makeText(appContext, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                params.put("first_name", "ahmed");

                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(appContext);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);*/
    }

    public static boolean isURLReachable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            try {
                URL url = new URL(context.getString(R.string.url_base));   // Change to "http://google.com" for www  test.
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(6 * 1000);          // 10 s.
                urlc.connect();
                if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
                    Log.e("Connection", "Success 2 ! 2");
                    return true;
                } else {
                    return false;
                }
            } catch (MalformedURLException e1) {
                return false;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }


}