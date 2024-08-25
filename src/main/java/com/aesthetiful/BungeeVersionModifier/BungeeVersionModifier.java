package com.aesthetiful.BungeeVersionModifier;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.api.plugin.Listener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class BungeeVersionModifier extends Plugin implements Listener {

    private Configuration config;

    @Override
    public void onEnable() {
		
	if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        // Save the default config.yml
        saveDefaultConfig();
		
        // Load the configuration file
        loadConfig();

        // Register the event listener
        getProxy().getPluginManager().registerListener(this, this);
    }

    private void loadConfig() {
        // Create the plugin's data folder if it doesn't exist
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        // Define the configuration file
        File configFile = new File(getDataFolder(), "config.yml");

        // Copy the default config.yml from resources to the plugin's data folder if it doesn't exist
        if (!configFile.exists()) {
            try {
                Files.copy(getResourceAsStream("config.yml"), configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Load the configuration
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private void saveDefaultConfig() {
        File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            try {
                Files.copy(getResourceAsStream("config.yml"), configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                getLogger().severe("Could not save default config.yml: " + e.getMessage());
            }
        }
    }

    @EventHandler
    public void onProxyPing(ProxyPingEvent event) {
        // Get the server version from the config.yml file, or use a default value if not specified
        String version = config.getString("server-version", "1.8.x-1.21.x (BungeeCord)");
        event.getResponse().getVersion().setName(version);
    }
}
