package joshie.mariculture.fishery.fish;

import static joshie.mariculture.api.core.Environment.Salinity.SALINE;
import static joshie.mariculture.core.lib.MCLib.dropletPoison;
import static joshie.mariculture.core.lib.MCLib.dropletWater;
import static joshie.mariculture.core.lib.MCLib.slimeBall;
import joshie.mariculture.api.core.Environment.Salinity;
import joshie.mariculture.api.fishery.RodType;
import joshie.mariculture.api.fishery.fish.FishSpecies;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class FishJelly extends FishSpecies {
    @Override
    public int getTemperatureBase() {
        return 22;
    }

    @Override
    public int getTemperatureTolerance() {
        return 13;
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
        return 1;
    }

    @Override
    public int getFertility() {
        return 1;
    }

    @Override
    public int getFoodConsumption() {
        return 0;
    }

    @Override
    public boolean requiresFood() {
        return false;
    }

    @Override
    public int getWaterRequired() {
        return 65;
    }

    @Override
    public void addFishProducts() {
        addProduct(dropletWater, 3D);
        addProduct(dropletPoison, 6D);
        addProduct(slimeBall, 4D);
    }

    @Override
    public double getFishOilVolume() {
        return 0D;
    }

    @Override
    public int getFishMealSize() {
        return 0;
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
    public void onConsumed(World world, EntityPlayer player) {
        player.addPotionEffect(new PotionEffect(Potion.harm.id, 15, 0));
        player.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0));
    }

    @Override
    public void affectLiving(EntityLivingBase entity) {
        entity.addPotionEffect(new PotionEffect(Potion.poison.id, 1, 200));
    }

    @Override
    public RodType getRodNeeded() {
        return RodType.GOOD;
    }

    @Override
    public double getCatchChance(World world, int height) {
        return world.isDaytime() ? 15D : 5D;
    }
}
