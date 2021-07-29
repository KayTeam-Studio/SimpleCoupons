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

import java.util.ArrayList;
import java.util.List;

public class Command_SimpleCoupons implements CommandExecutor, TabCompleter {

    private final SimpleCoupons plugin;

    public Command_SimpleCoupons(SimpleCoupons plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length>1){
            switch (args[0]){
                case "get":{
                    plugin.getCouponManager().giveCoupon(args[1], (Player) sender);
                }
                case "give":{
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
            tabs.add("get");
            tabs.add("give");
            tabs.add("reload");
            tabs.add("help");
            tabs.add("version");
        }else if(args.length==2){
            if(args[0].equalsIgnoreCase("get") || args[0].equalsIgnoreCase("give")){
                for(String coupon : plugin.getCouponManager().getCoupons().keySet()){
                    tabs.add(coupon);
                }
            }
        }
        return tabs;
    }
}
