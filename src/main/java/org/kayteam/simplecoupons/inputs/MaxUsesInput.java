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
import org.kayteam.inputapi.inputs.ChatInput;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.coupon.Coupon;
import org.kayteam.simplecoupons.coupon.CouponManager;
import org.kayteam.storageapi.storage.Yaml;

public class MaxUsesInput {
    private final SimpleCoupons plugin;

    private final Coupon coupon;

    public MaxUsesInput(SimpleCoupons plugin, Coupon coupon) {
        this.plugin = plugin;
        this.coupon = coupon;
    }

    public void addMaxUsesInput(Player player) {
        final CouponManager couponManager = this.plugin.getCouponManager();
        Yaml.sendSimpleMessage(player, this.plugin.getMessagesYaml().get("edit.chat"), new String[][]{{"%path%", this.coupon
                .getName() + "/uses"}, {"%value%", "valid number"}});
        this.plugin.getInputManager().addInput(player, new ChatInput() {
            public boolean onChatInput(Player player, String input) {
                try {
                    MaxUsesInput.this.coupon.setUses(Integer.parseInt(input));
                    couponManager.saveCoupon(MaxUsesInput.this.coupon);
                    Bukkit.getScheduler().runTaskLater(MaxUsesInput.this.plugin, () -> MaxUsesInput.this.plugin.getServer().dispatchCommand(player, "sc edit " + MaxUsesInput.this.coupon.getName()), 1L);
                    return true;
                } catch (Exception e) {
                    Yaml.sendSimpleMessage(player, MaxUsesInput.this.plugin.getMessagesYaml().get("edit.chat"), new String[][]{{"%path%",
                            coupon.getName() + "/uses"}, {"%value%", "valid number"}});
                    return false;
                }
            }

            public void onPlayerSneak(Player player) {
                Bukkit.getScheduler().runTaskLater(MaxUsesInput.this.plugin, () -> MaxUsesInput.this.plugin.getServer().dispatchCommand(player, "sc edit " + MaxUsesInput.this.coupon.getName()), 1L);
            }
        });
    }
}