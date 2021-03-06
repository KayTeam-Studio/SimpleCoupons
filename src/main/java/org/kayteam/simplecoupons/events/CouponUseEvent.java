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
import org.jetbrains.annotations.NotNull;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.coupon.CouponManager;

public class CouponUseEvent extends Event implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();

    private boolean cancelled = false;

    private final Player player;
    private final String couponName;
    private final EquipmentSlot equipmentSlot;

    /**
     *
     * @param player Player who use the coupon
     * @param couponName Coupon name.
     * @param equipmentSlot Slot of the player inventory where the coupon item was.
     */
    public CouponUseEvent(Player player, String couponName, EquipmentSlot equipmentSlot) {
        this.player = player;
        this.couponName = couponName;
        this.equipmentSlot = equipmentSlot;
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

    public static HandlerList getHandlerList() {
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
