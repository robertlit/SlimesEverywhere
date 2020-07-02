package me.robertlit.slimeseverywhere;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public final class SlimesEverywhere extends JavaPlugin implements Listener {

    private int chance;
    private Collection<EntityType> MOB_TYPES;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        chance = getConfig().getInt("chance", 10);
        MOB_TYPES = getConfig().getStringList("types").stream().map(EntityType::valueOf).collect(Collectors.toSet());
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.SLIME || !MOB_TYPES.contains(event.getEntityType())) {
            return;
        }
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) {
            return;
        }
        if (!event.getLocation().getWorld().getName().equals("world")) {
            return;
        }
        Random random = ThreadLocalRandom.current();
        if (!(random.nextInt(100) < chance)) {
            return;
        }
        event.setCancelled(true);
        event.getLocation().getWorld().spawnEntity(event.getLocation(), EntityType.SLIME);
    }
}
