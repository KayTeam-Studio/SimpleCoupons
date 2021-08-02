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

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kayteam.simplecoupons.SimpleCoupons;
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
        couponManager.executeCouponActions(couponManager.getCoupons().get(event.getCouponName()), player);
        player.getInventory().getItem(event.getEquipmentSlot()).setAmount(player.getInventory().getItem(event.getEquipmentSlot()).getAmount()-1);
    }
}
