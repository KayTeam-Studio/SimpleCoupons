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
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.coupon.Coupon;
import org.kayteam.simplecoupons.util.Yaml;
import org.kayteam.simplecoupons.util.inventory.inventories.PagesInventory;

import java.util.ArrayList;
import java.util.List;

public class CommandsEditMenu extends PagesInventory {

    private final SimpleCoupons plugin;
    private Coupon coupon;

    public CommandsEditMenu(SimpleCoupons plugin, List<Object> index, Coupon coupon) {
        super(plugin.getConfigYaml().getString("menu.texts-menu.title").replaceAll("%path%", "commands"),
                plugin.getConfigYaml().getInt("menu.texts-menu.rows"),
                index);
        this.plugin = plugin;
        this.coupon = coupon;
    }

    @Override
    public ItemStack getListedItem(Object object) {
        if(object != null) {
            NBTItem nbtItem = new NBTItem(Yaml.replace(plugin.getConfigYaml().getItemStack("menu.texts-menu.items.commands"),
                    new String[][]{{"%command%", coupon.getCommands().get((int) object)}}));
            nbtItem.setInteger("command-index", (Integer) object);
            return nbtItem.getItem();
        }
        return null;
    }

    @Override
    public ItemStack getPanel(){
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
        int index = (int) object;
        List<String> commands = coupon.getCommands();
        if(index+1 < commands.size()) {
            String line = commands.get(index);
            String line2 = commands.get(index + 1);
            commands.set(index + 1, line);
            commands.set(index, line2);
            coupon.setCommands(commands);
            plugin.getCouponManager().saveCoupon(coupon);
            List<Object> indexList = new ArrayList<>();
            for (int i = 0; i < coupon.getCommands().size(); i++) {
                indexList.add(i);
            }
            player.closeInventory();
            plugin.getMenuInventoryManager().openInventory(player, new CommandsEditMenu(plugin, indexList, coupon));
        }
    }

    @Override
    public void onLeftClick(Player player, Object object) {
        int index = (int) object;
        List<String> commands = coupon.getCommands();
        if(index >= 1){
            String line = commands.get(index);
            String line2 = commands.get(index-1);
            commands.set(index-1, line);
            commands.set(index, line2);
            coupon.setCommands(commands);
            plugin.getCouponManager().saveCoupon(coupon);
            List<Object> indexList = new ArrayList<>();
            for(int i = 0; i < commands.size(); i++){
                indexList.add(i);
            }
            player.closeInventory();
            if(indexList.size() == 0){
                plugin.getMenuInventoryManager().openInventory(player, new EditMenu(plugin, coupon));
            }else{
                plugin.getMenuInventoryManager().openInventory(player, new CommandsEditMenu(plugin, indexList, coupon));
            }
        }
    }

    @Override
    public void onMiddleClick(Player player, Object object) {
        int index = (int) object;
        coupon.getCommands().remove(index);
        plugin.getCouponManager().saveCoupon(coupon);
        List<Object> indexList = new ArrayList<>();
        for(int i = 0; i < coupon.getCommands().size(); i++){
            indexList.add(i);
        }
        player.closeInventory();
        plugin.getMenuInventoryManager().openInventory(player, new CommandsEditMenu(plugin, indexList, coupon));
    }
}
