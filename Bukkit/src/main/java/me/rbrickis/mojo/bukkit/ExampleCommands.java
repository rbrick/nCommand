package me.rbrickis.mojo.bukkit;

import me.rbrickis.mojo.annotations.Command;
import me.rbrickis.mojo.annotations.Default;
import me.rbrickis.mojo.annotations.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

public class ExampleCommands {

    Map<UUID, UUID> replyMap = new HashMap<>();

    @Command(aliases = "hello")
    @me.rbrickis.mojo.bukkit.annotations.Player
    public void helloWorld(Player player) {
        player.sendMessage(ChatColor.GOLD + "Hello, " + player.getName());
    }

    @Command(aliases = {"broadcast", "bc"})
    public void broadcast(CommandSender sender, @Text @Default("&6Hello World!") String message) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    @Command(aliases = {"message", "msg", "tell"})
    @me.rbrickis.mojo.bukkit.annotations.Player
    public void message(Player sender, Player receiver, @Text String message) {
        if (receiver == null) {
            sender.sendMessage(ChatColor.RED + "Could not find player!");
        } else {
            replyMap.put(sender.getUniqueId(), receiver.getUniqueId());
            replyMap.put(receiver.getUniqueId(), sender.getUniqueId());

            sender.sendMessage(ChatColor.GRAY + "(To " + receiver.getDisplayName() + ChatColor.GRAY + "): "
                    + ChatColor.WHITE + message);
            sender.playSound(sender.getLocation(), Sound.ANVIL_LAND, 1f, 1f);
            receiver.sendMessage(ChatColor.GRAY + "(From " + sender.getDisplayName() + ChatColor.GRAY + "): "
                    + ChatColor.WHITE + message);

            receiver.playSound(sender.getLocation(), Sound.ANVIL_LAND, 1f, 1f);
        }
    }

    @Command(aliases = {"reply", "r"}, desc = "Reply to someone who has messaged you!")
    @me.rbrickis.mojo.bukkit.annotations.Player
    public void reply(Player sender, @Text String message) {
        if (replyMap.containsKey(sender.getUniqueId())) {
            UUID toSend = replyMap.get(sender.getUniqueId());

            if (Bukkit.getPlayer(toSend) != null) {
                Player toSendPlayer = Bukkit.getPlayer(toSend);
                sender.sendMessage(ChatColor.GRAY + "(To " + toSendPlayer.getDisplayName() + ChatColor.GRAY + "): "
                        + ChatColor.WHITE + message);
                sender.playSound(sender.getLocation(), Sound.ANVIL_LAND, 1f, 1f);
                toSendPlayer.sendMessage(ChatColor.GRAY + "(From " + sender.getDisplayName() + ChatColor.GRAY + "): "
                        + ChatColor.WHITE + message);
                toSendPlayer.playSound(sender.getLocation(), Sound.ANVIL_LAND, 1f, 1f);
            } else {
                replyMap.remove(toSend);
                replyMap.remove(sender.getUniqueId());
                sender.sendMessage(ChatColor.RED + "The player you were messaging is not online!");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You have no one to reply to!");
        }
    }

    @Command(aliases = {"clearchat", "cc"})
    public void clearChat(CommandSender sender, @Default("120") int lines) {
        IntStream.range(0, lines).forEach(i -> Bukkit.broadcastMessage(""));
        Bukkit.broadcastMessage("Chat has been cleared by " + sender.getName());
    }


}
