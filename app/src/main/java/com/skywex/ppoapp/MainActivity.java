package com.skywex.ppoapp;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import static android.Manifest.permission.READ_PHONE_STATE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
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

    public String getIMEI(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        Button request_permission = findViewById(R.id.request_permission);

        if (!checkPermission()) {
            request_permission.setVisibility(View.VISIBLE);
            request_permission.setOnClickListener(this);
            Snackbar.make(request_permission, "Please, give permission to access your IMEI.", Snackbar.LENGTH_LONG).show();

            return "none";
        } else return manager.getDeviceId();
    }

    public String getVersion() {
        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);

            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

            return "none";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!BuildConfig.DEBUG) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        }

        mTextMessage = findViewById(R.id.message);
        TextView imei_value = findViewById(R.id.imei_value);
        TextView version_value = findViewById(R.id.version_value);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        imei_value.setText("IMEI: " + getIMEI(this));
        imei_value.setVisibility(View.VISIBLE);

        version_value.setText("Версия: " + getVersion());
    }

    @Override
    public void onClick(View v) {
        view = v;
        int id = v.getId();

        switch (id) {
            case R.id.request_permission:
                requestPermission();
                break;
        }
    }

    private boolean checkPermission() {
        return  ActivityCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Button request_permission = findViewById(R.id.request_permission);
                        request_permission.setVisibility(View.GONE);
                        TextView imei_value = (TextView) findViewById(R.id.imei_value);
                        imei_value.setText("IMEI: " + getIMEI(this));
                        imei_value.setVisibility(View.VISIBLE);
                        Snackbar.make(view, "Now you can see your device IMEI.", Snackbar.LENGTH_LONG).show();
                    } else
                        Snackbar.make(view, "Permission denied, you cannot access your device IMEI.", Snackbar.LENGTH_LONG).show();
                }

                break;
        }
    }
}
