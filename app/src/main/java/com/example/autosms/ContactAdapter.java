package com.example.autosms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends ArrayAdapter<String> {

    private Context context;
    private int resource;
    private List<String> contacts;
    private List<String> selectedContacts;

    public ContactAdapter(Context context, int resource, List<String> contacts) {
        super(context, resource, contacts);
        this.context = context;
        this.resource = resource;
        this.contacts = contacts;
        this.selectedContacts = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.checkBoxContact = convertView.findViewById(R.id.checkbox_contact);
            viewHolder.textViewContactName = convertView.findViewById(R.id.text_contact_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String contact = contacts.get(position);
        viewHolder.textViewContactName.setText(contact);
        viewHolder.checkBoxContact.setChecked(selectedContacts.contains(contact));

        viewHolder.checkBoxContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedContacts.add(contact);
                } else {
                    selectedContacts.remove(contact);
                }
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        CheckBox checkBoxContact;
        TextView textViewContactName;
    }

    public List<String> getSelectedContacts() {
        return selectedContacts;
    }
}
