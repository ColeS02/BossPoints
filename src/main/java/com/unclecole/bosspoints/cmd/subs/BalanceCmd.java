package com.unclecole.bosspoints.cmd.subs;

import com.unclecole.bosspoints.cmd.AbstractCommand;
import com.unclecole.bosspoints.database.PointsData;
import com.unclecole.bosspoints.utils.MojangAPI;
import com.unclecole.bosspoints.utils.PlaceHolder;
import com.unclecole.bosspoints.utils.TL;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;

public class BalanceCmd extends AbstractCommand {

    public BalanceCmd() { super("balance", false); }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        UUID player;

        if(args.length < 1) {
            TL.PLAYER_BALANCE.send(sender, new PlaceHolder("%points%", PointsData.pointsList.getOrDefault(((Player) sender).getUniqueId(),0)));
            return false;
        }

        if(Bukkit.getPlayer(String.valueOf(args[0])) == null) {
            if (MojangAPI.getUUID(args[0]) == null) {
                TL.INVALID_PLAYER.send(sender);
                return false;
            }
            player = UUID.fromString(MojangAPI.getUUID(args[0]));
        } else {
            player = Bukkit.getPlayer(args[0]).getUniqueId();
        }


        if(!PointsData.pointsList.containsKey(player)) {
            PointsData.load(player.toString());
        }

        TL.OTHER_PLAYER_BALANCE.send(sender,new PlaceHolder("%player%", args[0]), new PlaceHolder("%points%", PointsData.pointsList.getOrDefault(player,0)));

        return false;
    }

    @Override
    public String getDescription() {
        return "Get Balance";
    }

    @Override
    public String getPermission() {
        return "bosspoints.menu";
    }
}
