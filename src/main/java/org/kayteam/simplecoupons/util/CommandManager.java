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

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kayteam.simplecoupons.SimpleCoupons;

public class CommandManager {

    private SimpleCoupons plugin;

    public CommandManager(SimpleCoupons plugin) {
        this.plugin = plugin;
    }

    public boolean playerHasPerm(Player player, String permission){
        if(player.hasPermission(permission)){
            return true;
        }else{
            plugin.getMessagesYaml().sendMessage(player, "no-permissions");
            return false;
        }
    }

    public void insufficientArgs(Player player, String usage){
        plugin.getMessagesYaml().sendMessage(player, "insufficient-args", new String[][]{{"%usage%", usage}});
    }

    public void insufficientArgs(CommandSender sender, String usage){
        plugin.getMessagesYaml().sendMessage(sender, "insufficient-args", new String[][]{{"%usage%", usage}});
    }
}
