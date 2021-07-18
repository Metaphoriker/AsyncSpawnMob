package de.luzifer.asm.commands;

import de.luzifer.asm.api.mob.spawner.SpawnTask;
import de.luzifer.asm.api.mob.spawner.SpawnTaskData;
import de.luzifer.asm.api.mob.spawner.SpawnTaskId;
import de.luzifer.asm.api.user.User;
import de.luzifer.asm.api.user.UserService;
import de.luzifer.asm.config.Variables;
import de.luzifer.asm.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ASMCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {

            sender.sendMessage("I'm sorry, you can't do that.");
            return true;
        }

        Player player = (Player) sender;
        User user = UserService.getOrCreateUser(player.getUniqueId());

        if(command.getName().equalsIgnoreCase("asyncspawnmob")) {

            if(!isPermitted(player)) {
                player.sendMessage(ChatUtil.formatMessage("§cYou don't have the permission to do that."));
                return true;
            }

            Location location = getTargetBlock(player).getLocation();

            switch (args.length) {
                case 1:
                    if(args[0].equalsIgnoreCase("list")) {

                        player.sendMessage(ChatUtil.formatMessage("§7You have §8[§a" + user.getTaskIds().size() + "§8] §7tasks running:"));
                        for(SpawnTaskId taskId : user.getTaskIds())
                            player.sendMessage(ChatUtil.formatMessage("§7- §f" + taskId.getTaskId()));

                        return true;
                    }
                case 2:
                    if(args[0].equalsIgnoreCase("stop")) {

                        int taskId;
                        try {
                            taskId = Integer.parseInt(args[1]);
                        } catch (Exception e) {
                            player.sendMessage(ChatUtil.formatMessage("§7Please enter a valid TaskId."));
                            return true;
                        }

                        if(!user.getTaskIds().contains(SpawnTaskId.of(taskId))) {
                            player.sendMessage(ChatUtil.formatMessage("§7Couldn't find a Task with the id §f" + taskId));
                            return true;
                        }

                        Bukkit.getScheduler().cancelTask(taskId);

                        player.sendMessage(ChatUtil.formatMessage("§7Task stopped successfully. TaskId: §f" + taskId));
                        user.getTaskIds().remove(SpawnTaskId.of(taskId));
                        return true;
                    } else if(args[0].equalsIgnoreCase("spawn")) {

                        if(assertEntityTypeDoesNotExist(args[1])) {
                            user.asPlayer().sendMessage(ChatUtil.formatMessage("§7Couldn't find an EntityType named: §c" + args[1]));
                            return true;
                        }

                        spawnEntity(args[1], user, location);
                        player.sendMessage(ChatUtil.formatMessage("§7Spawned 1 " + args[1].toUpperCase()));
                        return true;
                    }
                case 3:
                    if(args[0].equalsIgnoreCase("spawn")) {

                        int amount = getAmount(args[2], user);

                        if(amount == 0) return true;

                        if(assertEntityTypeDoesNotExist(args[1])) {
                            user.asPlayer().sendMessage(ChatUtil.formatMessage("§7Couldn't find an EntityType named: §c" + args[1]));
                            return true;
                        }

                        if(amount == 1) {

                            spawnEntity(args[1], user, location);
                            player.sendMessage(ChatUtil.formatMessage("§7Spawned 1 " + args[1].toUpperCase()));
                            return true;
                        }

                        List<SpawnTaskData> spawnTaskDataList = new ArrayList<>();
                        for(int i = 0; i < amount; i++) {
                            spawnTaskDataList.add(new SpawnTaskData(args[1], location));
                        }

                        player.sendMessage(ChatUtil.formatMessage("§7Spawning " + amount + " " + args[1].toUpperCase() + "s"));

                        SpawnTask spawnTask = new SpawnTask(spawnTaskDataList, user);
                        spawnTask.start();

                        return true;
                    }
                default:
                    sendHelpList(player);
            }
        }
        return true;
    }

    private boolean isPermitted(Player player) {
        return player.hasPermission(Variables.permission) || player.isOp();
    }

    private void sendHelpList(Player player) {

        player.sendMessage(ChatUtil.formatMessage("§a§oProtip: Use §6§o/asm"));

        player.sendMessage(ChatUtil.formatMessage("§6/asyncspawnmob"));
        player.sendMessage(ChatUtil.formatFollowMessage("§8All commands"));

        player.sendMessage(ChatUtil.formatMessage("§6/asyncspawnmob list"));
        player.sendMessage(ChatUtil.formatFollowMessage("§8List of running tasks"));

        player.sendMessage(ChatUtil.formatMessage("§6/asyncspawnmob spawn <type>"));
        player.sendMessage(ChatUtil.formatFollowMessage("§8Spawn 1 entity of type X"));

        player.sendMessage(ChatUtil.formatMessage("§6/asyncspawnmob spawn <type> <amount>"));
        player.sendMessage(ChatUtil.formatFollowMessage("§8Spawn X entities of type X"));

        player.sendMessage(ChatUtil.formatMessage("§6/asyncspawnmob stop <id>"));
        player.sendMessage(ChatUtil.formatFollowMessage("§8Stop SpawnTask with the id X"));
    }

    private boolean assertEntityTypeDoesNotExist(String entityTypeName) {

        if(entityTypeName == null) return true;

        try {
            EntityType.valueOf(entityTypeName.toUpperCase(Locale.ENGLISH));
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    private void spawnEntity(String entityTypeName, User user, Location spawnAt) throws IllegalArgumentException {

        EntityType entityType = EntityType.valueOf(entityTypeName.toUpperCase());
        user.asPlayer().getWorld().spawnEntity(spawnAt.clone().add(0.5, 1, 0.5), entityType);
    }

    private int getAmount(String amountString, User user) {

        int amount = 1;
        try {
            amount = Integer.parseInt(amountString);
        } catch (Exception e) {
            user.asPlayer().sendMessage(ChatUtil.formatMessage("§7Invalid amount: Amount has been set to 1."));
        }
        return amount;
    }

    private Block getTargetBlock(Player player) {

        BlockIterator iterator = new BlockIterator(player, Variables.spawningDistance);
        Block lastBlock = iterator.next();
        while (iterator.hasNext()) {
            lastBlock = iterator.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        return lastBlock;
    }

}
