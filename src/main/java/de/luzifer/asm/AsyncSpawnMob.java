package de.luzifer.asm;

import de.luzifer.asm.commands.ASMCommand;
import de.luzifer.asm.commands.tabcompleter.ASMTabCompleter;
import de.luzifer.asm.config.Variables;
import de.luzifer.asm.listener.PlayerJoinQuitListener;
import de.luzifer.asm.updatechecker.UpdateChecker;
import de.luzifer.asm.updatechecker.UpdateCheckerTimer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AsyncSpawnMob extends JavaPlugin {

    public static final String PREFIX = "§8[§eAsyncSpawnMob§8] ";
    public static AsyncSpawnMob instance;

    @Override
    public void onEnable() {

        instance = this;

        loadConfig();
        loadCommands();
        loadListener();

        Variables.initialize();

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new UpdateCheckerTimer(new UpdateChecker(this)), 0, 20*60*10);
    }

    @Override
    public void onDisable() {

    }

    private void loadConfig() {

        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    private void loadCommands() {

        getCommand("asyncspawnmob").setExecutor(new ASMCommand());
        getCommand("asyncspawnmob").setTabCompleter(new ASMTabCompleter());
    }

    private void loadListener() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinQuitListener(), this);
    }
}
