package com.hunar.arduinobluetoothcontroller.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hunar.arduinobluetoothcontroller.Models.TerminalMessagesModel;
import com.hunar.arduinobluetoothcontroller.R;
import java.util.List;

public class TerminalMessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Object> deviceList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        LinearLayout linearLayout;

        public ViewHolder(View v) {
            super(v);
            textName = v.findViewById(R.id.terminalMessages);
        }
    }

    public TerminalMessagesAdapter(Context context, List<Object> deviceList) {
        this.deviceList =deviceList;

    }

    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_terminal_messages, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        ViewHolder itemHolder = (ViewHolder) holder;
        TerminalMessagesModel msgModel = (TerminalMessagesModel) deviceList.get(position);
        if (msgModel!=null)
        {
            itemHolder.textName.setText(msgModel.getMessage());
        }



    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }
}