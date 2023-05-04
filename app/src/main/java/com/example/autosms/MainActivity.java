package com.example.autosms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter autoSMSAdapter;
    private RecyclerView recyclerViewReplys;

    //BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerview();

        //bottomNavigationView = findViewById(R.id.)
    }

    private void initRecyclerview(){
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

        recyclerViewReplys = findViewById(R.id.recyclerView);
        recyclerViewReplys.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        autoSMSAdapter = new AutoSMSAdapter(replys);
        recyclerViewReplys.setAdapter(autoSMSAdapter);
    }
}