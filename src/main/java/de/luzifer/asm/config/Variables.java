package de.luzifer.asm.config;

import de.luzifer.asm.AsyncSpawnMob;

public class Variables {

    public static int spawningDelay, spawningDistance, maxSpawningAmount;
    public static double spawnPerTick;
    public static String permission;

    private Variables() {}

    public static void initialize() {

        spawningDelay = AsyncSpawnMob.instance.getConfig().getInt("Spawning-Delay");
        spawningDistance = AsyncSpawnMob.instance.getConfig().getInt("Spawning-Distance");
        maxSpawningAmount = AsyncSpawnMob.instance.getConfig().getInt("Max-Spawning-Amount");

        spawnPerTick = AsyncSpawnMob.instance.getConfig().getDouble("Spawn-Per-Delay");

        permission = AsyncSpawnMob.instance.getConfig().getString("Needed-Permission");
    }

}
