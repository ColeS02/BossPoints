package com.unclecole.bosspoints.objects;

import lombok.Getter;

public class Menus {

    @Getter private String title;
    @Getter private int size;

    public Menus(String title, int size) {
        this.title = title;
        this.size = size;
    }
}
