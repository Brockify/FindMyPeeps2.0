package com.gcm.rockyfish.findmypeeps20;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.jar.Attributes;

public class TabLayout extends AppCompatActivity {

    // Declaring Your View and Variables

    Toolbar toolbar;
    ViewPager pager;
    ViewPageAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Map","Friends", "Pending", "Status"};
    int Numboftabs = 4;
    ImageView imgView;
    // JSON parser class
    private Button bChange, bDelete, bReset;
    private static final String ChangeUSER_URL = "http://skyrealmstudio.com/cgi-bin/changeuser.py";
    private static final String LOGIN_URL2 = "http://skyrealmstudio.com/cgi-bin/delete.py";
    private static final String LOGIN_URL3 = "http://skyrealmstudio.com/cgi-bin/resetpass.py";
    private static final String UPLOAD_IMAGE_URL = "http://skyrealmstudio.com/cgi-bin/upload_image.py";
    private static final String CHANGE_SUCCESS = "success";
    private static final String CHANGE_MESSAGE = "message";
    private EditText biotxt;
    TextView usernameTextView;
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    private Button bSend, top, middle, bottom;
    String encodedString;
    String imgPath, fileName;
    Bitmap bitmap, origmap;
    private static int RESULT_LOAD_IMG = 1;
    private int checkbit = 0;
    private int checkbutts = 0;
    // Progress Dialog
    private ProgressDialog pDialog;
    ProgressDialog prgDialog;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = "http://skyrealmstudio.com/cgi-bin/Bio.py";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "Message";
    String time;
    Bundle bundle;

