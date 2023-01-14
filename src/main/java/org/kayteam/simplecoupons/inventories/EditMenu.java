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

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.kayteam.inventoryapi.InventoryBuilder;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.commands.Command_Delete;
import org.kayteam.simplecoupons.coupon.Coupon;
import org.kayteam.simplecoupons.coupon.CouponManager;
import org.kayteam.simplecoupons.inputs.*;
import org.kayteam.simplecoupons.util.Color;
import org.kayteam.storageapi.storage.YML;
import org.kayteam.storageapi.storage.Yaml;

import java.util.ArrayList;
import java.util.List;

public class EditMenu extends InventoryBuilder {
    public EditMenu(SimpleCoupons plugin, Coupon coupon) {
        super(plugin.getConfigYaml().getString("menu.edit.title"), 5);
        CouponManager couponManager = plugin.getCouponManager();
        YML messages = plugin.getMessagesYaml();
        YML config = plugin.getConfigYaml();
        fillItem(() -> config.getItemStack("menu.list.items.fill"));
        addItem(28, () -> config.getItemStack("menu.list.items.close"));
        addLeftAction(28, (player, slot) -> player.closeInventory());
        addItem(10, () -> {
            List<String> lore;
            ItemStack item = new ItemStack(coupon.getCouponItem());
            ItemMeta meta = item.getItemMeta();
            if (meta.getLore() != null) {
                lore = meta.getLore();
            } else {
                lore = new ArrayList<>();
            }
            lore.add(Color.convert("&f"));
            lore.add(Color.convert(" &f- &7Left Click to change item"));
            meta.setLore(lore);
            item.setItemMeta(meta);
            return item;
        });
        addLeftAction(10, (player, slot) -> {
            player.closeInventory();
            new CouponItemInput(plugin, coupon).addCouponItemInput(player);
        });
        addItem(12, () -> {
            try {
                return Yaml.replace(config.getItemStack("menu.edit.items.money"), new String[][]{{"%coupon_money%", String.valueOf(coupon.getMoney())}});
            } catch (Exception e) {
                return Yaml.replace(config.getItemStack("menu.edit.items.money"), new String[][]{{"%coupon_money%", "0"}});
            }
        });
        addLeftAction(12, (player, slot) -> {
            player.closeInventory();
            (new MoneyInput(plugin, coupon)).addMoneyInput(player);
        });
        addItem(13, () -> {
            try {
                return Yaml.replace(config.getItemStack("menu.edit.items.xp"), new String[][]{{"%coupon_xp%", String.valueOf(coupon.getXp())}});
            } catch (Exception e) {
                return Yaml.replace(config.getItemStack("menu.edit.items.xp"), new String[][]{{"%coupon_xp%", "0"}});
            }
        });
        addLeftAction(13, (player, slot) -> {
            player.closeInventory();
            (new XPInput(plugin, coupon)).addXPInput(player);
        });
        addItem(14, () -> {
            try {
                return Yaml.replace(config.getItemStack("menu.edit.items.items"), new String[][]{{"%coupon_items%", String.valueOf(coupon.getItems().size())}});
            } catch (Exception e) {
                return Yaml.replace(config.getItemStack("menu.edit.items.items"), new String[][]{{"%coupon_items%", "0"}});
            }
        });
        addLeftAction(14, (player, slot) -> (new ItemsImput(plugin, coupon)).addItemsInput(player));
        addItem(15, () -> {
            try {
                return Yaml.replace(config.getItemStack("menu.edit.items.commands"), new String[][]{{"%coupon_commands%", String.valueOf(coupon.getCommands().size())}});
            } catch (Exception e) {
                return Yaml.replace(config.getItemStack("menu.edit.items.commands"), new String[][]{{"%coupon_commands%", "0"}});
            }
        });
        addLeftAction(15, (player, slot) -> {
            player.closeInventory();
            (new CommandsInput(plugin, coupon)).addCommandInput(player);
        });
        addRightAction(15, (player, slot) -> {
            if (coupon.getCommands().size() > 0) {
                player.closeInventory();
                List<Object> index = new ArrayList();
                for (int i = 0; i < coupon.getCommands().size(); i++)
                    index.add(Integer.valueOf(i));
                plugin.getInventoryManager().openInventory(player, new CommandsEditMenu(plugin, index, coupon, 0));
            }
        });
        addItem(16, () -> {
            try {
                return Yaml.replace(config.getItemStack("menu.edit.items.messages"), new String[][]{{"%coupon_messages%", String.valueOf(coupon.getMessages().size())}});
            } catch (Exception e) {
                return Yaml.replace(config.getItemStack("menu.edit.items.messages"), new String[][]{{"%coupon_messages%", "0"}});
            }
        });
        addLeftAction(16, (player, slot) -> {
            player.closeInventory();
            (new MessagesInput(plugin, coupon)).addMessageInput(player);
        });
        addRightAction(16, (player, slot) -> {
            if (coupon.getMessages().size() > 0) {
                player.closeInventory();
                List<Object> index = new ArrayList();
                for (int i = 0; i < coupon.getMessages().size(); i++)
                    index.add(Integer.valueOf(i));
                plugin.getInventoryManager().openInventory(player, new MessagesEditMenu(plugin, index, coupon, 0));
            }
        });
        addItem(30, () -> Yaml.replace(config.getItemStack("menu.edit.items.permission"), new String[][]{{"%coupon_permission%", coupon.getPermission()}}));
        addLeftAction(30, (player, slot) -> {
            player.closeInventory();
            (new PermissionInput(plugin, coupon)).addPermissionInput(player);
        });
        addItem(31, () -> Yaml.replace(config.getItemStack("menu.edit.items.max-uses"), new String[][]{{"%coupon_max_uses%", String.valueOf(coupon.getUses())}}));
        addLeftAction(31, (player, slot) -> {
            player.closeInventory();
            (new MaxUsesInput(plugin, coupon)).addMaxUsesInput(player);
        });
        addItem(34, () -> config.getItemStack("menu.edit.items.delete"));
        addLeftAction(34, (player, slot) -> {
            if (player.hasPermission("simplecoupons.delete")) {
                player.closeInventory();
                (new Command_Delete(plugin)).openDeleteMenu(player, coupon);
            }
        });
    }
}
