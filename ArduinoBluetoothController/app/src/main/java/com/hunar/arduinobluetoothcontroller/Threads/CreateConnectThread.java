package com.hunar.arduinobluetoothcontroller.Threads;

import static android.content.ContentValues.TAG;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.hunar.arduinobluetoothcontroller.SharedPreference.SharedBluetoothConnectionSocket;

import java.io.IOException;
import java.util.UUID;


public class CreateConnectThread extends Thread {

    public static BluetoothSocket mmSocket;
    public static Handler handler;
    public  ConnectedThread connectedThread;
    public  int CONNECTING_STATUS = 1; // used in bluetooth handler to identify message status
    public SharedBluetoothConnectionSocket s=new SharedBluetoothConnectionSocket();

    public CreateConnectThread(BluetoothAdapter bluetoothAdapter, String address) {
            /*
            Use a temporary object that is later assigned to mmSocket
            because mmSocket is final.
             */
        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
        BluetoothSocket tmp = null;
        UUID uuid = bluetoothDevice.getUuids()[0].getUuid();

        try {
                /*
                Get a BluetoothSocket to connect with the given BluetoothDevice.
                Due to Android device varieties,the method below may not work fo different devices.
                You should try using other methods i.e. :
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                 */
            tmp = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);

        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }

        mmSocket = tmp;
        // set

    }

    public void run() {
        // Cancel discovery because it otherwise slows down the connection.
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.cancelDiscovery();
        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket.connect();
            Log.e("Status", "Device connected");
            Log.e("Status", String.valueOf(CONNECTING_STATUS));
            handler = new Handler(Looper.getMainLooper());
            handler.obtainMessage(CONNECTING_STATUS, 1, -1).sendToTarget();
            s.setHandler(handler);
            s.setSocketConnection(mmSocket);


        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            try {
                mmSocket.close();
                Log.e("Status", "Cannot connect to device");
                handler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget();
                s.setHandler(handler);
                s.setSocketConnection(mmSocket);
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close the client socket", closeException);
            }
            return;
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        connectedThread = new ConnectedThread(mmSocket);
        connectedThread.run();
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
            s.setHandler(handler);
            s.setSocketConnection(mmSocket);
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }
}

