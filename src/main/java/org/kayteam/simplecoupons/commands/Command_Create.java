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

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.coupon.Coupon;
import org.kayteam.simplecoupons.coupon.CouponManager;
import org.kayteam.simplecoupons.inventories.EditMenu;
import org.kayteam.simplecoupons.util.Color;

public class Command_Create {
    private SimpleCoupons plugin;

    public Command_Create(SimpleCoupons plugin) {
        this.plugin = plugin;
    }

    public void createCoupon(Player player, String couponName){
        if(!plugin.getCouponManager().getCoupons().containsKey(couponName)){
            Coupon coupon = new Coupon(couponName);
            CouponManager couponManager = plugin.getCouponManager();
            ItemStack couponItem = new ItemStack(Material.STONE);
            ItemMeta itemMeta = couponItem.getItemMeta();
            itemMeta.setDisplayName(Color.convert("&7&oDefault Coupon"));
            couponItem.setItemMeta(itemMeta);
            coupon.setCouponItem(couponItem);
            couponManager.saveCoupon(coupon);
            couponManager.getCoupons().put(couponName, coupon);
            plugin.getMenuInventoryManager().openInventory(player, new EditMenu(plugin, coupon));
        }else{
            plugin.getMessagesYaml().sendMessage(player, "coupon.already-exist");
        }
    }
}
