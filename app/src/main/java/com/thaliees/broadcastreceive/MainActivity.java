package com.thaliees.broadcastreceive;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private String[] permissions = new String[] { Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS };
    private final int MY_PERMISSIONS_REQUEST_CODE = 123;

    private TextView from, message;
    private final SmsReceive sms = new SmsReceive() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final int contentBroadcast = getResultCode();
            if(contentBroadcast == Activity.RESULT_OK){
                Bundle result = getResultExtras(true);
                from.setText(getString(R.string.label_from, result.getString("From")));
                message.setText(result.getString("Message"));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        from = findViewById(R.id.from);
        message = findViewById(R.id.message);

        // Check if the application has permission to read messages
        checkPermissionsDevice();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("SMS_RECEIVED");
        sendOrderedBroadcast(new Intent(), null, sms, null, Activity.RESULT_FIRST_USER, null, null);
        registerReceiver(sms, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(sms);
    }

    private void checkPermissionsDevice() {
        // If you do not have the permits, it is requested
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission();
        }
    }

    private void requestStoragePermission() {
        // If you do not have the permissions or
        // If the user denies once and does not select "Do not ask again",
        // Request the permission again
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.RECEIVE_SMS) ||
                ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_SMS)) {
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thanks!", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(this, ":(", Toast.LENGTH_SHORT).show();
        }
    }
}