package com.projectmine.app;

import helper.SQLiteHandler;
import helper.SessionManager;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Activity_Main extends Activity {

    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    private ImageButton btn_cc_request;
    private ImageButton btn_qr_payment;
    private ImageButton btn_my_project;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        //txtName = (TextView) findViewById(R.id.name);
        //txtEmail = (TextView) findViewById(R.id.email);
        //btnLogout = (Button) findViewById(R.id.btnLogout);
        btn_cc_request = (ImageButton) findViewById(R.id.imageButton);
        btn_qr_payment = (ImageButton) findViewById(R.id.imageButton5);
        btn_my_project = (ImageButton) findViewById(R.id.imageButton9);
        //txtName = (TextView) findViewById(R.id.textView16);
        ImageView image_exit = (ImageView) findViewById(R.id.imageView4);
        ImageButton btn_logout = (ImageButton) findViewById(R.id.imageButton10);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        // Displaying the user details on the screen
        //txtName.setText(name+" / "+email);
        //txtEmail.setText(email);

        // Logout button click event
        /*btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });*/

        btn_cc_request.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        new_project.class);
                startActivity(i);
                finish();
            }

        });

        btn_qr_payment.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        qr_payment.class);
                startActivity(i);
                finish();
            }

        });

        btn_my_project.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        my_project.class);
                startActivity(i);
                finish();
            }

        });

        image_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog ad = new AlertDialog.Builder(Activity_Main.this).create();
                ad.setMessage("Exit Bukopin Digital Services");
                ad.setCancelable(false);
                ad.setButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //finish all activity
                        logoutUser();
                    }
                });
                ad.setButton2("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                ad.show();

                //Toast.makeText(getBaseContext(),
                // "Terima kasih telah menggunakan SMS Banking Bukopin",
                //Toast.LENGTH_SHORT).show();
                //finish();
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog ad = new AlertDialog.Builder(Activity_Main.this).create();
                ad.setMessage("Exit Bukopin Digital Services");
                ad.setCancelable(false);
                ad.setButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //finish all activity
                        logoutUser();
                    }
                });
                ad.setButton2("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                ad.show();

                //Toast.makeText(getBaseContext(),
                // "Terima kasih telah menggunakan SMS Banking Bukopin",
                //Toast.LENGTH_SHORT).show();
                //finish();
            }
        });
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(Activity_Main.this, Activity_Login.class);
        startActivity(intent);
        finish();
    }
}