package joshie.mariculture.core.tile;

import joshie.mariculture.Mariculture;
import joshie.mariculture.core.config.Machines.Client;
import joshie.mariculture.core.helpers.PlayerHelper;
import joshie.mariculture.core.helpers.cofh.BlockHelper;
import joshie.mariculture.core.network.PacketHandler;
import joshie.mariculture.core.network.PacketParticle;
import joshie.mariculture.core.network.PacketParticle.Particle;
import joshie.mariculture.core.tile.base.TileStorage;
import joshie.mariculture.core.util.XPRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class TileAutohammer extends TileStorage {
    private int offset = -1;
    public boolean[] up;
    public float[] angle;

    public TileAutohammer() {
        inventory = new ItemStack[4];
        up = new boolean[4];
        angle = new float[4];
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    public boolean onTick(int i) {
        return (worldObj.getWorldTime() + offset) % i == 0;
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (worldObj.isRemote && inventory[slot] == null) {
            angle[slot] = 0.0F;
            up[slot] = false;
        }

        super.setInventorySlotContents(slot, stack);
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)) {
            if (Client.HAMMER_ANIM && worldObj.isRemote && canConsume()) {
                if (offset < 0) {
                    for (int i = 0; i < angle.length; i++) {
                        angle[i] = 5F + worldObj.rand.nextInt(4);
                    }

                    offset = 1;
                }

                for (int i = 0; i < angle.length; i++) {
                    if (up[i]) {
                        if (angle[i] <= 5F) {
                            up[i] = false;
                        } else angle[i] -= 2F;
                    } else {
                        if (angle[i] >= 56F) {
                            up[i] = true;
                        } else angle[i] += 5F;
                    }
                }
            }

            if (!worldObj.isRemote) {
                if (offset < 0) {
                    offset = worldObj.rand.nextInt(80);
                }

                if (onTick(50) && canConsume()) {
                    for (int i = 0; i < inventory.length; i++) {
                        if (inventory[i] != null) {
                            ForgeDirection dir = ForgeDirection.values()[i + 2];
                            TileEntity tile = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord - 1, zCoord + dir.offsetZ);
                            if (tile instanceof TileAnvil) {
                                TileAnvil anvil = (TileAnvil) tile;
                                //Created a fake player and add the levels that are stored in to the tank in to their experience
                                FakePlayer player = PlayerHelper.getFakePlayer(worldObj);
                                player.addExperience(getExperience());

                                int ret = ((TileAnvil) tile).workItem(player, inventory[i]);
                                if (ret >= 5000) {
                                    drainExperience(ret - 5000);
                                    //Perform huge explosion effect
                                    worldObj.playSoundEffect(anvil.xCoord, anvil.yCoord, anvil.zCoord, Mariculture.modid + ":bang", 1.0F, 1.0F);
                                    PacketHandler.sendAround(new PacketParticle(Particle.EXPLODE_LRG, 1, anvil.xCoord, anvil.yCoord + 0.5, anvil.zCoord), worldObj.provider.dimensionId, anvil.xCoord, tile.yCoord + 1, anvil.zCoord);
                                } else if (ret < 0) {
                                    //Perform small explosion effect
                                    drainExperience(1);
                                    worldObj.playSoundEffect(anvil.xCoord, anvil.yCoord, anvil.zCoord, Mariculture.modid + ":hammer", 1.0F, 1.0F);
                                    PacketHandler.sendAround(new PacketParticle(Particle.EXPLODE_SML, 1, anvil.xCoord, anvil.yCoord + 0.5, anvil.zCoord), worldObj.provider.dimensionId, anvil.xCoord, tile.yCoord + 1, anvil.zCoord);
                                } else if (ret != 0) {
                                    drainExperience(ret);
                                    //Perform small explosion effect
                                    worldObj.playSoundEffect(anvil.xCoord, anvil.yCoord, anvil.zCoord, Mariculture.modid + ":hammer", 1.0F, 1.0F);
                                    PacketHandler.sendAround(new PacketParticle(Particle.EXPLODE_SML, 21, anvil.xCoord, anvil.yCoord, anvil.zCoord), worldObj.provider.dimensionId, anvil.xCoord, tile.yCoord + 1, anvil.zCoord);
                                }

                                if (ret != 0) {
                                    if (inventory[i].attemptDamageItem(1, worldObj.rand)) {
                                        inventory[i] = null;
                                    }

                                    markDirty();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void drainExperience(int xp) {
        TileEntity tile = BlockHelper.getAdjacentTileEntity(this, ForgeDirection.DOWN);
        if (tile instanceof IFluidHandler) {
            ((IFluidHandler) tile).drain(ForgeDirection.UP, XPRegistry.getFluidValueOfXP(((IFluidHandler) tile).drain(ForgeDirection.UP, Integer.MAX_VALUE, false), xp), true);
        }
    }

    private int getExperience() {
        TileEntity tile = BlockHelper.getAdjacentTileEntity(this, ForgeDirection.DOWN);
        if (tile instanceof IFluidHandler) {
            //Get the entire volume of fluid in the tank
            FluidStack fluid = ((IFluidHandler) tile).drain(ForgeDirection.UP, Integer.MAX_VALUE, false);
            if (XPRegistry.isXP(fluid)) {
                //Convert this volume in to raw experience
                return XPRegistry.getXPValueOfFluid(fluid);
            }
        }

        return 0;
    }

    private boolean canConsume() {
        return getExperience() > 0;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    @Override
    public void markDirty() {
        super.markDirty();

        if (!worldObj.isRemote) {
            PacketHandler.syncInventory(this, inventory);
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }
}
