package me.leontliov.test67;

import me.leontliov.test67.abilities.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import me.leontliov.test67.abilities.*;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ability implements CommandExecutor {

    private final TerraSlam terraSlam;
    private final InfernoBurst infernoBurst;
    private final FrostWave frostWave;
    private final LightningVein lightningVein;

    public ability(Test67 plugin) {
        this.terraSlam = new TerraSlam(plugin);
        this.infernoBurst = new InfernoBurst(plugin);
        this.frostWave = new FrostWave(plugin);
        this.lightningVein = new LightningVein(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


            if (args.length == 0) {
                sender.sendMessage("Usage: /ability <ability>");
                return true;
            }

            if (!(sender instanceof Player player)) {
                return true;
            }

            String abilityArg  = args[0];

            if (abilityArg.equalsIgnoreCase("terraslam")) {
                terraSlam.use(player);
            } else if (abilityArg.equalsIgnoreCase("infernoblast")) {
                infernoBurst.use(player);
            } else if (abilityArg.equalsIgnoreCase("frostwave")) {
                frostWave.use(player);
            } else if (abilityArg.equalsIgnoreCase("lightningvein")) {
                lightningVein.use(player);
            }

        return true;
    }
}
