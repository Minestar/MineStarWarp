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

package de.minestar.MineStarWarp.commands.warp;

import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.MineStarWarp.Core;
import de.minestar.MineStarWarp.Warp;
import de.minestar.minestarlibrary.commands.Command;
import de.minestar.minestarlibrary.utils.ChatUtils;

public class GuestListCommand extends Command {
    public GuestListCommand(String syntax, String arguments, String node) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Zeigt die G�ste eines Warps an.";
    }

    @Override
    /**
     * Representing the command <br>
     * /warp guestlist <br>
     * Show the guests of a warp.
     */
    public void execute(String[] args, Player player) {

        String warpName = args[0];
        // Warp is not existing
        if (!Core.warpManager.isWarpExisting(warpName)) {
            ChatUtils.printError(player, pluginName, "'" + warpName + "' existiert nicht!");
            return;
        }
        Warp warp = Core.warpManager.getWarp(warpName);
        // Player isn't allowed to see the guestlist
        if (!warp.canEdit(player)) {
            ChatUtils.printError(player, pluginName, "Du darfst die G�steliste f�r '" + warpName + "' nicht sehen!");
            return;
        }

        HashSet<String> guests = warp.getGuests();
        // guests == null -> warp is public
        if (guests == null) {
            ChatUtils.printError(player, pluginName, "Der Warp '" + warpName + "' ist ein �ffentlicher Warp und hat somit keine G�steliste!");
            return;
        }

        // warp has no guests
        if (guests.isEmpty()) {
            ChatUtils.printError(player, pluginName, "Der Warp '" + warpName + "' hat keine G�ste!");
            return;
        }

        ChatUtils.printSuccess(player, pluginName, "G�ste von '" + warpName + "': " + formatGuestList(guests));
    }

    private String formatGuestList(HashSet<String> guests) {
        StringBuilder result = new StringBuilder(50);
        for (String temp : guests) {
            result.append(temp);
            result.append(", ");
        }
        return ChatColor.AQUA + result.substring(0, result.length() - 2);
    }
}