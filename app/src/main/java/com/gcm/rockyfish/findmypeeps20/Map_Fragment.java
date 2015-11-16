package com.gcm.rockyfish.findmypeeps20;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
import android.widget.ArrayAdapter;
=======
import android.view.inputmethod.InputMethodManager;
>>>>>>> origin/master
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by RockyFish on 10/27/15.
 */
public class Map_Fragment extends Fragment {
    Double latitude;
    Double longitude;
    ListView listView;
    String lastUpdated = null;
    Marker userMarker = null;
    MapView googleMap;
    GoogleApiClient mGoogleApiClient;
    private ArrayList<MarkerOptions> mMyMarkersArray = new ArrayList<MarkerOptions>();
    LatLngBounds friendsListBoundaries;
    LatLng userCurrentLocation;
    MarkerScript startupRun;
    Bitmap icon;
    String user = "rockyfish";
    GPSTracker gps;
    private ProgressDialog pDialog;
    String address;
    String comments;
    String Number = "MTAwMDAwMDEzMw==";
    LatLng otherUserLocation;
    boolean isOtherUserClicked;
    Marker otherUserMarker;
    double otherUserLat;
    double otherUserLong;
    String otherUserUsername;
    String otherUserComment;
    ArrayList<String> groupnames;
    int groupnum;
    ArrayList<ArrayList<String>> groupFinal;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
<<<<<<< HEAD
        View v = inflater.inflate(R.layout.map_tab, container, false);
        Button updateLocationButton = (Button) v.findViewById(R.id.getLocationButton);
        Button GroupsButton = (Button)v.findViewById(R.id.GroupsButton);
        ViewPager mapPager = (ViewPager) v.findViewById(R.id.mapPager);
        mapPager.setSaveEnabled(false);
        googleMap = (MapView) v.findViewById(R.id.googleMap);
        gps = new GPSTracker(getActivity());
        googleMap.onCreate(savedInstanceState);
        groupFinal = new ArrayList<ArrayList<String>>();
        googleMap.onResume();// needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        GroupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupnames = new ArrayList<String>();
                new getGroups().execute();

=======
        View v =inflater.inflate(R.layout.map_tab,container,false);
<<<<<<< HEAD
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            Button updateLocationButton = (Button) v.findViewById(R.id.getLocationButton);
            ViewPager mapPager = (ViewPager) v.findViewById(R.id.mapPager);
            mapPager.setSaveEnabled(false);
            googleMap = (MapView) v.findViewById(R.id.googleMap);
            gps = new GPSTracker(getActivity());
            googleMap.onCreate(savedInstanceState);
            googleMap.onResume();// needed to get the map to display immediately
            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
>>>>>>> origin/master
            }
        });
        if (checkPlayServices()) {
            googleMap.getMap().setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                //called once the map is done loading
                public void onMapLoaded() {
                    if (gps.isGPSEnabledOrNot() && gps.canGetLocation() && gps.getLatitude() != 0 && gps.getLongitude() != 0) {
                        latitude = gps.getLocation().getLatitude();
                        longitude = gps.getLocation().getLongitude();
                        userCurrentLocation = new LatLng(latitude, longitude);
                        //if (isOtherUserClicked) {
                        //get the extras
                        //  otherUserLat = getIntent().getExtras().getDouble("otherLat");
                        //otherUserLong = getIntent().getExtras().getDouble("otherLong");
                        //otherUserUsername = getIntent().getExtras().getString("userUsername");
                        //otherUserComment = getIntent().getExtras().getString("otherComment");
                        //zoom to show both the users location and the user clicked location
                        //otherUserLocation = new LatLng(otherUserLat, otherUserLong);
                        //LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        //builder.include(userCurrentLocation);
                        //builder.include(otherUserLocation);
                        //bounds = builder.build();
                        //String urlTest = "http://skyrealmstudio.com/img/" + otherUserUsername.toLowerCase() + ".jpg";
                        //new DownloadImageTask().execute(urlTest, otherUserUsername);
                        //int padding = 50;
                        //googleMap.getMap().moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
                        //userMarker = googleMap.getMap().addMarker(new MarkerOptions().title(user).position(userCurrentLocation).icon(BitmapDescriptorFactory.fromBitmap(icon)));
                        //} else {
                        //set friends on the map
                        startupRun = new MarkerScript();
                        startupRun.execute();
                        // userMarker = googleMap.getMap().addMarker(new MarkerOptions().title(user).position(userCurrentLocation).icon(BitmapDescriptorFactory.fromBitmap(icon)));
                        //}
                    } else {
                        gps.showSettingsAlert();
                    }
                }
            });
        }

