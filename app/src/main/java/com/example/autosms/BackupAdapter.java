package com.example.autosms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class BackupAdapter extends RecyclerView.Adapter<BackupAdapter.ViewHolder> {

    private ArrayList<BackupItem> backupList;

    public BackupAdapter(ArrayList<BackupItem> backupList) {
        this.backupList = backupList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_item_backup, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BackupItem backupItem = backupList.get(position);
        holder.dateTextView.setText(backupItem.getDate());
        holder.backupCheckBox.setChecked(backupItem.isChecked());
        holder.backupCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                backupItem.setChecked(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return backupList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;
        public CheckBox backupCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            backupCheckBox = itemView.findViewById(R.id.backupCheckBox);
        }
    }

    public ArrayList<BackupItem> getSelectedBackups() {
        ArrayList<BackupItem> selectedItems = new ArrayList<>();
        for (BackupItem item : backupList) {
            if (item.isChecked()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

}
