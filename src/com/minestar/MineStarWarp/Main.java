/*
 * Copyright (C) 2011 MineStar.de 
 * 
 * This file is part of MineStarWarp.
 * 
 * MineStarWarp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * MineStarWarp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MineStarWarp.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.minestar.MineStarWarp;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.minestar.MineStarWarp.commands.CommandList;
import com.minestar.MineStarWarp.dataManager.BankManager;
import com.minestar.MineStarWarp.dataManager.ConnectionManager;
import com.minestar.MineStarWarp.dataManager.DatabaseManager;
import com.minestar.MineStarWarp.dataManager.HomeManager;
import com.minestar.MineStarWarp.dataManager.SpawnManager;
import com.minestar.MineStarWarp.dataManager.WarpManager;
import com.minestar.MineStarWarp.listeners.PlayerRespawnListener;

public class Main extends JavaPlugin {

    private static Logger log = Logger.getLogger("Minecraft");

    private static final String PLUGIN_NAME = "MineStarWarp";

    public static WarpManager warpManager;
    public static HomeManager homeManager;
    public static SpawnManager spawnManager;
    public static BankManager bankManager;
    private DatabaseManager dbManager;

    private CommandList commandList;

    public static Configuration config;

    public static void writeToLog(String info) {

        log.info("[" + PLUGIN_NAME + "]:" + info);
    }

    public void onDisable() {
        ConnectionManager.closeConnection();
        warpManager = null;
        homeManager = null;
        spawnManager = null;
        dbManager = null;
        bankManager = null;
        commandList = null;
        System.gc();
        writeToLog("disabled");
    }

    public void onEnable() {

        loadConfig();
        commandList = new CommandList(getServer());

        if (ConnectionManager.initialize()) {
            dbManager = new DatabaseManager(getServer());
            warpManager = new WarpManager(dbManager, config);
            homeManager = new HomeManager(dbManager);
            spawnManager = new SpawnManager(dbManager);
            bankManager = new BankManager(dbManager);

            getServer().getPluginManager().registerEvent(Type.PLAYER_RESPAWN,
                    new PlayerRespawnListener(), Priority.Normal, this);

            writeToLog("enabled");
        }
        else {
            writeToLog("Can't connect to database!");
        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {
        commandList.handleCommand(sender, label, args);
        return true;
    }

    /**
     * Load the properties from the configFile. If the configFile not exists it
     * create ones
     */
    public void loadConfig() {
        File pluginDir = getDataFolder();
        if (!pluginDir.exists())
            pluginDir.mkdirs();
        File configFile = new File(pluginDir.getAbsolutePath().concat(
                "/config.yml"));
        config = new Configuration(new File(pluginDir.getAbsolutePath().concat(
                "/config.yml")));
        if (!configFile.exists())
            createConfig();
        else
            config.load();
    }

    /**
     * Creates a config and use default values for it. They are stored in a yml
     * format.
     */
    public void createConfig() {
        config.setProperty("warps.default", 0);
        config.setProperty("warps.proble", 2);
        config.setProperty("warps.free", 5);
        config.setProperty("warps.pay", 9);
        config.setProperty("warps.warpsPerPage", 8);
        config.save();
    }
}
