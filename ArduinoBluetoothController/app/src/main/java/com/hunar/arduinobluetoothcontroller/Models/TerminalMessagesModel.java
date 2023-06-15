package com.hunar.arduinobluetoothcontroller.Models;

public class TerminalMessagesModel {

    public String Message;

    public TerminalMessagesModel(String message) {
        Message = message;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
