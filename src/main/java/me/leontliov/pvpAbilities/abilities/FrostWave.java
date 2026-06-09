package me.leontliov.pvpAbilities.abilities;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FrostWave {
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    private final double damage;
    private final int cooldown;
    private final double duration;
    private final int radius;

    public FrostWave(JavaPlugin plugin) {
        damage = plugin.getConfig().getInt("abilities.frostwave.damage");
        cooldown = plugin.getConfig().getInt("abilities.frostwave.cooldown");
        duration = plugin.getConfig().getInt("abilities.frostwave.duration");
        radius = plugin.getConfig().getInt("abilities.frostwave.radius");
    }

    public void use(Player player) {
        long now = System.currentTimeMillis();

        if (cooldowns.containsKey(player.getUniqueId())
                && cooldowns.get(player.getUniqueId()) > now) {

            player.sendMessage("Frost Wave is on cooldown!");
            return;
        }

        Location centre = player.getLocation();

        for (Player nearby : centre.getNearbyPlayers(radius)) {
            nearby.damage(damage, player);
            nearby.setFreezeTicks((int) (20 * duration));
            nearby.setWalkSpeed(0.1f);
        }

        for (double angle = 0; angle < Math.PI * 2; angle += 0.2) {
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;

            centre.getWorld().spawnParticle(
                    Particle.SNOWFLAKE,
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