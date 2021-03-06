package joshie.mariculture.core.blocks;

import java.util.Random;

import joshie.mariculture.Mariculture;
import joshie.mariculture.core.Core;
import joshie.mariculture.core.blocks.base.BlockFluid;
import joshie.mariculture.core.helpers.BlockHelper;
import joshie.mariculture.core.helpers.PlayerHelper;
import joshie.mariculture.core.lib.BottleMeta;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPressurisedWater extends BlockFluid {
    public static IIcon[] still;
    public static IIcon[] flowing;
    public static IIcon xp_still;
    public static IIcon xp_flow;

    public BlockPressurisedWater(Fluid fluid, Material material) {
        super(fluid, material);
        quantaPerBlock = 16;
        quantaPerBlockFloat = 16;
    }

    @Override
    public FluidStack drain(World world, int x, int y, int z, boolean doDrain) {
        return null;
    }

    private int tick;

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        super.updateTick(world, x, y, z, rand);
        tick++;

        if (tick % 15 == 0 && !world.isRemote && world instanceof WorldServer) {
            ForgeDirection dir = ForgeDirection.values()[rand.nextInt(6)];
            int x2 = x + dir.offsetX;
            int y2 = y - rand.nextInt(2);
            int z2 = z + dir.offsetZ;
            Block block = world.getBlock(x2, y2, z2);
            if (block != null && !(block instanceof BlockPressurisedWater) && !block.hasTileEntity(world.getBlockMetadata(x2, y2, z2))) {
                float resistanceMax = Blocks.stone.getExplosionResistance(PlayerHelper.getFakePlayer(world), world, x2, y2, z2, 0, 0, 0);
                float resistance = block.getExplosionResistance(PlayerHelper.getFakePlayer(world), world, x2, y2, z2, 0, 0, 0);
                if (resistance <= resistanceMax) {
                    BlockHelper.destroyBlock(world, x2, y2, z2);
                }
            }
        }
    }

    @Override
    public void velocityToAddToEntity(World world, int x, int y, int z, Entity entity, Vec3 vec) {
        Vec3 vec_flow = getFlowVector(world, x, y, z);

        double xSpeed = vec_flow.xCoord * 0.0845;
        double zSpeed = vec_flow.zCoord * 0.0845;

        entity.addVelocity(xSpeed, 0, zSpeed);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return meta == 0 ? Blocks.water.getIcon(side, meta) : Blocks.water.getIcon(side, meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        flowing = new IIcon[BottleMeta.COUNT];
        still = new IIcon[BottleMeta.COUNT];

        for (int i = 0; i < flowing.length; i++) {
            ItemStack bottle = new ItemStack(Core.bottles, 1, i);
            FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(bottle);
            if (fluid != null && fluid.getFluid() != null && fluid.getFluid().getName() != null) {
                String name = fluid.getFluid().getName().replaceAll("\\.", "");

                if (name.contains("molten")) {
                    name = "molten" + name.replaceAll("molten", "");
                }

                if (name.contains("fish")) {
                    name = "fish" + name.replaceAll("fish", "");
                }

                if (name.contains("natural")) {
                    name = "natural" + name.replaceAll("natural", "");
                }

                if (!name.contains("normal") && !name.contains("water") && !name.contains("lava") && !name.contains("dirt")) {
                    flowing[i] = iconRegister.registerIcon(Mariculture.modid + ":liquids/" + name + "_flow");
                    still[i] = iconRegister.registerIcon(Mariculture.modid + ":liquids/" + name + "_still");
                }
            }
        }

        xp_still = iconRegister.registerIcon(Mariculture.modid + ":liquids/xp_flow");
        xp_flow = iconRegister.registerIcon(Mariculture.modid + ":liquids/xp_still");
    }
}
