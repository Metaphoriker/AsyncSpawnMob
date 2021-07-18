package de.luzifer.asm.commands;

import de.luzifer.asm.api.mob.Mob;
import de.luzifer.asm.api.mob.task.SpawnTask;
import de.luzifer.asm.api.mob.task.SpawnTaskData;
import de.luzifer.asm.api.mob.task.SpawnTaskId;
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
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.util.*;

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

            boolean sendHelpList = false;
            switch (args.length) {
                case 1:
                    if(args[0].equalsIgnoreCase("moblist")) {

                        sendMobList(user, 0);
                        return true;
                    } else if(args[0].equalsIgnoreCase("list")) {

                        sendTaskList(user);
                        return true;
                    }

                    sendHelpList = true;
                    break;
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

                        stopAndRemoveTask(user, taskId);
                        return true;
                    } else if(args[0].equalsIgnoreCase("spawn")) {

                        if(assertEntityTypeDoesNotExist(args[1], user))
                            return true;

                        spawnASingleEntity(args[1], user, location);
                        return true;
                    } else if(args[0].equalsIgnoreCase("moblist")) {

                        int page = 0;
                        try {
                            page = Integer.parseInt(args[1]);
                        } catch (Exception e) {
                            player.sendMessage(ChatUtil.formatMessage("§7Please enter a valid page."));
                        }

                        sendMobList(user, page);
                        return true;
                    }

                    sendHelpList = true;
                    break;
                case 3:
                    if(args[0].equalsIgnoreCase("spawn")) {

                        if(assertEntityTypeDoesNotExist(args[1], user))
                            return true;

                        int amount = getAmount(args[2], user);

                        if(checkAmountForInvalid(amount, player))
                            return true;

                        if(amount == 1) {

                            spawnASingleEntity(args[1], user, location);
                            return true;
                        }

                        player.sendMessage(ChatUtil.formatMessage("§7Spawning " + amount + " " + args[1].toUpperCase() + "s"));
                        prepareAndStartSpawnTask(amount, args[1], user, location);

                        return true;
                    }

                    sendHelpList = true;
                    break;
                default:
                    sendHelpList(player);
            }

            if(sendHelpList) sendHelpList(player);
        }
        return true;
    }

    private boolean isPermitted(Player player) {
        return player.hasPermission(Variables.permission) || player.isOp();
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

    private void sendHelpList(Player player) {

        player.sendMessage(ChatUtil.formatMessage("§a§oProtip: Use §6§o/asm"));

        player.sendMessage(ChatUtil.formatMessage("§6/asyncspawnmob"));
        player.sendMessage(ChatUtil.formatFollowMessage("§8All commands"));

        player.sendMessage(ChatUtil.formatMessage("§6/asyncspawnmob list"));
        player.sendMessage(ChatUtil.formatFollowMessage("§8List of running tasks"));

        player.sendMessage(ChatUtil.formatMessage("§6/asyncspawnmob moblist <page>"));
        player.sendMessage(ChatUtil.formatFollowMessage("§8PaginatedList of spawnable monsters"));

        player.sendMessage(ChatUtil.formatMessage("§6/asyncspawnmob spawn <type>"));
        player.sendMessage(ChatUtil.formatFollowMessage("§8Spawn 1 entity of type X"));

        player.sendMessage(ChatUtil.formatMessage("§6/asyncspawnmob spawn <type> <amount>"));
        player.sendMessage(ChatUtil.formatFollowMessage("§8Spawn X entities of type X"));

        player.sendMessage(ChatUtil.formatMessage("§6/asyncspawnmob stop <id>"));
        player.sendMessage(ChatUtil.formatFollowMessage("§8Stop SpawnTask with the id X"));
    }

    private void sendTaskList(User user) {

        user.asPlayer().sendMessage(ChatUtil.formatMessage("§7You have §8[§a" + user.getTaskIds().size() + "§8] §7task(s) running:"));
        for(SpawnTaskId taskId : user.getTaskIds())
            user.asPlayer().sendMessage(ChatUtil.formatFollowMessage("§7§f" + taskId.getTaskId()));
    }

    private void sendMobList(User user, int page) {

        Container<Mob> container = getPaginatedListWithPage(Arrays.asList(Mob.values()), page, 8);
        user.asPlayer().sendMessage(ChatUtil.formatMessage("§7A list of spawnable mobs §8[§7" + container.getPage() + "§8]:"));

        sendMobList(user, container, getBukkitVersion());
    }

    private void sendMobList(User user, Container<Mob> container, double version) {

        for(Mob mob : container.getList()) {

            String versionString = (doesMobExist(mob, version) ? "§a" : "§c") + mob.getSinceVersion();
            String mobSynonyms = mob.getIdentifier().replace("[", "").toLowerCase().replace("]", "");

            user.asPlayer().sendMessage(ChatUtil.formatFollowMessage("§f" + mobSynonyms + " §8(" + versionString + (mob.getRemovedSinceVersion().equalsIgnoreCase("NOT") ? " or higher§8)" : " - " + mob.getRemovedSinceVersion() + "§8)")));
        }
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

    private void spawnEntity(String entityTypeName, User user, Location spawnAt) {

        Mob mob = Mob.fromName(entityTypeName.toUpperCase());
        user.asPlayer().getWorld().spawnEntity(spawnAt.clone().add(0.5, 1, 0.5), mob.convertToEntityType());
    }

    private void spawnASingleEntity(String entityName, User user, Location location) {

        spawnEntity(entityName, user, location);
        user.asPlayer().sendMessage(ChatUtil.formatMessage("§7Spawned 1 " + entityName.toUpperCase()));
    }

    private void stopAndRemoveTask(User user, int taskId) {

        Bukkit.getScheduler().cancelTask(taskId);

        user.asPlayer().sendMessage(ChatUtil.formatMessage("§7Task stopped successfully. TaskId: §f" + taskId));
        user.getTaskIds().remove(SpawnTaskId.of(taskId));
    }

    private boolean checkAmountForInvalid(int amount, Player player) {

        if(amount == 0) {

            player.sendMessage(ChatUtil.formatMessage("§7Can't spawn 0 entities."));
            return true;
        }

        if(amount >= Variables.maxSpawningAmount) {

            player.sendMessage(ChatUtil.formatMessage("§7No more mobs than §c" + Variables.maxSpawningAmount + "§7 may be spawned."));
            return true;
        }
        return false;
    }

    private void prepareAndStartSpawnTask(int amount, String entityName, User user, Location location) {

        List<SpawnTaskData> spawnTaskDataList = new ArrayList<>();
        for(int i = 0; i < amount; i++) {
            spawnTaskDataList.add(new SpawnTaskData(entityName, location));
        }

        SpawnTask spawnTask = new SpawnTask(spawnTaskDataList, user);
        spawnTask.start();
    }

    private boolean assertEntityTypeDoesNotExist(String entityTypeName, User user) {

        if(entityTypeName == null)
            throw new IllegalArgumentException();

        try {
            Mob.fromName(entityTypeName.toUpperCase(Locale.ENGLISH));
        } catch (Exception e) {

            user.asPlayer().sendMessage(ChatUtil.formatMessage("§7Couldn't find a Mob named: §c" + entityTypeName));
            return true;
        }
        return false;
    }

    public <V> Container<V> getPaginatedListWithPage(List<V> toPaginate, int page, int sizePerPage) {

        List<V> list = new ArrayList<>();

        for(int i = 0; i < sizePerPage; i++) {

            int index = sizePerPage*page+i;
            if(index >= toPaginate.size()) break;

            list.add(toPaginate.get(index));
        }

        if(list.isEmpty() && page != 0) return getPaginatedListWithPage(toPaginate, page-1, sizePerPage);

        return new Container<>(list, page);
    }

    /*
    Not sure why i am doing it like this, but it works... so....
     */
    static class Container<K> {

        private final List<K> list;
        private final Integer page;

        public Container(List<K> list, Integer page) {

            this.list = list;
            this.page = page;
        }

        public List<K> getList() {
            return list;
        }

        public Integer getPage() {
            return page;
        }
    }
}
