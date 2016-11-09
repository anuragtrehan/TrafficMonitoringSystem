package com.osahub.anurag.trafficmonitoringsystem.Profile;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Locale;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import android.app.Activity;
        import android.app.ListActivity;
        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.preference.PreferenceManager;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.AdapterView.OnItemClickListener;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ListAdapter;
        import android.widget.ListView;
        import android.widget.SimpleAdapter;
        import android.widget.TextView;

        import com.osahub.anurag.trafficmonitoringsystem.JSONParser;
        import com.osahub.anurag.trafficmonitoringsystem.R;

public class ProfileActivity extends Activity {
    // Progress Dialog
    private ProgressDialog pDialog;

    //php read comments script

    //localhost :
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
    // private static final String READ_COMMENTS_URL = "http://xxx.xxx.x.x:1234/webservice/comments.php";

    //testing on Emulator:
    private static final String READ_COMMENTS_URL = "http://192.168.0.106:8068/tms/fetch.php";

    //testing from a real server:
    //private static final String READ_COMMENTS_URL = "http://www.mybringback.com/webservice/comments.php";

    //JSON IDS:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NAME = "name";
    private static final String TAG_POSTS = "posts";
    private static final String TAG_ID = "id";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_MOBNO = "mobileno";
    //it's important to note that the message is both in the parent branch of
    //our JSON tree that displays a "Post Available" or a "No Post Available" message,
    //and there is also a message for each individual post, listed under the "posts"
    //category, that displays what the user typed as their message.


    //An array of all of our comments
    private JSONArray mComments = null;
    //manages all of our comments in a list.
    TextView username1;
    TextView mobileno1;
    TextView name1;
    String name;
    String mobileno;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //note that use read_comments.xml instead of our single_post.xml
        setContentView(R.layout.activity_profile);
         username1= (TextView) findViewById(R.id.username1);
         mobileno1= (TextView) findViewById(R.id.mobno1);
         name1= (TextView) findViewById(R.id.name1);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //loading the comments via AsyncTask
        new LoadComments().execute();
    }


    /**
     * Retrieves json data of comments
     */
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
//                int id=c.getInt(TAG_ID);
                name = c.getString(TAG_NAME);
                mobileno = c.getString(TAG_MOBNO);
                username = c.getString(TAG_USERNAME);


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
            pDialog = new ProgressDialog(ProfileActivity.this);
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
}