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

package org.kayteam.simplecoupons.coupon;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Coupon {

    private String name;
    private ItemStack couponItem;
    private int money = 0;
    private int xp = 0;
    private List<String> messages = new ArrayList<>();
    private List<String> commands = new ArrayList<>();
    private List<ItemStack> items = new ArrayList<>();

    public Coupon(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setCouponItem(ItemStack couponItem){
        this.couponItem = couponItem;
    }

    public ItemStack getCouponItem() {
        return couponItem;
    }

    public int getMoney() {
        return money;
    }

    public int getXp() {
        return xp;
    }

    public List<String> getMessages() {
        return messages;
    }

    public List<String> getCommands() {
        return commands;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }
}
