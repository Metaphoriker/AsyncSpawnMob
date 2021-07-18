package de.luzifer.asm.api.mob;

import org.bukkit.entity.EntityType;

import java.util.Arrays;

public enum Mob {

    ARMOR_STAND("1.8", "ARMOR_STAND"),
    ARROW("1.8", "ARROW"),
    BAT("1.8", "BAT"),
    BLAZE("1.8", "BLAZE"),
    BOAT("1.8", "BOAT"),
    CAVE_SPIDER("1.8", "CAVE_SPIDER"),
    CHICKEN("1.8", "CHICKEN"),
    COW("1.8", "COW"),
    CREEPER("1.8", "CREEPER"),
    EGG("1.8", "EGG"),
    ENDER_CRYSTAL("1.8", "ENDER_CRYSTAL"),
    ENDER_DRAGON("1.8", "ENDER_DRAGON"),
    ENDER_SIGNAL("1.8", "ENDER_SIGNAL"),
    EXPERIENCE_ORB("1.8", "EXPERIENCE_ORB"),
    FIREBALL("1.8", "FIREBALL"),
    FIREWORK("1.8", "FIREWORK"),
    GHAST("1.8", "GHAST"),
    GIANT("1.8", "GIANT"),
    GUARDIAN("1.8", "GUARDIAN"),
    HORSE("1.8", "HORSE"),
    IRON_GOLEM("1.8", "IRON_GOLEM"),
    LIGHTNING("1.8", "LIGHTNING"),
    MAGMA_CUBE("1.8", "MAGMA_CUBE"),
    MINECART("1.8", "MINECART"),
    MINECART_CHEST("1.8", "MINECART_CHEST"),
    MINECART_COMMAND("1.8", "MINECART_COMMAND"),
    MINECART_FURNACE("1.8", "MINECART_FURNACE"),
    MINECART_HOPPER("1.8", "MINECART_HOPPER"),
    MINECART_MOB_SPAWNER("1.8", "MINECART_MOB_SPAWNER"),
    MINECART_TNT("1.8", "MINECART_TNT"),
    MUSHROOM_COW("1.8", "MUSHROOM_COW"),
    OCELOT("1.8", "OCELOT"),
    PIG("1.8", "PIG"),
    PIG_ZOMBIE("1.8", "PIG_ZOMBIE"),
    PRIMED_TNT("1.8", "PRIMED_TNT"),
    RABBIT("1.8", "RABBIT"),
    SHEEP("1.8", "SHEEP"),
    SILVERFISH("1.8", "SILVERFISH"),
    SKELETON("1.8", "SKELETON"),
    SLIME("1.8", "SLIME"),
    SMALL_FIREBALL("1.8", "SMALL_FIREBALL"),
    SNOWBALL("1.8", "SNOWBALL"),
    SNOWMAN("1.8", "SNOWMAN"),
    SPIDER("1.8", "SPIDER"),
    SPLASH_POTION("1.8", "SPLASH_POTION"),
    SQUID("1.8", "SQUID"),
    THROWN_EXP_BOTTLE("1.8", "THROWN_EXP_BOTTLE"),
    VILLAGER("1.8", "VILLAGER"),
    WITCH("1.8", "WITCH"),
    WITHER("1.8", "WITHER"),
    WITHER_SKULL("1.8", "WITHER_SKULL"),
    WOLF("1.8", "WOLF"),
    ZOMBIE("1.8", "ZOMBIE");

    private final String sinceVersion;
    private final String[] names;

    public static Mob fromName(String name) {

        for(Mob mob : Mob.values()) {
            for(String names : mob.getNames()) {
                if(mob.doesEntityTypeExist(names) && names.equalsIgnoreCase(name)) return mob;
            }
        }
        throw new IllegalArgumentException("There is no Mob with the specified name: " + name);
    }

    Mob(String version, String... names) {
        this.sinceVersion = version;
        this.names = names;
    }

    public String getSinceVersion() {
        return sinceVersion;
    }

    public String[] getNames() {
        return names;
    }

    public EntityType convertToEntityType() {

        for(String name : names) {
            if(doesEntityTypeExist(name)) return EntityType.valueOf(name);
        }
        throw new IllegalArgumentException("There is no EntityType with the specified name(s): " + Arrays.toString(names));
    }

    private boolean doesEntityTypeExist(String name) {

        if(name == null) return false;

        try {
            EntityType.valueOf(name);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
