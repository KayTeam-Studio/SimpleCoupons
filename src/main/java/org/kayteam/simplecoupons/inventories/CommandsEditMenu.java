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

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kayteam.inventoryapi.InventoryBuilder;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.coupon.Coupon;
import org.kayteam.storageapi.storage.Yaml;

import java.util.ArrayList;
import java.util.List;

public class CommandsEditMenu extends InventoryBuilder {
    private final SimpleCoupons plugin;

    private Coupon coupon;

    private int page;

    public CommandsEditMenu(SimpleCoupons plugin, List<Object> index, Coupon coupon, int page) {
        super(plugin.getConfigYaml().getString("menu.texts-menu.title", new String[][]{{"%path%", "commands"}}), 3);
        this.plugin = plugin;
        this.coupon = coupon;
        this.page = page;
        fillItem(() -> plugin.getConfigYaml().getItemStack("menu.list.items.fill"));
        addItem(22, () -> plugin.getConfigYaml().getItemStack("menu.list.items.close"));
        addLeftAction(22, (player, slot) -> player.closeInventory());
        int itemSlot = 0;
        for (int i = 9 * page; i < index.size() && i < 9 * page + 8; i++) {
            int finalI = i;
            addItem(9 + itemSlot, () -> getListedItem(Integer.valueOf(finalI)));
            addLeftAction(9 + itemSlot, (player, slot) -> onLeftClick(player, Integer.valueOf(finalI)));
            addMiddleAction(9 + itemSlot, (player, slot) -> onMiddleClick(player, Integer.valueOf(finalI)));
            addRightAction(9 + itemSlot, (player, slot) -> onRightClick(player, Integer.valueOf(finalI)));
            itemSlot++;
        }
        if (page > 0) {
            addItem(26, () -> plugin.getConfigYaml().getItemStack("menu.list.items.previous-page"));
            addLeftAction(26, (player, slot) -> plugin.getInventoryManager().openInventory(player, new MessagesEditMenu(plugin, index, coupon, page - 1)));
        }
        if (index.size() >= 9 * page + 9) {
            addItem(26, () -> plugin.getConfigYaml().getItemStack("menu.list.items.next-page"));
            addLeftAction(26, (player, slot) -> plugin.getInventoryManager().openInventory(player, new MessagesEditMenu(plugin, index, coupon, page + 1)));
        }
    }

    public ItemStack getListedItem(Object object) {
        if (object != null) {
            NBTItem nbtItem = new NBTItem(Yaml.replace(this.plugin.getConfigYaml().getItemStack("menu.texts-menu.items.commands"), new String[][]{{"%command%", this.coupon
                    .getCommands().get(((Integer) object).intValue())}}));
            nbtItem.setInteger("command-index", (Integer) object);
            return nbtItem.getItem();
        }
        return null;
    }

    public void onRightClick(Player player, Object object) {
        int index = ((Integer) object).intValue();
        List<String> commands = this.coupon.getCommands();
        if (index + 1 < commands.size()) {
            String line = commands.get(index);
            String line2 = commands.get(index + 1);
            commands.set(index + 1, line);
            commands.set(index, line2);
            this.coupon.setCommands(commands);
            this.plugin.getCouponManager().saveCoupon(this.coupon);
            List<Object> indexList = new ArrayList();
            for (int i = 0; i < this.coupon.getCommands().size(); i++)
                indexList.add(Integer.valueOf(i));
            player.closeInventory();
            this.plugin.getInventoryManager().openInventory(player, new CommandsEditMenu(this.plugin, indexList, this.coupon, this.page));
        }
    }

    public void onLeftClick(Player player, Object object) {
        int index = ((Integer) object).intValue();
        List<String> commands = this.coupon.getCommands();
        if (index >= 1) {
            String line = commands.get(index);
            String line2 = commands.get(index - 1);
            commands.set(index - 1, line);
            commands.set(index, line2);
            this.coupon.setCommands(commands);
            this.plugin.getCouponManager().saveCoupon(this.coupon);
            List<Object> indexList = new ArrayList();
            for (int i = 0; i < commands.size(); i++)
                indexList.add(Integer.valueOf(i));
            player.closeInventory();
            if (indexList.size() == 0) {
                this.plugin.getInventoryManager().openInventory(player, new EditMenu(this.plugin, this.coupon));
            } else {
                this.plugin.getInventoryManager().openInventory(player, new CommandsEditMenu(this.plugin, indexList, this.coupon, this.page));
            }
        }
    }

    public void onMiddleClick(Player player, Object object) {
        int index = ((Integer) object).intValue();
        this.coupon.getCommands().remove(index);
        this.plugin.getCouponManager().saveCoupon(this.coupon);
        List<Object> indexList = new ArrayList();
        for (int i = 0; i < this.coupon.getCommands().size(); i++)
            indexList.add(Integer.valueOf(i));
        player.closeInventory();
        this.plugin.getInventoryManager().openInventory(player, new CommandsEditMenu(this.plugin, indexList, this.coupon, this.page));
    }
}
