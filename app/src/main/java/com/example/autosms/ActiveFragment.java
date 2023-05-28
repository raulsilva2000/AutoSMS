package com.example.autosms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ActiveFragment extends Fragment {
    private RecyclerView.Adapter autoSMSAdapter;
    private RecyclerView recyclerViewReplys;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.active, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerview(view);
    }

    private void initRecyclerview(View view){
        ArrayList<AutoSMS> replys = new ArrayList<>();

        ArrayList<String> simCards = new ArrayList<>();
        simCards.add("SIM1");

        ArrayList<String> numbers = new ArrayList<>();
        numbers.add("+351937309155");
        numbers.add("+351937309155");
        numbers.add("+351937309155");

        replys.add(new AutoSMS("For all my contacts",
                "Obrigado por entrar em contacto. De momento não me encontro disponível. Irei ligar-lhe de volta assim que possível.",
                simCards,
                numbers,
                "1",
                "1"));

        replys.add(new AutoSMS("For all my contacts",
                "Obrigado por entrar em contacto. De momento não me encontro disponível. Irei ligar-lhe de volta assim que possível.",
                simCards,
                numbers,
                "1",
                "1"));

        replys.add(new AutoSMS("For all my contacts",
                "Obrigado por entrar em contacto. De momento não me encontro disponível. Irei ligar-lhe de volta assim que possível.",
                simCards,
                numbers,
                "1",
                "1"));

        replys.add(new AutoSMS("For all my contacts",
                "Obrigado por entrar em contacto. De momento não me encontro disponível. Irei ligar-lhe de volta assim que possível.",
                simCards,
                numbers,
                "1",
                "1"));

        replys.add(new AutoSMS("For all my contacts",
                "Obrigado por entrar em contacto. De momento não me encontro disponível. Irei ligar-lhe de volta assim que possível.",
                simCards,
                numbers,
                "1",
                "1"));

        replys.add(new AutoSMS("For all my contacts",
                "Obrigado por entrar em contacto. De momento não me encontro disponível. Irei ligar-lhe de volta assim que possível.",
                simCards,
                numbers,
                "1",
                "1"));

        replys.add(new AutoSMS("For all my contacts",
                "Obrigado por entrar em contacto. De momento não me encontro disponível. Irei ligar-lhe de volta assim que possível.",
                simCards,
                numbers,
                "1",
                "1"));

        replys.add(new AutoSMS("For all my contacts",
                "Obrigado por entrar em contacto. De momento não me encontro disponível. Irei ligar-lhe de volta assim que possível.",
                simCards,
                numbers,
                "1",
                "1"));

        replys.add(new AutoSMS("For all my contacts",
                "Obrigado por entrar em contacto. De momento não me encontro disponível. Irei ligar-lhe de volta assim que possível.",
                simCards,
                numbers,
                "1",
                "1"));

        recyclerViewReplys = view.findViewById(R.id.recyclerView);
        recyclerViewReplys.setLayoutManager(new LinearLayoutManager(getView().getContext(), LinearLayoutManager.VERTICAL, false));

        autoSMSAdapter = new AutoSMSAdapter(replys);
        recyclerViewReplys.setAdapter(autoSMSAdapter);
    }
}