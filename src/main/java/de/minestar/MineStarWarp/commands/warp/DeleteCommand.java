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

import org.bukkit.entity.Player;

import de.minestar.MineStarWarp.Core;
import de.minestar.MineStarWarp.Warp;
import de.minestar.MineStarWarp.dataManager.WarpManager;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class DeleteCommand extends AbstractCommand {

    private WarpManager wManager;

    public DeleteCommand(String syntax, String arguments, String node, WarpManager wManager) {
        super(Core.NAME, syntax, arguments, node);
        this.wManager = wManager;
        
        this.description = "Loescht einen deiner Warps";
    }

    @Override
    /**
     * Representing the command <br>
     * /warp delete <br>
     * If the player is the owner or an admin, it deletes the warp
     * 
     * @param player
     *            Called the command
     * @param split
     *            args[0] is the warp name
     */
    public void execute(String[] args, Player player) {

        String warpName = args[0];
        if (!wManager.isWarpExisting(warpName)) {
            PlayerUtils.sendError(player, pluginName, "'" + warpName + "' exisiert nicht!");
            return;
        }
        Warp warp = wManager.getWarp(warpName);
        if (warp.canEdit(player))
            wManager.deleteWarp(player, warpName);
        else
            PlayerUtils.sendError(player, pluginName, "Du darfst den Warp '" + warpName + "' nicht l�schen!");

    }
}
