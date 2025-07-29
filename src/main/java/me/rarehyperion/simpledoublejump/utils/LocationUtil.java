package me.rarehyperion.simpledoublejump.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class LocationUtil {

    private static final double GROUND_DIVISOR = 0.015625D;

    public static boolean onGround(final Location location) {
        final double y = location.getY();

        if(Math.abs(y) % GROUND_DIVISOR != 0.0D) {
            return false;
        }

        final World world = location.getWorld();

        assert world != null;

        final int groundY = (int) Math.floor(y) - 1;
        final double x = location.getX();
        final double z = location.getZ();

        final int minX = (int) Math.floor(x - 0.3);
        final int maxX = (int) Math.floor(x + 0.3);
        final int minZ = (int) Math.floor(z - 0.3);
        final int maxZ = (int) Math.floor(z + 0.3);

        for (int checkX = minX; checkX <= maxX; checkX++) {
            for (int checkZ = minZ; checkZ <= maxZ; checkZ++) {
                final Block block = world.getBlockAt(checkX, groundY, checkZ);
                final Material type = block.getType();

                if (type != Material.AIR && type.isSolid()) {
                    return true;
                }
            }
        }

        return false;
    }

}
