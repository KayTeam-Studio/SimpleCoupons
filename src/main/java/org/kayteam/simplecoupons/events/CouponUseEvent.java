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

package org.kayteam.simplecoupons.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.coupon.CouponManager;

public class CouponUseEvent extends Event implements Cancellable {

    private final SimpleCoupons plugin;
    private static HandlerList handlerList;

    private boolean cancelled = false;

    private final Player player;
    private final String couponName;
    private EquipmentSlot equipmentSlot;

    public CouponUseEvent(SimpleCoupons plugin, Player player, String couponName, EquipmentSlot equipmentSlot) {
        this.plugin = plugin;
        this.player = player;
        this.couponName = couponName;
        this.equipmentSlot = equipmentSlot;
        executeActions();
    }


    private void executeActions(){
        CouponManager couponManager = plugin.getCouponManager();
        couponManager.executeCouponActions(couponManager.getCoupons().get(couponName), player);
        player.getInventory().getItem(equipmentSlot).setAmount(player.getInventory().getItem(equipmentSlot).getAmount()-1);
    }

    public EquipmentSlot getEquipmentSlot() {
        return equipmentSlot;
    }

    public Player getPlayer() {
        return player;
    }

    public String getCouponName() {
        return couponName;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
