package joshie.mariculture.core.gui;

import joshie.mariculture.core.items.ItemMCStorage;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class GuiStorage extends GuiMariculture {
    protected final InventoryStorage storage;

    public GuiStorage(ContainerStorage storage, String gui) {
        super(storage, gui);
        this.storage = storage.storage;
        ((ItemMCStorage) storage.storage.player.getCurrentEquippedItem().getItem()).addFeatures(features);
    }

    public GuiStorage(IInventory playerInv, InventoryStorage storage, World world, String gui, int offset) {
        super(new ContainerStorage(playerInv, storage, world, offset), gui, offset);
        this.storage = storage;
        ((ItemMCStorage) storage.player.getCurrentEquippedItem().getItem()).addFeatures(features);
    }

    @Override
    public String getName() {
        if (storage != null) {
            ItemStack stack = storage.player.getCurrentEquippedItem();
            if (stack != null && stack.getItem() instanceof ItemMCStorage) {
                ItemMCStorage item = (ItemMCStorage) stack.getItem();
                return StatCollector.translateToLocal(item.getUnlocalizedName(stack) + ".name");
            }
        }

        return "";
    }

    @Override
    public int getX() {
        if (storage != null) {
            ItemStack stack = storage.player.getCurrentEquippedItem();
            if (stack != null && stack.getItem() instanceof ItemMCStorage) {
                ItemMCStorage item = (ItemMCStorage) stack.getItem();
                return item.getX(stack);
            }
        }

        return 0;
    }
}
