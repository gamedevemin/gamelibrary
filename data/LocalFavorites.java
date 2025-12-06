package com.igd.igame.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Sadece favori ID'lerini saklar. */
public class LocalFavorites {
    private static final String PREF_NAME = "favorites_prefs";
    private static final String KEY_IDS   = "favorite_ids";

    private LocalFavorites() { }

    // GameStore → SharedPreferences
    public static void persistFromGameStore(Context context) {
        GameStore store = GameStore.get();
        List<Game> favs = store.getFavoriteGames();

        Set<String> ids = new HashSet<>();
        for (Game g : favs) {
            ids.add(g.id);
        }

        SharedPreferences prefs =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putStringSet(KEY_IDS, ids)
                .apply();
    }

    // SharedPreferences → GameStore
    public static void restoreIntoGameStore(Context context) {
        Set<String> ids = loadIds(context);
        GameStore store = GameStore.get();
        List<Game> all = store.getAllGames();

        for (Game g : all) {
            g.isFavorite = ids.contains(g.id);
        }
    }

    // --- private ---

    private static Set<String> loadIds(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getStringSet(KEY_IDS, new HashSet<>());
    }
}
