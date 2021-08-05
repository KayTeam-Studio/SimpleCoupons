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

package org.kayteam.simplecoupons.inputs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.coupon.Coupon;
import org.kayteam.simplecoupons.util.inventoryitems.InventoryItems;

public class ItemsImput {

    private SimpleCoupons plugin;
    private Coupon coupon;

    public ItemsImput(SimpleCoupons plugin, Coupon coupon) {
        this.plugin = plugin;
        this.coupon = coupon;
    }

    public void addItemsInput(Player player){
        player.closeInventory();
        Inventory inventory = Bukkit.createInventory(null, 54, plugin.getConfigYaml().getString("menu.items.title"));
        if(coupon.getItems().size()>0){
            for(ItemStack item : coupon.getItems()){
                if(item != null){
                    inventory.addItem(item);
                }
            }
        }
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                player.openInventory(inventory);
            }
        }, 1);
        plugin.getInventoryItemsManager().addInteract(player, new InventoryItems() {
            @Override
            public boolean onInventoryClose(Player player, Inventory input) {
                coupon.getItems().clear();
                for(ItemStack item : input.getContents()){
                    if(item != null){
                        if(!item.getType().isAir()){
                            coupon.getItems().add(item);
                        }
                    }
                }
                plugin.getCouponManager().saveCoupon(coupon);
                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        plugin.getServer().dispatchCommand(player, "sc edit "+coupon.getName());
                    }
                }, 3);
                return true;
            }
        }, true);
    }
}
