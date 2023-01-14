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

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kayteam.inventoryapi.InventoryBuilder;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.coupon.Coupon;
import org.kayteam.storageapi.storage.Yaml;

import java.util.ArrayList;
import java.util.List;

public class CouponsMenu extends InventoryBuilder {
    private final SimpleCoupons plugin;

    public CouponsMenu(SimpleCoupons plugin, int page) {
        super(plugin.getConfigYaml().getString("menu.list.title"), 3);
        this.plugin = plugin;
        List<Coupon> coupons = new ArrayList<>(plugin.getCouponManager().getCoupons().values());
        fillItem(() -> plugin.getConfigYaml().getItemStack("menu.list.items.fill"));
        addItem(4, () -> Yaml.replace(plugin.getConfigYaml().getItemStack("menu.list.items.information"), new String[][]{{"%coupons_amount%", String.valueOf(plugin.getCouponManager().getCoupons().keySet().size())}}));
        int itemSlot = 0;
        for (int i = 9 * page; i < coupons.size() && i < 9 * page + 8; i++) {
            int finalI = i;
            Coupon coupon = coupons.get(finalI);
            addItem(9 + itemSlot, () -> {
                if (coupon != null) {
                    String money = String.valueOf(coupon.getMoney());
                    String xp = String.valueOf(coupon.getXp());
                    String permission = coupon.getPermission();
                    String items = String.valueOf(coupon.getItems().size());
                    String messages = String.valueOf(coupon.getMessages().size());
                    String commands = String.valueOf(coupon.getCommands().size());
                    String maxUses = String.valueOf(coupon.getUses());
                    ItemStack itemStack = plugin.getConfigYaml().getItemStack("menu.list.items.coupon");
                    itemStack.setType(coupon.getCouponItem().getType());
                    return Yaml.replace(itemStack, new String[][]{{"%coupon_name%", coupon.getName()}, {"%coupon_money%", money}, {"%coupon_xp%", xp}, {"%coupon_items%", items}, {"%coupon_messages%", messages}, {"%coupon_commands%", commands}, {"%coupon_permission%", permission}, {"%coupon_max_uses%", maxUses}});
                }
                return null;
            });
            addLeftAction(9 + itemSlot, (player, slot) -> onLeftClick(player, coupon));
            addRightAction(9 + itemSlot, (player, slot) -> onRightClick(player, coupon));
            itemSlot++;
        }
        addItem(22, () -> plugin.getConfigYaml().getItemStack("menu.list.items.close"));
        addLeftAction(22, (player, slot) -> player.closeInventory());
        if (page > 0) {
            addItem(26, () -> plugin.getConfigYaml().getItemStack("menu.list.items.previous-page"));
            addLeftAction(26, (player, slot) -> plugin.getInventoryManager().openInventory(player, new CouponsMenu(plugin, page - 1)));
        }
        if (coupons.size() >= 9 * page + 9) {
            addItem(26, () -> plugin.getConfigYaml().getItemStack("menu.list.items.next-page"));
            addLeftAction(26, (player, slot) -> plugin.getInventoryManager().openInventory(player, new CouponsMenu(plugin, page + 1)));
        }
    }

    public void onRightClick(Player player, Coupon coupon) {
        if (player.hasPermission("simplecoupons.edit") && coupon != null) {
            player.closeInventory();
            this.plugin.getInventoryManager().openInventory(player, new EditMenu(this.plugin, coupon));
        }
    }

    public void onLeftClick(Player player, Coupon coupon) {
        if (coupon != null)
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.plugin.getServer().dispatchCommand(player, "sc get " + coupon.getName()), 1L);
    }
}
