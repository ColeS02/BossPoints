package com.unclecole.bosspoints.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.logging.Level;

public enum TL {
	NO_PERMISSION("messages.no-permission", "&c&lERROR! &fYou don't have the permission to do that."),
	INVALID_ARGUMENT_NUMBER("messages.invalid-number", "&c&lERROR: &c'<argument>' has to be a number"),
	INVALID_COMMAND_USAGE("messages.invalid-command-usage", "&cIncorrect Usage: &7<command>"),
	PLAYER_ONLY("messages.player-only", "&cThis command is for players only!"),
	INVALID_PLAYER("messages.invalid-player", "&c&lERROR! &fYou entered a invalid player!"),
	PLAYER_BALANCE("messages.player-balance", "&e&lBALANCE: &fYour balance is &a%points%"),
	OTHER_PLAYER_BALANCE("messages.other-player-balance", "&a%player% &fhas %points% points!"),
	RECIEVED_POINTS("messages.recieved-points", "&fYou have been given &a%points% &fpoints!"),
	GIVEN_POINTS("messages.given-points", "&e&lSUCCESS! &fYou successfully gave &e%player% &a%points% &fpoints!"),
	TOOK_POINTS("messages.given-points", "&e&lSUCCESS! &fYou successfully took &e%player% &a%points% &fpoints!"),
	SET_POINTS("messages.set-points", "&e&lSUCCESS! &fYou successfully set &e%player%'s points too &a%points% &fpoints!"),
	CANT_TAKE_MONEY("messages.cant-take-money", "&c&lERROR: &fPlayer doesn't have enough money!"),
	INSUFFICIENT_FUNDS("messages.insufficient-funds", "&c&lERROR: &fInsufficient funds!"),
	SUCCESSFULLY_PURCHASED("messages.successfully-purchased", "&e&lSUCCESS! &fSuccessfully purchased %name%"),
	CANT_BE_LESS_THAN_ZERO("messages.cant-be-less-than-zero","&c&lERROR! &fYou entered a number less than zero!" );

	private String path, def;
	private static ConfigFile config;

	TL(String path, String start) {
		this.path = path;
		this.def = start;
	}

	public String getDefault() {
		return this.def;
	}

	public String getPath() {
		return this.path;
	}

	public void setDefault(String message) {
		this.def = message;
	}

	public static void loadMessages(ConfigFile configFile) {
		config = configFile;
		FileConfiguration data = configFile.getConfig();
		for (TL message : values()) {
			if (!data.contains(message.getPath())) {
				data.set(message.getPath(), message.getDefault());
			}
		}
		configFile.save();
	}

	public void send(CommandSender sender) {
		if (sender instanceof org.bukkit.entity.Player) {
			sender.sendMessage(C.color(getDefault()));
		} else {
			sender.sendMessage(C.strip(getDefault()));
		}
	}

	public void send(CommandSender sender, PlaceHolder... placeHolders) {
		if (sender instanceof org.bukkit.entity.Player) {
			sender.sendMessage(C.color(getDefault(), placeHolders));
		} else {
			sender.sendMessage(C.strip(getDefault(), placeHolders));
		}
	}

	public static void message(CommandSender sender, String message) {
		sender.sendMessage(C.color(message));
	}

	public static void message(CommandSender sender, String message, PlaceHolder... placeHolders) {
		sender.sendMessage(C.color(message, placeHolders));
	}

	public static void message(CommandSender sender, List<String> message) {
		message.forEach(m -> sender.sendMessage(C.color(m)));
	}

	public static void message(CommandSender sender, List<String> message, PlaceHolder... placeHolders) {
		message.forEach(m -> sender.sendMessage(C.color(m, placeHolders)));
	}

	public static void log(Level lvl, String message) {
		Bukkit.getLogger().log(lvl, message);
	}
}
