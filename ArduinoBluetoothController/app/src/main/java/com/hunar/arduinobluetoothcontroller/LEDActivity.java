package com.hunar.arduinobluetoothcontroller;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hunar.arduinobluetoothcontroller.SharedPreference.SharedBluetoothConnectionSocket;
import com.hunar.arduinobluetoothcontroller.Threads.ConnectedThread;

import de.hdodenhof.circleimageview.CircleImageView;



public class LEDActivity extends AppCompatActivity {


    private CircleImageView LEDImage;
    private static BluetoothSocket mmSocket;
    private TextView onLEDBtn, OffLEDBtn, arduinoMsg;
    public String cmdText;
    public  ConnectedThread connectedThread;
    private final static int MESSAGE_READ = 2;
    public static Handler handler;
    public SharedBluetoothConnectionSocket con=new SharedBluetoothConnectionSocket();
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_layout);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("LED");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(LEDActivity.this, MainActivity.class);
                startActivity(in);
            }
        });

        initFields();
        listeners();
        MessageHandler();

    }

    private void MessageHandler() {
        handler = new Handler(Looper.getMainLooper()) {
            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(Message msg){
                if (msg.what == MESSAGE_READ) {
                    String arduinoMsgTxt = msg.obj.toString(); // Read message from Arduino
                    switch (arduinoMsgTxt.toLowerCase()) {
                        case "led is turned on":
                            @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = getResources().getDrawable(R.drawable.onled);
                            LEDImage.setImageDrawable(drawable);
                            arduinoMsg.setText("Arduino Message : " + arduinoMsgTxt);
                            break;
                        case "led is turned off":
                            @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable2 = getResources().getDrawable(R.drawable.offled);
                            LEDImage.setImageDrawable(drawable2);
                            arduinoMsg.setText("Arduino Message : " + arduinoMsgTxt);
                            break;
                    }

                }

            }
        };
        con.setHandler(handler);

    }

    private void initFields() {
        LEDImage=findViewById(R.id.LEDImage);
        onLEDBtn=findViewById(R.id.OnLED);
        OffLEDBtn=findViewById(R.id.OffLED);
        arduinoMsg=findViewById(R.id.textViewInfo);
        mmSocket=con.getSocketConnection();
        connectedThread=new ConnectedThread(mmSocket);

    }

    public void listeners() {
        onLEDBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cmdText = "<turn on>";
                Log.d("msg",cmdText);
                connectedThread.write(cmdText);
            }
        });
        OffLEDBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cmdText = "<turn off>";
                Log.d("msg",cmdText);
                connectedThread.write(cmdText);
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in=new Intent(LEDActivity.this, MainActivity.class);
        startActivity(in);
        finish();
    }
}
