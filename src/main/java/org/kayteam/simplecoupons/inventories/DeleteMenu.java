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

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.coupon.Coupon;
import org.kayteam.simplecoupons.util.Yaml;
import org.kayteam.simplecoupons.util.inventory.inventories.ConfirmInventory;

public class DeleteMenu extends ConfirmInventory {

    private SimpleCoupons plugin;
    private Player player;
    private Coupon coupon;

    public DeleteMenu(SimpleCoupons plugin, Player player, Coupon coupon) {
        this.plugin = plugin;
        this.player = player;
        this.coupon = coupon;
    }

    @Override
    public String getTitle() {
        return plugin.getConfigYaml().getString("menu.delete.title");
    }

    @Override
    public ItemStack getPanel() {
        return plugin.getConfigYaml().getItemStack("menu.list.items.fill");
    }

    @Override
    public ItemStack getInformation() {
        if(coupon!=null){
            String money = String.valueOf(coupon.getMoney());
            String xp = String.valueOf(coupon.getXp());
            String items = String.valueOf(coupon.getItems().size());
            String messages = String.valueOf(coupon.getMessages().size());
            String commands = String.valueOf(coupon.getCommands().size());
            ItemStack itemStack = plugin.getConfigYaml().getItemStack("menu.delete.items.coupon");
            itemStack.setType(coupon.getCouponItem().getType());
            return Yaml.replace(itemStack,
                    new String[][]{
                            {"%coupon_name%", coupon.getName()},
                            {"%coupon_money%", money},
                            {"%coupon_xp%", xp},
                            {"%coupon_items%", items},
                            {"%coupon_messages%", messages},
                            {"%coupon_commands%", commands}});
        }else{
            return null;
        }
    }

    @Override
    public ItemStack getAccept() {
        return plugin.getConfigYaml().getItemStack("menu.delete.items.confirm");
    }

    @Override
    public ItemStack getCancel() {
        return plugin.getConfigYaml().getItemStack("menu.delete.items.cancel");
    }

    @Override
    public void onAccept() {
        if(coupon != null){
            if(plugin.getCouponManager().deleteCoupon(coupon)){
                player.closeInventory();
                plugin.getMessagesYaml().sendMessage(player, "edit.deleted", new String[][]{{"%coupon_name%", coupon.getName()}});
            }else{
                plugin.getMessagesYaml().sendMessage(player, "coupon.invalid");
            }
        }else{
            plugin.getMessagesYaml().sendMessage(player, "coupon.invalid");
        }
    }

    @Override
    public void onCancel() {
        player.closeInventory();
    }
}
