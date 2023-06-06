package com.example.autosms;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class SentMessagesAdapter extends RecyclerView.Adapter<SentMessagesAdapter.ViewHolder> {
    List<SentMessage> messages;
    Context context;

    public SentMessagesAdapter(List<SentMessage> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_message_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull SentMessagesAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageTitle;
        TextView messageTime;
        TextView messageNumber;
        ImageView openMessage;
        String message;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTitle = itemView.findViewById(R.id.textViewMessageTitle);
            messageTime = itemView.findViewById(R.id.textViewMessageTime);
            messageNumber = itemView.findViewById(R.id.textViewMessageNumber);
            openMessage = itemView.findViewById(R.id.imageViewMessageArrow);

            openMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CheckMessageSent.class);
                    intent.putExtra("title", messageTitle.getText().toString());
                    intent.putExtra("time", messageTime.getText().toString());
                    intent.putExtra("number", messageNumber.getText().toString());
                    intent.putExtra("message", message);
                    context.startActivity(intent);
                }
            });
        }

        public void bind(int position) {
            this.position = position;
            //Attribution of title
            messageTitle.setText(String.format("Sent by: %s", messages.get(position).getTitle()));

            //Attribution of time
            messageTime.setText(messages.get(position).getTime());

            //Attribution of numbers
            messageNumber.setText(messages.get(position).getNumber());

            message = messages.get(position).getMessage();
        }
    }

    // Sort method
    public void sortItemsByCreation(int sortBy) {
        // Apply the sorting criterion to the filtered list
        messages.sort(new Comparator<SentMessage>() {
            @Override
            public int compare(SentMessage item1, SentMessage item2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try {
                    Date date1 = format.parse(item1.getTime());
                    Date date2 = format.parse(item2.getTime());

                    if (sortBy == 0) { // Last sent
                        return date2.compareTo(date1);
                    } else { // First sent
                        return date1.compareTo(date2);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return 0;
            }
        });

        notifyDataSetChanged();
    }

}
