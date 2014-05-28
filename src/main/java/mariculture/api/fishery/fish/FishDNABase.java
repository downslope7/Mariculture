package mariculture.api.fishery.fish;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mariculture.api.fishery.Fishing;
import mariculture.core.lib.Text;
import mariculture.fishery.Fish;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class FishDNABase {
	protected String category;
	public static final ArrayList<FishDNABase> DNAParts = new ArrayList<FishDNABase>();
	public FishDNABase register() {
		DNAParts.add(this);
		return this;
	}
	
	public String getName() {
		return this.getClass().getSimpleName().substring(7);
	}

	/** The name of the string the egg array saves this in **/
	public String getEggString() {
		return getName() + "List";
	}

	/** The name of the string to save the Dominant part of the gene as **/
	public String getHigherString() {
		return getName();
	}

	/** The name of the string to save the Recessive part of the gene as **/
	public String getLowerString() {
		return "lower" + getName();
	}

	/** Add information about this piece of DNA to the list if necessary **/

	public void getInformationDisplay(ItemStack stack, List list) {
		// Do Nothing
	}

	/** Attempt to cause a mutation **/
	public int[] attemptMutation(int parent1dna, int parent2dna) {
		int[] ret = new int[2];
		ret[0] = parent1dna;
		ret[1] = parent2dna;
		return ret;
	}

	/** return a list of these based on the dominance, dominant goes first **/
	public int[] getDominant(int option1, int option2, Random rand) {
		int[] ret = new int[2];
		ret[0] = option1;
		ret[1] = option2;
		return ret;
	}

	/**
	 * return the data needed for this piece of dna if it's coming from the
	 * species file
	 **/
	public Integer getDNAFromSpecies(FishSpecies species) {
		return -1;
	}
	
	//Everything below this point is mostly irrelevant when adding new dna except in special cases

	/** Automatically called when generating a fish **/
	public ItemStack addDNA(ItemStack stack, Integer data) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}

		if(category == null) {
			stack.stackTagCompound.setInteger(getHigherString(), data);
		} else {
			NBTTagCompound tag = stack.stackTagCompound.getCompoundTag(category);
			if(tag == null) tag = new NBTTagCompound();
			tag.setInteger(getHigherString(), data);
			stack.stackTagCompound.setCompoundTag(category, tag);
		}

		return stack;
	}

	/** Automatically called when generating a fish **/
	public ItemStack addLowerDNA(ItemStack stack, Integer data) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}

		if(category == null) {
			stack.stackTagCompound.setInteger(getLowerString(), data);
		} else {
			NBTTagCompound tag = stack.stackTagCompound.getCompoundTag(category);
			if(tag == null) tag = new NBTTagCompound();
			tag.setInteger(getLowerString(), data);
			stack.stackTagCompound.setCompoundTag(category, tag);
		}

		return stack;
	}

	/** Automatically called when generating a fish **/
	public void addDNAList(ItemStack stack, int[] data) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}

		if(category == null) {
			stack.stackTagCompound.setIntArray(getEggString(), data);
		} else {
			NBTTagCompound tag = stack.stackTagCompound.getCompoundTag(category);
			if(tag == null) tag = new NBTTagCompound();
			tag.setIntArray(getEggString(), data);
			stack.stackTagCompound.setCompoundTag(category, tag);
		}
	}
	
	/** Automatically checks if the egg has the dna it should have **/
	public boolean hasEggData(ItemStack egg) {
		if(category == null) {
			return egg.stackTagCompound.hasKey(getEggString());
		} else {
			NBTTagCompound tag = egg.stackTagCompound.getCompoundTag(category);
			if(tag == null) tag = new NBTTagCompound();
			return tag.hasKey(getEggString());
		}
	}

	/** Automatically called when reading a fish **/
	public Integer getDNA(ItemStack stack) {
		if(stack == null || stack.getItem() == null || !stack.hasTagCompound()) return 0;
		if(category == null) {
			if(!stack.stackTagCompound.hasKey(getHigherString()) && stack.stackTagCompound.hasKey(Fish.species.getHigherString())) {
				FishSpecies species = Fishing.fishHelper.getSpecies(Fish.species.getDNA(stack));
				addDNA(stack, getDNAFromSpecies(species));
			}
			
			return stack.stackTagCompound.getInteger(getHigherString());
		} else {
			NBTTagCompound tag = stack.stackTagCompound.getCompoundTag(category);
			if(tag == null) tag = new NBTTagCompound();
			return tag.getInteger(getHigherString());
		}
	}

	/** Automatically called when reading a fish **/
	public Integer getLowerDNA(ItemStack stack) {
		if(stack == null || stack.getItem() == null || !stack.hasTagCompound()) return 0;
		if(category == null) {
			if(!stack.stackTagCompound.hasKey(getLowerString()) && stack.stackTagCompound.hasKey(Fish.species.getLowerString())) {
				FishSpecies species = Fishing.fishHelper.getSpecies(Fish.species.getLowerDNA(stack));
				addLowerDNA(stack, getDNAFromSpecies(species));
			}
			
			return stack.stackTagCompound.getInteger(getLowerString());
		} else {
			NBTTagCompound tag = stack.stackTagCompound.getCompoundTag(category);
			if(tag == null) tag = new NBTTagCompound();
			return tag.getInteger(getLowerString());
		}
	}

	/** Automatically called when reading a fish **/
	public int[] getDNAList(ItemStack stack) {
		if(category == null) {
			return stack.stackTagCompound.getIntArray(getEggString());
		} else {
			NBTTagCompound tag = stack.stackTagCompound.getCompoundTag(category);
			if(tag == null) tag = new NBTTagCompound();
			return tag.getIntArray(getEggString());
		}
	}
	
	/** Returns the display data for this dna type in the fish scanner, make sure you return a string with three length **/
	public String[] getScannedDisplay(ItemStack stack) {
		return new String[] { Text.translate(getName()), "" + getDNA(stack), "" + getLowerDNA(stack) };
	}
}