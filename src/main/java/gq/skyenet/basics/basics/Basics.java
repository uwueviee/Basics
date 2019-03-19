package gq.skyenet.basics.basics;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.nio.file.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.List;

public final class Basics extends JavaPlugin {

    double currentVersion = 0.1;

    private String pluginFolder = getDataFolder().getAbsolutePath();
    private JSONObject pluginSettings = new JSONObject();

    @Override
    public void onEnable() {
        // Plugin startup logic
        pluginFolderVert(pluginFolder);
        pluginSettingsLoad(pluginSettings);
        getLogger().info("Loaded version " + currentVersion);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Shutting down...");
        pluginSettingsSave(pluginSettings);
        getLogger().info("Saved plugin settings");
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        // Lemme try and explain the spawn command code
        if (cmd.getName().equalsIgnoreCase("spawn")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                // Get the array with the spawn location from the settings object
                String[] spawnLocationArray = pluginSettings.get("spawnLocation").toString().split(",");
                // Remove the [] from the array also get the world name and yaw
                String worldString = spawnLocationArray[0].replaceAll("\\p{P}","");
                String yawString = spawnLocationArray[5].replaceAll("\\p{P}","");
                // Grab the coords from the array
                double x = Double.valueOf(spawnLocationArray[1]);
                double y = Double.valueOf(spawnLocationArray[2]);
                double z = Double.valueOf(spawnLocationArray[3]);
                float yaw = Float.valueOf(yawString);
                float pitch = Float.valueOf(spawnLocationArray[4]);
                sender.sendMessage("[Basics] Sending you to spawn!");
                // Create new location and then teleport the player to that location
                Location spawnLocation = new Location(getServer().getWorld(worldString), x, y, z, yaw, pitch);
                player.teleport(spawnLocation);
                return true;
            } else {
                sender.sendMessage("[Basics] You must be a player to run this command!");
                return true;
            }
        } else if (cmd.getName().equalsIgnoreCase("setspawn")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                // Manually create the array, we don't need to append as the positions should be the same each time but this is bad practice
                String[] spawnLocationArray = {"","","","","",""};
                // Get the actual world name without the "CraftWorld" stuff
                String spawnWorld = player.getWorld().getName().replaceAll("^\\[CraftWorld^\\{name=", "").replaceAll("^\\}", "");
                // Put the location in the array
                spawnLocationArray[0] = spawnWorld;
                spawnLocationArray[1] = String.valueOf(player.getLocation().getX());
                spawnLocationArray[2] = String.valueOf(player.getLocation().getY());
                spawnLocationArray[3] = String.valueOf(player.getLocation().getZ());
                spawnLocationArray[4] = String.valueOf(player.getLocation().getPitch());
                spawnLocationArray[5] = String.valueOf(player.getLocation().getYaw());
                // Save it to the settings object and then save it to the settings file
                pluginSettings.put("spawnLocation", "" + Arrays.toString(spawnLocationArray) + "");
                pluginSettingsSave(pluginSettings);
                sender.sendMessage("[Basics] Spawn set!");
                return true;
            } else {
                sender.sendMessage("[Basics] You must be a player to run this command!");
                return true;
            }
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
        } else if (cmd.getName().equalsIgnoreCase("basicsreload")) {
            pluginSettingsLoad(pluginSettings);
            sender.sendMessage("[Basics] Settings loaded from file!");
            return true;
        } else if (cmd.getName().equalsIgnoreCase("clearlag")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                World world = player.getWorld();
                List<Entity> entList = ((World) world).getEntities();
                for (Entity current : entList) {
                    if (current instanceof Item) {
                        current.remove();
                    }
                }
                sender.sendMessage("[Basics] Cleared all items.");
                return true;
            } else {
                sender.sendMessage("[Basics] You must be a player to run this command!");
                return true;
            }
        }
        return true;
    }

    public void pluginSettingsLoad(JSONObject obj) {
        // Load plugin settings.
        JSONParser parser = new JSONParser();
        try {
            pluginSettings = (JSONObject) parser.parse(new FileReader(pluginFolder + "/settings.json"));
        } catch (IOException | ParseException ex) {
            System.out.println (ex.toString());
        }
    }

    public void pluginSettingsSave(JSONObject obj) {
        // Save plugin settings.
        try {
            Files.write(Paths.get(pluginFolder + "/settings.json"), obj.toJSONString().getBytes());
        } catch (IOException ex) {
            System.out.println (ex.toString());
        }
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
