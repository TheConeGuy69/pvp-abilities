package me.leontliov.test67.abilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TerraSlam {
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    private final double damage;
    private final int cooldown;
    private final double duration;
    private final double radius;
    private final JavaPlugin plugin;

    public TerraSlam(JavaPlugin plugin) {
        damage = plugin.getConfig().getDouble("abilities.terraSlam.damage");
        cooldown = plugin.getConfig().getInt("abilities.terraSlam.cooldown");
        duration = plugin.getConfig().getDouble("abilities.terraSlam.duration");
        radius = plugin.getConfig().getDouble("abilities.terraSlam.radius");
        this.plugin = plugin;
    }

    public void use(Player player) {
        long now = System.currentTimeMillis();

        if (cooldowns.containsKey(player.getUniqueId())
                && cooldowns.get(player.getUniqueId()) > now) {

            player.sendMessage("Terra Slam is on cooldown!");
            return;
        }

        Location centre = player.getLocation();
        World world = player.getWorld();

        for (Player nearby : centre.getNearbyPlayers(radius)) {
            if (nearby.equals(player)) {
                continue;
            }

            Vector knockback = nearby.getLocation()
                    .toVector()
                    .subtract(centre.toVector())
                    .normalize()
                    .multiply(3);

            nearby.setVelocity(knockback);
            nearby.damage(damage, player);
        }

        new BukkitRunnable() {

            double radius = 0;

            @Override
            public void run() {

                radius += 0.5;

                for (double angle = 0; angle < Math.PI * 2; angle += 0.15) {

                    double x = Math.cos(angle) * radius;
                    double z = Math.sin(angle) * radius;

                    Location particleLoc = centre.clone().add(x, 0.1, z);

                    world.spawnParticle(
                            Particle.BLOCK_CRUMBLE,
                            particleLoc,
                            1,
                            Material.DIRT.createBlockData()
                    );
                }

                if (radius >= 8) {
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