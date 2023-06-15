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
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hunar.arduinobluetoothcontroller.SharedPreference.SharedBluetoothConnectionSocket;
import com.hunar.arduinobluetoothcontroller.Threads.ConnectedThread;

public class ServoMotorActivity extends AppCompatActivity {

    private TextView startServoBtn, showYourSelectedAngle,servoAngleRangeTxt;
    private SeekBar seekBar;
    private static BluetoothSocket mmSocket;
    public String cmdText;
    public ConnectedThread connectedThread;
    private final static int MESSAGE_READ = 2;
    public static Handler handler;
    public SharedBluetoothConnectionSocket con=new SharedBluetoothConnectionSocket();
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servo_motor_layout);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("Servo Motor");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(ServoMotorActivity.this, MainActivity.class);
                startActivity(in);
            }
        });

        initFields();
    }

    @SuppressLint("SetTextI18n")
    private void initFields() {
        startServoBtn=findViewById(R.id.startServo);
        showYourSelectedAngle=findViewById(R.id.progressServo);
        seekBar=findViewById(R.id.seekBar);
        servoAngleRangeTxt=findViewById(R.id.servoProgress);
        mmSocket=con.getSocketConnection();
        connectedThread=new ConnectedThread(mmSocket);
        setServoAngle();



    }

    private void setServoAngle() {

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                showYourSelectedAngle.setText(String.valueOf("Servo motor Rotation Angle  :"+progress));
                cmdText = String.valueOf(progress);
                Log.d("msg",cmdText);
                connectedThread.write(cmdText);
                MessageHandler();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });
    }
    private void MessageHandler() {
        handler = new Handler(Looper.getMainLooper()) {
            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(Message msg){
                if (msg.what == MESSAGE_READ) {
                    String arduinoMsgTxt = msg.obj.toString(); // Read message from Arduino
                   // servoAngleRangeTxt.setText(String.valueOf("Arduino Output "+arduinoMsgTxt));
                    }
            }
        };
        con.setHandler(handler);
      //  servoAngleRangeTxt.setText(String.valueOf("Arduino Output "+" "));

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in=new Intent(ServoMotorActivity.this, MainActivity.class);
        startActivity(in);
        finish();
    }
}
