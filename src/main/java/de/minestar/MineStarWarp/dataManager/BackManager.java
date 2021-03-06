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

package de.minestar.MineStarWarp.dataManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BackManager {

    private static final int MAX_BACKS = 10;

    private HashMap<String, LinkedList<Location>> backs;

    private HashSet<String> backUser = new HashSet<String>();

    public BackManager() {
        backs = new HashMap<String, LinkedList<Location>>();
    }

    public void addBack(Player player) {

        String pName = player.getName();
        // Player has used /back command a second before
        if (backUser.remove(pName))
            return;
        LinkedList<Location> l = backs.get(pName);
        if (l == null)
            l = new LinkedList<Location>();
        l.add(player.getLocation());
        if (l.size() >= MAX_BACKS)
            l.removeFirst();
        backs.put(pName, l);

    }

    public Location getBack(Player player) {
        String pName = player.getName();
        LinkedList<Location> l = backs.get(pName);
        return l == null || l.isEmpty() ? null : l.removeLast();
    }

    public void usedBack(Player player) {
        backUser.add(player.getName());
    }
}
