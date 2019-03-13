package gq.skyenet.basics.basics;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.simple.JSONObject;
import java.io.IOException;

public final class Basics extends JavaPlugin {

    double currentVersion = 0.1;

    private String pluginFolder = getDataFolder().getAbsolutePath();
    private JSONObject pluginSettings = new JSONObject();

    @Override
    public void onEnable() {
        // Plugin startup logic
        pluginFolderVert(pluginFolder);
        getLogger().info("Loaded version " + currentVersion);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (cmd.getName().equalsIgnoreCase("spawn")) {
            return true;
        } else if (cmd.getName().equalsIgnoreCase("setspawn")) {
            return true;
        } else if (cmd.getName().equalsIgnoreCase("ping")) {
            sender.sendMessage("Pong!");
            return true;
        } else if (cmd.getName().equalsIgnoreCase("tickrate")) {
            double tps = Lag.getTPS();
            double lag = Math.round((1.0D - tps / 20.0D) * 100.0D);
            sender.sendMessage("[Basics] Current TPS: " + Lag.getTPS());
            sender.sendMessage("[Basics] Lag percentage is %" + lag);
            return true;
        } else if (cmd.getName().equalsIgnoreCase("basics")) {
            sender.sendMessage("This server is currently running Basics version " + currentVersion);
            sender.sendMessage("For commands do /help Basics");
            return true;
        }
        return true;
    }

    public void pluginFolderVert(String pluginFolder) {
        // So like, this probably isn't the best way to do it but I'm quite bad at Java so this is how I do stuff like this
        if (Files.isDirectory(Paths.get(pluginFolder))) {
            if (!Files.exists(Paths.get(pluginFolder+"/settings.json"))) {
                // If the settings file doesn't exist but the folder exists, we just recreate it here.
                try {
                    Files.createDirectories(Paths.get(pluginFolder));
                } catch (IOException ex) {
                    System.out.println (ex.toString());
                }
            }
        } else {
            // If the folder is missing, assume the worst and that everything is missing. Make the folder and settings file.
            try {
                Files.createDirectories(Paths.get(pluginFolder));
                Files.write(Paths.get(pluginFolder + "/settings.json"), "{}".getBytes());
            } catch (IOException ex) {
                System.out.println (ex.toString());
            }
        }
    }
}
