package com.igd.igame.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.view.ViewGroup;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

import com.igd.igame.R;
import com.igd.igame.data.Game;
import com.igd.igame.data.GameStore;
import com.igd.igame.data.LocalFavorites;

public class GameDetailActivity extends AppCompatActivity {

    public static final String EXTRA_GAME_ID = "extra_game_id";
    private Game game; // alan

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        // Intent: oyun id'si
        String gameId = getIntent().getStringExtra(EXTRA_GAME_ID);
        game = GameStore.get().findByIdOrNull(gameId);
        if (game == null) {
            Toast.makeText(this, "Oyun bulunamadı.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Üst bar
        TextView topTitle = findViewById(R.id.txtPlatformTitle);
        ImageButton platformBtn = findViewById(R.id.btnPlatform);
        if (platformBtn != null) platformBtn.setVisibility(View.GONE);
        if (topTitle != null) topTitle.setText(game.title);

        // İç kart
        LinearLayout detailTagContainer = findViewById(R.id.detailTagContainer);
        TextView txtTitle   = findViewById(R.id.txtDetailTitle);
        TextView txtPrice   = findViewById(R.id.txtDetailPrice);
        TextView txtDesc    = findViewById(R.id.txtDetailDescription);
        ImageView imgHero   = findViewById(R.id.imgHero);
        ImageView imgLogo   = findViewById(R.id.imgLogo);
        ImageButton btnAdd  = findViewById(R.id.btnDetailAdd);
        ImageButton btnGo   = findViewById(R.id.btnDetailGo);

        if (txtTitle != null) txtTitle.setText(game.title);
        if (txtPrice != null) txtPrice.setText(game.price);
        if (txtDesc  != null) txtDesc.setText(game.shortDescription);

        // Şimdilik placeholder görseller
        if (imgHero != null) imgHero.setImageResource(R.mipmap.ic_launcher);
        if (imgLogo != null) imgLogo.setImageResource(R.mipmap.ic_launcher);

        // Favori butonu
        if (btnAdd != null) {
            updateDetailFavoriteIcon(btnAdd);

            btnAdd.setOnClickListener(v -> {
                GameStore.get().toggleFavorite(game);
                LocalFavorites.persistFromGameStore(getApplicationContext());
                updateDetailFavoriteIcon(btnAdd);

                Toast.makeText(this,
                        game.title + (game.isFavorite
                                ? " kütüphaneye eklendi"
                                : " kaldırıldı"),
                        Toast.LENGTH_SHORT
                ).show();
            });
        }

        // Tagler
        if (detailTagContainer != null) {
            detailTagContainer.removeAllViews();

            if (game.tags != null) {
                for (String tag : game.tags) {
                    addTag(detailTagContainer, tag);
                }
            }
        }

        // Oyuna git
        if (btnGo != null) {
            btnGo.setOnClickListener(v -> {
                String url = game.getPrimaryUrlOrNull();
                if (url == null) {
                    Toast.makeText(this,
                            "Bu oyun için link eklenmemiş.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
            });
        }
    }

    private void updateDetailFavoriteIcon(ImageButton btnAdd){
        btnAdd.setImageResource(
                game.isFavorite
                        ? android.R.drawable.btn_star_big_on
                        : android.R.drawable.btn_star_big_off
        );
    }

    private void addTag(LinearLayout parent, String text){
        if (parent == null) return;
        if (text == null || text.trim().isEmpty()) return;

        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(12);
        tv.setTextColor(Color.parseColor("#DDDDDD"));
        tv.setBackgroundColor(Color.parseColor("#333333"));

        int padH = dp(8);
        int padV = dp(4);
        tv.setPadding(padH, padV, padH, padV);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lp.setMarginEnd(dp(6));
        tv.setLayoutParams(lp);

        parent.addView(tv);
    }

    private int dp(int value){
        float d = getResources().getDisplayMetrics().density;
        return (int) (value * d);
    }
}
