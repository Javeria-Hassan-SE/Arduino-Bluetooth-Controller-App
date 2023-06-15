package com.hunar.arduinobluetoothcontroller.SharedPreference;

import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

public class SharedBluetoothConnectionSocket extends Application {

    public  static String deviceName;
    public static BluetoothSocket mmSocket;
    public static Handler handler;

    public BluetoothSocket getSocketConnection() {
        return mmSocket;
    }

    public void setSocketConnection(BluetoothSocket _mmSocket) {
        mmSocket = _mmSocket;
    }
    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler _handler) {
        handler = _handler;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String _deviceName) {
        deviceName = _deviceName;
    }
}
