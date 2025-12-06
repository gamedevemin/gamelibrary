package com.igd.igame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.igd.igame.component.BotBarController;
import com.igd.igame.data.Game;
import com.igd.igame.adapter.GameAdapter;
import com.igd.igame.data.GameStore;
import com.igd.igame.R;
import com.igd.igame.data.LocalFavorites;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText inputSearch;
    private RecyclerView recyclerSearch;

    private GameAdapter adapter;
    private List<Game> allGames;
    private final List<Game> filteredGames = new ArrayList<>();

    // SearchActivity.java → onCreate içinde SON HAL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        initList();
        initSearchInput();

        BotBarController.attach(this); // sadece bu yeter
    }


    // --- UI refs ---

    private void initViews() {
        TextView title = findViewById(R.id.txtPlatformTitle);
        ImageButton platformBtn = findViewById(R.id.btnPlatform);

        if (title != null) title.setText("Aradığım");
        if (platformBtn != null) platformBtn.setVisibility(android.view.View.GONE);

        inputSearch = findViewById(R.id.inputSearch);
        recyclerSearch = findViewById(R.id.recyclerSearch);
        recyclerSearch.setLayoutManager(new LinearLayoutManager(this));
    }


    // --- Liste & adapter ---

    private void initList() {
        allGames = GameStore.get().getAllGames();
        filteredGames.clear();
        filteredGames.addAll(allGames);

        adapter = new GameAdapter(filteredGames, new GameAdapter.Listener() {
            @Override
            public void onGameClick(Game game) {
                openGameDetail(game);
            }

            @Override
            public void onFavoriteClick(Game game) {
                GameStore.get().toggleFavorite(game);
                LocalFavorites.persistFromGameStore(getApplicationContext());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onGoClick(Game game) {
                Toast.makeText(SearchActivity.this,
                        "Oyuna git: " + game.title,
                        Toast.LENGTH_SHORT).show();
            }
        });

        recyclerSearch.setAdapter(adapter);
    }

    // --- Arama inputu ---

    private void initSearchInput() {
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterGames(s.toString());
            }
        });
    }

    private void filterGames(String query) {
        filteredGames.clear();

        if (query == null || query.trim().isEmpty()) {
            filteredGames.addAll(allGames);
        } else {
            String q = query.toLowerCase();
            for (Game g : allGames) {
                if (g.title.toLowerCase().contains(q)) {
                    filteredGames.add(g);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void openGameDetail(Game game) {
        Intent intent = new Intent(this, GameDetailActivity.class);
        intent.putExtra(GameDetailActivity.EXTRA_GAME_ID, game.id);
        startActivity(intent);
    }


}
