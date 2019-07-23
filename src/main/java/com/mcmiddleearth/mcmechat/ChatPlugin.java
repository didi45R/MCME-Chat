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
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import me.lucko.luckperms.LuckPerms;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Eriol_Eandur
 */
public class ChatPlugin extends JavaPlugin{

    @Getter
    static JavaPlugin instance;
    
    @Getter
    static boolean luckPerms = false;
    
    private FileConfiguration questionsConfig;
    
    /**
     *
     */
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        Path resource = new File(getClass().getClassLoader().getResource("questions.yml").getFile()).toPath();
        Path questions = new File(this.getDataFolder(), "questions.yml").toPath();
        try {
            Files.copy(resource, questions);
        } catch (FileAlreadyExistsException e) {
          //do nothing
        } catch (IOException ex) {
            Logger.getLogger(ChatPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        File questionsConfigFile = new File(getDataFolder(), "questions.yml");
        questionsConfig = new YamlConfiguration();
        try {
            questionsConfig.load(questionsConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            //handle the error somehow
        }
        
        if(getConfig().getBoolean("useLuckPerms")) {
            try {
                LuckPerms.getApi();
                luckPerms = true;
            } catch(IllegalStateException e) {
                Logger.getGlobal().info("LuckPerms not found, using permission attachments.");
            }
        }
        getServer().getPluginManager().registerEvents(new AfkListener(), this);
        instance = this;
        
        getServer().getPluginManager().registerEvents(new PlayerChatHandler(), this);
    }
    
    /**
     *
     * @param input
     * @return
     */
    public String getAnswer(String input) {   
    input = input.toLowerCase();
    
    input = input.replaceAll("\\?", "");
    input = input.replaceAll("\\s+", "_");
    input = input.trim();
    String answer = questionsConfig.getString(input);
    return answer;
}
    
    /**
     *
     * @return
     */
    public static String getTabColorPermission() {
        return instance.getConfig().getString("playerTabList.tabColorPermission");
    }
    
    /**
     *
     * @return
     */
    public static String getAfkColor() {
        return instance.getConfig().getString("playerTabList.afkColor");
    }
    
  
}
