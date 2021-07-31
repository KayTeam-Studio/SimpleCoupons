package org.kayteam.simplecoupons.util.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class PagesInventory {

    private final String title;
    private final int rows;
    private int page;
    private List<Object> objects;

    public PagesInventory(String title, int rows, List<Object> objects) {
        this.title = title;
        this.rows = rows;
        page = 1;
        this.objects = objects;
    }

    public String getTitle() {
        return title;
    }
    public int getRows() {
        return rows;
    }

    public void setPage(int page) {
        this.page = page;
    }
    public int getPage() {
        return page;
    }

    public List<Object> getObjects() {
        return objects;
    }



    public abstract ItemStack getListedItem(Object object);
    public abstract ItemStack getPanel();
    public abstract ItemStack getInformation();
    public abstract ItemStack getPrevious();
    public abstract ItemStack getNext();
    public abstract ItemStack getClose();



    public void onLeftClick(Player player, Object object) {};
    public void onRightClick(Player player, Object object) {};
    public void onMiddleClick(Player player, Object object) {};
    public void onShiftRightClick(Player player, Object object) {};
    public void onShiftLeftClick(Player player, Object object) {};


}