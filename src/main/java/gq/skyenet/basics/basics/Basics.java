package gq.skyenet.basics.basics;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

public final class Basics extends JavaPlugin {
    double currentVersion = 0.1;
    @Override
    public void onEnable() {
        // Plugin startup logic
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
}
