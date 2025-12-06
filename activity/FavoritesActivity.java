package com.mehmedemincelenk.gamelib.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mehmedemincelenk.gamelib.component.BotBarController;
import com.mehmedemincelenk.gamelib.data.Game;
import com.mehmedemincelenk.gamelib.adapter.GameAdapter;
import com.mehmedemincelenk.gamelib.data.GameStore;
import com.mehmedemincelenk.gamelib.R;
import com.mehmedemincelenk.gamelib.data.LocalFavorites;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerFavorites;
    private GameAdapter adapter;

    // FavoritesActivity.java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        TextView title = findViewById(R.id.txtPlatformTitle);
        title.setText("Oyunlarım");

        recyclerFavorites = findViewById(R.id.recyclerFavorites);
        recyclerFavorites.setLayoutManager(new LinearLayoutManager(this));

        loadFavorites();

        BotBarController.attach(this); // alt bar bağla
        // şimdilik disable yok
    }


    @Override
    protected void onResume(){
        super.onResume();
        loadFavorites();
    }



    private void loadFavorites() {
        List<Game> favs = GameStore.get().getFavoriteGames();

        adapter = new GameAdapter(favs, new GameAdapter.Listener() {
            @Override
            public void onGameClick(Game game) {
                openGameDetail(game);
            }

            @Override
            public void onFavoriteClick(Game game) {
                GameStore.get().toggleFavorite(game);
                LocalFavorites.persistFromGameStore(getApplicationContext());
                loadFavorites(); // tekrar yükle
            }

            @Override
            public void onGoClick(Game game) {
                Toast.makeText(FavoritesActivity.this,
                        "Oyuna git: " + game.title,
                        Toast.LENGTH_SHORT).show();
            }
        });

        recyclerFavorites.setAdapter(adapter);
    }

    private void openGameDetail(Game game) {
        Intent intent = new Intent(this, GameDetailActivity.class);
        intent.putExtra(GameDetailActivity.EXTRA_GAME_ID, game.id);
        startActivity(intent);
    }



}
