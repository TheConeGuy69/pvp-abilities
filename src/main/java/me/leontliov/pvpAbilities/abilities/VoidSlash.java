package me.leontliov.pvpAbilities.abilities;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VoidSlash {
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    private final double damage;
    private final int cooldown;
    private final double duration;
    private final int radius;
    private final double speed;
    private final double range;

    private final JavaPlugin plugin;

    public VoidSlash(JavaPlugin plugin) {
        damage = plugin.getConfig().getDouble("abilities.voidSlash.damage");
        cooldown = plugin.getConfig().getInt("abilities.voidSlash.cooldown");
        duration = plugin.getConfig().getDouble("abilities.voidSlash.duration");
        radius = plugin.getConfig().getInt("abilities.voidSlash.radius");
        speed = plugin.getConfig().getDouble("abilities.voidSlash.speed");
        range = plugin.getConfig().getDouble("abilities.voidSlash.range");
        this.plugin = plugin;
    }

    public void use(Player player) {
        long now = System.currentTimeMillis();

        if (cooldowns.containsKey(player.getUniqueId())
                && cooldowns.get(player.getUniqueId()) > now) {

            player.sendMessage("VoidSlash is on cooldown!");
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

                Location currentCentre = player.getEyeLocation()
                        .add(player.getLocation().getDirection().multiply(travelled));

                for (double angle = -60; angle <= 60; angle += 5) {

                    double radians = Math.toRadians(angle);

                    double x = Math.sin(radians) * 2;
                    double y = Math.cos(radians) * 2;

                    Location particleLoc = currentCentre.clone()
                            .add(x, y, 0);

                    player.getWorld().spawnParticle(
                            Particle.BLOCK,
                            particleLoc,
                            1,
                            Material.AMETHYST_BLOCK.createBlockData()
                    );
                }

                for (Entity entity : world.getNearbyEntities(loc, 0.5, 0.5, 0.5)) {

                    if (!(entity instanceof Player target)) {
                        continue;
                    }

                    if (target.equals(player)) {
                        continue;
                    }

                    target.damage(damage, player);

                    target.setWalkSpeed(0.1f);

                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        target.setWalkSpeed(0.2f); // default player speed
                    }, (int) (20L * duration));

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