package com.igd.igame.data;

public enum Platform {
    CONSOLE("Console"),
    MOBILE_HORIZONTAL("Mobile Horizontal"),
    MOBILE_VERTICAL("Mobile Vertical"),
    PC("PC");

    private final String label;

    Platform(String label) {
        this.label = label;
    }

    public String getLabel(){
        return label;
    }
}
