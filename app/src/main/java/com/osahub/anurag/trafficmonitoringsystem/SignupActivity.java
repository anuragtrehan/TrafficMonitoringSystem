package com.osahub.anurag.trafficmonitoringsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


public class SignupActivity extends AppCompatActivity {
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private static final String LOGIN_URL = "http://192.168.43.185:8068/tms/register.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int value = 1;
    EditText name;
    EditText mobileno;
    EditText user;
    EditText pass;
    ImageButton signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name = (EditText) findViewById(R.id.name);
        mobileno = (EditText) findViewById(R.id.mobileno);
        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        signup = (ImageButton) findViewById(R.id.signup);

        user.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                user.setHint("UserName");
                user.setHintTextColor(Color.RED);
                return false;
            }
        });
        name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                name.setHint("Name");
                name.setHintTextColor(Color.RED);
                return false;
            }
        });
        mobileno.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mobileno.setHint("Mobile");
                mobileno.setHintTextColor(Color.GREEN);
                return false;
            }
        });
        pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pass.setHint("Password");
                pass.setHintTextColor(Color.GREEN);
                return false;
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().toString().equals("")) {
                    name.setHint(name.getText().toString() + "(Required field)");
                    name.setHintTextColor(getResources().getColor(R.color.green));
                    value = 0;
                }
                if (pass.getText().toString().equals("")) {
                    pass.setHint(pass.getText().toString() + "(Required field)");
                    pass.setHintTextColor(getResources().getColor(R.color.green));
                    value = 0;
                }
                if (mobileno.getText().toString().equals("")) {
                    mobileno.setHint(mobileno.getText().toString() + "(Required field)");
                    mobileno.setHintTextColor(getResources().getColor(R.color.green));
                    value = 0;
                }
                if (user.getText().toString().equals("")) {
                    user.setHint(user.getText().toString() + "(Required field)");
                    user.setHintTextColor(getResources().getColor(R.color.green));
                    value = 0;
                }

                if (value == 1) {
                    new CreateUser().execute();
                }

            }
        });

    }

    class CreateUser extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;
        SharedPreferences prefs=getSharedPreferences("tms",MODE_PRIVATE);
        SharedPreferences.Editor edit=prefs.edit();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignupActivity.this);
            pDialog.setMessage("Creating User...");
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
            String mobile = mobileno.getText().toString();
            String names = name.getText().toString();
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("mobileno", mobile));
                params.add(new BasicNameValuePair("name", names));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                // full json response
                Log.d("Signup attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("User Created!", json.toString());
                    finish();
                    Intent i=new Intent(SignupActivity.this,DisplayActivity.class);
                    String a="LoggedIn";
                    edit.putString("LogIn", a);
                    edit.apply();
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                } else {
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
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(SignupActivity.this, file_url, Toast.LENGTH_LONG).show();
            }

        }


    }
}