=======
        ViewPager mapPager = (ViewPager) v.findViewById(R.id.mapPager);
        mapPager.setSaveEnabled(false);
        googleMap = (MapView) v.findViewById(R.id.googleMap);
        gps = new GPSTracker(getActivity());
        googleMap.onCreate(savedInstanceState);
        googleMap.onResume();// needed to get the map to display immediately
        Button updateLocationButton = (Button) v.findViewById(R.id.getLocationButton);
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap.getMap().setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            //called once the map is done loading
            public void onMapLoaded() {
                if (gps.isGPSEnabledOrNot() && gps.canGetLocation() && gps.getLatitude() != 0 && gps.getLongitude() != 0) {
                    latitude = gps.getLocation().getLatitude();
                    longitude = gps.getLocation().getLongitude();
                    userCurrentLocation = new LatLng(latitude, longitude);
                    //if (isOtherUserClicked) {
                    //get the extras
                    //  otherUserLat = getIntent().getExtras().getDouble("otherLat");
                    //otherUserLong = getIntent().getExtras().getDouble("otherLong");
                    //otherUserUsername = getIntent().getExtras().getString("userUsername");
                    //otherUserComment = getIntent().getExtras().getString("otherComment");
                    //zoom to show both the users location and the user clicked location
                    //otherUserLocation = new LatLng(otherUserLat, otherUserLong);
                    //LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    //builder.include(userCurrentLocation);
                    //builder.include(otherUserLocation);
                    //bounds = builder.build();
                    //String urlTest = "http://skyrealmstudio.com/img/" + otherUserUsername.toLowerCase() + ".jpg";
                    //new DownloadImageTask().execute(urlTest, otherUserUsername);
                    //int padding = 50;
                    //googleMap.getMap().moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
                    //userMarker = googleMap.getMap().addMarker(new MarkerOptions().title(user).position(userCurrentLocation).icon(BitmapDescriptorFactory.fromBitmap(icon)));
                    //} else {
                    //set friends on the map
                    startupRun = new MarkerScript();
                    startupRun.execute();
                    // userMarker = googleMap.getMap().addMarker(new MarkerOptions().title(user).position(userCurrentLocation).icon(BitmapDescriptorFactory.fromBitmap(icon)));

                    //}
                } else {
                    gps.showSettingsAlert();
                }
            }
        });

>>>>>>> parent of e23bf53... cvcvcvc
        updateLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //build a dialog for sending the location
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(R.layout.activity_popup_comment);
                if (gps.isGPSEnabledOrNot() && gps.canGetLocation()) {
                    //sends a alert dialog making sure they want to delete the user
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Dialog dialoger = (Dialog) dialog;
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    EditText commentEditText = (EditText) dialoger.findViewById(R.id.commentEditText);
                                    comments = commentEditText.getText().toString();
                                    new getLocation().execute();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };
                    //build the dialog box
                    builder.setTitle("Send Location");
                    builder.setPositiveButton("Update Location", dialogClickListener);
                    builder.setNegativeButton("Cancel", dialogClickListener);
                    builder.create();
                    builder.show();
                } else {
                    gps.showSettingsAlert();
                }
<<<<<<< HEAD
<<<<<<< HEAD
            }
        });

=======
            });
