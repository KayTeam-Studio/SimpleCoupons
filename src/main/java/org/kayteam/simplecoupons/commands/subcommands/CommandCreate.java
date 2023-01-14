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

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.coupon.Coupon;
import org.kayteam.simplecoupons.coupon.CouponManager;
import org.kayteam.simplecoupons.inventories.EditMenu;
import org.kayteam.simplecoupons.util.Color;
import org.kayteam.storageapi.storage.Yaml;

public class CommandCreate {
    private final SimpleCoupons plugin;

    public CommandCreate(SimpleCoupons plugin) {
        this.plugin = plugin;
    }

    public void createCoupon(Player player, String couponName) {
        if (!this.plugin.getCouponManager().getCoupons().containsKey(couponName)) {
            Coupon coupon = new Coupon(couponName);
            CouponManager couponManager = this.plugin.getCouponManager();
            ItemStack couponItem = new ItemStack(Material.STONE);
            ItemMeta itemMeta = couponItem.getItemMeta();
            itemMeta.setDisplayName(Color.convert("&7&oDefault Coupon"));
            couponItem.setItemMeta(itemMeta);
            coupon.setCouponItem(couponItem);
            couponManager.saveCoupon(coupon);
            couponManager.getCoupons().put(couponName, coupon);
            this.plugin.getInventoryManager().openInventory(player, new EditMenu(this.plugin, coupon));
        } else {
            Yaml.sendSimpleMessage(player, this.plugin.getMessagesYaml().get("coupon.already-exist"));
        }
    }
}