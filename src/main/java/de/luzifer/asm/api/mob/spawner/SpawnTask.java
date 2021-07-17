package de.luzifer.asm.api.mob.spawner;

import de.luzifer.asm.AsyncSpawnMob;
import de.luzifer.asm.api.user.User;
import de.luzifer.asm.config.Variables;
import de.luzifer.asm.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.List;

public class SpawnTask {

    private final List<SpawnTaskData> spawnTaskDataList;
    private final User user;
    private final int amount;

    private int taskId;

    public SpawnTask(List<SpawnTaskData> spawnTaskDataList, User user) {
        this.spawnTaskDataList = spawnTaskDataList;
        this.amount = spawnTaskDataList.size();
        this.user = user;
    }

    public void start() {

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(AsyncSpawnMob.instance, () -> {

            double calc = (amount*1.0)/100*Variables.spawnPerTick;
            for(int i = 0; i < (calc <= 1 ? 1 : Math.round(calc)); i++) {
                if(!spawnTaskDataList.isEmpty()) {

                    SpawnTaskData spawnTaskData = spawnTaskDataList.get(0);
                    spawnEntity(spawnTaskData.getEntityTypeName(), user, spawnTaskData.getSpawnLocation());
                    spawnTaskDataList.remove(0);
                } else {

                    Bukkit.getScheduler().cancelTask(taskId);

                    user.asPlayer().sendMessage(ChatUtil.formatMessage("§7Task finished successfully. TaskId: §f" + taskId));
                    user.getTaskIds().remove(SpawnTaskId.of(taskId));
                    break;
                }
            }
        }, 0, Variables.spawningDelay);

        user.asPlayer().sendMessage(ChatUtil.formatMessage("§7Task started successfully. TaskId: §f" + taskId));
        user.getTaskIds().add(SpawnTaskId.of(taskId));
    }

    private void spawnEntity(String entityTypeName, User user, Location spawnAt) throws IllegalArgumentException {

        EntityType entityType = EntityType.valueOf(entityTypeName.toUpperCase());
        user.asPlayer().getWorld().spawnEntity(spawnAt.clone().add(0.5, 1, 0.5), entityType);
    }
}
