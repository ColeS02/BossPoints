package com.unclecole.bosspoints.cmd.subs;

import com.unclecole.bosspoints.BossPoints;
import com.unclecole.bosspoints.cmd.AbstractCommand;
import com.unclecole.bosspoints.database.PointsData;
import com.unclecole.bosspoints.utils.C;
import com.unclecole.bosspoints.utils.PlaceHolder;
import com.unclecole.bosspoints.utils.TL;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;
import redempt.redlib.itemutils.ItemBuilder;

import java.awt.*;
import java.util.Arrays;

public class GamePointsCmd extends AbstractCommand {

    public GamePointsCmd() {
        super("gamepoints", false, "store","gp");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;
        openMainMenu(player);
        return false;
    }

    public void openMainMenu(Player player) {
        InventoryGUI gui = new InventoryGUI(BossPoints.instance.getMain_size(),C.color(BossPoints.instance.getMain_title()));

        ItemBuilder placeholder = new ItemBuilder(Material.STAINED_GLASS_PANE, 1).setDurability(7).setName(C.color("&7"));

        gui.fill(0,BossPoints.instance.getMain_size(),placeholder);
        for(int i = 0; i < BossPoints.instance.getMainMenuItems().size(); i++) {
            int finalI = i;
            gui.addButton(BossPoints.instance.getMainMenuItems().get(i).getSlot(), new ItemButton(BossPoints.instance.getMainMenuItems().get(finalI).getItem()) {
                @Override
                public void onClick(InventoryClickEvent inventoryClickEvent) {
                    nextStore(player, BossPoints.instance.getMainMenuItems().get(finalI).getType());
                }
            });
        }

        gui.open(player);
    }

    public void nextStore(Player player, String store) {
        if(BossPoints.instance.getProductList().containsKey(store)) {
            ItemBuilder placeholder = new ItemBuilder(Material.STAINED_GLASS_PANE, 1).setDurability(7).setName(C.color("&7")).addItemFlags(ItemFlag.HIDE_ATTRIBUTES);;
            ItemBuilder back = new ItemBuilder(Material.BARRIER, 1).setName(C.color("&c&lRETURN")).addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            InventoryGUI gui = new InventoryGUI(BossPoints.instance.getMenus().get(store).getSize(),C.color(BossPoints.instance.getMenus().get(store).getTitle()));
            gui.fill(0,BossPoints.instance.getMenus().get(store).getSize(), placeholder);
            gui.addButton(BossPoints.instance.getMenus().get(store).getSize()-1, new ItemButton(back) {
                @Override
                public void onClick(InventoryClickEvent inventoryClickEvent) {
                    openMainMenu(player);
                }
            });
            BossPoints.instance.getProductList().get(store).forEach(product -> {
                gui.addButton(product.getSlot(), new ItemButton(product.getItem()) {
                    @Override
                    public void onClick(InventoryClickEvent inventoryClickEvent) {
                        if(product.getPrice() > PointsData.pointsList.getOrDefault(player.getUniqueId(),0)) {
                            TL.INSUFFICIENT_FUNDS.send(player);
                            return;
                        }
                        product.getRewards().forEach(commands -> {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands.replaceAll("%player%", player.getName()));
                        });
                        PointsData.pointsList.put(player.getUniqueId(), PointsData.pointsList.get(player.getUniqueId()) - product.getPrice());
                        PointsData.save(player.getUniqueId().toString());
                        TL.SUCCESSFULLY_PURCHASED.send(player, new PlaceHolder("%name%", product.getName()));
                    }
                });
            });
            gui.open(player);
        }
    }

    @Override
    public String getDescription() {
        return "Game Points Menu";
    }

    @Override
    public String getPermission() {
        return "bosspoints.menu";
    }
}
