package me.leontliov.pvpAbilities;

import me.leontliov.pvpAbilities.abilities.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ability implements CommandExecutor, TabCompleter {

    private final TerraSlam terraSlam;
    private final InfernoBurst infernoBurst;
    private final FrostWave frostWave;
    private final LightningVein lightningVein;
    private final NightFall nightFall;
    private final AeroCrush aeroCrush;
    private final HorrorMark horrorMark;
    private final CrystalSpikes crystalSpikes;

    public ability(Test67 plugin) {
        this.terraSlam = new TerraSlam(plugin);
        this.infernoBurst = new InfernoBurst(plugin);
        this.frostWave = new FrostWave(plugin);
        this.lightningVein = new LightningVein(plugin);
        this.nightFall = new NightFall(plugin);
        this.aeroCrush = new AeroCrush(plugin);
        this.horrorMark = new HorrorMark(plugin);
        this.crystalSpikes = new CrystalSpikes(plugin);
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
            } else if (abilityArg.equalsIgnoreCase("infernoburst")) {
                infernoBurst.use(player);
            } else if (abilityArg.equalsIgnoreCase("frostwave")) {
                frostWave.use(player);
            } else if (abilityArg.equalsIgnoreCase("lightningvein")) {
                lightningVein.use(player);
            } else if (abilityArg.equalsIgnoreCase("nightfall")) {
                nightFall.use(player);
            } else if (abilityArg.equalsIgnoreCase("aerocrush")) {
                aeroCrush.use(player);
            } else if (abilityArg.equalsIgnoreCase("horrormark")) {
                horrorMark.use(player);
            } else if (abilityArg.equalsIgnoreCase("crystalSpikes")) {
                crystalSpikes.use(player);
            } else {
                player.sendMessage("not a real shape");
            }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,
                                      Command command,
                                      String alias,
                                      String[] args) {

        if (args.length == 1) {
            return Arrays.asList(
                    "TerraSlam",
                    "InfernoBurst",
                    "FrostWave",
                    "LightningVein",
                    "NightFall",
                    "AeroCrush",
                    "HorrorMark",
                    "CrystalSpikes"
            );
        }

        return Collections.emptyList();
    }
}
