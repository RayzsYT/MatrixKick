package de.rayzs.matrixkick.plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.Random;

public class MatrixKick extends JavaPlugin implements Listener, CommandExecutor {

    private String kickMessage;

    @Override
    public void onEnable() {
        final StringBuilder kickMessage = new StringBuilder();
        kickMessage.append((new Random().nextInt(100) >= 50 ? "b" : ":"));
        for(int i = 0; i < 4524; i++) kickMessage.append("b");
        this.kickMessage = "§a§k" + kickMessage + kickMessage + kickMessage;
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("mk").setExecutor(this); getCommand("mf").setExecutor(this);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
       Bukkit.getScheduler().scheduleSyncDelayedTask(this, () ->  LabyProtocol.sendCurrentPlayingGamemode(event.getPlayer(), true, "§2§lFuture§a§lMC §8» §2" + "WIR SIND ONLINE!"), 20);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onAsyncPlayerPreLogin(final AsyncPlayerPreLoginEvent event) {
        final String name = event.getName();
        if(Bukkit.getOfflinePlayer(name).isWhitelisted()) return;
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> Bukkit.broadcastMessage("§8[§2§lMATRIXKICK§8] §a§n" + name + "§7 got kicked cause of the §2M§2§ka§atr§ki§2x§7!"));
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, kickMessage);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onServerListPing(final ServerListPingEvent event) {
        event.setMotd("§2§lFuture§a§lMC §8| §7Dein §aMiniGames §7Server!§r\n         §8-> §2§l§nENDLICH ONLINE!");
        event.setMaxPlayers(event.getNumPlayers());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final StringBuilder message = new StringBuilder();
        for (String arg : args) message.append(arg).append(" ");
        if(command.getName().equalsIgnoreCase("mk"))
        LabyProtocol.sendCurrentPlayingGamemode((Player) sender, true, message.toString().replace("&", "§"));
        sender.sendMessage("§7Sent: §e" + message);
        return super.onCommand(sender, command, label, args);
    }
}