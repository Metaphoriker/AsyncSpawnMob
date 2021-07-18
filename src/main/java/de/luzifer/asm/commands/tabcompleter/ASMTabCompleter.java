package de.luzifer.asm.commands.tabcompleter;

import de.luzifer.asm.api.mob.Mob;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.*;

public class ASMTabCompleter implements TabCompleter {

    final String[] ARGS = {"list", "moblist", "spawn", "stop"};

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        final List<String> complete = new ArrayList<>();

        switch (args.length) {
            case 1:
                StringUtil.copyPartialMatches(args[0], Arrays.asList(ARGS), complete);
                Collections.sort(complete);
                break;
            case 2:
                if(args[0].equalsIgnoreCase("moblist")) {
                    StringUtil.copyPartialMatches(args[1], Collections.singletonList("<page>"), complete);
                } else if(args[0].equalsIgnoreCase("stop")) {
                    StringUtil.copyPartialMatches(args[1], Collections.singletonList("<id>"), complete);
                } else if(args[0].equalsIgnoreCase("spawn")) {

                    List<String> mobs = new ArrayList<>();

                    double version = getBukkitVersion();
                    for(Mob mob : Mob.values())
                        if(doesMobExist(mob, version)) mobs.add(mob.getIdentifier().toLowerCase());

                    StringUtil.copyPartialMatches(args[1], mobs, complete);
                    Collections.sort(complete);
                }
                break;
            case 3:
                if(args[0].equalsIgnoreCase("spawn")) {
                    StringUtil.copyPartialMatches(args[2], Collections.singletonList("<amount>"), complete);
                }
                break;
        }
        return complete;
    }

    private boolean doesMobExist(Mob mob, double version) {

        boolean versionIsHigherThanSinceVersion = version >= Double.parseDouble(mob.getSinceVersion().split("\\.")[1]);
        boolean versionIsLowerThanRemovedSinceVersion = mob.getRemovedSinceVersion().equalsIgnoreCase("NOT") || Double.parseDouble(mob.getRemovedSinceVersion().split("\\.")[1]) >= version;
        return (versionIsHigherThanSinceVersion && versionIsLowerThanRemovedSinceVersion);
    }

    private double getBukkitVersion() {

        String version = Bukkit.getBukkitVersion().split("-")[0];
        return Double.parseDouble(version.split("\\.")[1]);
    }
}
