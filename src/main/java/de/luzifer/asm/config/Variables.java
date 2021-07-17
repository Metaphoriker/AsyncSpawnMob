package de.luzifer.asm.config;

import de.luzifer.asm.AsyncSpawnMob;

public class Variables {

    public static int spawningDelay, spawnPerTick;

    private Variables() {}

    public static void initialize() {

        spawningDelay = AsyncSpawnMob.instance.getConfig().getInt("Spawning-Delay");
        spawnPerTick = AsyncSpawnMob.instance.getConfig().getInt("Spawn-Per-Delay");
    }

}
