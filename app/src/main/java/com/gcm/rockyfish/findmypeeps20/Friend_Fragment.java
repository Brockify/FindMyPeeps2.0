package com.gcm.rockyfish.findmypeeps20;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tagmanager.Container;

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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by RockyFish on 10/27/15.
 */

public class Friend_Fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG_FRIEND = "friend";

    List<String> FriendsList;
    ListView friendListView;
    getFriendsList loadFriendsList;
    SwipeRefreshLayout swipeLayout;
    Button deleteFriendButton;
    FriendsAdapter adapter;
    private String user;
    private String userBeingClicked;
    private Double latitude;
    private Double longitude;
    private String lastUpdated;
    private Double userLatitude;
    private Double userLongitude;
    private String userComment;
    private String userUsername;
    final String[] userDelete = {null};
    private boolean mIsViewInited;
    search_person search;
    ImageButton addButton;
    List otherUsers;
    String finalresult = null;
    SearchView sv;
    SearchAdapter searchAdapter;
    ArrayList Friends;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.friends_tab, container, false);
        //set a swipe refresh layout
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.friendsRefresh);
        deleteFriendButton = (Button) inflater.inflate(R.layout.friends_list_items, container, false).findViewById(R.id.deleteButton);
        friendListView = (ListView) v.findViewById(R.id.friendListView);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        Friends = new ArrayList();

        sv = (SearchView) v.findViewById(R.id.searchView2);
        sv.onActionViewExpanded();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search = new search_person();
                search.execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(search != null) {
                    if (search.getStatus() == AsyncTask.Status.RUNNING) {
                        search.cancel(true);
                    }
                }
                search = new search_person();
                search.execute(newText);
                return false;
            }
        });

        //declare new FriendsList as ArrayList
        FriendsList = new ArrayList<String>();
        friendListView = (ListView) v.findViewById(R.id.friendListView);

        //only allows the swipe if it is at the top of the list
        friendListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (friendListView != null && friendListView.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = friendListView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = friendListView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeLayout.setEnabled(enable);
            }
        });
        sv.clearFocus();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        super.onResume();
        if(loadFriendsList.getStatus() == AsyncTask.Status.RUNNING)
        {
            loadFriendsList.cancel(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        super.onResume();
        if(loadFriendsList.getStatus() == AsyncTask.Status.RUNNING)
        {
            loadFriendsList.cancel(true);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadFriendsList = new getFriendsList();
        loadFriendsList.execute();
    }

    @Override
    public void onRefresh() {
        FriendsList.clear();
        friendListView.setAdapter(null);
        new getFriendsList().execute();
        swipeLayout.setRefreshing(false);
    }

    class getFriendsList extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            //start the post to the database
            String responseBody = null;
            HttpResponse response;
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://skyrealmstudio.com/cgi-bin/GetFriendsList.py");

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("username", "Rockyfish"));
            nameValuePair.add(new BasicNameValuePair("Number", "MTAwMDAwMDEzMw=="));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                response = httpClient.execute(httpPost);
                responseBody = EntityUtils.toString(response.getEntity());
                // writing response to log
            } catch (IOException e) {
                e.printStackTrace();
            }
            //end the post response
            String jsonStr = responseBody;

            if (jsonStr != null) {
                try {
                    JSONArray jsonArr = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonArr.length(); i++) {
                        {

                            JSONObject c = jsonArr.getJSONObject(i);
                            String Friend = c.getString(TAG_FRIEND);
                            FriendsList.add(Friend);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

            @Override
            protected void onPostExecute (Void result){
                adapter = new FriendsAdapter(getActivity(), FriendsList);
                friendListView.setAdapter(adapter);
                friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                        TextView tv = (TextView) v.findViewById(R.id.username);

                        userBeingClicked = tv.getText().toString();

                        userUsername = userBeingClicked;
                        new getSpecificUserLocation().execute(userBeingClicked);


                    }
                });
            }
        }

    class FriendsAdapter extends BaseAdapter {
        private List<String> friendslist;
        private Activity activity;

        public FriendsAdapter(Activity activity, List<String> items) {
            this.friendslist = items;
            this.activity = activity;
        }

        @Override
        public int getCount() {
            return friendslist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v;
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.friends_list_items, null);
            TextView usernameTextView = (TextView) v.findViewById(R.id.username);
            usernameTextView.setText(friendslist.get(position));

            Button delete = (Button) v.findViewById(R.id.deleteButton);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // TODO Auto-generated method stub
                    RelativeLayout vwParentRow = (RelativeLayout) v.getParent();

                    final Button deleteButton = (Button) vwParentRow.findViewById(R.id.deleteButton);
                    final TextView userDeleteText = (TextView) vwParentRow.findViewById(R.id.username);
                    final Button profileButton = (Button) vwParentRow.findViewById(R.id.profileButton);

                    //sends a alert dialog making sure they want to delete the user
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    // send post
                                    String htmlUrl = "http://www.skyrealmstudio.com/cgi-bin/DeleteFriend.py";
                                    HTTPSendPost sendPost = new HTTPSendPost();
                                    userDelete[0] = userDeleteText.getText().toString();
                                    sendPost.setUpOnDeleteFriend("Rockyfish", userDelete[0], htmlUrl, "MTAwMDAwMDEzMw==");
                                    sendPost.execute();
                                    Toast.makeText(getContext(), userDelete[0] + " deleted.", Toast.LENGTH_LONG).show();
                                    //set everything to be not visible
                                    deleteButton.setVisibility(View.GONE);
                                    profileButton.setVisibility(View.GONE);
                                    userDeleteText.setVisibility(View.GONE);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };
                    //

                    //show the dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Are you sure you would like to delete " + userDeleteText.getText().toString() + " as a friend.").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                    //


                }
            });

            return v;
        }
    }

    class SearchAdapter extends BaseAdapter {
        private List<String> friendslist;
        private Activity activity;

        public SearchAdapter(Activity activity, List<String> items) {
            this.friendslist = items;
            this.activity = activity;
        }

        @Override
        public int getCount() {
            return friendslist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.searchable_list_items, null);
            TextView usernameTextView = (TextView) v.findViewById(R.id.username);
            addButton = (ImageButton) v.findViewById(R.id.addButtonSearchable);
            if (finalresult.equals("\nadded\n")) {
                addButton.setVisibility(View.VISIBLE);
            } else if (finalresult.equals("\nusers already friends\n")) {
                Toast.makeText(getActivity(), "users already friends", Toast.LENGTH_LONG).show();
            } else {
                addButton.setImageResource(R.drawable.green_add);
                addButton.setVisibility(View.INVISIBLE);
            }

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addButton.setImageResource(R.drawable.checkbox);
                }
            });

            usernameTextView.setText(otherUsers.get(position).toString());
            return v;
        }
    }


    class getSpecificUserLocation extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params) {
            //start the post to the database
            String   responseBody = null;
            HttpResponse response;
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://skyrealmstudio.com/cgi-bin/GetSpecificUsersLocation.py");

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("Username", params[0]));
            nameValuePair.add(new BasicNameValuePair("accountusername", "rockyfish"));
            nameValuePair.add(new BasicNameValuePair("Number", "MTAwMDAwMDEzMw=="));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                response = httpClient.execute(httpPost);
                responseBody = EntityUtils.toString(response.getEntity());
                // writing response to log
            } catch (IOException e) {
                e.printStackTrace();
            }
            //end the post response

            //JSON the string that is got from the post.
            String jsonStr = responseBody;

            if (jsonStr != null) {
                try {
                    JSONArray jsonArr = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonArr.length(); i++) {
                        {

                            JSONObject c = jsonArr.getJSONObject(i);

                            Double userClickedLatitude = c.getDouble("latitude");
                            Double userClickedLongitude = c.getDouble("longitude");
                            String comment = c.getString("comments");
                            longitude = userClickedLongitude;
                            latitude = userClickedLatitude;
                            userComment = comment;
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            String address = null;
            if (latitude == null|| longitude == null){
                Toast.makeText(getContext(), "User has not updated their location.", Toast.LENGTH_LONG).show();
            } else {
                userLatitude = latitude;
                userLongitude = longitude;
                List<Address> addresses = null;
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(userLatitude, userLongitude, 1);
                    address = addresses.get(0).getAddressLine(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getContext(), userUsername + "'s address: " + address, Toast.LENGTH_LONG).show();


                Bundle bundle = new Bundle();
                bundle.putDouble("otherLat", userLatitude);
                bundle.putDouble("otherLong", userLongitude);
                bundle.putBoolean("isOtherUserClicked", true);
                bundle.putString("username", user);
                bundle.putString("otherComment", userComment);
                bundle.putString("userUsername", userUsername);
                bundle.putString("Number", "MTAwMDAwMDEzMw==");
                Fragment fragment = new Map_Fragment();
                fragment.setArguments(bundle);
                // Insert the fragment by replacing any existing fragment
                final FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.friendFragment, fragment).addToBackStack("next").commit();
            }
        }
    }

    class search_person extends AsyncTask<String, Void, String> {
        String otherUser = null;

        @Override
        protected String doInBackground(String... strings) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.skyrealmstudio.com/cgi-bin/SearchPerson.py");
            String responseString = null;
            otherUser = strings[0];

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("Username", strings[0]));
                nameValuePairs.add(new BasicNameValuePair("User", "rockyfish"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                responseString = EntityUtils.toString(response.getEntity());

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block

            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            Friends.clear();
            otherUsers = new ArrayList<String>();
            otherUsers.add(otherUser);
            if (otherUser.length() >= 4) {
                searchAdapter = new SearchAdapter(getActivity(), otherUsers);
                finalresult = result;
                if (result.equals("\nadded\n")) {
                    friendListView.setAdapter(searchAdapter);
                } else if (result.equals("\nusers already friends\n")) {
                    Toast.makeText(getActivity(), "users already friends", Toast.LENGTH_LONG).show();
                } else {
                    friendListView.setAdapter(searchAdapter);
                }
            } else {
                if(otherUser.equals(""))
                {
                    adapter = new FriendsAdapter(getActivity(), FriendsList);
                } else {
                    for(int i = 0; i < FriendsList.size(); i++)
                    {
                        if(otherUser.toLowerCase().equals(FriendsList.get(i).substring(0, otherUser.length()).toLowerCase()))
                        {
                            Friends.add(FriendsList.get(i));
                        }
                    }
                }
                if(Friends.size() != 0) {
                    adapter = new FriendsAdapter(getActivity(), Friends);
                } else {
                    adapter = new FriendsAdapter(getActivity(), FriendsList);
                }
                friendListView.setAdapter(adapter);
            }
        }
    }

}
