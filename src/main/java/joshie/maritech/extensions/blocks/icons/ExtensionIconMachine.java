package joshie.maritech.extensions.blocks.icons;

import static joshie.maritech.lib.SpecialIcons.generatorBack;
import static joshie.maritech.lib.SpecialIcons.sluiceAdvanced;
import static joshie.maritech.lib.SpecialIcons.sluiceAdvancedBack;
import static joshie.maritech.lib.SpecialIcons.sluiceAdvancedDown;
import static joshie.maritech.lib.SpecialIcons.sluiceAdvancedUp;
import static joshie.maritech.lib.SpecialIcons.sluiceBack;
import static joshie.maritech.lib.SpecialIcons.sluiceDown;
import static joshie.maritech.lib.SpecialIcons.sluiceUp;
import joshie.mariculture.core.Core;
import joshie.mariculture.core.blocks.BlockMachine;
import joshie.mariculture.core.lib.MachineMeta;
import joshie.mariculture.core.lib.MetalMeta;
import joshie.maritech.tile.TileGenerator;
import joshie.maritech.tile.TileSluice;
import joshie.maritech.tile.TileSluiceAdvanced;
import joshie.maritech.util.IIconExtension;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ExtensionIconMachine implements IIconExtension {
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getInventoryIcon(int meta, int side, IIcon icon) {
        if (meta == MachineMeta.SLUICE) return side == 3 ? ((BlockMachine) Core.machines).icons[MachineMeta.SLUICE] : Core.metals.getIcon(side, MetalMeta.BASE_IRON);
        else if (meta == MachineMeta.GENERATOR) return side == 3 ? ((BlockMachine) Core.machines).icons[MachineMeta.GENERATOR] : Core.metals.getIcon(side, MetalMeta.BASE_IRON);
        else if (meta == MachineMeta.SLUICE_ADVANCED) return side == 3 ? ((BlockMachine) Core.machines).icons[MachineMeta.SLUICE_ADVANCED] : sluiceAdvanced;
        else if (side < 2 && meta == MachineMeta.SPONGE) {
            return Core.metals.getIcon(side, MetalMeta.BASE_IRON);
        }

        return icon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getWorldIcon(IBlockAccess block, int x, int y, int z, int side, IIcon icon) {
        TileEntity tile = block.getTileEntity(x, y, z);
        if (tile instanceof TileSluice) {
            TileSluice sluice = (TileSluice) tile;
            if (tile instanceof TileSluiceAdvanced) {
                if (sluice.orientation.ordinal() == side) return side > 1 ? ((BlockMachine) Core.machines).icons[MachineMeta.SLUICE_ADVANCED] : sluiceAdvancedUp;
                else if (sluice.orientation.getOpposite().ordinal() == side) return side > 1 ? sluiceAdvancedBack : sluiceAdvancedDown;
                else return sluiceAdvanced;
            } else {
                if (sluice.orientation.ordinal() == side) return side > 1 ? ((BlockMachine) Core.machines).icons[MachineMeta.SLUICE] : sluiceUp;
                else if (sluice.orientation.getOpposite().ordinal() == side) return side > 1 ? sluiceBack : sluiceDown;
                else return Core.metals.getIcon(side, MetalMeta.BASE_IRON);
            }
        } else if (tile instanceof TileGenerator) {
            TileGenerator generator = (TileGenerator) tile;
            return (generator.orientation.ordinal() == side) ? ((BlockMachine) Core.machines).icons[MachineMeta.GENERATOR] : generator.orientation.getOpposite().ordinal() == side ? generatorBack : Core.metals.getIcon(side, MetalMeta.BASE_IRON);
        } else return icon;
    }
}
