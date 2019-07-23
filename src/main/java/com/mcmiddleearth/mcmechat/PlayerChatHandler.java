/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.mcmechat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 *
 * @author Didi45
 */
public class PlayerChatHandler implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        //handle your event here
        //for example:
        String answer = ((ChatPlugin) ChatPlugin.getInstance()).getAnswer(event.getMessage());
        if (event.getPlayer() != null && answer != null) {
            event.getPlayer().sendMessage(answer);
        }
    }
}