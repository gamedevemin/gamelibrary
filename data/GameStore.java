package com.igd.igame.data;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Arrays;


public class GameStore {
    private static final GameStore INSTANCE = new GameStore();

    private final List<Game> games = new ArrayList<>();

    private GameStore(){
        seedFakeData();
    }

    public static GameStore get(){
        return INSTANCE;
    }

    public List<Game> getAllGames(){
        return new ArrayList<>(games);
    }

    public List<Game> getFavoriteGames(){
        List<Game> favs = new ArrayList<>();
        for(Game g : games){
            if(g.isFavorite) favs.add(g);
        }
        return favs;
    }

    public void toggleFavorite(Game game){
        game.isFavorite = !game.isFavorite;
    }

    private void seedFakeData(){

        // ORI
        List<StoreLink> oriLinks = new ArrayList<>();
        oriLinks.add(new StoreLink(Store.STEAM, "https://store.steampowered.com/app/ori-demo"));
        List<String> oriTags = Arrays.asList(
                "Metroidvania",
                "Platformer",
                "Story-rich"
        );

        games.add(new Game(
                "ori",
                "ORI",
                "$45",
                "Hızlı ve akıcı bir platform macerası.",
                EnumSet.of(Platform.PC, Platform.CONSOLE),
                oriLinks,
                oriTags
        ));

        // EOU
        List<StoreLink> eouLinks = new ArrayList<>();
        eouLinks.add(new StoreLink(Store.STEAM, "https://store.steampowered.com/app/eou-demo"));
        eouLinks.add(new StoreLink(Store.EPIC,  "https://epicgames.com/app/eou-demo"));
        List<String> eouTags = Arrays.asList(
                "Tactical",
                "Historical",
                "Strategy"
        );

        games.add(new Game(
                "eou",
                "EOU",
                "$56",
                "Tarih esintili, taktiksel bir serüven.",
                EnumSet.of(Platform.PC),
                eouLinks,
                eouTags
        ));
    }

    public Game findByIdOrNull(String id){
        if(id == null) return null;

        for(Game g : games){
            if(id.equals(g.id)){
                return g;
            }
        }
        return null;
    }
}
