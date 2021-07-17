package de.luzifer.asm.utils;

import de.luzifer.asm.AsyncSpawnMob;

public class ChatUtil {

    public static String formatMessage(String message) {
        return AsyncSpawnMob.PREFIX + message;
    }

    public static String formatFollowMessage(String message) {
        return AsyncSpawnMob.PREFIX + "§7⤷ " + message;
    }

}
