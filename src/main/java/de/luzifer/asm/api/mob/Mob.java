package de.luzifer.asm.api.mob;

import org.bukkit.entity.EntityType;

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
    ENDERMAN("1.8", "ENDERMAN"),
    ENDERMITE("1.8", "ENDERMITE"),
    EXPERIENCE_ORB("1.8", "EXPERIENCE_ORB"),
    FALLING_BLOCK("1.8", "FALLING_BLOCK"),
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
    PIG_ZOMBIE("1.8", "1.16", "PIG_ZOMBIE"),
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
    ZOMBIE("1.8", "ZOMBIE"),

    AREA_EFFECT_CLOUD("1.9", "AREA_EFFECT_CLOUD"),
    DRAGON_FIREBALL("1.9", "DRAGON_FIREBALL"),
    LINGERING_POTION("1.9", "LINGERING_POTION"),
    SHULKER("1.9", "SHULKER"),
    SHULKER_BULLET("1.9", "SHULKER_BULLET"),
    SPECTRAL_ARROW("1.9", "SPECTRAL_ARROW"),
    TIPPED_ARROW("1.9", "TIPPED_ARROW"),

    POLAR_BEAR("1.10", "POLAR_BEAR"),

    DONKEY("1.11", "DONKEY"),
    ELDER_GUARDIAN("1.11", "ELDER_GUARDIAN"),
    EVOKER("1.11", "EVOKER"),
    EVOKER_FANGS("1.11", "EVOKER_FANGS"),
    HUSK("1.11", "HUSK"),
    LLAMA("1.11", "LLAMA"),
    LLAMA_SPIT("1.11", "LLAMA_SPIT"),
    MULE("1.11", "MULE"),
    SKELETON_HORSE("1.11", "SKELETON_HORSE"),
    STRAY("1.11", "STRAY"),
    VEX("1.11", "VEX"),
    VINDICATOR("1.11", "VINDICATOR"),
    WITHER_SKELETON("1.11", "WITHER_SKELETON"),
    ZOMBIE_HORSE("1.11", "ZOMBIE_HORSE"),
    ZOMBIE_VILLAGER("1.11", "ZOMBIE_VILLAGER"),

    ILLUSIONER("1.12", "ILLUSIONER"),
    PARROT("1.12", "PARROT"),

    COD("1.13", "COD"),
    DOLPHIN("1.13", "DOLPHIN"),
    DROWNED("1.13", "DROWNED"),
    PHANTOM("1.13", "PHANTOM"),
    PUFFERFISH("1.13", "PUFFERFISH"),
    SALMON("1.13", "SALMON"),
    TRIDENT("1.13", "TRIDENT"),
    TROPICAL_FISH("1.13", "TROPICAL_FISH"),
    TURTLE("1.13", "TURTLE"),

    CAT("1.14", "CAT"),
    FOX("1.14", "FOX"),
    PANDA("1.14", "PANDA"),
    PILLAGER("1.14", "PILLAGER"),
    RAVAGER("1.14", "RAVAGER"),
    TRADER_LLAMA("1.14", "TRADER_LLAMA"),
    WANDERING_TRADER("1.14", "WANDERING_TRADER"),

    BEE("1.15", "BEE"),

    HOGLIN("1.16", "HOGLIN"),
    PIGLIN("1.16", "PIGLIN"),
    PIGLIN_BRUTE("1.16", "PIGLIN_BRUTE"),
    STRIDER("1.16", "STRIDER"),
    ZOGLIN("1.16", "ZOGLIN"),
    ZOMBIFIED_PIGLIN("1.16", "ZOMBIFIED_PIGLIN"),

    AXOLOTL("1.17", "AXOLOTL"),
    GLOW_SQUID("1.17", "GLOW_SQUID"),
    GOAT("1.17", "GOAT");

    private final String sinceVersion;
    private final String removedSinceVersion;
    private final String identifier;

    public static Mob fromName(String name) {

        for(Mob mob : Mob.values()) {
            if(mob.doesEntityTypeExist(mob.identifier) && mob.identifier.equalsIgnoreCase(name)) return mob;
        }
        throw new IllegalArgumentException("There is no Mob with the specified name: " + name);
    }

    Mob(String existsSinceVersion, String identifier) {
        this(existsSinceVersion, "NOT", identifier);
    }

    Mob(String existsSinceVersion, String removedSinceVersion, String identifier) {

        this.sinceVersion = existsSinceVersion;
        this.removedSinceVersion = removedSinceVersion;
        this.identifier = identifier;
    }

    public String getSinceVersion() {
        return sinceVersion;
    }

    public String getRemovedSinceVersion() {
        return removedSinceVersion;
    }

    public String getIdentifier() {
        return identifier;
    }

    public EntityType convertToEntityType() {

        if(doesEntityTypeExist(identifier)) return EntityType.valueOf(identifier);
        throw new IllegalArgumentException("There is no EntityType with the specified name(s): " + identifier);
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
