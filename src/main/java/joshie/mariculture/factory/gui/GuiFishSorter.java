package joshie.mariculture.factory.gui;

import joshie.mariculture.core.gui.GuiMariculture;
import joshie.mariculture.core.gui.feature.FeatureEject;
import joshie.mariculture.core.network.PacketClick;
import joshie.mariculture.core.network.PacketHandler;
import joshie.mariculture.factory.tile.TileFishSorter;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiFishSorter extends GuiMariculture {

    private TileFishSorter tile;

    public GuiFishSorter(InventoryPlayer player, TileFishSorter tile) {
        super(new ContainerFishSorter(tile, player), "fishsorter", 10);
        features.add(new FeatureEject(tile));
        this.tile = tile;
    }

    @Override
    public void drawBackground(int x, int y) {
        if (tile.getInventory()[TileFishSorter.input] == null) {
            drawTexturedModalRect(x + 9, y + 25, 230, 118, 16, 16);
        }

        drawTexturedModalRect(x + 8, y + 53, 220, tile.getDefaultSide() * 18, 18, 18);

        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 7; ++k) {
                int side = tile.getSide(k + j * 7);
                drawTexturedModalRect(x + 37 + k * 18, y + 20 + j * 18, 238, side * 18, 18, 18);
            }
        }
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);

        if (mouseX >= 8 && mouseX <= 25 && mouseY >= 53 && mouseY <= 70) {
            PacketHandler.sendToServer(new PacketClick(tile.xCoord, tile.yCoord, tile.zCoord, TileFishSorter.DFT_SWITCH));
        }
    }
}
