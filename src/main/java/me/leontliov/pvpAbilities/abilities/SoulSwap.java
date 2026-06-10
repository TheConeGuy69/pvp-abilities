package me.leontliov.pvpAbilities.abilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SoulSwap {
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    private final double damage;
    private final int cooldown;
    private final double duration;
    private final int radius;
    private final double speed;
    private final double range;

    private final JavaPlugin plugin;

    public SoulSwap(JavaPlugin plugin) {
        damage = plugin.getConfig().getDouble("abilities.soulSwap.damage");
        cooldown = plugin.getConfig().getInt("abilities.soulSwap.cooldown");
        duration = plugin.getConfig().getDouble("abilities.soulSwap.duration");
        radius = plugin.getConfig().getInt("abilities.soulSwap.radius");
        speed = plugin.getConfig().getDouble("abilities.soulSwap.speed");
        range = plugin.getConfig().getDouble("abilities.soulSwap.range");
        this.plugin = plugin;
    }

    public void use(Player player) {
        long now = System.currentTimeMillis();

        if (cooldowns.containsKey(player.getUniqueId())
                && cooldowns.get(player.getUniqueId()) > now) {

            player.sendMessage("Pyro Lash is on cooldown!");
            return;
        }
            Location start = player.getEyeLocation();
            Vector direction = player.getLocation().getDirection();
            World world = player.getWorld();

            new BukkitRunnable() {

                double travelled = 0;

                @Override
                public void run() {

                    travelled += speed;

                    Location loc = start.clone()
                            .add(direction.clone().multiply(travelled));

                    world.spawnParticle(
                            Particle.BLOCK,
                            loc,
                            10,
                            radius, radius, radius,
                            Material.REDSTONE_BLOCK.createBlockData()
                    );

                    world.spawnParticle(
                            Particle.BLOCK,
                            loc,
                            10,
                            radius, radius, radius,
                            Material.BLUE_ICE.createBlockData()
                    );

                    for (Entity entity : world.getNearbyEntities(loc, 0.5, 0.5, 0.5)) {

                        if (!(entity instanceof Player target)) {
                            continue;
                        }

                        if (target.equals(player)) {
                            continue;
                        }

                        Location attackerLocation = player.getLocation();
                        Location victimLocation = target.getLocation();

                        player.teleport(victimLocation);
                        target.teleport(attackerLocation);

                        target.damage(damage);

                        cancel();
                        return;
                    }

                    if (travelled >= range) {
                        cancel();
                    }
                }

            }.runTaskTimer(plugin, 0L, 1L);

        cooldowns.put(
                player.getUniqueId(),
                now + cooldown * 1000L
        );
    }
}