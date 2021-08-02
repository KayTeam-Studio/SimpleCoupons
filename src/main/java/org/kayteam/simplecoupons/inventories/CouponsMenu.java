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

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kayteam.simplecoupons.SimpleCoupons;
import org.kayteam.simplecoupons.commands.Command_Edit;
import org.kayteam.simplecoupons.commands.Command_Give;
import org.kayteam.simplecoupons.coupon.Action;
import org.kayteam.simplecoupons.coupon.Coupon;
import org.kayteam.simplecoupons.util.Yaml;
import org.kayteam.simplecoupons.util.chat.ChatInput;
import org.kayteam.simplecoupons.util.inventory.MenuItem;
import org.kayteam.simplecoupons.util.inventory.PagesInventory;

import java.util.ArrayList;
import java.util.HashMap;

public class CouponsMenu extends PagesInventory {

    private SimpleCoupons plugin;

    public CouponsMenu(SimpleCoupons plugin) {
        super(plugin.getConfigYaml().getString("menu.list.title"), plugin.getConfigYaml().getInt("menu.list.rows"), new ArrayList<>(plugin.getCouponManager().getCoupons().values()));
        this.plugin = plugin;
    }

    @Override
    public ItemStack getPanel() {
        return plugin.getConfigYaml().getItemStack("menu.list.items.fill");
    }

    @Override
    public ItemStack getInformation() {
        return Yaml.replace(plugin.getConfigYaml().getItemStack("menu.list.items.information"), new String[][]{{"%coupons_amount%", String.valueOf(getObjects().size())}});
    }

    @Override
    public ItemStack getListedItem(Object objects) {
        if(objects!=null){
            Coupon coupon = (Coupon) objects;
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
                items = String.valueOf(new ArrayList<>(coupon.getActions().get(Action.ITEM)).size());
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
    public ItemStack getNext() {
        return plugin.getConfigYaml().getItemStack("menu.list.items.next-page");
    }

    @Override
    public ItemStack getPrevious() {
        return plugin.getConfigYaml().getItemStack("menu.list.items.previous-page");
    }

    @Override
    public ItemStack getClose() {
        return plugin.getConfigYaml().getItemStack("menu.list.items.close");
    }

    @Override
    public void onRightClick(Player player, Object object) {
        if(player.hasPermission("simplecoupons.edit")){
            Coupon coupon = (Coupon) object;
            if(coupon!=null){
                new Command_Edit(plugin).editCoupon(player, coupon);
            }
        }
    }
}
