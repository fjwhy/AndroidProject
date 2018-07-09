package com.projectmine.app;

/**
 * Created by istefa on 2017-12-16.
 */

        import android.app.ActionBar;
        import android.content.Intent;
        import android.graphics.Color;
        import android.graphics.Point;
        import android.graphics.drawable.ColorDrawable;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

        import com.google.android.gms.common.api.CommonStatusCodes;
        import com.google.android.gms.vision.barcode.Barcode;
        import com.projectmine.app.barcode.BarcodeCaptureActivity;

public class qr_payment extends AppCompatActivity {


    private static final int BARCODE_READER_REQUEST_CODE = 1;
    private TextView scanResult;
    private Button scanButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_payment);
        scanResult = (TextView) findViewById(R.id.result_textview);

        //enable display navigator actionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_background)));

        scanButton = (Button) findViewById(R.id.scan_barcode_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] p = barcode.cornerPoints;
                    scanResult.setText(barcode.displayValue);
                } else {
                    scanResult.setText("No Result Found");
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

