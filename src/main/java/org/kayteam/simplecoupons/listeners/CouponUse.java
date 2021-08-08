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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.coupon.Coupon;
import org.kayteam.simplecoupons.coupon.CouponManager;
import org.kayteam.simplecoupons.events.CouponUseEvent;

public class CouponUse implements Listener {

    private SimpleCoupons plugin;

    public CouponUse(SimpleCoupons plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onCouponUse(CouponUseEvent event){
        Player player = event.getPlayer();
        CouponManager couponManager = plugin.getCouponManager();
        Coupon coupon = couponManager.getCoupons().get(event.getCouponName());
        if(!coupon.getPermission().equalsIgnoreCase("none")){
            if(!player.hasPermission(coupon.getPermission())){
                plugin.getMessagesYaml().sendMessage(player, "coupon.no-permissions");
                return;
            }
        }
        NBTItem nbtItem = new NBTItem(player.getInventory().getItem(event.getEquipmentSlot()));
        if(nbtItem.getInteger("coupon-uses") > 0) {
            int oldUses = nbtItem.getInteger("coupon-uses");
            int newUses = nbtItem.getInteger("coupon-uses") - 1;
            nbtItem.setInteger("coupon-uses", newUses);
            couponManager.executeCouponActions(coupon, player);
            if (newUses == 0) {
                player.getInventory().getItem(event.getEquipmentSlot()).setAmount(player.getInventory().getItem(event.getEquipmentSlot()).getAmount() - 1);
            }else{
                player.getInventory().setItem(event.getEquipmentSlot(), nbtItem.getItem());
            }
        }else{
            couponManager.executeCouponActions(coupon, player);
        }
    }
}
