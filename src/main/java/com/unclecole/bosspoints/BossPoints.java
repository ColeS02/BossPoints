package com.unclecole.bosspoints;

import com.unclecole.bosspoints.cmd.BaseCommand;
import com.unclecole.bosspoints.database.PointsData;
import com.unclecole.bosspoints.database.serializer.Persist;
import com.unclecole.bosspoints.hooks.PlaceholderAPIHook;
import com.unclecole.bosspoints.listeners.JoinListener;
import com.unclecole.bosspoints.objects.MainItems;
import com.unclecole.bosspoints.objects.Menus;
import com.unclecole.bosspoints.objects.Product;
import com.unclecole.bosspoints.utils.C;
import com.unclecole.bosspoints.utils.ConfigFile;
import com.unclecole.bosspoints.utils.TL;
import lombok.Getter;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.material.Dye;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public final class BossPoints extends JavaPlugin {

    private List<String> productDescription;
    @Getter private String alreadyHave,coolDown,available,main_title;
    @Getter private int main_size;
    @Getter private ArrayList<MainItems> mainMenuItems;
    @Getter private HashMap<String, ArrayList<Product>> productList;
    @Getter private HashMap<String, Menus> menus;
    @Getter private static Persist persist;
    private CoreProtectAPI api;

    @Getter public static BossPoints instance;

    private PlaceholderAPIHook placeholderAPIHook;


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        persist = new Persist();
        productDescription = new ArrayList<>();
        mainMenuItems = new ArrayList<>();
        productList = new HashMap<>();
        menus = new HashMap<>();
        Objects.requireNonNull(getCommand("gamepoints")).setExecutor(new BaseCommand());
        saveDefaultConfig();
        TL.loadMessages(new ConfigFile("messages.yml", this));
        (this.placeholderAPIHook = new PlaceholderAPIHook()).register();
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        loadMainStore();
        loadProducts();
        loadConfig();

        api = getCoreProtect();
        if (api != null){ // Ensure we have access to the API
            api.testAPI(); // Will print out "[CoreProtect] API test successful." in the console.
        }

        if (Bukkit.getOnlinePlayers().size() != 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PointsData.load(player.getUniqueId().toString());
            }
        }
    }

    @Override
    public void onDisable() {
        if (this.placeholderAPIHook != null) {
            this.placeholderAPIHook.unregister();
            this.placeholderAPIHook = null;
        }
    }

    public void loadConfig() {
        productDescription = getConfig().getStringList("Store.Description");
        alreadyHave = getConfig().getString("Store.Product.Purchase.Format.Already_Have");
        coolDown = getConfig().getString("Store.Product.Purchase.Format.Cooldown");
        available = getConfig().getString("Store.Product.Purchase.Format.Available");
        main_title = getConfig().getString("Stores.Title");
        main_size = getConfig().getInt("Stores.Size");

    }

    public void loadMainStore() {
        for(String key : getConfig().getConfigurationSection("Stores").getKeys(false)) {
            if(key.equals("Title") || key.equals("Size")) continue;
            if(Material.getMaterial(getConfig().getString("Stores." + key + ".Item.Material")) == null) {
                getLogger().warning("INVALID MATERIAL (Main): " + getConfig().getString("Stores." + key + ".Item.Material"));
                continue;
            }
            ItemBuilder item = new ItemBuilder(Material.getMaterial(getConfig().getString("Stores." + key + ".Item.Material")),getConfig().getInt("Stores." + key + ".Amount"))
                    .addLore(C.color(getConfig().getStringList("Stores." + key + ".Item.Lore")))
                    .setName(C.color(getConfig().getString("Stores." + key + ".Item.Name"))).addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_POTION_EFFECTS);

            mainMenuItems.add(new MainItems(item,key,getConfig().getInt("Stores." + key + ".Slot"),getConfig().getInt("Stores." + key + ".Pages")));
        }
    }

    public void loadProducts() {
        for(String key : getConfig().getConfigurationSection("Products").getKeys(false)) {
            productList.put(key, new ArrayList<>());
            menus.put(key, new Menus(getConfig().getString("Products." + key + ".Title"),getConfig().getInt("Products." + key + ".Size")));

            for(String product : getConfig().getConfigurationSection("Products." + key).getKeys(false)) {
                if(product.equals("Title") || product.equals("Size")) continue;
                if(Material.getMaterial(getConfig().getString("Products." + key + "." + product + ".Preview.Material")) == null) {
                    getLogger().warning("INVALID MATERIAL (" + key + "): " + getConfig().getString("Products." + key + "." + product + ".Preview.Material"));
                    continue;
                }
                productList.get(key).add( new Product(getConfig().getString("Products." + key + "." + product + ".Name"),
                        getConfig().getStringList("Store.Description"),
                        getConfig().getStringList("Products." + key + "." + product + ".Description"),
                        getConfig().getInt("Products." + key + "." + product + ".Price"),
                        getConfig().getInt("Products." + key + "." + product + ".Store.Page"),
                        getConfig().getInt("Products." + key + "." + product + ".Store.Slot"),
                        getConfig().getStringList("Products." + key + "." + product + ".Rewards.Commands"),
                        Material.getMaterial(getConfig().getString("Products." + key + "." + product + ".Preview.Material")),
                        getConfig().getInt("Products." + key + "." + product + ".Preview.Amount"),
                        getConfig().getBoolean("Products." + key + "." + product + ".Preview.Enchanted")));
            }
        }
    }

    private CoreProtectAPI getCoreProtect() {
        Plugin plugin = getServer().getPluginManager().getPlugin("CoreProtect");

        // Check that CoreProtect is loaded
        if (plugin == null || !(plugin instanceof CoreProtect)) {
            return null;
        }

        // Check that the API is enabled
        CoreProtectAPI CoreProtect = ((CoreProtect) plugin).getAPI();
        if (CoreProtect.isEnabled() == false) {
            return null;
        }

        // Check that a compatible version of the API is loaded
        if (CoreProtect.APIVersion() < 9) {
            return null;
        }

        return CoreProtect;
    }
}
