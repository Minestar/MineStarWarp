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

package de.minestar.MineStarWarp.commands.bank;

import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.minestar.MineStarWarp.Core;
import de.minestar.MineStarWarp.dataManager.BankManager;
import de.minestar.minestarlibrary.commands.ExtendedCommand;
import de.minestar.minestarlibrary.utils.ChatUtils;

public class BankListCommand extends ExtendedCommand {

    private BankManager bankManager;

    public BankListCommand(String syntax, String arguments, String node, BankManager bankManager) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Listet alle Banken auf";
        this.bankManager = bankManager;
    }

    @Override
    /**
     * Representing the command <br>
     * /bank list (#) <br>
     * This send all banks as a list
     * 
     * @param player
     *            Called the command
     * @param args
     *            (args[0] = pageNumber)
     */
    public void execute(String[] args, Player player) {

        int pageNumber = 1;

        if (args.length != 0 && !args[0].matches("\\d*")) {
            ChatUtils.printError(player, pluginName, "Benutze eine Zahl f�r die Seite!");
            return;
        } else if (args.length == 1)
            pageNumber = Integer.parseInt(args[0]);

        int maxPage = bankManager.getMaxPage();

        if (maxPage == 0) {
            ChatUtils.printError(player, pluginName, "Es existiert keine einzige Bank!");
            return;
        }

        if (pageNumber > maxPage || pageNumber <= 0) {
            ChatUtils.printError(player, pluginName, "Benutze nur Zahlen von 1 bis " + maxPage);
            return;
        }

        TreeMap<String, Location> banks = bankManager.getBanksForList(pageNumber);

        ChatUtils.printInfo(player, pluginName, ChatColor.WHITE, "------------------- Seite " + pageNumber + "/" + maxPage + " -------------------");
        showBankList(player, banks);
    }

    /**
     * This list all banks formatted to the command caller
     * 
     * @param player
     *            The command caller
     * @param warps
     *            The warps to present
     */
    private void showBankList(Player player, TreeMap<String, Location> banks) {

        for (Entry<String, Location> bank : banks.entrySet()) {
            String playerName = bank.getKey();
            Location loc = bank.getValue();

            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();
            ChatUtils.printInfo(player, playerName, ChatColor.AQUA, "'" + playerName + "' " + " @(" + x + ", " + y + ", " + z);
        }
    }
}
