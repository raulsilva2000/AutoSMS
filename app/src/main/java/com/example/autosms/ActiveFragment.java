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

import java.util.ArrayList;

public class ActiveFragment extends Fragment {
    private RecyclerView.Adapter autoSMSAdapter;
    private RecyclerView recyclerViewReplys;
    DrawerLayout drawerLayout;
    private ImageView menuIcon;
    private ImageView closeMenu;
    private ConstraintLayout homeMenu;
    private ConstraintLayout newAutoSMS;
    private ConstraintLayout messagesMenu;
    private ConstraintLayout editProfileMenu;
    private ConstraintLayout languageMenu;
    private ConstraintLayout logoutMenu;


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
        drawerLayout = view.findViewById(R.id.drawer_layer);
        initRecyclerview(view);

        menuIcon = view.findViewById(R.id.openDrawerMenu);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ClickDrawerMenu(view);
            }
        });

        closeMenu = view.findViewById(R.id.imageView21);
        closeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                closeDrawer(view);
            }
        });

        homeMenu = view.findViewById(R.id.homeMenu);
        homeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dashboard(view);
            }
        });

        newAutoSMS = view.findViewById(R.id.newautosmsMenu);
        newAutoSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                newAutoSMS(view);
            }
        });

        messagesMenu = view.findViewById(R.id.messagesMenu);
        messagesMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                sentMessages(view);
            }
        });

        editProfileMenu = view.findViewById(R.id.editprofileMenu);
        editProfileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                editProfile(view);
            }
        });

        languageMenu = view.findViewById(R.id.languageMenu);
        languageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                language(view);
            }
        });

        logoutMenu = view.findViewById(R.id.logoutMenu);
        logoutMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                logout(view);
            }
        });
    }

    public void ClickDrawerMenu(View view) {
        openDrawer(drawerLayout);
    }

    private void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeDrawer(View view){
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void dashboard(View view) {
        //mudar para fragmento
        /*
        Intent intent = new Intent((MainActivity) getActivity(), MainActivity.class);
        startActivity(intent);*/

    }

    public void newAutoSMS(View view) {
        /*
        Intent intent = new Intent((MainActivity) getActivity(), NewAutoSMS.class);
        startActivity(intent);*/
    }

    public void sentMessages(View view) {
        /*
        Intent intent = new Intent((MainActivity) getActivity(), SentMessages.class);
        startActivity(intent);*/
    }

    public void editProfile(View view) {
        /*
        Intent intent = new Intent((MainActivity) getActivity(), MainActivity.class);
        startActivity(intent);*/
    }

    public void language(View view) {
        /*
        Intent intent = new Intent((MainActivity) getActivity(), MainActivity.class);
        startActivity(intent);*/
    }

    public void logout(View view) {
        logoutMenu((MainActivity) getActivity());
    }

    private void logoutMenu(MainActivity mainActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mainActivity.finish();
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