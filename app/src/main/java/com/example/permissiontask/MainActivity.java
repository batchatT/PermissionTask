package com.example.permissiontask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private final int REQUEST_PERMISSION_CAMERA = 1;
    private final int REQUEST_PERMISSION_CALL = 2;
    private final int REQUEST_PERMISSION_SMS = 3;
    private final int REQUEST_PERMISSION_STORAGE = 4;
    private final int REQUEST_PERMISSION_CONTACTS = 5;

    private static final String TAG = "MainActivity123";

    private CompoundButton cameraCompoundButton;
    private CompoundButton callCompoundButton;
    private CompoundButton smsCompoundButton;
    private CompoundButton storageCompoundButton;
    private CompoundButton contactsCompoundButton;

    private final String[] permissions = {Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CONTACTS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViewBinding();

        Log.d(TAG, "onCreate: ");

        checkPermissionsStatus();
        setListeners();
    }

    private void disposeListeners() {
        Log.d(TAG, "disposeListeners: ");
        cameraCompoundButton.setOnCheckedChangeListener(null);
        callCompoundButton.setOnCheckedChangeListener(null);
        smsCompoundButton.setOnCheckedChangeListener(null);
        storageCompoundButton.setOnCheckedChangeListener(null);
        contactsCompoundButton.setOnCheckedChangeListener(null);
    }

    private void setViewBinding() {
        cameraCompoundButton = findViewById(R.id.cameraPermission);
        callCompoundButton = findViewById(R.id.callPermission);
        smsCompoundButton = findViewById(R.id.sendSmsPermission);
        storageCompoundButton = findViewById(R.id.storagePermission);
        contactsCompoundButton = findViewById(R.id.readContactsPermission);
    }

    private void setListeners() {
        Log.d(TAG, "setListeners: ");
        cameraCompoundButton.setOnCheckedChangeListener(this);
        callCompoundButton.setOnCheckedChangeListener(this);
        smsCompoundButton.setOnCheckedChangeListener(this);
        storageCompoundButton.setOnCheckedChangeListener(this);
        contactsCompoundButton.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.cameraPermission:
                    checkPermission(Manifest.permission.CAMERA, REQUEST_PERMISSION_CAMERA, buttonView);
                    break;
                case R.id.callPermission:
                    checkPermission(Manifest.permission.CALL_PHONE, REQUEST_PERMISSION_CALL, buttonView);
                    break;
                case R.id.sendSmsPermission:
                    checkPermission(Manifest.permission.SEND_SMS, REQUEST_PERMISSION_SMS, buttonView);
                    break;
                case R.id.storagePermission:
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PERMISSION_STORAGE, buttonView);
                    break;
                case R.id.readContactsPermission:
                    checkPermission(Manifest.permission.READ_CONTACTS, REQUEST_PERMISSION_CONTACTS, buttonView);
                    break;
            }
        }
    }

    private void checkPermissionsStatus() {
        Log.d(TAG, "checkPermissionsStatus: ");
        for (final String permission : permissions) {
            final int permissionStatus = ContextCompat.checkSelfPermission(this, permission);
            if (permissionStatus == 0) {
                switch (permission) {
                    case Manifest.permission.CAMERA:
                        cameraCompoundButton.setEnabled(false);
                        cameraCompoundButton.setChecked(true);
                        break;
                    case Manifest.permission.READ_EXTERNAL_STORAGE:
                        storageCompoundButton.setEnabled(false);
                        storageCompoundButton.setChecked(true);
                        break;
                    case Manifest.permission.CALL_PHONE:
                        callCompoundButton.setEnabled(false);
                        callCompoundButton.setChecked(true);
                        break;
                    case Manifest.permission.SEND_SMS:
                        smsCompoundButton.setEnabled(false);
                        smsCompoundButton.setChecked(true);
                        break;
                    case Manifest.permission.READ_CONTACTS:
                        contactsCompoundButton.setEnabled(false);
                        contactsCompoundButton.setChecked(true);
                        break;
                }
            }
        }
    }

    private void checkPermission(String permission, int requestCode, CompoundButton cb) {
        Log.d(TAG, "checkPermission: ");
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        } /*else {
            Toast.makeText(this, getText(R.string.granted), Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);
        boolean showRationale;
        Log.d(TAG, "onRequestPermissionsResult: ");
        switch (requestCode) {
            case REQUEST_PERMISSION_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, getText(R.string.camera_granted), Toast.LENGTH_SHORT).show();
                    //checkPermissionsStatus();
                } else {
                    showRationale = shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);
                    if (!showRationale) {
                        new DenyDialog().show(
                                getSupportFragmentManager(), DenyDialog.TAG);
                    }
                    cameraCompoundButton.setChecked(false);
                }
                break;
            }
            case REQUEST_PERMISSION_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, getText(R.string.call_granted), Toast.LENGTH_SHORT).show();
                    checkPermissionsStatus();
                } else {
                    showRationale = shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE);
                    if (!showRationale) {
                        new DenyDialog().show(
                                getSupportFragmentManager(), DenyDialog.TAG);
                    }
                    callCompoundButton.setChecked(false);
                }
                break;
            }
            case REQUEST_PERMISSION_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, getText(R.string.contacts_granted), Toast.LENGTH_SHORT).show();
                    checkPermissionsStatus();
                } else {
                    showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS);
                    if (!showRationale) {
                        new DenyDialog().show(
                                getSupportFragmentManager(), DenyDialog.TAG);
                    }
                    contactsCompoundButton.setChecked(false);
                }
                break;
            }
            case REQUEST_PERMISSION_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, getText(R.string.sms_granted), Toast.LENGTH_SHORT).show();
                    checkPermissionsStatus();
                } else {
                    showRationale = shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS);
                    if (!showRationale) {
                        new DenyDialog().show(
                                getSupportFragmentManager(), DenyDialog.TAG);
                    }
                    smsCompoundButton.setChecked(false);
                }
                break;
            }
            case REQUEST_PERMISSION_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, getText(R.string.storage_granted), Toast.LENGTH_SHORT).show();
                    checkPermissionsStatus();
                } else {
                    showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (!showRationale) {
                        new DenyDialog().show(
                                getSupportFragmentManager(), DenyDialog.TAG);
                    }
                    storageCompoundButton.setChecked(false);
                }
                break;
            }
        }
    }
}

