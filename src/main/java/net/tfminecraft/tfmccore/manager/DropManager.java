package net.tfminecraft.tfmccore.manager;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.tfminecraft.tfmccore.TFMCCore;
import net.tfminecraft.tfmccore.loader.DropLoader;
import net.tfminecraft.tfmccore.reference.Drop;

public class DropManager implements Listener{
    @EventHandler
    public void blockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();
        ItemStack tool = p.getInventory().getItemInMainHand();

        for(Drop drop : DropLoader.get()) {
            if(!drop.appliesTo(p, b, tool)) continue;
            if(!drop.hasVanillaDrops(p, b, tool) && e.isDropItems()) e.setDropItems(false);
            new BukkitRunnable() {
            public void run()
                {
                    if(b.getLocation().getBlock().getType().equals(b.getType())) return;
                    drop.trigger(p, b, tool);
                }
            }.runTaskLater(TFMCCore.getInstance(),5L);
        }
    }
}
