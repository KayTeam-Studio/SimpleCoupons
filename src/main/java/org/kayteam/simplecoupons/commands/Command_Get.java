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

import org.bukkit.entity.Player;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.storageapi.storage.Yaml;

public class Command_Get {
    private final SimpleCoupons plugin;

    public Command_Get(SimpleCoupons plugin) {
        this.plugin = plugin;
    }

    public void getCoupon(Player player, String couponName, int amount) {
        if (this.plugin.getCouponManager().getCoupons().containsKey(couponName)) {
            for (int i = 0; i < amount; i++)
                this.plugin.getCouponManager().giveCoupon(couponName, player);
        } else {
            Yaml.sendSimpleMessage(player, this.plugin.getMessagesYaml().get("coupon.invalid"));
        }
    }
}

