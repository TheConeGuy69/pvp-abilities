package me.leontliov.pvpAbilities;

import me.leontliov.pvpAbilities.abilities.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class Test67 extends JavaPlugin {


    @Override
    public void onEnable() {
        saveDefaultConfig();
        TerraSlam terraSlam = new TerraSlam(this);

        getCommand("ability").setExecutor(
                new ability(this)
        );
    }

    @Override
    public void onDisable() {
        //gelp
    }
}
