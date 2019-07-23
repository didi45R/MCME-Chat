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
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.DataMutateResult;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

/**
 *
 * @author Eriol_Eandur
 */
public class AfkListener implements Listener{
    

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void afkStatusChange(AfkStatusChangeEvent event) {
//Logger.getGlobal().info("value: "+event.getValue());
//event.getAffected().getBase().sendMessage("value: "+event.getValue());
//Logger.getGlobal().info("LuckPerms found: "+ChatPlugin.isLuckPerms());
        if(event.getValue()) {
            setAfk(event.getAffected().getBase());
        } else {
            removeAfk(event.getAffected().getBase());
        }
    }
    
    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        if(ChatPlugin.isLuckPerms()) {
            removeAfkLuckPerms(event.getPlayer());
        } else {
            attachments.remove(event.getPlayer().getUniqueId());
        }
    }
     
    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        if(ChatPlugin.isLuckPerms()) {
            removeAfkLuckPerms(event.getPlayer());
        }
    }
     
    private static void setAfk(Player player) {
        if(ChatPlugin.isLuckPerms()) {
            setAfkLuckPerms(player);
        } else {
            setAfkAttachment(player);
        }
    }
    
    private static void removeAfk(Player player) {
        if(ChatPlugin.isLuckPerms()) {
            removeAfkLuckPerms(player);
        } else {
            removeAfkAttachment(player);
        }
    }
    
    private static void setAfkLuckPerms(Player player) {
        LuckPermsApi lpApi = LuckPerms.getApi();
        Node afkNode = lpApi.buildNode(ChatPlugin.getTabColorPermission()
                                       +TabUtil.getTabColor(ChatPlugin.getAfkColor()))
                            .setValue(true)
                            .build();
       
        User user = lpApi.getUser(player.getUniqueId());
        if(user == null) {
            return;
        }
        DataMutateResult result = user.setPermission(afkNode);
        for(ChatColor color : ChatColor.values()) {
            if(!color.name().equals(ChatPlugin.getAfkColor())) {
                Node tabColorNode = lpApi.buildNode(ChatPlugin.getTabColorPermission()
                                       +TabUtil.getTabColor(color.name())).setValue(false).build();
                user.setPermission(tabColorNode);
            }
        }
        lpApi.getStorage().saveUser(user); 
        user.refreshCachedData();
    }
  
    public static void removeAfkLuckPerms(Player player) {
        LuckPermsApi lpApi = LuckPerms.getApi();
        User user = lpApi.getUser(player.getUniqueId());
        if(user == null) {
            return;
        }
        for(ChatColor color : ChatColor.values()) {
            Node tabColorNode = lpApi.buildNode(ChatPlugin.getTabColorPermission()
                                   +TabUtil.getTabColor(color.name())).build();
            user.unsetPermission(tabColorNode);
        }
        lpApi.getStorage().saveUser(user);        
        user.refreshCachedData();
    }
 
    private static Map<UUID, PermissionAttachment> attachments = new HashMap<>();

    private static void setAfkAttachment(Player player) {
        //showAttachments(player);
        PermissionAttachment attachment = attachments.get(player.getUniqueId());
        if(attachment==null) {
            attachment = player.addAttachment(ChatPlugin.getInstance());
            attachments.put(player.getUniqueId(), attachment);
        }
        attachment.setPermission(ChatPlugin.getTabColorPermission()
                                       +TabUtil.getTabColor(ChatPlugin.getAfkColor()), true);
        for(ChatColor color : ChatColor.values()) {
            if(!color.name().equals(ChatPlugin.getAfkColor())) {
                attachment.setPermission(ChatPlugin.getTabColorPermission()+TabUtil.getTabColor(color.name()), false);
            }
            //player.recalculatePermissions();
        }
    }
  
    public static void removeAfkAttachment(Player player) {
        PermissionAttachment attachment = attachments.get(player.getUniqueId());
        if(attachment!=null) {
            player.removeAttachment(attachment);
            attachments.remove(player.getUniqueId());
            attachment = player.addAttachment(ChatPlugin.getInstance());
            for(ChatColor color : ChatColor.values()) {
                attachment.unsetPermission(ChatPlugin.getTabColorPermission()+TabUtil.getTabColor(color.name()));
            }
            attachments.put(player.getUniqueId(), attachment);
            player.recalculatePermissions();
            //showAttachments(player);
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
