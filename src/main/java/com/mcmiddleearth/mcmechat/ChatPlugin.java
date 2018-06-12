/*
 * Copyright (C) 2018 MCME
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mcmiddleearth.mcmechat;

import com.mcmiddleearth.mcmechat.listener.AfkListener;
import java.util.logging.Logger;
import lombok.Getter;
import me.lucko.luckperms.LuckPerms;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;

/**
 *
 * @author Eriol_Eandur
 */
public class ChatPlugin extends JavaPlugin{

    @Getter
    static JavaPlugin instance;
    
    @Getter
    static boolean luckPerms = false;
    
    @Override
    public void onEnable() {
        /*Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        User user = ess.getUser("Eriol_Eandur");
        user.isAfk();*/
        this.saveDefaultConfig();
        if(getConfig().getBoolean("useLuckPerms")) {
            try {
                LuckPerms.getApi();
                luckPerms = true;
            } catch(IllegalStateException e) {
                Logger.getGlobal().info("LuckPerms not found, using permission attachments.");
            }
        }
        getServer().getPluginManager().registerEvents(new AfkListener(), this);
//Logger.getGlobal().info("AfkListener registered.");
        instance = this;
    }
    
    public static String getTabColorPermission() {
        return instance.getConfig().getString("playerTabList.tabColorPermission");
    }
    
    public static String getAfkColor() {
        return instance.getConfig().getString("playerTabList.afkColor");
    }
    
  
}
