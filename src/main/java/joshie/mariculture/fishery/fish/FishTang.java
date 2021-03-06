package joshie.mariculture.fishery.fish;

import static joshie.mariculture.api.core.Environment.Salinity.SALINE;
import static joshie.mariculture.core.lib.MCLib.dropletAqua;
import static joshie.mariculture.core.lib.MCLib.dropletWater;
import static joshie.mariculture.core.lib.MCLib.lapis;
import joshie.mariculture.api.core.Environment.Height;
import joshie.mariculture.api.core.Environment.Salinity;
import joshie.mariculture.api.fishery.RodType;
import joshie.mariculture.api.fishery.fish.FishSpecies;
import joshie.mariculture.core.util.Fluids;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class FishTang extends FishSpecies {
    @Override
    public int getTemperatureBase() {
        return 33;
    }

    @Override
    public int getTemperatureTolerance() {
        return 11;
    }

    @Override
    public Salinity getSalinityBase() {
        return SALINE;
    }

    @Override
    public int getSalinityTolerance() {
        return 1;
    }

    @Override
    public boolean isDominant() {
        return false;
    }

    @Override
    public int getLifeSpan() {
        return 9;
    }

    @Override
    public int getFertility() {
        return 4000;
    }

    @Override
    public int getWaterRequired() {
        return 25;
    }
    
    @Override
    public boolean isValidWater(Block block) {
        return super.isValidWater(block) || Fluids.isHalfway(block);
    }

    @Override
    public void addFishProducts() {
        addProduct(dropletWater, 6.5D);
        addProduct(dropletAqua, 4.5D);
        addProduct(lapis, 2.0D);
    }

    @Override
    public double getFishOilVolume() {
        return 0.725D;
    }

    @Override
    public ItemStack getLiquifiedProduct() {
        return lapis;
    }

    @Override
    public int getLiquifiedProductChance() {
        return 8;
    }

    @Override
    public void onConsumed(World world, EntityPlayer player) {
        player.addPotionEffect(new PotionEffect(Potion.jump.id, 80, 0));
    }

    @Override
    public boolean canWorkAtThisTime(boolean isDay) {
        return isDay;
    }

    @Override
    public RodType getRodNeeded() {
        return RodType.GOOD;
    }

    @Override
    public double getCatchChance(World world, int height) {
        return Height.isShallows(height) ? 25D : 0D;
    }
}
