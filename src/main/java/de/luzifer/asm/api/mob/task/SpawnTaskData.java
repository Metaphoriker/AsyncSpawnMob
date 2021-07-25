package de.luzifer.asm.api.mob.task;

import org.bukkit.Location;

public class SpawnTaskData {

    private final Location spawnLocation;
    private final String entityTypeName;
    private final int amount;

    public SpawnTaskData(String entityTypeName, int amount,  Location spawnLocation) {

        this.entityTypeName = entityTypeName;
        this.spawnLocation = spawnLocation;
        this.amount = amount;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public String getEntityTypeName() {
        return entityTypeName;
    }

    public int getAmount() {
        return amount;
    }
}
