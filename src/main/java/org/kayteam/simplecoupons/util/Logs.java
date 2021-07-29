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

package org.kayteam.simplecoupons.util;

import org.bukkit.plugin.java.JavaPlugin;

public class Logs {
    public static void sendLoadLogError(JavaPlugin plugin, String object, String path){

        plugin.getLogger().info(Color.convert("&cAn error has occurred trying &6load file section &f"+object+" &cfrom &f"+path+".yml"));
    }

    public static void sendExecuteLogError(JavaPlugin plugin, String object, String path){
        plugin.getLogger().info(Color.convert("&cAn error has occurred trying &6execute command &f"+object+" &cfrom &f"+path+".yml"));
    }

    public static void sendGiveItemLogError(JavaPlugin plugin, String object, String path, String target){
        plugin.getLogger().info(Color.convert("&cAn error has occurred trying give &6item &f"+object+" &cto &f"+target+" &cfrom &f"+path+".yml"));
    }

    public static void sendGiveXPLogError(JavaPlugin plugin, String path, String target){
        plugin.getLogger().info(Color.convert("&cAn error has occurred trying give &6xp &cto &f"+target+" &cfrom &f"+path+".yml"));
    }

    public static void sendMessageLogError(JavaPlugin plugin, String path, String target){
        plugin.getLogger().info(Color.convert("&cAn error has occurred trying send &6message &cto &f"+target+" &cfrom &f"+path+".yml"));
    }

    public static void sendGiveMoneyLogError(JavaPlugin plugin, String path, String target){
        plugin.getLogger().info(Color.convert("&cAn error has occurred trying give &6money &cto &f"+target+" &cfrom &f"+path+".yml"));
    }

    public static void sendGetItemLogError(JavaPlugin plugin, String path){
        plugin.getLogger().info(Color.convert("&cAn error has occurred trying get &6item &cfrom &f"+path+".yml"));
    }

    public static void sendCorrectCouponLoadLog(JavaPlugin plugin, String couponName){
        plugin.getLogger().info(Color.convert("&aThe &f"+couponName+" &acoupon was loaded correctly from &f"+couponName+".yml"));
    }
}
