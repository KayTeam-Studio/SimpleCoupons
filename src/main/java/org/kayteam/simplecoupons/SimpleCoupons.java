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

package org.kayteam.simplecoupons;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.kayteam.simplecoupons.commands.Command_Delete;
import org.kayteam.simplecoupons.commands.Command_SimpleCoupons;
import org.kayteam.simplecoupons.coupon.CouponManager;
import org.kayteam.simplecoupons.inventories.CouponsMenu;
import org.kayteam.simplecoupons.listeners.CouponUse;
import org.kayteam.simplecoupons.listeners.PlayerInteract;
import org.kayteam.simplecoupons.util.*;
import org.kayteam.simplecoupons.util.chat.ChatInputManager;
import org.kayteam.simplecoupons.util.interact.InteractManager;
import org.kayteam.simplecoupons.util.inventory.MenuInventoryManager;
import org.kayteam.simplecoupons.util.inventoryitems.InventoryItemsManager;

public class SimpleCoupons extends JavaPlugin {

    private CouponManager couponManager = new CouponManager(this);
    private CommandManager commandManager = new CommandManager(this);
    private static Economy econ = null;
    private Yaml config = new Yaml(this, "config");
    private Yaml messages = new Yaml(this, "messages");

    @Override
    public void onEnable() {
        KayTeam.sendBrandMessage(this, "&aEnabled");
        registerFiles();
        setupEconomy();
        registerCommands();
        couponManager.loadCoupons();
        couponsMenu = new CouponsMenu(this);
        commandDelete = new Command_Delete(this);
        registerListeners();
        enablebStats();
        updateChecker = new UpdateChecker(this, 95021);
        if (updateChecker.getUpdateCheckResult().equals(UpdateChecker.UpdateCheckResult.OUT_DATED)) {
            updateChecker.sendOutDatedMessage(getServer().getConsoleSender());
        }
    }

    private UpdateChecker updateChecker;
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    private Command_Delete commandDelete;
    public Command_Delete getCommandDelete() {
        return commandDelete;
    }

    private final InventoryItemsManager inventoryItemsManager = new InventoryItemsManager();
    public InventoryItemsManager getInventoryItemsManager() {
        return inventoryItemsManager;
    }

    private final InteractManager interactManager = new InteractManager();
    public InteractManager getInteractManager() {
        return interactManager;
    }

    private final ChatInputManager chatInputManager = new ChatInputManager();
    public ChatInputManager getChatInputManager() {
        return chatInputManager;
    }

    private final MenuInventoryManager menuInventoryManager = new MenuInventoryManager();
    public MenuInventoryManager getMenuInventoryManager() {
        return menuInventoryManager;
    }

    private CouponsMenu couponsMenu;
    public CouponsMenu getCouponsMenu() {
        return couponsMenu;
    }

    private void registerFiles() {
        try{
            if(Yaml.getFolderFiles(getDataFolder()+"/coupons").size()==0){
                Yaml Example = new Yaml(this, "coupons", "Example");
                Example.registerFileConfiguration();
            }
        }catch (Exception e){
            Yaml Example = new Yaml(this, "coupons", "Example");
            Example.registerFileConfiguration();
        }
        config.registerFileConfiguration();
        messages.registerFileConfiguration();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);
        getServer().getPluginManager().registerEvents(new CouponUse(this), this);
        getServer().getPluginManager().registerEvents(menuInventoryManager, this);
        getServer().getPluginManager().registerEvents(chatInputManager, this);
        getServer().getPluginManager().registerEvents(interactManager, this);
        getServer().getPluginManager().registerEvents(inventoryItemsManager, this);
    }

    private void registerCommands() {
        getCommand("simplecoupons").setExecutor(new Command_SimpleCoupons(this));
    }

    private void enablebStats() {
        int pluginId = 12232;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SingleLineChart("coupons", () -> {
            int coupons = 0;
            coupons = getCouponManager().getCoupons().keySet().size();
            return coupons;
        }));
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public CouponManager getCouponManager(){
        return couponManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public Yaml getMessagesYaml() {
        return messages;
    }

    public Yaml getConfigYaml() {
        return config;
    }

    @Override
    public void onDisable() {
        KayTeam.sendBrandMessage(this, "&cDisabled");
    }
}