>>>>>>> origin/master
        return v;
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getActivity());
        // When Play services not found in device
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // Show Error dialog to install Play services
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), 9000).show();
            } else {
                Toast.makeText(getActivity(), "Google Play Services are not all up to date, would you like to update?", Toast.LENGTH_LONG).show();
=======
>>>>>>> parent of e23bf53... cvcvcvc
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        googleMap.onResume();
        startupRun = new MarkerScript();
        startupRun.execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        googleMap.onPause();
        if (startupRun.getStatus() == AsyncTask.Status.RUNNING) {
            startupRun.cancel(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        googleMap.onDestroy();
        if (startupRun.getStatus() == AsyncTask.Status.RUNNING) {
            startupRun.cancel(true);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        googleMap.onLowMemory();
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    class MarkerScript extends AsyncTask<Void, Void, Void> {
        int userCounter = 0;

        @Override
        protected Void doInBackground(Void... strings) {
            HttpResponse response;
            String responseStr = null;
            String username;
            String comment;
            String latitude;
            String longitude;
            String lastUpdated;

            JSONObject obj = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.skyrealmstudio.com/cgi-bin/MarkerScript.py");
            JSONArray json = null;


            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", "rockyfish"));
                nameValuePairs.add(new BasicNameValuePair("Number", "MTAwMDAwMDEzMw=="));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            System.out.println(responseStr);
            try {
                json = new JSONArray(responseStr);
                for (int counter = 0; counter < json.length(); counter++)
                    try {
                        //parse the data
                        username = json.getJSONObject(counter).getString("Username");
                        comment = json.getJSONObject(counter).getString("Comment");
                        latitude = json.getJSONObject(counter).getString("Latitude");
                        longitude = json.getJSONObject(counter).getString("Longitude");
                        lastUpdated = json.getJSONObject(counter).getString("LastUpdated");
                        if (latitude.isEmpty() || longitude.isEmpty() || lastUpdated.isEmpty()) {

                        } else {
                            String tempDate = lastUpdated.substring(5, 10);
                            SimpleDateFormat todaysDateC = new SimpleDateFormat("MM-dd");
                            String todaysDate = todaysDateC.format(new Date());
                            if (tempDate.equals(todaysDate)) {
                                tempDate = "Today";
                            }

                            String tempTime = lastUpdated.substring(11, 13);
                            if (Integer.parseInt(tempTime) > 12) {
                                tempTime = String.valueOf((Integer.parseInt(tempTime) - 12));
                            } else if (Integer.parseInt(tempTime) < 10) {
                                tempTime = tempTime.substring(1);
                            }
                            String amOrPm = lastUpdated.substring(20, 22);
                            lastUpdated = tempDate + " " + tempTime + lastUpdated.substring(13, 16) + amOrPm;

                            Bitmap userIcon = null;
                            String urldisplay = "http://skyrealmstudio.com/img/" + username.toLowerCase() + ".jpg";
                            try {
                                InputStream in = new URL(urldisplay).openStream();
                                userIcon = BitmapFactory.decodeStream(in);
                                //make the icon a circle,'
                                userIcon = userIcon.createScaledBitmap(userIcon, userIcon.getWidth(), userIcon.getHeight(), false);
                                userIcon = getCroppedBitmap(userIcon);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //log the data
                            if (latitude.equals("User did not update location") || longitude.equals("User did not update location") || latitude.equals("") || longitude.equals("")) {

                            } else {
                                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))).title(username).snippet(lastUpdated + " - " + comment).icon(BitmapDescriptorFactory.fromBitmap(userIcon));
                                mMyMarkersArray.add(userCounter, markerOption);
                                userCounter++;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        //insert data onto map and set the boundaries
        protected void onPostExecute(Void result) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (int counter = 0; counter < mMyMarkersArray.size(); counter++) {
                LatLng userLatLng = new LatLng(mMyMarkersArray.get(counter).getPosition().latitude, mMyMarkersArray.get(counter).getPosition().longitude);
                builder.include(userLatLng);
            }
            for (int counter = 0; counter < mMyMarkersArray.size(); counter++) {
                googleMap.getMap().addMarker(mMyMarkersArray.get(counter));
            }
            if (userMarker != null) {
                userMarker.remove();
                userMarker = googleMap.getMap().addMarker(new MarkerOptions().title("rockyfish").position(userCurrentLocation).icon(BitmapDescriptorFactory.fromBitmap(icon)));
                builder.include(userCurrentLocation);
            }
            friendsListBoundaries = builder.build();
            googleMap.getMap().moveCamera(CameraUpdateFactory.newLatLngBounds(friendsListBoundaries, 100));
        }
    }

    //gets the profile pictures of a user when clicked
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            //add the other users location to the map

            Bitmap bhalfsize = result.createScaledBitmap(result, result.getWidth(), result.getHeight(), false);
            bhalfsize = getCroppedBitmap(bhalfsize);

            otherUserMarker = googleMap.getMap().addMarker(new MarkerOptions()
                    .position(new LatLng(otherUserLat, otherUserLong))
                    .title(otherUserUsername)
                    .snippet(otherUserComment)
                    .icon(BitmapDescriptorFactory.fromBitmap(bhalfsize)));

        }
    }

    //gets the location class (ASYNC)
    public class getLocation extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            gps = new GPSTracker(getActivity());
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Sharing your location...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            //If the update location button is clicked------------------------------------------\
            latitude = gps.getLocation().getLatitude();
            longitude = gps.getLocation().getLongitude();

            //get time and date
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int amorpmint = c.get(Calendar.AM_PM);
            String amorpm;
            if (amorpmint == 0) {
                amorpm = "AM";
            } else {
                amorpm = "PM";
            }

            lastUpdated = df.format(c.getTime()) + " " + amorpm;
            SimpleDateFormat timef = new SimpleDateFormat("HH:mm");
            String time = timef.format(c.getTime()) + " " + amorpm;
            //getting the street address---------------------------------------------------;
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getActivity(), Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                address = addresses.get(0).getAddressLine(0);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //website to post too
            String htmlUrl = "http://skyrealmstudio.com/cgi-bin/updatelocation.py";

            //send the post and execute it
            HTTPSendPost postSender = new HTTPSendPost();
            postSender.Setup(user, longitude, latitude, address, htmlUrl, comments, lastUpdated, time, Number);
            postSender.execute();
            //done executing post

            //finished getting the street address-----------------------------------------
            return null;
        }
        // end showing it on the map ------------------------------------------------------------------------

        //this happens whenever an async task is done
        public void onPostExecute(Void result) {
            LatLngBounds.Builder temp = new LatLngBounds.Builder();

            userCurrentLocation = new LatLng(latitude, longitude);
            //if the address comes back null send a toast
            if (address == null) {
                Toast.makeText(getContext(), "Could not update location! Try again.", Toast.LENGTH_LONG).show();
            } else {
                String urlTest = "http://skyrealmstudio.com/img/" + user.toLowerCase() + ".jpg";
                //if it is the first time clicking get location
                Toast.makeText(getContext(), "Updated location!", Toast.LENGTH_LONG).show();
                new DownloadImageTask().execute(urlTest);
                userCurrentLocation = new LatLng(latitude, longitude);
                if (mMyMarkersArray.size() == 0) {
                    googleMap.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(userCurrentLocation, 60));
                } else {
                    temp.include(userCurrentLocation);
                    for (int counter = 0; counter < mMyMarkersArray.size(); counter++) {
                        LatLng user = new LatLng(mMyMarkersArray.get(counter).getPosition().latitude, mMyMarkersArray.get(counter).getPosition().longitude);
                        temp.include(user);
                    }
                    LatLngBounds bound = temp.build();
                    googleMap.getMap().moveCamera(CameraUpdateFactory.newLatLngBounds(bound, 100));
                }


                new DownloadImageTask().execute(urlTest, user);
            }
<<<<<<< HEAD

            if (userMarker != null) {
                userMarker.remove();
            }
=======
            //userMarker.remove();
>>>>>>> parent of e23bf53... cvcvcvc
            userMarker = googleMap.getMap().addMarker(new MarkerOptions().position(userCurrentLocation).title(user).icon(BitmapDescriptorFactory.fromBitmap(icon)));
            gps.stopUsingGps();
            pDialog.dismiss();

        }
    }

    //--------------------------------------------Finish getLocation()-----------------------------------


    class getGroups extends AsyncTask<Void, Void, Void> {

        private ProgressDialog pDialog;
        String responseStr;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("getting groups");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpResponse response;
            String responseStr = null;
            String groups;
            String jsonStr = null;
            JSONArray json = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.skyrealmstudio.com/cgi-bin/testcgi-bin/ListGroups.py");


            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", "rockyfish"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                jsonStr = responseStr;


            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            System.out.println(responseStr);
            try {
                json = new JSONArray(jsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int counter = 0; counter < json.length(); counter++)
                try {
                    groups = json.getJSONObject(counter).getString("groupnames");
                    groupnum = json.getJSONObject(counter).getInt("groupnumber");
                    groupnames.add(groups);
                    groupFinal.get(counter).add(0, String.valueOf(groups));
                    groupFinal.get(counter).add(1, String.valueOf(groupnum));
                    Log.d("Message:", groupnames.get(counter));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            return null;

        }

        public void onPostExecute(Void result)
        {
            Bundle bundle = new Bundle();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getLayoutInflater(bundle);
            View convertView = (View) inflater.inflate(R.layout.activity_popup_groups, null);
            alertDialog.setView(convertView);
            ListView lv = (ListView) convertView.findViewById(R.id.grouplistView);
            ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.activity_group_list_layout, R.id.groupname, groupFinal.get(0));
            lv.setAdapter(adapter);
            alertDialog.show();
        }
    }

}
