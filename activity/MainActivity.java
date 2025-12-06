package com.igd.igame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.igd.igame.data.Platform;
import com.igd.igame.R;

import java.util.ArrayList;
import java.util.EnumSet;

import com.igd.igame.data.LocalFavorites;

public class MainActivity extends AppCompatActivity {

    private CheckBox chkConsole, chkMobileHorizontal, chkMobileVertical, chkPc;
    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initListeners();
        LocalFavorites.restoreIntoGameStore(getApplicationContext());
    }

    // View references
    private void initViews() {
        chkConsole = findViewById(R.id.chkConsole);
        chkMobileHorizontal = findViewById(R.id.chkMobileHorizontal);
        chkMobileVertical = findViewById(R.id.chkMobileVertical);
        chkPc = findViewById(R.id.chkPc);
        btnContinue = findViewById(R.id.btnContinue);
    }

    // Event wiring
    private void initListeners() {
        btnContinue.setOnClickListener(v -> {
            EnumSet<Platform> selectedPlatforms = collectSelectedPlatforms();
            ArrayList<String> platformNames = toNameList(selectedPlatforms);

            Intent intent = new Intent(MainActivity.this, GameListActivity.class);
            intent.putStringArrayListExtra("platforms", platformNames);
            startActivity(intent);
        });
    }

    // read UI → domain enum set
    private EnumSet<Platform> collectSelectedPlatforms() {
        EnumSet<Platform> result = EnumSet.noneOf(Platform.class);

        if (chkConsole.isChecked())          result.add(Platform.CONSOLE);
        if (chkMobileHorizontal.isChecked()) result.add(Platform.MOBILE_HORIZONTAL);
        if (chkMobileVertical.isChecked())   result.add(Platform.MOBILE_VERTICAL);
        if (chkPc.isChecked())               result.add(Platform.PC);

        return result;
    }

    // enum set → intent’te taşınacak string listesi
    private ArrayList<String> toNameList(EnumSet<Platform> platforms) {
        ArrayList<String> names = new ArrayList<>();
        for (Platform platform : platforms) {
            names.add(platform.name()); // GameListActivity'de valueOf ile okunacak
        }
        return names;
    }
}
