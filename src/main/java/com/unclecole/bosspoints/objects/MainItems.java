package com.unclecole.bosspoints.objects;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

public class MainItems {

    @Getter private ItemStack item;
    @Getter private int slot, page;
    @Getter private String type;

    public MainItems(ItemStack item, String type, int slot, int page) {
        this.item = item;
        this.type = type;
        this.slot = slot;
        this.page = page;
    }
}
