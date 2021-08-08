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
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.coupon.Coupon;
import org.kayteam.simplecoupons.coupon.CouponManager;
import org.kayteam.simplecoupons.util.chat.ChatInput;

import java.util.HashMap;

public class MaxUsesInput {

    private SimpleCoupons plugin;

    private Coupon coupon;

    public MaxUsesInput(SimpleCoupons plugin, Coupon coupon) {
        this.plugin = plugin;
        this.coupon = coupon;
    }

    public void addMaxUsesInput(Player player){
        CouponManager couponManager = plugin.getCouponManager();
        plugin.getChatInputManager().addChatInput(player, new ChatInput(new HashMap<>()) {
            @Override
            public boolean onChatInput(Player player, String input) {
                try{
                    coupon.setUses(Integer.parseInt(input));
                    couponManager.saveCoupon(coupon);
                    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                        @Override
                        public void run() {
                            plugin.getServer().dispatchCommand(player, "sc edit "+coupon.getName());
                        }
                    }, 1);
                    return true;
                }catch (Exception e){
                    plugin.getMessagesYaml().sendMessage(player, "edit.chat",
                            new String[][]{{"%path%", coupon.getName()+"/uses"}, {"%value%", "valid number"}});
                    return false;
                }
            }

            @Override
            public void onPlayerSneak(Player player) {
                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        plugin.getServer().dispatchCommand(player, "sc edit "+coupon.getName());
                    }
                }, 1);
            }
        });
    }
}
