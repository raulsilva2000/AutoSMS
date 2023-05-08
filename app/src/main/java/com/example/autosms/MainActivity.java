package com.example.autosms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    /*private RecyclerView.Adapter autoSMSAdapter;
    private RecyclerView recyclerViewReplys;
    DrawerLayout drawerLayout;*/
    BottomNavigationView bottomNavigationView;

    Active activeFragment = new Active();
    NewAutoSMS newAutoSMSFragment = new NewAutoSMS();
    SentMessages sentMessagesFragment = new SentMessages();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initRecyclerview();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, activeFragment).commit();

        //drawerLayout = findViewById(R.id.drawer_layer);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.active){
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, activeFragment).commit();
                } else if (id == R.id.autoSMS) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, newAutoSMSFragment).commit();
                } else if (id == R.id.sent) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerFrame, sentMessagesFragment).commit();
                }
                return false;
            }
        });

    }

    /*
    public void ClickDrawerMenu(View view){
        openDrawer(drawerLayout);
    }

    private void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeDrawer(View view){
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void dashboard(View view) {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void newAutoSMS(View view) {
        Intent intent = new Intent(MainActivity.this, NewAutoSMS.class);
        startActivity(intent);
    }

    public void sentMessages(View view) {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void editProfile(View view) {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void language(View view) {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        logoutMenu(MainActivity.this);
    }

    private void logoutMenu(MainActivity mainActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
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
    */

}