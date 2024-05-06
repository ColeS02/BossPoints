package com.unclecole.bosspoints.hooks;

import com.unclecole.bosspoints.BossPoints;
import com.unclecole.bosspoints.database.PointsData;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Locale;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    BossPoints plugin = BossPoints.instance;

    @Override
    @NotNull
    public String getAuthor() {
        return "Cole";
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "BossPoints";
    }

    @Override
    @NotNull
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String holder) {
        if (holder.equalsIgnoreCase("balance")) {
            return PointsData.pointsList.get(player.getUniqueId()) + "";
        }
        if (holder.equalsIgnoreCase("balance_formatted")) {
            int points = PointsData.pointsList.get(player.getUniqueId());

            Locale locale = new Locale("en", "US");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

            return currencyFormatter.format(points);
        }

        return "0";
    }
}
