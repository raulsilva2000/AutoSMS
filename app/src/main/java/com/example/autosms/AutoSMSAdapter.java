package com.example.autosms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AutoSMSAdapter extends RecyclerView.Adapter<AutoSMSAdapter.ViewHolder>{
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
        /* Aqui faz a correspondencia */
        holder.replyTitle.setText(replys.get(position).getTitle());


    }

    @Override
    public int getItemCount() {
        return replys.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView replyTitle;
        TextView replyDays;
        TextView replyNumbers;
        TextView replyTime;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            replyTitle=itemView.findViewById(R.id.textViewReplyTitle);
            replyDays=itemView.findViewById(R.id.textViewReplyDays);
            replyNumbers=itemView.findViewById(R.id.textViewReplyNumbers);
            replyTime=itemView.findViewById(R.id.textViewReplyTime);
        }
    }
}
