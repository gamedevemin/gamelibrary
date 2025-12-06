package com.igd.igame.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.igd.igame.R;
import com.igd.igame.data.Game;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    public interface Listener{
        void onGameClick(Game game);
        void onFavoriteClick(Game game);
        void onGoClick(Game game);
    }

    private final List<Game> games;
    private final Listener listener;

    public GameAdapter(List<Game> games, Listener listener) {
        this.games = games;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game_card, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = games.get(position);
        holder.txtTitle.setText(game.title);
        holder.txtPrice.setText(game.price);
        holder.imgIcon.setImageResource(R.mipmap.ic_launcher);

        // Favori ikon
        holder.btnAdd.setImageResource(
                game.isFavorite
                        ? android.R.drawable.btn_star_big_on
                        : android.R.drawable.btn_star_big_off
        );

        // Tagler
        if (holder.tagContainer != null) {
            holder.tagContainer.removeAllViews();
            if (game.tags != null) {
                for (String tag : game.tags) {
                    addTagChip(holder.tagContainer, tag);
                }
            }
        }

        // Kart tıklaması -> detay
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onGameClick(game);
        });

        // + butonu
        holder.btnAdd.setOnClickListener(v -> {
            if (listener != null) listener.onFavoriteClick(game);
        });

        // OK -> oyuna git
        holder.btnGo.setOnClickListener(v -> {
            if (listener != null) listener.onGoClick(game);
        });
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    private void addTagChip(LinearLayout parent, String text){
        if (text == null || text.trim().isEmpty()) return;

        TextView tv = new TextView(parent.getContext());
        tv.setText(text);
        tv.setTextSize(12);
        tv.setTextColor(Color.parseColor("#DDDDDD"));
        tv.setBackgroundColor(Color.parseColor("#333333"));

        int padH = dp(parent, 8);
        int padV = dp(parent, 4);
        tv.setPadding(padH, padV, padH, padV);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lp.setMarginEnd(dp(parent, 6));
        tv.setLayoutParams(lp);

        parent.addView(tv);
    }

    private int dp(View view, int value){
        float d = view.getResources().getDisplayMetrics().density;
        return (int) (value * d);
    }

    static class GameViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView txtTitle, txtPrice;
        ImageButton btnAdd, btnGo;
        LinearLayout tagContainer;

        GameViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon       = itemView.findViewById(R.id.imgGameIcon);
            txtTitle      = itemView.findViewById(R.id.txtGameTitle);
            txtPrice      = itemView.findViewById(R.id.txtPrice);
            btnAdd        = itemView.findViewById(R.id.btnAddToLibrary);
            btnGo         = itemView.findViewById(R.id.btnGoToGame);
            tagContainer  = itemView.findViewById(R.id.tagContainer);
        }
    }
}
