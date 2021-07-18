package de.luzifer.asm.listener;

import de.luzifer.asm.api.user.User;
import de.luzifer.asm.api.user.UserService;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        UserService.getOrCreateUser(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

        User user = UserService.getOrCreateUser(e.getPlayer().getUniqueId());
        user.getTaskIds().forEach(id -> Bukkit.getScheduler().cancelTask(id.getTaskId()));

        UserService.removeUser(e.getPlayer().getUniqueId());
    }

}
