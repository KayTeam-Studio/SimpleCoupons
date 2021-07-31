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

import java.util.ArrayList;
import java.util.List;

public class Command_SimpleCoupons implements CommandExecutor, TabCompleter {

    private final SimpleCoupons plugin;

    public Command_SimpleCoupons(SimpleCoupons plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender;
        CommandManager commandManager = plugin.getCommandManager();
        if(args.length>0){
            switch (args[0].toLowerCase()){
                case "get":{
                    if(args.length>1){
                        if(commandManager.playerHasPerm(player, "simplecoupons.get")){
                            new Command_Get(plugin).getCoupon(player, args[1]);
                        }
                    }else{
                        commandManager.insufficientArgs(player, "sc get <coupon-name>");
                    }
                    break;
                }
                case "give":{
                    if(commandManager.playerHasPerm(player, "simplecoupons.give")){
                        if(args.length>2){
                            new Command_Give(plugin).giveCoupon(player, args[2], args[1]);
                        }else{
                            commandManager.insufficientArgs(player, "sc give <coupon-name> <player>");
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
                            }
                        }else{
                            commandManager.insufficientArgs(player, "sc edit <coupon-name>");
                        }
                    }
                    break;
                }
                case "list":{
                    if(commandManager.playerHasPerm(player, "simplecoupons.list")){
                        if(args.length>1){
                            try{
                                new Command_List(plugin).sendCouponList(player, Integer.parseInt(args[1]));
                            }catch (Exception e){
                                Bukkit.getLogger().info(e.getLocalizedMessage());
                            }
                        }else{
                            new Command_List(plugin).sendCouponList(player, 1);
                        }
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
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> tabs = new ArrayList<>();
        if(args.length==1){
            tabs.add("get");
            tabs.add("give");
            tabs.add("edit");
            tabs.add("list");
            tabs.add("reload");
            tabs.add("help");
            tabs.add("version");
        }else if(args.length==2){
            if(args[0].equalsIgnoreCase("get") || args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("edit")){
                for(String coupon : plugin.getCouponManager().getCoupons().keySet()){
                    tabs.add(coupon);
                }
            }
        }else if(args.length>2){
            return null;
        }
        return tabs;
    }
}
