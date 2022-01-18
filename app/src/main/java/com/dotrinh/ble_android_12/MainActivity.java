/*
 * Created by dotrinh on 1/18/22, 7:26 PM
 * Copyright (c) 2022. dotr Inc. All rights reserved.
 */

package com.dotrinh.ble_android_12;

import static com.dotrinh.ble_android_12.LogUtil.LogI;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] permissionArr;
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.R) { //Android 12 above
            permissionArr = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN};
        } else {
            permissionArr = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        }
        Dexter.withContext(this).withPermissions(permissionArr).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    LogI("Cho phep app su dung location runtime");
                    startScan();
                }
                if (report.isAnyPermissionPermanentlyDenied()) {
                    List<PermissionDeniedResponse> a = report.getDeniedPermissionResponses();
                    for (PermissionDeniedResponse item : a) {
                        LogI("--------------- bi tu choi: " + item.getPermissionName());
                        startScan();
                    }
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();//request tiep neu chua co quyen
            }
        }).check();
    }

    void startScan() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter _centralManager = bluetoothManager.getAdapter();
        ;
        _centralManager.getBluetoothLeScanner().startScan(mScanCB);
    }

    ScanCallback mScanCB = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            String deviceName = result.getDevice().getName();
            if (deviceName == null) {
                return;
            }
            LogI("Found the device: " + deviceName);
            BluetoothDevice device = result.getDevice();
//            LogI(deviceName);
        }
    };
}