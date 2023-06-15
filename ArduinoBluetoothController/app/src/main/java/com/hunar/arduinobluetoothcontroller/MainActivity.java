package com.hunar.arduinobluetoothcontroller;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.hunar.arduinobluetoothcontroller.SharedPreference.SharedBluetoothConnectionSocket;

public class MainActivity extends AppCompatActivity {


    private  static BluetoothSocket mmSocket;
    private TextView bluetoothConnection, ConnectionStatus;
    private CardView LED, Servo, Terminal, Sensor;
    private SharedBluetoothConnectionSocket btConn=new SharedBluetoothConnectionSocket();

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("Home");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_home));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(MainActivity.this, MainActivity.class);
                startActivity(in);
                finish();
            }
        });


        initFields();
        listeners();

    }



    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    private void initFields() {
        bluetoothConnection=findViewById(R.id.buildBluetoothConnection);
        ConnectionStatus=findViewById(R.id.BluetoothConnectionStatus);
        LED=findViewById(R.id.cardViewLED);
        Servo=findViewById(R.id.cardViewServo);
        Terminal=findViewById(R.id.cardViewTerminal);
        Sensor=findViewById(R.id.cardViewSensor);

       mmSocket= btConn.getSocketConnection();
       if (mmSocket!=null)
       {
           String deviceName = getIntent().getStringExtra("deviceName");
           btConn.setDeviceName(deviceName);
           String DName=btConn.getDeviceName();
           if (DName != null) {

               // Get the device address to make BT Connection
               String deviceAddress = getIntent().getStringExtra("deviceAddress");
               // Show progress and connection status


               ConnectionStatus.setTextColor(this.getResources().getColor(R.color.green));
               ConnectionStatus.setText("Connected to " + DName + "...");
               bluetoothConnection.setEnabled(false);
           }
       }

    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    private void listeners() {

        bluetoothConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MainActivity.this, ConnectDeviceActiviy.class);
                startActivity(in);
                finish();
            }
        });

            LED.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mmSocket != null) {
                        Intent in = new Intent(MainActivity.this, LEDActivity.class);
                        startActivity(in);
                        finish();

                    } else {
                        errorMessage();

                    }
                }
            });

            Servo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mmSocket != null) {
                    Intent in=new Intent(MainActivity.this, ServoMotorActivity.class);
                    startActivity(in);
                    finish();

                    } else {
                        errorMessage();

                    }
                }
            });

            Terminal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mmSocket != null) {
                    Intent in=new Intent(MainActivity.this, TerminalActivity.class);
                    startActivity(in);
                    finish();

                    } else {
                        errorMessage();

                    }
                }
            });

        Sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mmSocket != null) {
                    Intent in=new Intent(MainActivity.this, SensorActivity.class);
                    startActivity(in);
                    finish();

                } else {
                    errorMessage();

                }
            }
        });
        if (mmSocket == null) {
            ConnectionStatus.setTextColor(this.getResources().getColor(R.color.red));
            ConnectionStatus.setText("Your Device is not Connected");
            Toast.makeText(MainActivity.this, "Check Your bluetooth Connection", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(MainActivity.this, "Exit", Toast.LENGTH_LONG).show();
        finish();
    }
    public void errorMessage()
    {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Connection Error")
                .setIcon(R.drawable.bluetooth)
                .setMessage("Connect Your Bluetooth First")
                .setPositiveButton("Ok", null).show();
    }
}