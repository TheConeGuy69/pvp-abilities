package me.leontliov.pvpAbilities.abilities;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NightFall {
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    private final double damage;
    private final int cooldown;
    private final int duration;
    private final int radius;

    private final JavaPlugin plugin;

    public NightFall(JavaPlugin plugin) {
        damage = plugin.getConfig().getInt("abilities.nightFall.damage");
        cooldown = plugin.getConfig().getInt("abilities.nightFall.cooldown");
        duration = plugin.getConfig().getInt("abilities.nightFall.duration");
        radius = plugin.getConfig().getInt("abilities.nightFall.radius");
        this.plugin = plugin;
    }

    public void use(Player player) {
        long now = System.currentTimeMillis();

        if (cooldowns.containsKey(player.getUniqueId())
                && cooldowns.get(player.getUniqueId()) > now) {

            player.sendMessage("Night Fall is on cooldown!");
            return;
        }

        Location centre = player.getLocation();

        for (Player nearby : player.getLocation().getNearbyPlayers(radius)) {

            new BukkitRunnable() {

                int secondsLeft = duration;

                @Override
                public void run() {

                    if (secondsLeft <= 0 || !nearby.isOnline()) {
                        cancel();
                        return;
                    }

                    nearby.damage(damage);

                    secondsLeft--;
                }

            }.runTaskTimer(plugin, 0L, 20L);
        }

        for (double angle = 0; angle < Math.PI * 2; angle += 0.2) {
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;

            centre.getWorld().spawnParticle(
                    Particle.ENCHANTED_HIT,
                    centre.clone().add(x, 0, z),
                    10
            );
        }

        cooldowns.put(
                player.getUniqueId(),
                now + cooldown * 1000L
        );
    }
}