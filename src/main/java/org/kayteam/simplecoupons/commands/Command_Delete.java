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
import org.bukkit.inventory.ItemStack;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.coupon.Action;
import org.kayteam.simplecoupons.coupon.Coupon;
import org.kayteam.simplecoupons.util.Yaml;
import org.kayteam.simplecoupons.util.inventory.ConfirmInventory;

public class Command_Delete extends ConfirmInventory {

    private SimpleCoupons plugin;

    public Command_Delete(SimpleCoupons plugin) {
        this.plugin = plugin;
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
    public ItemStack getInformation(Object object) {
        if(object!=null){
            Coupon coupon = (Coupon) object;
            String money = "0";
            String xp = "0";
            String items = "0";
            String messages = "0";
            String commands = "0";
            try{
                money = String.valueOf(coupon.getActions().get(Action.MONEY).get(0));
            }catch (Exception e){}
            try{
                xp = String.valueOf(coupon.getActions().get(Action.XP).get(0));
            }catch (Exception e){}
            try{
                items = String.valueOf(coupon.getActions().get(Action.ITEM).size());
            }catch (Exception e){}
            try{
                messages = String.valueOf(coupon.getActions().get(Action.MESSAGE).size());
            }catch (Exception e){}
            try{
                commands = String.valueOf(coupon.getActions().get(Action.COMMAND).size());
            }catch (Exception e){}
            ItemStack itemStack = plugin.getConfigYaml().getItemStack("menu.list.items.coupon");
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
    public ItemStack getCancelButton() {
        return plugin.getConfigYaml().getItemStack("menu.delete.items.cancel");
    }

    @Override
    public ItemStack getAcceptButton() {
        return plugin.getConfigYaml().getItemStack("menu.delete.items.confirm");
    }

    @Override
    public void onAcceptClick(Player player, Object object) {
        Coupon coupon = (Coupon) object;
        if(coupon != null){
            if(plugin.getCouponManager().deleteCoupon(coupon)){
                plugin.getMessagesYaml().sendMessage(player, "edit.deleted", new String[][]{{"%coupon_name%", coupon.getName()}});
            }else{
                plugin.getMessagesYaml().sendMessage(player, "coupon.invalid");
            }
        }else{
            plugin.getMessagesYaml().sendMessage(player, "coupon.invalid");
        }
    }

    @Override
    public void onCancelClick(Player player, Object object) {
        player.closeInventory();
    }
}
