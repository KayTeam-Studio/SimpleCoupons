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
import org.bukkit.inventory.ItemStack;
import org.kayteam.inputapi.inputs.DropInput;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.coupon.Coupon;
import org.kayteam.storageapi.storage.Yaml;

public class ItemsImput {
    public SimpleCoupons plugin;

    private final Coupon coupon;

    public ItemsImput(SimpleCoupons plugin, Coupon coupon) {
        this.plugin = plugin;
        this.coupon = coupon;
    }

    public void addItemsInput(Player player) {
        player.closeInventory();
        Yaml.sendSimpleMessage(player, this.plugin.getMessagesYaml().get("edit.item"), new String[][]{{"%path%", this.coupon
                .getName() + "/itemRewards"}, {"%value%", "valid item"}});
        this.plugin.getInputManager().addInput(player, new DropInput() {
            public void onPLayerDrop(Player player, ItemStack itemStack) {
                ItemsImput.this.coupon.getItems().add(itemStack);
                ItemsImput.this.plugin.getCouponManager().saveCoupon(ItemsImput.this.coupon);
                Bukkit.getScheduler().runTaskLater(ItemsImput.this.plugin, () -> ItemsImput.this.plugin.getServer().dispatchCommand(player, "sc edit " + ItemsImput.this.coupon.getName()), 3L);
            }

            public void onPlayerSneak(Player player) {
                Bukkit.getScheduler().runTaskLater(ItemsImput.this.plugin, () -> ItemsImput.this.plugin.getServer().dispatchCommand(player, "sc edit " + ItemsImput.this.coupon.getName()), 3L);
            }
        });
    }
}