    SharedPreferences prefs;
    private EditText userchange, userverify, oldpasswrd, newpasswrd;
    String user = "rockyfish";
    String Number = "MTAwMDAwMDEzMw==";
    String lastUpdated;
    boolean isOtherUserClicked;
    Marker otherUserMarker;
    double otherUserLat = 0;
    double otherUserLong = 0;
    String otherUserUsername = null;
    String otherUserComment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPageAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        getSupportActionBar().setDisplayOptions(android.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.mainactivity_actionbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3366CC")));

        // show The Image
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void setUpBundle(Bundle bundle)
    {
        otherUserLat = bundle.getDouble("otherLat");
        otherUserLong = bundle.getDouble("otherLong");
        isOtherUserClicked = bundle.getBoolean("isOtherUserClicked");
        user = bundle.getString("username");
        otherUserComment = bundle.getString("otherComment");
        otherUserUsername = bundle.getString("userUsername");
        Number = bundle.getString("Number");
        this.bundle = bundle;
    }

    public Bundle getBundle()
    {
        return this.bundle;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            LayoutInflater lv = LayoutInflater.from(this);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final View promptView = lv.inflate(R.layout.activity_settings, null);
            builder.setView(promptView);
            AlertDialog alert = builder.create();
            alert.show();

            bChange = (Button) promptView.findViewById(R.id.Change);
            bDelete = (Button) promptView.findViewById(R.id.Delete);
            bReset = (Button) promptView.findViewById(R.id.restpass);

            bChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userchange = (EditText) promptView.findViewById(R.id.Newuser);
                    new AttemptChange().execute();
                }
            });

            bDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userverify = (EditText) promptView.findViewById(R.id.Verify);
                    new AttemptDelete().execute();
                }
            });

            bReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oldpasswrd = (EditText) promptView.findViewById(R.id.oldpass);
                    newpasswrd = (EditText) promptView.findViewById(R.id.newpass);
                    new Attemptrest().execute();
                }
            });
            return true;
        }

        if(id == R.id.action_profile)
        {
            LayoutInflater lv = LayoutInflater.from(this);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final View promptView = lv.inflate(R.layout.activity_profile, null);
            builder.setView(promptView);
            AlertDialog alert = builder.create();
            alert.show();
            top = (Button) promptView.findViewById(R.id.Top);
            bottom = (Button) promptView.findViewById(R.id.Bottom);
            middle = (Button) promptView.findViewById(R.id.Middle);
            bSend = (Button) promptView.findViewById(R.id.button);
            imgView = (ImageView) promptView.findViewById(R.id.imgView);


            new DownloadImageTask(imgView)
                    .execute("http://skyrealmstudio.com/img/" + user.toLowerCase() + "orig.jpg");

            top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkbit != 0) {
                        if (origmap.getWidth() >= origmap.getHeight()) {

                            bitmap = Bitmap.createBitmap(
                                    origmap,
                                    0,
                                    0,
                                    origmap.getHeight(),
                                    origmap.getHeight()
                            );

                        } else {

                            bitmap = Bitmap.createBitmap(
                                    origmap,
                                    0,
                                    0,
                                    origmap.getWidth(),
                                    origmap.getWidth()
                            );
                        }
                        checkbutts = 1;
                        imgView.setImageBitmap(bitmap);
                    } else {
                        Toast.makeText(TabLayout.this, "Image must be selected from gallery before cropping", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });

            bSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // When Image is selected from Gallery

                    if(checkbutts != 0) {
                        if (imgPath != null && !imgPath.isEmpty()) {
                            prgDialog = new ProgressDialog(TabLayout.this);
                            prgDialog.setMessage("Uploading Image");
                            prgDialog.show();
                            // Convert image to String using Base64
                            encodeImagetoString();
                            // When Image is not selected from Gallery
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "You must select image from gallery before you try to upload",
                                    Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(TabLayout.this, "Photo must be cropped before uploading", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });

            bottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkbit != 0) {
                        if (origmap.getWidth() >= origmap.getHeight()){

                            bitmap = Bitmap.createBitmap(
                                    origmap,
                                    origmap.getWidth() - bitmap.getHeight(),
                                    0,
                                    origmap.getHeight(),
                                    origmap.getHeight()
                            );

                        }else{

                            bitmap = Bitmap.createBitmap(
                                    origmap,
                                    0,
                                    origmap.getHeight() - bitmap.getWidth(),
                                    origmap.getWidth(),
                                    origmap
                                            .getWidth()
                            );
                        }
                        checkbutts = 1;
                        imgView.setImageBitmap(bitmap);
                    }else{
                        Toast.makeText(TabLayout.this, "Image must be selected from gallery before cropping", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });

            middle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkbit != 0) {
                        if (origmap.getWidth() >= origmap.getHeight()){

                            bitmap = Bitmap.createBitmap(
                                    origmap,
                                    origmap.getWidth()/2 - origmap.getHeight()/2,
                                    0,
                                    origmap.getHeight(),
                                    origmap.getHeight()
                            );

                        }else{

                            bitmap = Bitmap.createBitmap(
                                    origmap,
                                    0,
                                    origmap.getHeight()/2 - bitmap.getWidth()/2,
                                    origmap.getWidth(),
                                    origmap.getWidth()
                            );
                        }
                        checkbutts = 1;
                        imgView.setImageBitmap(bitmap);
                    }else{
                        Toast.makeText(TabLayout.this, "Image must be selected from gallery before cropping", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });

            imgView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Create intent to Open Image applications like Gallery, Google Photos
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Start the Intent
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                    return false;
                }
            });
        }

        if (id == R.id.action_logout) {
            //Intent ii = new Intent(MainActivity.this, Login.class);
            //if (timer != null)
                //timer.cancel();
            //ii.putExtra("LoggedOut", true);
            //startActivity(ii);
            //finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class AttemptChange extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        String newuser;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TabLayout.this);
            pDialog.setMessage("Attempting to change Username...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            newuser = userchange.getText().toString();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // here Check for success tag

            int success;
            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("Newuser", newuser));
                params.add(new BasicNameValuePair("username", user));
                params.add(new BasicNameValuePair("Number", Number));


                JSONObject json = jsonParser.makeHttpRequest(
                        ChangeUSER_URL, "POST", params);

                // checking  log for json response

                // success tag for json
                success = json.getInt(CHANGE_SUCCESS);
                if (success == 1) {
                    user = newuser;
                    return json.getString(CHANGE_MESSAGE);
                }else{

                    return json.getString(CHANGE_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



            return null;
        }
        /**
         * Once the background process is done we need to  Dismiss the progress dialog asap
         * **/
        protected void onPostExecute(String message) {

            pDialog.dismiss();
            if (message != null){
                Toast.makeText(TabLayout.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    class AttemptDelete extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;
        String vuser;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TabLayout.this);
            pDialog.setMessage("Attempting to delete account...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            vuser = userverify.getText().toString();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // here Check for success tag

            int success;
            try {

                List<NameValuePair> delete = new ArrayList<NameValuePair>();
                delete.add(new BasicNameValuePair("Verify", vuser));
                delete.add(new BasicNameValuePair("username", user));
                delete.add(new BasicNameValuePair("Number", Number));


                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL2, "POST", delete);

                // checking  log for json response

                // success tag for json
                success = json.getInt(CHANGE_SUCCESS);
                if (success == 1) {
                    Intent ii = new Intent(TabLayout.this, Login.class);
                    finish();

                    // this finish() method is used to tell android os that we are done with current //activity now! Moving to other activity
                    startActivity(ii);
                    return json.getString(CHANGE_MESSAGE);
                } else {

                    return json.getString(CHANGE_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        /**
         * Once the background process is done we need to  Dismiss the progress dialog asap
         **/
        protected void onPostExecute(String message) {

            pDialog.dismiss();
            if (message != null) {


                Toast.makeText(TabLayout.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    class Attemptrest extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;
        String newpsw;
        String oldpsw;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TabLayout.this);
            pDialog.setMessage("Attempting to reset password...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            newpsw = newpasswrd.getText().toString();
            oldpsw = oldpasswrd.getText().toString();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // here Check for success tag

            int success;
            String usr = user;
            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("oldpass", oldpsw));
                params.add(new BasicNameValuePair("newpass", newpsw));
                params.add(new BasicNameValuePair("user", user));
                params.add(new BasicNameValuePair("Number", Number));


                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL3, "POST", params);

                // checking  log for json response

                // success tag for json
                success = json.getInt(CHANGE_SUCCESS);
                if (success == 1) {
                    return json.getString(CHANGE_MESSAGE);
                } else {

                    return json.getString(CHANGE_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        /**
         * Once the background process is done we need to  Dismiss the progress dialog asap
         **/
        protected void onPostExecute(String message) {

            pDialog.dismiss();
            if (message != null) {

                Toast.makeText(TabLayout.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    class upload_image extends AsyncTask<Void, Void, String>
    {

        @Override
        protected String doInBackground(Void... test) {
            // TODO Auto-generated method stub
            // here Check for success tag

            int success;
            String usr = user;
            try {

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
                time = timef.format(c.getTime()) + " " + amorpm;

                JSONObject json = jsonParser.makeHttpRequest(
                        UPLOAD_IMAGE_URL, "POST", params);

                // checking  log for json response

                // success tag for json
                success = json.getInt(CHANGE_SUCCESS);
                if (success == 1) {
                    return json.getString(CHANGE_MESSAGE);
                } else {

                    return json.getString(CHANGE_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            Toast.makeText(TabLayout.this, result,
                    Toast.LENGTH_LONG).show();
            prgDialog.cancel();
        }
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

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
            bmImage.setImageBitmap(result);
        }
    }

    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }
    public static Bitmap scaleImage(Context context, Uri photoUri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        int MAX_IMAGE_DIMENSION = 512;
        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

        /*
         * if the orientation is not 0 (or -1, which means we don't know), we
         * have to do a rotation.
         */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }

        String type = context.getContentResolver().getType(photoUri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (type.equals("image/png")) {
            srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        } else if (type.equals("image/jpg") || type.equals("image/jpeg")) {
            srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        byte[] bMapArray = baos.toByteArray();
        baos.close();
        return BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);
    }

    public static int getOrientation(Context context, Uri photoUri) {
        /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }
    // When Image is selected from Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
                checkbit = 1;
                Uri selectedImage = data.getData();
                bitmap = scaleImage(this,selectedImage);
                origmap = bitmap;

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();
                imgView.setImageBitmap(bitmap);

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                int amorpmint = c.get(Calendar.AM_PM);
                String amorpm;
                if (amorpmint == 0)
                {
                    amorpm = "AM";
                } else {
                    amorpm = "PM";
                }

                lastUpdated = df.format(c.getTime()) + " " + amorpm;
                SimpleDateFormat timef = new SimpleDateFormat("HH:mm");
                time = timef.format(c.getTime()) + " " + amorpm;

                fileName = user + "orig";
                params = new ArrayList<>();
                // Put file name in Async Http Post Param which will used in Php web app
                params.add(new BasicNameValuePair("filename", fileName));
                params.add(new BasicNameValuePair("username", user));
                params.add(new BasicNameValuePair("Time", time));
                params.add(new BasicNameValuePair("LastUpdated", lastUpdated));

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    // AsyncTask - To convert Image to String
    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            }

            ;

            @Override
            protected String doInBackground(Void... params) {

                //bitmap = Bitmap.createScaledBitmap(bitmap, original_width, original_height, true);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {

                // Put converted Image string into Async Http Post param
                params.add(new BasicNameValuePair("image", encodedString));
                // Trigger Image upload
                new upload_image().execute();
            }
        }.execute(null, null, null);
    }
}
