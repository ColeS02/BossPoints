package com.unclecole.bosspoints.cmd.subs;

import com.unclecole.bosspoints.cmd.AbstractCommand;
import com.unclecole.bosspoints.database.PointsData;
import com.unclecole.bosspoints.utils.MojangAPI;
import com.unclecole.bosspoints.utils.PlaceHolder;
import com.unclecole.bosspoints.utils.TL;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class SetCmd extends AbstractCommand {

    public SetCmd() { super("set", false); }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        UUID player;

        if(args.length < 2) {
            TL.INVALID_COMMAND_USAGE.send(sender, new PlaceHolder("<command>", "/gamepoints set <player> <amount>"));
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

        if(Integer.parseInt(args[1]) < 0) {
            TL.CANT_BE_LESS_THAN_ZERO.send(sender);
            return false;
        }
        PointsData.pointsList.put(player,Integer.parseInt(args[1]));
        PointsData.save(player.toString());

        TL.SET_POINTS.send(sender, new PlaceHolder("%player%", args[0]), new PlaceHolder("%points%", args[1]));


        return false;
    }

    @Override
    public String getDescription() {
        return "Give Balance";
    }

    @Override
    public String getPermission() {
        return "bosspoints.admin.set";
    }
}
