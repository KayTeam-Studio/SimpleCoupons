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

package org.kayteam.simplecoupons.listeners;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.events.CouponUseEvent;

import java.util.Objects;


public class PlayerInteract implements Listener {
    private final SimpleCoupons plugin;

    public PlayerInteract(SimpleCoupons plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        try {
            if (this.plugin.getCouponManager().getCoupons().containsKey((new NBTItem(Objects.requireNonNull(event.getItem()))).getString("coupon-name"))) {
                event.setCancelled(true);
                this.plugin.getServer().getPluginManager().callEvent(new CouponUseEvent(event.getPlayer(), (new NBTItem(event.getItem())).getString("coupon-name"), event.getPlayer().getInventory().getHeldItemSlot()));
            }
        } catch (Exception exception) {
        }
    }
}
