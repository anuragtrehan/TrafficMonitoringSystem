package com.osahub.anurag.trafficmonitoringsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


public class HomeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences prefs=getSharedPreferences("tms",MODE_PRIVATE);
        final SharedPreferences.Editor edit=prefs.edit();
        if(prefs.contains("LogIn"))
        {
            Intent i1=new Intent(HomeActivity.this,DisplayActivity.class);
            startActivity(i1);
        }
        final ImageButton login = (ImageButton) findViewById(R.id.login);
        final ImageButton signup = (ImageButton) findViewById(R.id.signup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
    }


}

