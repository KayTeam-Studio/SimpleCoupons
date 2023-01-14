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

package org.kayteam.simplecoupons.commands.subcommands;

import org.bukkit.entity.Player;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.coupon.Coupon;
import org.kayteam.simplecoupons.inventories.DeleteMenu;

public class CommandDelete {
    private final SimpleCoupons plugin;

    public CommandDelete(SimpleCoupons plugin) {
        this.plugin = plugin;
    }

    public void openDeleteMenu(Player player, Coupon coupon) {
        this.plugin.getInventoryManager().openInventory(player, new DeleteMenu(this.plugin, player, coupon));
    }
}
