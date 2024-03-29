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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.coupon.Coupon;
import org.kayteam.simplecoupons.coupon.CouponManager;
import org.kayteam.simplecoupons.events.CouponUseEvent;
import org.kayteam.storageapi.storage.Yaml;

import java.util.Objects;

public class CouponUse implements Listener {
    private final SimpleCoupons plugin;

    public CouponUse(SimpleCoupons plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onCouponUse(CouponUseEvent event) {
        Player player = event.getPlayer();
        CouponManager couponManager = this.plugin.getCouponManager();
        Coupon coupon = couponManager.getCoupons().get(event.getCouponName());
        if (!coupon.getPermission().equalsIgnoreCase("none") &&
                !player.hasPermission(coupon.getPermission())) {
            Yaml.sendSimpleMessage(player, this.plugin.getMessagesYaml().get("coupon.no-permissions"));
            return;
        }
        NBTItem nbtItem = new NBTItem(Objects.<ItemStack>requireNonNull(player.getInventory().getItem(event.getInventorySlot())));
        if (nbtItem.getInteger("coupon-uses").intValue() > 0) {
            int oldUses = nbtItem.getInteger("coupon-uses").intValue();
            int newUses = oldUses - 1;
            nbtItem.setInteger("coupon-uses", Integer.valueOf(newUses));
            couponManager.executeCouponActions(coupon, player);
            if (newUses == 0) {
                Objects.<ItemStack>requireNonNull(player.getInventory().getItem(event.getInventorySlot())).setAmount(Objects.<ItemStack>requireNonNull(player.getInventory().getItem(event.getInventorySlot())).getAmount() - 1);
            } else {
                player.getInventory().setItem(event.getInventorySlot(), nbtItem.getItem());
            }
        } else {
            couponManager.executeCouponActions(coupon, player);
        }
    }
}
