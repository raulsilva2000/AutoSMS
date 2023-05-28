package com.example.autosms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class ActiveFragment extends Fragment {
    private AutoSMSAdapter autoSMSAdapter;
    private RecyclerView recyclerViewReplys;
    private ArrayList<AutoSMS> replys;
    Spinner sortSpinner;

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

        sortSpinner = view.findViewById(R.id.spinner_filters);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                if (selectedOption.equals("First created")) {
                    autoSMSAdapter.sortItemsByCreation(position);
                } else if(selectedOption.equals("Last created")) {
                    autoSMSAdapter.sortItemsByCreation(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });
    }

    private void initRecyclerview(View view){
        replys = new ArrayList<>();

        ArrayList<String> simCards = new ArrayList<>();
        simCards.add("SIM1");

        ArrayList<String> numbers = new ArrayList<>();
        numbers.add("+351937309155");
        numbers.add("+351937309155");
        numbers.add("+351937309155");

        replys.add(new AutoSMS("For all my contacts1",
                "Obrigado por entrar em contacto. De momento não me encontro disponível. Irei ligar-lhe de volta assim que possível.",
                simCards,
                numbers,
                "1",
                "1"));

        replys.add(new AutoSMS("For all my contacts2",
                "Obrigado por entrar em contacto. De momento não me encontro disponível. Irei ligar-lhe de volta assim que possível.",
                simCards,
                numbers,
                "1",
                "1"));

        replys.add(new AutoSMS("For all my contacts3",
                "Obrigado por entrar em contacto. De momento não me encontro disponível. Irei ligar-lhe de volta assim que possível.",
                simCards,
                numbers,
                "1",
                "1"));

        replys.add(new AutoSMS("For all my contacts4",
                "Obrigado por entrar em contacto. De momento não me encontro disponível. Irei ligar-lhe de volta assim que possível.",
                simCards,
                numbers,
                "1",
                "1"));

        replys.add(new AutoSMS("For all my contacts5",
                "Obrigado por entrar em contacto. De momento não me encontro disponível. Irei ligar-lhe de volta assim que possível.",
                simCards,
                numbers,
                "1",
                "1"));

        replys.add(new AutoSMS("For all my contacts6",
                "Obrigado por entrar em contacto. De momento não me encontro disponível. Irei ligar-lhe de volta assim que possível.",
                simCards,
                numbers,
                "1",
                "1"));

        replys.add(new AutoSMS("For all my contacts7",
                "Obrigado por entrar em contacto. De momento não me encontro disponível. Irei ligar-lhe de volta assim que possível.",
                simCards,
                numbers,
                "1",
                "1"));

        replys.add(new AutoSMS("For all my contacts8",
                "Obrigado por entrar em contacto. De momento não me encontro disponível. Irei ligar-lhe de volta assim que possível.",
                simCards,
                numbers,
                "1",
                "1"));

        replys.add(new AutoSMS("For all my contacts9",
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