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
package com.mcmiddleearth.mcmechat.listener;

import com.mcmiddleearth.mcmechat.ChatPlugin;
import com.mcmiddleearth.mcmechat.util.TabUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

/**
 *
 * @author Eriol_Eandur
 */
public class AfkListener implements Listener{

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void afkStatusChange(AfkStatusChangeEvent event) {
//Logger.getGlobal().info("value: "+event.getValue());
//event.getAffected().getBase().sendMessage("value: "+event.getValue());
        if(event.getValue()) {
            addAfkAttachment(event.getAffected().getBase());
        } else {
            removeAfkAttachment(event.getAffected().getBase());
        }
    }
    
    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        attachments.remove(event.getPlayer().getUniqueId());
    }
    private static Map<UUID, PermissionAttachment> attachments = new HashMap<>();
    
    private static void addAfkAttachment(Player player) {
        PermissionAttachment attachment = attachments.get(player.getUniqueId());
        if(attachment==null) {
            attachment = player.addAttachment(ChatPlugin.getInstance(), 
                                        ChatPlugin.getTabColorPermission()
                                       +TabUtil.getTabColor(ChatPlugin.getAfkColor()), true);
//Logger.getGlobal().info("add attachment: "+ChatPlugin.getTabColorPermission()
//                                       +TabUtil.getTabColor(ChatPlugin.getAfkColor()));
            for(ChatColor color : ChatColor.values()) {
                if(!color.name().equals(ChatPlugin.getAfkColor())) {
                    attachment.setPermission(ChatPlugin.getTabColorPermission()+TabUtil.getTabColor(color.name()), false);
//Logger.getGlobal().info("set permission: "+ChatPlugin.getTabColorPermission()+TabUtil.getTabColor(color.name()));
                }
            }
            attachments.put(player.getUniqueId(), attachment);
            //player.recalculatePermissions();
        }
    }
  
    public static void removeAfkAttachment(Player player) {
        /*for(PermissionAttachmentInfo info: player.getEffectivePermissions()) {
            PermissionAttachment attachment = info.getAttachment();
Logger.getGlobal().info("attachment by: "+(attachment!=null?attachment.getPlugin():"parent"));
            if(attachment!=null && attachment.getPlugin().equals(ChatPlugin.getInstance())) {
                player.removeAttachment(attachment);
Logger.getGlobal().info("removed");
            }
        }*/
        PermissionAttachment attachment = attachments.get(player.getUniqueId());
        if(attachment!=null) {
            player.removeAttachment(attachment);
            attachments.remove(player.getUniqueId());
            //player.recalculatePermissions();
        }
        /*PermissionAttachment attachment = getAfkAttachment(player);
        if(attachment!=null) {
            player.removeAttachment(attachment);
            player.recalculatePermissions();
        }*/
  }
    
    /*private static PermissionAttachment getAfkAttachment(Player player) {
        for(PermissionAttachmentInfo info: player.getEffectivePermissions()) {
            PermissionAttachment attachment = info.getAttachment();
            if(attachment!=null && attachment.getPlugin().equals(ChatPlugin.getInstance())) {
                return attachment;
            }
        }
        return null;
    }*/
}
