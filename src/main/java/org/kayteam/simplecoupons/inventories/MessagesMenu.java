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

package org.kayteam.simplecoupons.inventories;

import com.sun.tools.javac.jvm.Items;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.coupon.Action;
import org.kayteam.simplecoupons.util.inventory.PagesInventory;

import java.util.ArrayList;
import java.util.List;

public class MessagesMenu extends PagesInventory {

    private final SimpleCoupons plugin;

    public MessagesMenu(SimpleCoupons plugin, Object coupon) {
        super(plugin.getConfigYaml().getString("menu.texts-menu.title"), plugin.getConfigYaml().getInt("menu.texts-menu.rows"), coupon);
        this.plugin = plugin;
    }

    @Override
    public ItemStack getListedItem(Object object) {
        return null;
    }

    @Override
    public ItemStack getPanel() {
        return plugin.getConfigYaml().getItemStack("menu.list.items.fill");
    }

    @Override
    public ItemStack getInformation() {
        return plugin.getConfigYaml().getItemStack("menu.list.items.fill");
    }

    @Override
    public ItemStack getPrevious() {
        return plugin.getConfigYaml().getItemStack("menu.list.items.previous-page");
    }

    @Override
    public ItemStack getNext() {
        return plugin.getConfigYaml().getItemStack("menu.list.items.next-page");
    }

    @Override
    public ItemStack getClose() {
        return plugin.getConfigYaml().getItemStack("menu.list.items.close");
    }

    @Override
    public void onRightClick(Player player, Object object) {

    }

    @Override
    public void onLeftClick(Player player, Object object) {

    }

    @Override
    public void onMiddleClick(Player player, Object object) {
        NBTItem item = new NBTItem((ItemStack) object);
        int messageIndex = item.getInteger("message-line");
        String couponName = item.getString("coupon-name");
        plugin.getCouponManager().getCoupons().get(couponName).getActions().get(Action.MESSAGE).remove(messageIndex);
        plugin.getCouponManager().saveCoupon(plugin.getCouponManager().getCoupons().get(couponName));
        player.closeInventory();
    }
}
