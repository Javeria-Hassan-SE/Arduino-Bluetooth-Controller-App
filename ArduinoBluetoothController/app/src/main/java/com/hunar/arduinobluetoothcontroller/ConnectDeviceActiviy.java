package com.hunar.arduinobluetoothcontroller;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.hunar.arduinobluetoothcontroller.Threads.CreateConnectThread;

public class ConnectDeviceActiviy extends AppCompatActivity {

    private Button connectBtn;
    private ProgressBar loadingBar;
    private TextView connectionStatus;

    private String deviceName = null;
    private String deviceAddress;
    public static Handler handler;
    public static CreateConnectThread createConnectThread;
    private Toolbar toolbar;
    private final static int CONNECTING_STATUS = 1; // used in bluetooth handler to identify message status


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_layout);

        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("Connect Device");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(ConnectDeviceActiviy.this, MainActivity.class);
                startActivity(in);
                finish();
            }
        });

        initFields();
        bluetoothConnection();
        GUIHandler();
        listeners();
    }
    private void initFields() {
        connectBtn=findViewById(R.id.buttonConnect);
        loadingBar=findViewById(R.id.progressBar);
        connectionStatus=findViewById(R.id.deviceConnectionStatus);
        loadingBar.setVisibility(View.GONE);
    }
    private void GUIHandler() {

        handler = new Handler(Looper.getMainLooper()) {
            @SuppressLint({"SetTextI18n", "ResourceAsColor"})
            @Override
            public void handleMessage(Message msg){
                if (msg.what == CONNECTING_STATUS) {
                    switch (msg.arg1) {
                        case 1:
                            connectionStatus.setTextColor(R.color.green);
                            connectionStatus.setText("Connected to " + deviceName);
                            loadingBar.setVisibility(View.GONE);
                            connectBtn.setEnabled(false);
                            Intent in=new Intent(ConnectDeviceActiviy.this, MainActivity.class);

                            in.putExtra("deviceName",deviceName);
                            in.putExtra("deviceAddress",deviceAddress);
                            startActivity(in);
                            finish();
                            break;
                        case -1:
                            connectionStatus.setTextColor(R.color.red);
                            connectionStatus.setText("Your Device is not Connected");
                            loadingBar.setVisibility(View.GONE);
                            connectBtn.setEnabled(true);
                            break;
                    }
                }
            }
        };

    }

    @SuppressLint("SetTextI18n")
    private void bluetoothConnection() {

        // If a bluetooth device has been selected from SelectDeviceActivity
        deviceName = getIntent().getStringExtra("deviceName");
        if (deviceName != null) {
            // Get the device address to make BT Connection
            deviceAddress = getIntent().getStringExtra("deviceAddress");
            // Show progress and connection status
            connectionStatus.setText("Connecting to " + deviceName + "...");
            connectionStatus.setTextColor(this.getResources().getColor(R.color.green));
            loadingBar.setVisibility(View.VISIBLE);
            connectBtn.setEnabled(false);

            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            createConnectThread = new CreateConnectThread(bluetoothAdapter, deviceAddress);
            createConnectThread.start();


           // Intent in=new Intent(ConnectDeviceActiviy.this, MainActivity.class);
          //  in.putExtra("deviceName",deviceName);
          //  in.putExtra("deviceAddress",deviceAddress);
          //  startActivity(in);
          //  finish();
        }
    }
    private void listeners() {
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        // Move to adapter list
                Intent intent = new Intent(ConnectDeviceActiviy.this, SelectDeviceActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in=new Intent(ConnectDeviceActiviy.this, MainActivity.class);
        in.putExtra("deviceName",deviceName);
        in.putExtra("deviceAddress",deviceAddress);
        startActivity(in);
        finish();

    }
}
