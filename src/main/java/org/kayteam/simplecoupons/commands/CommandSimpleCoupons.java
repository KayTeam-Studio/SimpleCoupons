/*
 *   Copyright (C) 2021 segu23
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.kayteam.simplecoupons.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.commands.subcommands.*;
import org.kayteam.simplecoupons.coupon.Coupon;
import org.kayteam.simplecoupons.util.CommandManager;
import org.kayteam.storageapi.storage.YML;
import org.kayteam.storageapi.storage.Yaml;

import java.util.ArrayList;
import java.util.List;

public class CommandSimpleCoupons implements CommandExecutor, TabCompleter {
    private final SimpleCoupons plugin;

    public CommandSimpleCoupons(SimpleCoupons plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        CommandManager commandManager = this.plugin.getCommandManager();
        YML messages = this.plugin.getMessagesYaml();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                switch (args[0].toLowerCase()) {
                    case "get":
                        if (commandManager.playerHasPerm(player, "simplecoupons.get"))
                            if (args.length > 1) {
                                Coupon coupon = this.plugin.getCouponManager().getCoupons().get(args[1]);
                                if (coupon != null) {
                                    try {
                                        (new CommandGet(this.plugin)).getCoupon(player, args[1], Integer.parseInt(args[2]));
                                    } catch (Exception e) {
                                        (new CommandGet(this.plugin)).getCoupon(player, args[1], 1);
                                    }
                                } else {
                                    Yaml.sendSimpleMessage((CommandSender) player, messages.get("coupon.invalid"));
                                }
                            } else {
                                commandManager.insufficientArgs(player, "sc get <coupon-name>");
                            }
                        return false;
                    case "give":
                        if (commandManager.playerHasPerm(player, "simplecoupons.give"))
                            if (args.length > 2) {
                                Coupon coupon = this.plugin.getCouponManager().getCoupons().get(args[1]);
                                if (coupon != null) {
                                    try {
                                        (new CommandGive(this.plugin)).giveCoupon(player, args[2], args[1], Integer.parseInt(args[3]));
                                    } catch (Exception e) {
                                        (new CommandGive(this.plugin)).giveCoupon(player, args[2], args[1], 1);
                                    }
                                } else {
                                    Yaml.sendSimpleMessage((CommandSender) player, messages.get("coupon.invalid"));
                                }
                            } else {
                                commandManager.insufficientArgs(player, "sc give <coupon-name> <player>");
                            }
                        return false;
                    case "create":
                        if (commandManager.playerHasPerm(player, "simplecoupons.create"))
                            if (args.length > 1) {
                                (new CommandCreate(this.plugin)).createCoupon(player, args[1]);
                            } else {
                                commandManager.insufficientArgs(player, "sc create <coupon-name>");
                            }
                        return false;
                    case "delete":
                        if (commandManager.playerHasPerm(player, "simplecoupons.delete"))
                            if (args.length > 1) {
                                Coupon coupon = this.plugin.getCouponManager().getCoupons().get(args[1]);
                                if (coupon != null) {
                                    player.closeInventory();
                                    (new CommandDelete(this.plugin)).openDeleteMenu(player, coupon);
                                } else {
                                    Yaml.sendSimpleMessage((CommandSender) player, messages.get("coupon.invalid"));
                                }
                            } else {
                                commandManager.insufficientArgs(player, "sc delete <coupon-name>");
                            }
                        return false;
                    case "edit":
                        if (commandManager.playerHasPerm(player, "simplecoupons.edit"))
                            if (args.length > 1) {
                                Coupon coupon = this.plugin.getCouponManager().getCoupons().get(args[1]);
                                if (coupon != null) {
                                    (new CommandEdit(this.plugin)).editCoupon(player, coupon);
                                } else {
                                    Yaml.sendSimpleMessage((CommandSender) player, messages.get("coupon.invalid"));
                                }
                            } else {
                                commandManager.insufficientArgs(player, "sc edit <coupon-name>");
                            }
                        return false;
                    case "list":
                        if (commandManager.playerHasPerm(player, "simplecoupons.list"))
                            (new CommandList(this.plugin)).sendCouponList(player);
                        return false;
                    case "reload":
                        if (commandManager.playerHasPerm(player, "simplecoupons.reload"))
                            (new CommandReload(this.plugin)).reloadPlugin(player);
                        return false;
                    case "help":
                        if (commandManager.playerHasPerm(player, "simplecoupons.help"))
                            (new CommandHelp(this.plugin)).sendHelp(player);
                        return false;
                    case "version":
                        (new CommandVersion(this.plugin)).getVersion(player);
                        return false;
                }
                (new CommandHelp(this.plugin)).sendHelp(player);
            } else {
                (new CommandHelp(this.plugin)).sendHelp(player);
            }
        } else if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "give":
                    if (args.length > 2) {
                        Coupon coupon = this.plugin.getCouponManager().getCoupons().get(args[1]);
                        if (coupon != null) {
                            try {
                                (new CommandGive(this.plugin)).giveCoupon(sender, args[2], args[1], Integer.parseInt(args[3]));
                            } catch (Exception e) {
                                (new CommandGive(this.plugin)).giveCoupon(sender, args[2], args[1], 1);
                            }
                        } else {
                            Yaml.sendSimpleMessage(sender, messages.get("coupon.invalid"));
                        }
                    } else {
                        commandManager.insufficientArgs(sender, "sc give <coupon-name> <player>");
                    }
                    return false;
                case "reload":
                    (new CommandReload(this.plugin)).reloadPlugin(sender);
                    return false;
                case "version":
                    (new CommandVersion(this.plugin)).getVersion(sender);
                    return false;
            }
            (new CommandHelp(this.plugin)).sendHelp(sender);
        }
        return false;
    }

    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> tabs = new ArrayList<>();
        if (args.length == 1) {
            tabs.add("edit");
            tabs.add("delete");
            tabs.add("create");
            tabs.add("get");
            tabs.add("give");
            tabs.add("list");
            tabs.add("reload");
            tabs.add("help");
            tabs.add("version");
        } else if (args.length == 2) {
            if ((args[0].equalsIgnoreCase("get") || args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("delete")))
                tabs.addAll(this.plugin.getCouponManager().getCoupons().keySet());
        } else if (args.length > 2) {
            return null;
        }
        return tabs;
    }
}