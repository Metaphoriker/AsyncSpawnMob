package de.luzifer.asm.api.user;

import de.luzifer.asm.api.mob.spawner.SpawnTaskId;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

    protected final UUID uuid;

    private final List<SpawnTaskId> taskIds = new ArrayList<>();

    protected User(UUID uuid) {
        this.uuid = uuid;
    }

    public Player asPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public List<SpawnTaskId> getTaskIds() {
        return taskIds;
    }
}
