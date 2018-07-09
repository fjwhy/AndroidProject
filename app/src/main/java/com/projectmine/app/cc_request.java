package com.projectmine.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by istefa on 2017-12-03.
 */

public class cc_request extends AppCompatActivity {

    private ImageButton btn_cc_request;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cc_request);

        //enable display navigator actionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2668ae")));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_background)));

        btn_cc_request = (ImageButton) findViewById(R.id.imageButton4);

    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(this, Activity_Main.class));
        finish();
    }

    @Override//back navigator
    public boolean onSupportNavigateUp(){
        startActivity(new Intent(this, Activity_Main.class));
        finish();
        return true;
    }
}
