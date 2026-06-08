package me.leontliov.test67;

import me.leontliov.test67.abilities.*;
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
