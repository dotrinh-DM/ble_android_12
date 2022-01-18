/*
 * Created by dotrinh on 1/18/22, 8:22 PM
 * Copyright (c) 2022. dotr Inc. All rights reserved.
 */

package com.dotrinh.ble_android_12.pojo;

import android.bluetooth.BluetoothDevice;

public class DeviceClass {
    public BluetoothDevice device;

    public DeviceClass(BluetoothDevice device) {
        this.device = device;
    }
}
