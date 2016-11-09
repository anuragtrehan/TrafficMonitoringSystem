package com.osahub.anurag.trafficmonitoringsystem;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {
    private static final String LOGIN_URL = "http://192.168.43.185:8068/tms/login.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private ProgressDialog pDialog;
    EditText user ;
    EditText pass ;
    int value = 1;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageButton login = (ImageButton) findViewById(R.id.login);
        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setHint("UserName");
                user.setHintTextColor(Color.RED);
            }
        });
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass.setHint("Password");
                pass.setHintTextColor(Color.GREEN);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getText().toString().equals("")) {
                    user.setHint(user.getText().toString() + "(Required field)");
                    user.setHintTextColor(getResources().getColor(R.color.green));
                    value = 0;
                }
                if (pass.getText().toString().equals("")) {
                    pass.setHint(pass.getText().toString() + "(Required field)");
                    pass.setHintTextColor(getResources().getColor(R.color.green));
                    value = 0;
                }
                if (value == 1) {
                    new AttemptLogin().execute();
                }
            }
        });

    }
    class AttemptLogin extends AsyncTask<String, String, String> {
        SharedPreferences prefs=getSharedPreferences("tms",MODE_PRIVATE);
        SharedPreferences.Editor edit=prefs.edit();
        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            String username = user.getText().toString();
            String password = pass.getText().toString();
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Login Successful!", json.toString());
                    Intent i = new Intent(LoginActivity.this, DisplayActivity.class);
                    finish();
                    String a="LoggedIn";
                    edit.putString("LogIn",a );
                    edit.apply();
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(LoginActivity.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }


}
