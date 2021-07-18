package de.luzifer.asm.api.mob.task;

import org.bukkit.Location;

public class SpawnTaskData {

    private final String entityTypeName;
    private final Location spawnLocation;

    public SpawnTaskData(String entityTypeName, Location spawnLocation) {

        this.entityTypeName = entityTypeName;
        this.spawnLocation = spawnLocation;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public String getEntityTypeName() {
        return entityTypeName;
    }
}
