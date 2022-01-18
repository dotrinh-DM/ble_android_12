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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dotrinh.ble_android_12.databinding.ActivityMainBinding;
import com.dotrinh.ble_android_12.databinding.RowItemBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    public ArrayList<BluetoothDevice> dataArr = new ArrayList<>();
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mainBinding.getRoot();
        setContentView(view);

        //setup
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
                        LogI("bi tu choi: " + item.getPermissionName());
                        startScan();
                    }
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();//request tiep neu chua co quyen
            }
        }).check();

        //adapter
        mainBinding.recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new ChildAdapter();
        mainBinding.recycler.setAdapter(adapter);
    }

    void startScan() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothAdapter.getBluetoothLeScanner().startScan(mScanCB);
    }

    ScanCallback mScanCB = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice device = result.getDevice();
            String deviceName = device.getName();
            if (deviceName == null) {
                return;
            }
            LogI("Found the device: " + deviceName + " - Add: " + device.getAddress());
            if (!checkExist(device)) {
                dataArr.add(device);
                adapter.notifyDataSetChanged();
            }
        }
    };

    boolean checkExist(BluetoothDevice device) {
        for (BluetoothDevice item : dataArr) {
            if (item.getAddress().equals(device.getAddress())) {
                return true;
            }
        }
        return false;
    }


    public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {

        @Override
        public int getItemCount() {
            return dataArr.size();
        }

        public ChildAdapter.ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
            return new ChildViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ChildAdapter.ChildViewHolder holder, int position) {
            holder.row.rowItemName.setText(dataArr.get(position).getName());
            holder.row.rowItemAddress.setText(dataArr.get(position).getAddress());
        }

        class ChildViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            RowItemBinding row;

            ChildViewHolder(@NonNull View itemView) {
                super(itemView);
                row = RowItemBinding.bind(itemView);
                row.cardViewItem.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                LogI("click: " + dataArr.get(getAdapterPosition()).getName() + " : " + dataArr.get(getAdapterPosition()).getAddress());
            }
        }

    }
}