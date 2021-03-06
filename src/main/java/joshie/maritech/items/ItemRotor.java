package joshie.maritech.items;

import joshie.mariculture.api.core.MaricultureTab;
import joshie.mariculture.core.Core;
import joshie.mariculture.core.helpers.cofh.BlockHelper;
import joshie.mariculture.core.items.ItemMCDamageable;
import joshie.mariculture.core.lib.MetalMeta;
import joshie.maritech.lib.MTModInfo;
import joshie.maritech.tile.TileGenerator;
import joshie.maritech.tile.TileRotor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemRotor extends ItemMCDamageable {
    private int meta;

    public ItemRotor(int dmg, int meta) {
        super(MTModInfo.MODPATH, MaricultureTab.tabFactory, dmg);
        this.meta = meta;
    }

    @Override
    public boolean onItemUse(ItemStack held, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking() && held != null && world.getBlockMetadata(x, y, z) == MetalMeta.BASE_IRON) {
            if (held.getItem() instanceof ItemRotor) {
                if (setBlock(held, world, x, y, z, ForgeDirection.getOrientation(side))) {
                    held.stackSize--;
                    return true;
                }
            }
        }

        return false;
    }

    public boolean setBlock(ItemStack stack, World world, int x, int y, int z, ForgeDirection orientation) {
        if (!world.setBlock(x, y, z, Core.renderedMachines, meta, 2)) return false;
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileRotor) {
            ((TileRotor) tile).setFacing(orientation);
            ((TileRotor) tile).setDamage(stack.getItemDamage());
            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                TileEntity other = BlockHelper.getAdjacentTileEntity(tile, dir);
                if (other instanceof TileRotor) {
                    ((TileRotor) other).recheck();
                } else if (other instanceof TileGenerator) {
                    ((TileGenerator) other).reset();
                }
            }
        }

        return true;
    }
}
