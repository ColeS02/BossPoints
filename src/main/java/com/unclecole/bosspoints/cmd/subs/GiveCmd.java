package com.unclecole.bosspoints.cmd.subs;

import com.unclecole.bosspoints.cmd.AbstractCommand;
import com.unclecole.bosspoints.database.PointsData;
import com.unclecole.bosspoints.utils.MojangAPI;
import com.unclecole.bosspoints.utils.PlaceHolder;
import com.unclecole.bosspoints.utils.TL;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GiveCmd extends AbstractCommand {

    public GiveCmd() { super("give", false, "add"); }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        UUID player;

        if(args.length < 2) {
            TL.INVALID_COMMAND_USAGE.send(sender, new PlaceHolder("<command>", "/gamepoints give <player> <amount>"));
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

        PointsData.pointsList.put(player,PointsData.pointsList.get(player) + Integer.parseInt(args[1]));
        PointsData.save(player.toString());
        if(Bukkit.getPlayer(player) != null) {
            TL.RECIEVED_POINTS.send(Bukkit.getPlayer(player), new PlaceHolder("%points%", PointsData.pointsList.get(player)));
        }

        TL.GIVEN_POINTS.send(sender, new PlaceHolder("%player%", args[0]), new PlaceHolder("%points%", args[1]));


        return false;
    }

    @Override
    public String getDescription() {
        return "Give Balance";
    }

    @Override
    public String getPermission() {
        return "bosspoints.admin.give";
    }
}
