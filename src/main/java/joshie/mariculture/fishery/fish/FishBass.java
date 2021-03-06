package joshie.mariculture.fishery.fish;

import static joshie.mariculture.api.core.Environment.Salinity.FRESH;
import static joshie.mariculture.core.lib.MCLib.dropletDestroy;
import static joshie.mariculture.core.lib.MCLib.dropletPlant;
import static joshie.mariculture.core.lib.MCLib.dropletWater;
import static joshie.mariculture.core.lib.MCLib.gunpowder;

import java.util.Random;

import joshie.mariculture.api.core.Environment.Height;
import joshie.mariculture.api.core.Environment.Salinity;
import joshie.mariculture.api.fishery.RodType;
import joshie.mariculture.api.fishery.fish.FishSpecies;
import joshie.mariculture.fishery.EntityBass;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FishBass extends FishSpecies {
    @Override
    public int getTemperatureBase() {
        return 15;
    }

    @Override
    public int getTemperatureTolerance() {
        return 8;
    }

    @Override
    public Salinity getSalinityBase() {
        return FRESH;
    }

    @Override
    public int getSalinityTolerance() {
        return 2;
    }

    @Override
    public boolean isDominant() {
        return true;
    }

    @Override
    public int getLifeSpan() {
        return 16;
    }

    @Override
    public int getFertility() {
        return 2000;
    }

    @Override
    public int getWaterRequired() {
        return 45;
    }

    @Override
    public void addFishProducts() {
        addProduct(dropletWater, 4D);
        addProduct(dropletDestroy, 5.0D);
        addProduct(dropletPlant, 3.0D);
        addProduct(gunpowder, 7.5D);
    }

    @Override
    public double getFishOilVolume() {
        return 4.325D;
    }

    @Override
    public ItemStack getLiquifiedProduct() {
        return new ItemStack(gunpowder);
    }

    @Override
    public int getFishMealSize() {
        return 5;
    }

    @Override
    public int getFoodStat() {
        return 1;
    }

    @Override
    public float getFoodSaturation() {
        return 0.1F;
    }

    @Override
    public int getFoodDuration() {
        return 1;
    }

    @Override
    public boolean canAlwaysEat() {
        return true;
    }

    @Override
    public ItemStack onRightClick(World world, EntityPlayer player, ItemStack stack, Random rand) {
        if (!player.capabilities.isCreativeMode) {
            --stack.stackSize;
        }

        world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (rand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote) {
            world.spawnEntityInWorld(new EntityBass(world, player));
        }

        return stack;
    }

    @Override
    public RodType getRodNeeded() {
        return RodType.GOOD;
    }

    @Override
    public double getCatchChance(World world, int height) {
        return Height.isOverground(height) ? 15D : 5D;
    }
}
