package mariculture.core.items;

import mariculture.api.core.MaricultureRegistry;
import mariculture.core.Mariculture;
import mariculture.core.gui.GuiGuideFishing;
import mariculture.core.lib.GuiIds;
import mariculture.core.lib.GuideMeta;
import mariculture.core.lib.Modules;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemGuide extends ItemMariculture {
	public ItemGuide(int id) {
		super(id);
	}
	
	public String getName(ItemStack stack) {
		switch(stack.getItemDamage()) {
			case GuideMeta.FISHING:
				return "fishing";
			default:
				return "guide";
		}
	}
	
	public static Object getGui(int meta) {
		if(meta == GuideMeta.FISHING) {
			return new GuiGuideFishing();
		}
		
		return null;
	}
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(world.isRemote) {
			player.openGui(Mariculture.instance, GuiIds.GUIDE, world, stack.getItemDamage(), 0, 0);
		}
		
        return stack;
    }
	
	@Override
	public boolean isActive(int meta) {
		switch(meta) {
		 case GuideMeta.FISHING:
			 return Modules.fishery.isActive();
		default:
			return true;
		}
	}
	
	@Override
	public int getMetaCount() {
		return GuideMeta.COUNT;
	}
	
	@Override
	public void register() {
		for (int j = 0; j < this.getMetaCount(); j++) {
			MaricultureRegistry.register(getName(new ItemStack(this.itemID, 1, j)) + "Guide", new ItemStack(this.itemID, 1, j));
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return getUnlocalizedName() + "." + getName(itemstack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		icons = new Icon[getMetaCount()];

		for (int i = 0; i < icons.length; i++) {
			if(isActive(i)) {
				icons[i] = iconRegister.registerIcon(Mariculture.modid + ":" + getName(new ItemStack(this.itemID, 1, i)) + "Guide");
			}
		}
	}
}
