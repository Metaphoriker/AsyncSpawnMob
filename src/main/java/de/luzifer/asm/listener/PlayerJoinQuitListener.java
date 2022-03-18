package de.luzifer.asm.listener;

import de.luzifer.asm.api.user.User;
import de.luzifer.asm.api.user.UserRepository;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {

    private final UserRepository userRepository;
    
    public PlayerJoinQuitListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        userRepository.getOrCreateUser(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

        User user = userRepository.getOrCreateUser(e.getPlayer().getUniqueId());
        user.getTaskIds().forEach(id -> Bukkit.getScheduler().cancelTask(id.getTaskId()));
    
        userRepository.removeUser(e.getPlayer().getUniqueId());
    }

}
