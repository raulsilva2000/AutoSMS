package com.example.autosms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {

    public ContactAdapter(Context context, List<Contact> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_item, parent, false);
        }

        final Contact contact = getItem(position);

        TextView nameTextView = convertView.findViewById(R.id.text_view_name);
        TextView numberTextView = convertView.findViewById(R.id.text_view_slot_number);
        CheckBox checkBox = convertView.findViewById(R.id.check_box_contact);

        nameTextView.setText(contact.getName());
        numberTextView.setText(contact.getNumber());
        checkBox.setChecked(contact.isSelected());

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact.setSelected(((CheckBox) v).isChecked());
            }
        });

        return convertView;
    }
}
