package com.hunar.arduinobluetoothcontroller.Threads;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import com.hunar.arduinobluetoothcontroller.SharedPreference.SharedBluetoothConnectionSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public  class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    public static Handler handler;
    private final static int MESSAGE_READ = 2;
    public SharedBluetoothConnectionSocket socket=new SharedBluetoothConnectionSocket();

    public ConnectedThread(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes = 0; // bytes returned from read()
        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                    /*
                    Read from the InputStream from Arduino until termination character is reached.
                    Then send the whole String message to GUI Handler.
                     */
                buffer[bytes] = (byte) mmInStream.read();
                String readMessage;
                if (buffer[bytes] == '\n'){
                    readMessage = new String(buffer,0,bytes);
                    Log.e("Arduino Message",readMessage);
                    handler=socket.getHandler();
                    handler.obtainMessage(MESSAGE_READ,readMessage).sendToTarget();
                    socket.setHandler(handler);
                    bytes = 0;
                } else {
                    bytes++;
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public  void write(String input) {
        byte[] bytes = input.getBytes(); //converts entered String into bytes
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            Log.e("Send Error","Unable to send message",e);
        }
    }


    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}

