package com.hunar.arduinobluetoothcontroller;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hunar.arduinobluetoothcontroller.Adapters.TerminalMessagesAdapter;
import com.hunar.arduinobluetoothcontroller.Models.TerminalMessagesModel;
import com.hunar.arduinobluetoothcontroller.SharedPreference.SharedBluetoothConnectionSocket;
import com.hunar.arduinobluetoothcontroller.Threads.ConnectedThread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TerminalActivity extends AppCompatActivity {


    private EditText enterMessage;
    private TextView sendMsg , stopMsg;
    private RecyclerView msgRecycler;
    private String Message;
    private static BluetoothSocket mmSocket;public ConnectedThread connectedThread;
    private final static int MESSAGE_READ = 2;
    public static Handler handler;
    int stop=0;
    public SharedBluetoothConnectionSocket con=new SharedBluetoothConnectionSocket();

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal_layout);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("Terminal");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(TerminalActivity.this, MainActivity.class);
                startActivity(in);
            }
        });

        initFields();
        listeners();

    }
    private void MessageHandler() {
        handler=con.getHandler();
        handler = new Handler(Looper.getMainLooper()) {
            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(android.os.Message msg){
                if (msg.what == MESSAGE_READ) {
                    String arduinoMsgTxt = msg.obj.toString(); // Read message from Arduino
                    List<Object> terminalMessages = new ArrayList<>();
                   //loop condition here to show list of messages
                            TerminalMessagesModel model = new TerminalMessagesModel(arduinoMsgTxt);
                            terminalMessages.add(model);


                    Log.d("terminal message", arduinoMsgTxt);
                    msgRecycler.setLayoutManager(new LinearLayoutManager(TerminalActivity.this));
                    TerminalMessagesAdapter msgListAdapter = new TerminalMessagesAdapter(TerminalActivity.this, terminalMessages);
                    msgRecycler.setAdapter(msgListAdapter);

                }
            }
        };
        con.setHandler(handler);

    }
    private void initFields() {
        enterMessage=findViewById(R.id.terminalMsg);
        sendMsg=findViewById(R.id.sendMsg);
        stopMsg=findViewById(R.id.stopMsg);
        msgRecycler=findViewById(R.id.recyclerViewMsg);
        mmSocket=con.getSocketConnection();
        connectedThread=new ConnectedThread(mmSocket);
    }

    private void listeners() {
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message=enterMessage.getText().toString();
                Log.d("msg",Message);
                connectedThread.write(Message);
                MessageHandler();
            }
        });
        stopMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop=1;
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in=new Intent(TerminalActivity.this, MainActivity.class);
        startActivity(in);
        finish();
    }
}
