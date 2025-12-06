package com.igd.igame.component;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageButton;

import com.igd.igame.R;
import com.igd.igame.activity.FavoritesActivity;
import com.igd.igame.activity.GameListActivity;
import com.igd.igame.activity.SearchActivity;

public class BotBarController {

    public static void attach(Activity activity) {

        ImageButton btnBack    = activity.findViewById(R.id.btnBottomBack);
        ImageButton btnSearch  = activity.findViewById(R.id.btnBottomSearch);
        ImageButton btnLibrary = activity.findViewById(R.id.btnBottomLibrary);
        ImageButton btnHome    = activity.findViewById(R.id.btnBottomHome);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> activity.finish());
        }

        if (btnSearch != null) {
            btnSearch.setOnClickListener(v -> {
                if (!(activity instanceof SearchActivity)) {
                    Intent i = new Intent(activity, SearchActivity.class);
                    activity.startActivity(i);
                }
            });
        }

        if (btnLibrary != null) {
            btnLibrary.setOnClickListener(v -> {
                if (!(activity instanceof FavoritesActivity)) {
                    Intent i = new Intent(activity, FavoritesActivity.class);
                    activity.startActivity(i);
                }
            });
        }

        if (btnHome != null) {
            btnHome.setOnClickListener(v -> {
                if (!(activity instanceof GameListActivity)) {
                    Intent i = new Intent(activity, GameListActivity.class);
                    activity.startActivity(i);
                }
            });
        }
    }
}
