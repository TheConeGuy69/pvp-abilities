package me.leontliov.pvpAbilities.abilities;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AeroCrush {
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    private final double damage;
    private final int cooldown;
    private final double duration;
    private final int radius;

    public AeroCrush(JavaPlugin plugin) {
        damage = plugin.getConfig().getInt("abilities.terraSlam.damage");
        cooldown = plugin.getConfig().getInt("abilities.terraSlam.cooldown");
        duration = plugin.getConfig().getInt("abilities.terraSlam.duration");
        radius = plugin.getConfig().getInt("abilities.terraSlam.radius");
    }

    public void use(Player player) {
        long now = System.currentTimeMillis();

        if (cooldowns.containsKey(player.getUniqueId())
                && cooldowns.get(player.getUniqueId()) > now) {

            player.sendMessage("Aero Crush is on cooldown!");
            return;
        }

        Location centre = player.getLocation();

        for (Player nearby : centre.getNearbyPlayers(radius)) {
            nearby.damage(damage, player);
        }

        for (double angle = 0; angle < Math.PI * 2; angle += 0.2) {
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;

            centre.getWorld().spawnParticle(
                    Particle.EXPLOSION,
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