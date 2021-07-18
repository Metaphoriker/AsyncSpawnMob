package de.luzifer.asm.config;

import de.luzifer.asm.AsyncSpawnMob;

public class Variables {

    public static int spawningDelay, spawningDistance, spawnPerTick, maxSpawningAmount;
    public static String permission;

    private Variables() {}

    public static void initialize() {

        spawningDelay = AsyncSpawnMob.instance.getConfig().getInt("Spawning-Delay");
        spawningDistance = AsyncSpawnMob.instance.getConfig().getInt("Spawning-Distance");
        spawnPerTick = AsyncSpawnMob.instance.getConfig().getInt("Spawn-Per-Delay");

        maxSpawningAmount = AsyncSpawnMob.instance.getConfig().getInt("Max-Spawning-Amount");

        permission = AsyncSpawnMob.instance.getConfig().getString("Needed-Permission");
    }

}
