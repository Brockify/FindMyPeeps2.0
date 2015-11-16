package com.gcm.rockyfish.findmypeeps20;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RockyFish on 10/27/15.
 */
public class Pending_Fragment extends Fragment {
    TextView usernameTextView;
    search_person search;
    ImageButton addButton;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pending_tab, container, false);
        Button addFriendButton = (Button) v.findViewById(R.id.addFriendButton);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View promptView = inflater.inflate(R.layout.searchable, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(promptView);
                AlertDialog alert = builder.create();
                alert.show();
                final SearchView sv = (SearchView) promptView.findViewById(R.id.searchView);
                sv.setFocusableInTouchMode(true);
                addButton = (ImageButton) promptView.findViewById(R.id.addButton);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addButton.setImageResource(R.drawable.checkbox);
                    }
                });
                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        search = new search_person();
                        search.execute(query);
                        sv.setVisibility(View.INVISIBLE);
                        sv.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        search = new search_person();
                        search.execute(newText);
                        return false;
                    }
                });


            }
        });

        return v;
    }

    class search_person extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.skyrealmstudio.com/cgi-bin/SearchPerson.py");
            String responseString = null;


            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("Username",strings[0]));
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
        protected void onPostExecute(String result)
        {
            if(result.equals("\nadded\n"))
            {
                addButton.setVisibility(View.VISIBLE);
            }
            else if(result.equals("\nusers already friends\n"))
            {
                Toast.makeText(getActivity(),"users already friends", Toast.LENGTH_LONG).show();
            }
            else
            {
                addButton.setVisibility(View.INVISIBLE);
                addButton.setImageResource(R.drawable.green_add);
            }
        }
    }
}
