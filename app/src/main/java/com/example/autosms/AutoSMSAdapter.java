package com.example.autosms;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Comparator;

public class AutoSMSAdapter extends RecyclerView.Adapter<AutoSMSAdapter.ViewHolder> {
    ArrayList<AutoSMS> replys;
    Context context;

    public AutoSMSAdapter(ArrayList<AutoSMS> replys) {
        this.replys = replys;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.autosms_reply, parent, false);
        context = parent.getContext();
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return replys.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView replyTitle;
        ImageView replyOptions;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            replyTitle = itemView.findViewById(R.id.textViewReplyTitle);
            replyOptions = itemView.findViewById(R.id.imageViewReplyOptions);

            replyOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(view);
                }
            });
        }

        public void bind(int position) {
            this.position = position;
            replyTitle.setText(replys.get(position).getTitle());
        }

        private void showPopupMenu(View anchorView) {
            PopupMenu popupMenu = new PopupMenu(context, anchorView);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();

                    // Handle menu item clicks here
                    if (id == R.id.edit) {
                        Log.d("EDIT", "EDITOU");
                        // Perform edit operation if needed
                        return true;
                    } else if (id == R.id.delete) {
                        Log.d("DELETE", "ELIMINOU");
                        // Remove the item at the clicked position from the list
                        if (position != RecyclerView.NO_POSITION) {
                            replys.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, replys.size());
                        }
                        return true;
                    }
                    return false;
                }
            });

            popupMenu.show();
        }
    }

    // Sort method
    public void sortItemsByCreation(int sortByFirstCreated) {
        // Apply the sorting criterion to the filtered list
        replys.sort(new Comparator<AutoSMS>() {
            @Override
            public int compare(AutoSMS item1, AutoSMS item2) {
                if (sortByFirstCreated == 0) {
                    return Long.compare(item1.getTimestamp(), item2.getTimestamp());
                } else {
                    return Long.compare(item2.getTimestamp(), item1.getTimestamp());
                }
            }
        });

        notifyDataSetChanged();
    }
}
