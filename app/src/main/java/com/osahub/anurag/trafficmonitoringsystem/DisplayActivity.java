package com.osahub.anurag.trafficmonitoringsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;


public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

     final ImageButton map=(ImageButton) findViewById(R.id.map);
     final ImageButton signout=(ImageButton) findViewById(R.id.signout);
     final Button address=(Button) findViewById(R.id.address);
     final SharedPreferences pr=getSharedPreferences("tms",MODE_PRIVATE);
     final SharedPreferences.Editor edit=pr.edit();

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DisplayActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DisplayActivity.this,HomeActivity.class);
                edit.remove("LogIn");
                edit.apply();
                startActivity(intent);
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DisplayActivity.this,AddressActivity.class);
                startActivity(intent);
            }
        });
    }
}
