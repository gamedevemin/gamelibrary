package com.igd.igame.data;

import java.util.EnumSet;
import java.util.List;

public class Game {
    public final String id, title, price, shortDescription;

    public final EnumSet<Platform> platforms;
    public final List<StoreLink> storeLinks;
    public boolean isFavorite;
    public final List<String> tags;


    public Game(String id,
                String title,
                String price,
                String shortDescription,
                EnumSet<Platform> platforms,
                List<StoreLink> storeLinks,
                List<String> tags) {

        this.id = id;
        this.title = title;
        this.price = price;
        this.shortDescription = shortDescription;
        this.platforms = platforms;
        this.storeLinks = storeLinks;
        this.tags = tags;
        this.isFavorite = false;
    }

    public boolean supportsAny(EnumSet<Platform> selected){
        if(selected == null || selected.isEmpty()) return true;

        for (Platform p : selected){
            if(platforms.contains(p)) return true;
        }
        return false;
    }

    public String getPrimaryUrlOrNull(){
        if (storeLinks == null || storeLinks.isEmpty()) return null;
        return storeLinks.get(0).url;
    }
}
