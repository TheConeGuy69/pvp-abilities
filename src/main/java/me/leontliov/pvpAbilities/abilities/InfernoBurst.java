package me.leontliov.pvpAbilities.abilities;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InfernoBurst {
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    private final double damage;
    private final int cooldown;
    private final double duration;
    private final int radius;

    public InfernoBurst(JavaPlugin plugin) {
        damage = plugin.getConfig().getInt("abilities.infernoBlast.damage");
        cooldown = plugin.getConfig().getInt("abilities.infernoBlast.cooldown");
        duration = plugin.getConfig().getInt("abilities.infernoBlast.duration");
        radius = plugin.getConfig().getInt("abilities.infernoBlast.radius");
    }

    public void use(Player player) {
        long now = System.currentTimeMillis();

        if (cooldowns.containsKey(player.getUniqueId())
                && cooldowns.get(player.getUniqueId()) > now) {

            player.sendMessage("Inferno Burst is on cooldown!");
            return;
        }

        Location centre = player.getLocation();

        for (Player nearby : centre.getNearbyPlayers(radius)) {
            nearby.damage(damage, player);
            nearby.setFireTicks((int) (20 * duration));
        }

        for (double angle = 0; angle < Math.PI * 2; angle += 0.2) {
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;

            centre.getWorld().spawnParticle(
                    Particle.LAVA,
                    centre.clone().add(x, 0, z),
                    1
            );
        }

        cooldowns.put(
                player.getUniqueId(),
                now + cooldown * 1000L
        );
    }
}