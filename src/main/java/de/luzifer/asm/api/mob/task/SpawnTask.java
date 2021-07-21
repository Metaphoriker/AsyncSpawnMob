package de.luzifer.asm.api.mob.task;

import de.luzifer.asm.AsyncSpawnMob;
import de.luzifer.asm.api.mob.Mob;
import de.luzifer.asm.api.user.User;
import de.luzifer.asm.config.Variables;
import de.luzifer.asm.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;

public class SpawnTask {

    private final List<SpawnTaskData> spawnTaskDataList;
    private final String entityTypeName;
    private final int amount;
    private final User user;

    private int taskId;

    public SpawnTask(List<SpawnTaskData> spawnTaskDataList, User user) {

        this.spawnTaskDataList = spawnTaskDataList;
        this.entityTypeName = spawnTaskDataList.get(0).getEntityTypeName();
        this.amount = spawnTaskDataList.size();
        this.user = user;
    }

    public void start() {

        final double calc = (amount*1.0)/100*Variables.spawnPerTick;
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(AsyncSpawnMob.instance, () -> {

            for(int i = 0; i < (calc <= 1 ? 1 : Math.round(calc)); i++) {

                if(spawnTaskDataList.isEmpty()) {

                    Bukkit.getScheduler().cancelTask(taskId);
                    removeTaskFromTaskOwner();
                    break;
                }

                spawnEntityAndRemoveFromList();
            }
        }, 0, Variables.spawningDelay);

        addTaskToTaskOwner();
    }

    private void spawnEntityAndRemoveFromList() {

        SpawnTaskData spawnTaskData = spawnTaskDataList.get(0);
        spawnEntity(spawnTaskData.getEntityTypeName(), user, spawnTaskData.getSpawnLocation());
        spawnTaskDataList.remove(0);
    }

    private void removeTaskFromTaskOwner() {

        user.asPlayer().sendMessage(ChatUtil.formatMessage("§7Spawned " + amount + " " + entityTypeName.toUpperCase() + "s"));
        user.getTaskIds().remove(SpawnTaskId.of(taskId));
    }

    private void addTaskToTaskOwner() {

        user.asPlayer().sendMessage(ChatUtil.formatFollowMessage("§8TaskId: " + taskId));
        user.getTaskIds().add(SpawnTaskId.of(taskId));
    }

    private void spawnEntity(String entityTypeName, User user, Location spawnAt) {

        Mob mob = Mob.fromName(entityTypeName.toUpperCase());
        user.asPlayer().getWorld().spawnEntity(spawnAt.clone().add(0.5, 1, 0.5), mob.convertToEntityType());
    }
}
