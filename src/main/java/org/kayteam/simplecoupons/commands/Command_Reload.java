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

import org.bukkit.command.CommandSender;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.storageapi.storage.Yaml;

public class Command_Reload {
    private SimpleCoupons plugin;

    public Command_Reload(SimpleCoupons plugin) {
        this.plugin = plugin;
    }

    public void reloadPlugin(CommandSender sender) {
        Thread thread = new Thread(() -> {
            this.plugin.getCouponManager().getCoupons().clear();
            this.plugin.getCouponManager().loadCoupons();
            this.plugin.getConfigYaml().reload();
            this.plugin.getMessagesYaml().reload();
            Yaml.sendSimpleMessage(sender, this.plugin.getMessagesYaml().get("reload"));
        });
        thread.start();
    }
}
