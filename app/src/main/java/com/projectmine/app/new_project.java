package com.projectmine.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.tv.TvInputService;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import helper.SQLiteHandler;
import helper.SessionManager;
import volley.AppController;
import volley.Config_URL;

public class new_project extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TextView textView;
    private Button button;
    private EditText editText;
    //datepicker
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    //gps tracker
    double latitude, longitude;
    //data save
    private EditText txtProjectName;
    private EditText txtDetailProject;
    private TextView txtDate;
    private Spinner spinner;
    private SQLiteHandler db;
    private SessionManager session;
    // Progress Dialog
    private ProgressDialog pDialog;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String SAVE_URL = "http://istefa.co.id/projectmine/insert_project.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_project);

        txtProjectName = (EditText) findViewById(R.id.txtProjectName);
        txtDetailProject = (EditText) findViewById(R.id.txtDetailProject);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        GPSTracker gps = new GPSTracker(this);
        if(gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            AppController global = AppController.getInstance();
            String username = global.getMailId();
            Toast.makeText(getApplicationContext(), "Take device location user "+username, Toast.LENGTH_LONG).show();
        }

        editText = (EditText) findViewById(R.id.txtBudget);
        button = (Button) findViewById(R.id.btn_save);

        editText.addTextChangedListener(onTextChangedListener());
        if (isOnline()){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //showing formatted text and original text of EditText to TextView
                    //textView.setText(String.format("Formatted number value: %s\nOriginal input: %s",
                    //editText.getText().toString(),
                    //editText.getText().toString().replaceAll(",", "")));

                    String txtProjectName_s = txtProjectName.getText().toString();
                    String txtDetailProject_s = txtDetailProject.getText().toString();
                    String txtCategory_s = spinner.getSelectedItem().toString();
                    String txtbudget_s = editText.getText().toString().replaceAll(",", "");
                    Double lat_s = latitude; Double lang_s = longitude;

                    if (!txtProjectName_s.isEmpty() && !txtDetailProject_s.isEmpty() && !txtCategory_s.isEmpty() && !txtbudget_s.isEmpty()) {
                        new AttemptLogin().execute();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please enter your details!", Toast.LENGTH_LONG)
                                .show();
                    }

                }
            });
        }else{
            Toast.makeText(getBaseContext(),"Conection Problem...",Toast.LENGTH_SHORT).show();
        }

        //datepicker start
        dateView = (TextView) findViewById(R.id.txtDate);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
        //datepicker finish

        //enable display navigator actionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2668ae")));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_background)));

        spinner = (Spinner) findViewById(R.id.spinnerKategori);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.kategori, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }

    class AttemptLogin extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(new_project.this);
            pDialog.setMessage("Attempting for Saving...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            // Building Parameters
            AppController global = AppController.getInstance();
            String username = global.getMailId();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("project_name", txtProjectName.getText().toString()));
            params.add(new BasicNameValuePair("project_detail", txtDetailProject.getText().toString()));
            params.add(new BasicNameValuePair("project_category", spinner.getSelectedItem().toString()));
            params.add(new BasicNameValuePair("project_budget", editText.getText().toString().replaceAll(",", "")));
            params.add(new BasicNameValuePair("project_date", dateView.getText().toString()));
            params.add(new BasicNameValuePair("project_lat", Double.toString(latitude)));
            params.add(new BasicNameValuePair("project_lang", Double.toString(longitude)));
            params.add(new BasicNameValuePair("project_mail", username));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(SAVE_URL ,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), Activity_Main.class);
                    startActivity(i);
                    finish();
                    return json.getString(TAG_MESSAGE);
                    // closing this screen
                } else {
                    // failed to create product
                    return json.getString(TAG_MESSAGE);
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
                Toast.makeText(new_project.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
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

    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    editText.setText(formattedString);
                    editText.setSelection(editText.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                editText.addTextChangedListener(this);
            }
        };
    }

    //datepicker
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

}
