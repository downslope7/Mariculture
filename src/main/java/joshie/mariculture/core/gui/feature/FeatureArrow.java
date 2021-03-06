package joshie.mariculture.core.gui.feature;

import joshie.mariculture.core.gui.GuiMariculture;
import joshie.mariculture.core.util.IProgressable;

public class FeatureArrow extends Feature {
    private IProgressable machine;
    private int xPoz;
    private int yPoz;

    public FeatureArrow(IProgressable machine, int x, int y) {
        this.machine = machine;
        xPoz = x;
        yPoz = y;
    }

    @Override
    public void draw(GuiMariculture gui, int x, int y, int mouseX, int mouseY) {
        super.draw(gui, x, y, mouseX, mouseY);
        gui.drawTexturedModalRect(x + xPoz, y + yPoz, 198, 74, 22, 16);

        int progress = machine.getProgressScaled(22);
        gui.drawTexturedModalRect(x + xPoz, y + yPoz, 176, 74, progress, 16);
    }
}
