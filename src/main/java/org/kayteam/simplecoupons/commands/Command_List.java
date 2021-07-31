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
import org.kayteam.simplecoupons.coupon.Coupon;
import org.kayteam.simplecoupons.inventories.CouponsMenu;
import org.kayteam.simplecoupons.util.Yaml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Command_List {

    private SimpleCoupons plugin;

    public Command_List(SimpleCoupons plugin) {
        this.plugin = plugin;
    }

    public void sendCouponList(Player player, int page){
        Map<String, Coupon> coupons = plugin.getCouponManager().getCoupons();
        List<Object> couponList = new ArrayList<>();
        for(String coupon : coupons.keySet()){
            couponList.add(coupons.get(coupon));
        }
        plugin.getPagesInventoryManager().openInventory(player, new CouponsMenu(plugin));
    }
}
