package com.osahub.anurag.trafficmonitoringsystem.friend;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.osahub.anurag.trafficmonitoringsystem.JSONParser;
import com.osahub.anurag.trafficmonitoringsystem.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendListActivity extends ActionBarActivity {
    private String m_Text = "";
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NAME = "name";
    private static final String TAG_POSTS = "posts";
    private static final String TAG_ID = "id";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_MOBNO = "mobileno";
    private static final String READ_COMMENTS_URL = "http://192.168.0.106:8068/tms/fetch.php";
    //it's important to note that the message is both in the parent branch of
    //our JSON tree that displays a "Post Available" or a "No Post Available" message,
    //and there is also a message for each individual post, listed under the "posts"
    //category, that displays what the user typed as their message.


    //An array of all of our comments
    private JSONArray mComments = null;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        Button addfriend = (Button)findViewById(R.id.friend) ;

        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FriendListActivity.this);
                builder.setTitle("Enter Friend Mobile No.");

// Set up the input
                final EditText input = new EditText(FriendListActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }
    public void updateJSONdata() {
// Instantiate the arraylist to contain all the JSON data.
        // we are going to use a bunch of key-value pairs, referring
        // to the json element name, and the content, for example,
        // message it the tag, and "I'm awesome" as the content..


        // Bro, it's time to power up the J parser
        JSONParser jParser = new JSONParser();
        // Feed the beast our comments url, and it spits us
        //back a JSON object.  Boo-yeah Jerome.
        JSONObject json = jParser.getJSONFromUrl(READ_COMMENTS_URL);

        //when parsing JSON stuff, we should probably
        //try to catch any exceptions:
        try {

            //I know I said we would check if "Posts were Avail." (success==1)
            //before we tried to read the individual posts, but I lied...
            //mComments will tell us how many "posts" or comments are
            //available
            mComments = json.getJSONArray(TAG_POSTS);

            // looping through all posts according to the json object returned
            for (int i = 0; i < mComments.length(); i++) {
                JSONObject c = mComments.getJSONObject(i);

                //gets the content of each tag
                int id=c.getInt(TAG_ID);
                name = c.getString(TAG_NAME);


                // creating new HashMap




                // adding HashList to ArrayList


                //annndddd, our JSON data is up to date same with our array list
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts the parsed data into our listview
     */
    public class LoadComments extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FriendListActivity.this);
            pDialog.setMessage("Loading Data");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... arg0) {
            //we will develop this method in version 2
            updateJSONdata();
            return null;

        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            name1.setText(name);
            mobileno1.setText(mobileno);
            username1.setText(username);
            //we will develop this method in version 2
        }
    }

    DbHelper dbHelper = new DbHelper(this.getApplicationContext());
    SQLiteDatabase newDB = dbHelper.getWritableDatabase();
    ArrayList<String> results = new ArrayList<String>();
    Cursor c = newDB.rawQuery("SELECT * FROM NAMES WHERE NAME=* ", null);


    if (c.moveToFirst()) {
        do {
            String firstName = c.getString(c.getColumnIndex("FirstName"));
            int age = c.getInt(c.getColumnIndex("Age"));
            results.add("Name: " + firstName + ",Age: " + age);
        }while (c.moveToNext());
    }
}