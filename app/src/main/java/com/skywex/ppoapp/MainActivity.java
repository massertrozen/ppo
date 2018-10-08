package com.skywex.ppoapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private View view;
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        TextView imei_value = (TextView) findViewById(R.id.imei_value);
        Button request_permission = (Button) findViewById(R.id.request_permission);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        request_permission.setOnClickListener(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (checkPermission())
            imei_value.setVisibility(View.VISIBLE);
        else {
            request_permission.setVisibility(View.VISIBLE);
            Snackbar.make(navigation, "Please, give application permission to access your IMEI.", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        view = v;
        int id = v.getId();

        switch (id) {
            case R.id.request_permission:
                if (!checkPermission())
                    requestPermission();
                else {
                    TextView imei_value = (TextView) findViewById(R.id.imei_value);
                    imei_value.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted) {
                        Button request_permission = findViewById(R.id.request_permission);
                        request_permission.setVisibility(View.GONE);
                        TextView imei_value = (TextView) findViewById(R.id.imei_value);
                        imei_value.setVisibility(View.VISIBLE);
                        Snackbar.make(view, "Now you can see your device IMEI.", Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(view, "Permission denied, you cannot access your device IMEI.", Snackbar.LENGTH_LONG).show();
                    }
                }

                break;
        }
    }
}
