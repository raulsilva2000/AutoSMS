package com.example.autosms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class SIMCardAdapter extends ArrayAdapter<SIMCard> {

    public SIMCardAdapter(Context context, List<SIMCard> simCards) {
        super(context, 0, simCards);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simcard_item, parent, false);
        }

        final SIMCard simCard = getItem(position);

        TextView nameTextView = convertView.findViewById(R.id.text_view_name);
        TextView slotNumberTextView = convertView.findViewById(R.id.text_view_slot_number);
        CheckBox checkBox = convertView.findViewById(R.id.check_box_contact);

        nameTextView.setText(simCard.getName());
        slotNumberTextView.setText(simCard.getSlotNumber());
        checkBox.setChecked(simCard.isSelected());

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simCard.setSelected(((CheckBox) v).isChecked());
            }
        });

        return convertView;
    }
}
