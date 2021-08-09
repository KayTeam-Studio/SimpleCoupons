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

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.coupon.Coupon;
import org.kayteam.simplecoupons.util.CommandManager;
import org.kayteam.simplecoupons.util.Yaml;

import java.util.ArrayList;
import java.util.List;

public class Command_SimpleCoupons implements CommandExecutor, TabCompleter {

    private final SimpleCoupons plugin;

    public Command_SimpleCoupons(SimpleCoupons plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        CommandManager commandManager = plugin.getCommandManager();
        Yaml messages = plugin.getMessagesYaml();
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length>0){
                switch (args[0].toLowerCase()){
                    case "get":{
                        if(commandManager.playerHasPerm(player, "simplecoupons.get")){
                            if(args.length > 1){
                                Coupon coupon = plugin.getCouponManager().getCoupons().get(args[1]);
                                if(coupon != null){
                                    try{
                                        new Command_Get(plugin).getCoupon(player, args[1], Integer.parseInt(args[2]));
                                    }catch (Exception e){
                                        new Command_Get(plugin).getCoupon(player, args[1], 1);
                                    }
                                }else{
                                    messages.sendMessage(player, "coupon.invalid");
                                }
                            }else{
                                commandManager.insufficientArgs(player, "sc get <coupon-name>");
                            }
                        }
                        break;
                    }
                    case "give":{
                        if(commandManager.playerHasPerm(player, "simplecoupons.give")){
                            if(args.length>2){
                                Coupon coupon = plugin.getCouponManager().getCoupons().get(args[1]);
                                if(coupon != null){
                                    try{
                                        new Command_Give(plugin).giveCoupon(player, args[2], args[1], Integer.parseInt(args[3]));
                                    }catch (Exception e){
                                        new Command_Give(plugin).giveCoupon(player, args[2], args[1], 1);
                                    }
                                }else{
                                    messages.sendMessage(player, "coupon.invalid");
                                }
                            }else{
                                commandManager.insufficientArgs(player, "sc give <coupon-name> <player>");
                            }
                        }
                        break;
                    }
                    case "create":{
                        if(commandManager.playerHasPerm(player, "simplecoupons.create")){
                            if(args.length>1){
                                new Command_Create(plugin).createCoupon(player, args[1]);
                            }else{
                                commandManager.insufficientArgs(player, "sc create <coupon-name>");
                            }
                        }
                        break;
                    }
                    case "delete":{
                        if(commandManager.playerHasPerm(player, "simplecoupons.delete")){
                            if(args.length>1){
                                Coupon coupon = plugin.getCouponManager().getCoupons().get(args[1]);
                                if(coupon != null){
                                    player.closeInventory();
                                    new Command_Delete(plugin).openDeleteMenu(player, coupon);
                                }else{
                                    messages.sendMessage(player, "coupon.invalid");
                                }
                            }else{
                                commandManager.insufficientArgs(player, "sc delete <coupon-name>");
                            }
                        }
                        break;
                    }
                    case "edit":{
                        if(commandManager.playerHasPerm(player, "simplecoupons.edit")){
                            if(args.length>1){
                                Coupon coupon = plugin.getCouponManager().getCoupons().get(args[1]);
                                if(coupon != null){
                                    new Command_Edit(plugin).editCoupon(player, coupon);
                                }else{
                                    messages.sendMessage(player, "coupon.invalid");
                                }
                            }else{
                                commandManager.insufficientArgs(player, "sc edit <coupon-name>");
                            }
                        }
                        break;
                    }
                    case "list":{
                        if(commandManager.playerHasPerm(player, "simplecoupons.list")){
                            new Command_List(plugin).sendCouponList(player);
                        }
                        break;
                    }
                    case "reload":{
                        if(commandManager.playerHasPerm(player, "simplecoupons.reload")){
                            new Command_Reload(plugin).reloadPlugin(player);
                        }
                        break;
                    }
                    case "help":{
                        if(commandManager.playerHasPerm(player, "simplecoupons.help")){
                            new Command_Help(plugin).sendHelp(player);
                        }
                        break;
                    }
                    case "version":{
                        new Command_Version(plugin).getVersion(player);
                        break;
                    }
                    default:{
                        new Command_Help(plugin).sendHelp(player);
                        break;
                    }
                }
            }else{
                new Command_Help(plugin).sendHelp(player);
            }
        }else{
            if(args.length>0) {
                switch (args[0].toLowerCase()) {
                    case "give":{
                        if(args.length>2){
                            Coupon coupon = plugin.getCouponManager().getCoupons().get(args[1]);
                            if(coupon != null){
                                try{
                                    new Command_Give(plugin).giveCoupon(sender, args[2], args[1], Integer.parseInt(args[3]));
                                }catch (Exception e){
                                    new Command_Give(plugin).giveCoupon(sender, args[2], args[1], 1);
                                }
                            }else{
                                messages.sendMessage(sender, "coupon.invalid");
                            }
                        }else{
                            commandManager.insufficientArgs(sender, "sc give <coupon-name> <player>");
                        }
                        break;
                    }
                    case "reload":{
                        new Command_Reload(plugin).reloadPlugin(sender);
                        break;
                    }
                    case "version":{
                        new Command_Version(plugin).getVersion(sender);
                        break;
                    }
                    default:{
                        new Command_Help(plugin).sendHelp(sender);
                        break;
                    }
                }
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> tabs = new ArrayList<>();
        if(args.length==1){
            if(commandSender.hasPermission("simplecoupons.edit")){
                tabs.add("edit");
            }if(commandSender.hasPermission("simplecoupons.delete")){
                tabs.add("delete");
            }
            if(commandSender.hasPermission("simplecoupons.create")){
                tabs.add("create");
            }
            if(commandSender.hasPermission("simplecoupons.get")){
                tabs.add("get");
            }
            if(commandSender.hasPermission("simplecoupons.give")){
                tabs.add("give");
            }
            if(commandSender.hasPermission("simplecoupons.list")){
                tabs.add("list");
            }
            if(commandSender.hasPermission("simplecoupons.reload")){
                tabs.add("reload");
            }
            if(commandSender.hasPermission("simplecoupons.help")){
                tabs.add("help");
            }
            tabs.add("version");
        }else if(args.length==2){
            if(args[0].equalsIgnoreCase("get") || args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("delete")){
                if(commandSender.hasPermission("simplecoupons.get") || commandSender.hasPermission("simplecoupons.edit") || commandSender.hasPermission("simplecoupons.give")){
                    tabs.addAll(plugin.getCouponManager().getCoupons().keySet());
                }
            }
        }else if(args.length>2){
            return null;
        }
        return tabs;
    }
}
