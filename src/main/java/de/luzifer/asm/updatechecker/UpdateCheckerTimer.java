package de.luzifer.asm.updatechecker;

import de.luzifer.asm.AsyncSpawnMob;
import de.luzifer.asm.config.Variables;
import de.luzifer.asm.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UpdateCheckerTimer implements Runnable {

    private final UpdateChecker updateChecker;

    public UpdateCheckerTimer(UpdateChecker updateChecker) {
        this.updateChecker = updateChecker;
    }

    @Override
    public void run() {

        if(!updateChecker.check()) {

            Bukkit.getScheduler().runTask(AsyncSpawnMob.instance, () -> {

                for(Player player : Bukkit.getOnlinePlayers())
                    if(isPermitted(player)) player.sendMessage(ChatUtil.formatMessage("§7An update is available."));
            });
        }
    }

    private boolean isPermitted(Player player) {
        return player.hasPermission(Variables.permission) || player.isOp();
    }

}
