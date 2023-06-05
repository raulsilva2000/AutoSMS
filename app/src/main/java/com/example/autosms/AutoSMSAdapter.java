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

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class AutoSMSAdapter extends RecyclerView.Adapter<AutoSMSAdapter.ViewHolder> {
    List<AutoSMS> replys = new ArrayList<>();
    List<AutoSMS> filteredReplys = new ArrayList<>();
    Context context;
    private RecyclerView myRecyclerView;

    public AutoSMSAdapter(List<AutoSMS> replys, Context context) {
        this.replys.addAll(replys);
        this.filteredReplys.addAll(replys);
        this.context = context;
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
        return filteredReplys.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView replyTitle;
        TextView replyDays;
        TextView replyNumbers;
        TextView replyTime;
        ImageView replyOptions;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            replyTitle = itemView.findViewById(R.id.textViewMessageTitle);
            replyDays = itemView.findViewById(R.id.textViewMessageTime);
            replyNumbers = itemView.findViewById(R.id.textViewMessageNumber);
            replyTime = itemView.findViewById(R.id.textViewReplyTime);
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
            //Atribuicao do titulo
            replyTitle.setText(filteredReplys.get(position).getTitle());

            //Atribuicao dos dias
            StringBuilder replyDaysText = new StringBuilder();
            if(Arrays.equals(filteredReplys.get(position).getDays(), new Boolean[]{true, true, true, true, true, true, true})){
                replyDaysText.append("Every day");
                replyDays.setText(replyDaysText);
            } else if (Arrays.equals(filteredReplys.get(position).getDays(), new Boolean[]{true, true, true, true, true, false, false})){
                replyDaysText.append("Week days");
                replyDays.setText(replyDaysText);
            } else if (Arrays.equals(filteredReplys.get(position).getDays(), new Boolean[]{false, false, false, false, false, true, true})) {
                replyDaysText.append("Weekend days");
                replyDays.setText(replyDaysText);
            } else {
                int i=0;
                for(Boolean value : filteredReplys.get(position).getDays()){
                    if(i==0 && value.equals(true)){
                        replyDaysText.append("Mon./");
                    } else if (i==1 && value.equals(true)) {
                        replyDaysText.append("Tue./");
                    } else if (i==2 && value.equals(true)) {
                        replyDaysText.append("Wed./");
                    } else if (i==3 && value.equals(true)) {
                        replyDaysText.append("Thu./");
                    } else if (i==4 && value.equals(true)) {
                        replyDaysText.append("Fri./");
                    } else if (i==5 && value.equals(true)) {
                        replyDaysText.append("Sat./");
                    } else if (i==6 && value.equals(true)) {
                        replyDaysText.append("Sun./");
                    }
                    i++;
                }
                replyDays.setText(replyDaysText.substring(0, replyDaysText.length() - 1));
            }

            //Atribuicao do total de numeros
            if(Objects.equals(filteredReplys.get(position).getNumbers().get(0), "unknownNumbers")){ //if it's to Unknown Numbers
                String totalNumbers = "Unknown numbers";
                replyNumbers.setText(totalNumbers);
            } else if(Objects.equals(filteredReplys.get(position).getNumbers().get(0), "anyNumber")){ //if it's to Any Number
                String totalNumbers = "Any number";
                replyNumbers.setText(totalNumbers);
            } else { //if it's to Specific Contact
                if(filteredReplys.get(position).getNumbers().size() == 1){
                    String totalNumbers = String.valueOf(filteredReplys.get(position).getNumbers().size()) + " number";
                    replyNumbers.setText(totalNumbers);
                } else {
                    String totalNumbers = String.valueOf(filteredReplys.get(position).getNumbers().size()) + " numbers";
                    replyNumbers.setText(totalNumbers);
                }
            }

            //Atribuicao do tempo
            String timeFromTo = filteredReplys.get(position).getTimeFrom() + " to " + filteredReplys.get(position).getTimeTo();
            replyTime.setText(timeFromTo);

        }

        private void showPopupMenu(View anchorView) {
            PopupMenu popupMenu = new PopupMenu(context, anchorView);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();

                    if (id == R.id.delete) {
                        // Perform delete operation
                        // Remove the item at the clicked position from the list
                        if (position != RecyclerView.NO_POSITION) {
                            //Update JSON file
                            try {
                                //delete item in the RecyclerView
                                filteredReplys.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, filteredReplys.size());

                                Gson gson = new Gson();

                                // Convert the updated data structure to JSON
                                String updatedJson = gson.toJson(filteredReplys);

                                // Write the updated JSON to the file
                                FileOutputStream fos = context.openFileOutput("data.json", Context.MODE_PRIVATE);
                                OutputStreamWriter osw = new OutputStreamWriter(fos);
                                osw.write(updatedJson);
                                osw.close();
                                fos.close();
                                Log.d("JSON Update", "JSON file updated successfully.");
                            } catch (IOException e) {
                                Log.e("JSON Update", "Error updating JSON file: " + e.getMessage());
                            }
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
    public void sortItemsByCreation(int sortBy) {
        // Apply the sorting criterion to the filtered list
        filteredReplys.sort(new Comparator<AutoSMS>() {
            @Override
            public int compare(AutoSMS item1, AutoSMS item2) {
                if (sortBy == 0) { //Last created
                    return Long.compare(item2.getTimestamp(), item1.getTimestamp());
                } else { //First created
                    return Long.compare(item1.getTimestamp(), item2.getTimestamp());
                }
            }
        });

        notifyDataSetChanged();
    }

    public void filter(String query) {
        filteredReplys.clear();

        if (query.isEmpty()) {
            filteredReplys.addAll(replys);
        } else {
            String lowerCaseQuery = query.toLowerCase();

            for (AutoSMS reply : replys) {
                if (reply.getTitle().toLowerCase().contains(lowerCaseQuery)) {
                    filteredReplys.add(reply);
                }
            }
        }

        notifyDataSetChanged();
    }
}
