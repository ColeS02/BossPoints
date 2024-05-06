package com.unclecole.bosspoints.objects;

import com.unclecole.bosspoints.BossPoints;
import com.unclecole.bosspoints.utils.C;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

public class Product {

    @Getter private String name;
    @Getter private List<String> description;
    @Getter private int price;
    @Getter private List<String> rewards;
    @Getter private int page;
    @Getter private int slot;
    @Getter private ItemStack item;
    private List<String> finalLore;


    public Product(String name, List<String> lore, List<String> description, int price, int page, int slot, List<String> rewards, Material material, int amount, Boolean enchanted) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.page = page;
        this.slot = slot;
        this.rewards = rewards;
        finalLore = new ArrayList<>();

        lore.forEach(list -> {
            if(list.contains("%product_description%")) {
                description.forEach(each -> {
                    finalLore.add(each);
                });
            }
            finalLore.add(list
                    .replaceAll("%product_description%", "")
                    .replaceAll("%product_price_final%", String.valueOf(price)));
        });

        ItemBuilder temp = new ItemBuilder(material,amount).setName(C.color(name)).addLore(C.color(finalLore)).addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_POTION_EFFECTS);
        if(enchanted) {
            temp.addEnchant(Enchantment.DIG_SPEED,1);
        }
        item = temp;
    }
}
