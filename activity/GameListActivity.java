package com.igd.igame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.igd.igame.R;
import com.igd.igame.adapter.GameAdapter;
import com.igd.igame.component.BotBarController;
import com.igd.igame.data.LocalFavorites;
import com.igd.igame.data.Game;
import com.igd.igame.data.GameStore;
import com.igd.igame.data.Platform;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class GameListActivity extends AppCompatActivity {

    // GameListActivity.java → alanlar
    private RecyclerView recyclerGames;
    private TextView titleText;
    private ImageButton platformButton;

    // DATA
    private List<Game> allGames;
    private final List<Game> visibleGames = new ArrayList<>();
    private GameAdapter adapter;


    // State: which platforms are selected for this screen
    private final EnumSet<Platform> selectedPlatforms =
            EnumSet.noneOf(Platform.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        initViews();
        BotBarController.attach(this); // bottom bar ortak
        initPlatformStateFromIntent();

        initGameList();   // data + adapter
        applyPlatformFilter(); // intent’ten gelen seçime göre filtrele
        renderTitle();
        initListeners();
    }

    // ---- init ----

    private void initViews() {
        titleText = findViewById(R.id.txtPlatformTitle);
        platformButton = findViewById(R.id.btnPlatform);

        recyclerGames = findViewById(R.id.recyclerGames);
        recyclerGames.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initPlatformStateFromIntent() {
        ArrayList<String> platformNames =
                getIntent().getStringArrayListExtra("platforms");

        if (platformNames == null) return;

        for (String name : platformNames) {
            try {
                selectedPlatforms.add(Platform.valueOf(name));
            } catch (IllegalArgumentException ignored) {
                // unknown name → ignore
            }
        }
    }


    private void initListeners() {
        platformButton.setOnClickListener(v -> openPlatformDialog());
    }

    // ---- UI render ----

    private void renderTitle() {
        if (selectedPlatforms.isEmpty()) {
            titleText.setText("Oyunlar - Tüm Platformlar");
            return;
        }

        List<String> labels = new ArrayList<>();
        for (Platform platform : selectedPlatforms) {
            labels.add(platform.getLabel());
        }

        String joined = TextUtils.join(", ", labels);
        titleText.setText("Oyunlar - " + joined);
    }

    // ---- Dialog ----

    private void openPlatformDialog() {
        Platform[] all = Platform.values();
        String[] labels = buildLabels(all);
        boolean[] checked = buildCheckedArray(all);

        new AlertDialog.Builder(this)
                .setTitle("Select platforms")
                .setMultiChoiceItems(labels, checked,
                        (dialog, which, isChecked) -> checked[which] = isChecked)
                .setPositiveButton("Apply", (dialog, which) -> {
                    applyCheckedToSelection(all, checked);
                    renderTitle();
                    applyPlatformFilter();
                })

                .setNegativeButton("Cancel", null)
                .show();
    }

    private String[] buildLabels(Platform[] all) {
        String[] labels = new String[all.length];
        for (int i = 0; i < all.length; i++) {
            labels[i] = all[i].getLabel();
        }
        return labels;
    }

    private boolean[] buildCheckedArray(Platform[] all) {
        boolean[] checked = new boolean[all.length];
        for (int i = 0; i < all.length; i++) {
            checked[i] = selectedPlatforms.contains(all[i]);
        }
        return checked;
    }

    private void applyCheckedToSelection(Platform[] all, boolean[] checked) {
        selectedPlatforms.clear();
        for (int i = 0; i < all.length; i++) {
            if (checked[i]) {
                selectedPlatforms.add(all[i]);
            }
        }
    }

    // ---- FAKE DATA ----
    // FAKE DATA + adapter bağlama
    private void initGameList() {
        GameStore store = GameStore.get();
        allGames = store.getAllGames();

        visibleGames.clear();
        visibleGames.addAll(allGames);

        adapter = new GameAdapter(visibleGames, new GameAdapter.Listener() {
            @Override
            public void onGameClick(Game game) {
                openGameDetail(game);
            }

            @Override
            public void onFavoriteClick(Game game) {
                GameStore.get().toggleFavorite(game);
                LocalFavorites.persistFromGameStore(getApplicationContext());

                Toast.makeText(GameListActivity.this,
                        game.title + (game.isFavorite ? " kütüphaneye eklendi" : " kaldırıldı"),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onGoClick(Game game) {
                Toast.makeText(GameListActivity.this,
                        "Oyuna git: " + game.title,
                        Toast.LENGTH_SHORT).show();
            }
        });

        recyclerGames.setAdapter(adapter);
    }


    private void applyPlatformFilter() {
        if (allGames == null) return;
        if (adapter == null) return;

        visibleGames.clear();
        for (Game g : allGames) {
            if (g.supportsAny(selectedPlatforms)) {
                visibleGames.add(g);
            }
        }
        adapter.notifyDataSetChanged();
    }


    private void openGameDetail(Game game){
        Intent intent = new Intent(this, GameDetailActivity.class);
        intent.putExtra(GameDetailActivity.EXTRA_GAME_ID, game.id);
        startActivity(intent);
    }
}
