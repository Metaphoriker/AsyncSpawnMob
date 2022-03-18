package de.luzifer.asm.api.mob.task;

import de.luzifer.asm.AsyncSpawnMob;
import de.luzifer.asm.api.mob.Mob;
import de.luzifer.asm.api.user.User;
import de.luzifer.asm.config.Variables;
import de.luzifer.asm.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.function.Consumer;

public class SpawnTask {

    private final SpawnTaskData spawnTaskData;
    private final String entityTypeName;
    private final int amount;
    private final User user;

    private Consumer<User> callback;
    private int taskId;

    public SpawnTask(SpawnTaskData spawnTaskData, User user) {

        this.spawnTaskData = spawnTaskData;
        this.entityTypeName = spawnTaskData.getEntityTypeName();
        this.amount = spawnTaskData.getAmount();
        this.user = user;
    }

    public void start() {

        final double calc = (amount*1.0)/100*Variables.spawnPerTick;

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(AsyncSpawnMob.instance, new Runnable() {

            private int index = 0;

            @Override
            public void run() {

                for(int i = 0; i < (calc <= 1 ? 1 : Math.round(calc)); i++) {

                    if(index >= amount) {

                        Bukkit.getScheduler().cancelTask(taskId);
                        finishTask();
                        break;
                    }

                    spawnEntity(spawnTaskData.getEntityTypeName(), user, spawnTaskData.getSpawnLocation());
                    index++;
                }

            }
        }, 0, Variables.spawningDelay);

        addTaskToTaskOwner();
    }

    public void whenDone(Consumer<User> consumer) {
        this.callback = consumer;
    }

    public boolean isDone() {
        return !Bukkit.getScheduler().isCurrentlyRunning(taskId);
    }

    private void finishTask() {

        if(callback != null) callback.accept(user);
        removeTaskFromTaskOwner();
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

        Mob mob = Mob.getMob(entityTypeName.toUpperCase());
        user.asPlayer().getWorld().spawnEntity(spawnAt.clone().add(0.5, 1, 0.5), mob.convertToEntityType());
    }
}
