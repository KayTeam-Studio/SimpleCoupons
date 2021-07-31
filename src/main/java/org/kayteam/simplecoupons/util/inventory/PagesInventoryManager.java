package org.kayteam.simplecoupons.util.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class PagesInventoryManager implements Listener {

    private static final HashMap<String, PagesInventory> inventories = new HashMap<>();

    public void openInventory(Player player, PagesInventory pagesInventory) {
        PagesInventoryManager.inventories.put(player.getName(), pagesInventory);
        int rows = pagesInventory.getRows();
        int slots = 0;
        if (!(rows > 0 && rows < 5)) {
            rows = 1;
        }
        slots = (rows + 2) * 9;
        Inventory inventory = Bukkit.createInventory(null, slots, ChatColor.translateAlternateColorCodes('&', pagesInventory.getTitle()));
        // Set Panels
        ItemStack panel = pagesInventory.getPanel();
        int[] panelSlot = new int[] {0, 1, 2, 3, 5, 6, 7, 8, slots - 8, slots - 7, slots - 6, slots - 4, slots - 3, slots - 2};
        for (int i:panelSlot) {
            inventory.setItem(i, panel);
        }

        // Set Information
        inventory.setItem(4, pagesInventory.getInformation());
        // Set Previous Button
        inventory.setItem(slots - 9, pagesInventory.getPrevious());
        // Set Close Button
        inventory.setItem(slots - 5, pagesInventory.getClose());
        // Set Next Button
        inventory.setItem(slots - 1, pagesInventory.getNext());

        // Set List
        for (int index = 9; index < slots - 9; index++) {
            int realIndex = ((pagesInventory.getPage() * (rows * 9)) - (rows * 9)) + (index - 8);
            int listIndex = realIndex - 1;
            if (pagesInventory.getObjects().size() > listIndex) {
                inventory.setItem(index, pagesInventory.getListedItem(pagesInventory.getObjects().get(listIndex)));
            } else {
                inventory.setItem(index, pagesInventory.getListedItem(null));
            }
        }
        // Open Inventory
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (PagesInventoryManager.inventories.containsKey(player.getName())) {
            PagesInventory pagesInventory = PagesInventoryManager.inventories.get(player.getName());
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', pagesInventory.getTitle()))) {
                event.setCancelled(true);
                int slot = event.getRawSlot();
                int page = pagesInventory.getPage();
                int rows = (event.getInventory().getSize() - 18) / 9;
                if (slot > 8 && slot < (event.getInventory().getSize() - 9)) {
                    int realIndex = ((page * (rows * 9)) -  (rows * 9)) + (slot - 8);
                    int listIndex = realIndex - 1;
                    List<Object> objects = pagesInventory.getObjects();
                    Object object = null;
                    if (objects.size() > listIndex) {
                        object = objects.get(listIndex);
                    }
                    switch (event.getClick()) {
                        case LEFT:
                            pagesInventory.onLeftClick(player, object);
                            break;
                        case RIGHT:
                            pagesInventory.onRightClick(player, object);
                            break;
                        case MIDDLE:
                            pagesInventory.onMiddleClick(player, object);
                            break;
                        case SHIFT_LEFT:
                            pagesInventory.onShiftLeftClick(player, object);
                            break;
                        case SHIFT_RIGHT:
                            pagesInventory.onShiftRightClick(player, object);
                            break;
                    }
                } else if (slot == (event.getInventory().getSize() - 9)) {
                    if (page > 1) {
                        page = page - 1;
                        pagesInventory.setPage(page);
                        List<Object> objects = pagesInventory.getObjects();
                        for (int index = 9; index < (event.getInventory().getSize() - 9); index++) {
                            int realIndex = ((page * (rows * 9)) - (rows * 9)) + (index - 8);
                            int listIndex = realIndex - 1;
                            if (objects.size() > listIndex) {
                                event.getInventory().setItem(index, pagesInventory.getListedItem(objects.get(listIndex)));
                            } else {
                                event.getInventory().setItem(index, pagesInventory.getListedItem(null));
                            }
                        }
                    }
                } else if (slot == (event.getInventory().getSize() - 1)) {
                    List<Object> objects = pagesInventory.getObjects();
                    if (objects.size() > (page * (rows * 9))) {
                        page = page + 1;
                        pagesInventory.setPage(page);
                        for (int index = 9; index < (event.getInventory().getSize() - 9); index++) {
                            int realIndex = ((page * (rows * 9)) - (rows * 9)) + (index - 8);
                            int listIndex = realIndex - 1;
                            if (objects.size() > listIndex) {
                                event.getInventory().setItem(index, pagesInventory.getListedItem(objects.get(listIndex)));
                            } else {
                                event.getInventory().setItem(index, pagesInventory.getListedItem(null));
                            }
                        }
                    }
                } else if (slot == (event.getInventory().getSize() - 5)) {
                    player.closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (PagesInventoryManager.inventories.containsKey(player.getName())) {
            PagesInventoryManager.inventories.remove(player.getName());
        }
    }

}
