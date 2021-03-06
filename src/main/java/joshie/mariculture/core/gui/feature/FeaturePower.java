package joshie.mariculture.core.gui.feature;

import java.util.List;

import joshie.mariculture.core.gui.GuiMariculture;
import joshie.mariculture.core.util.IPowered;

public class FeaturePower extends Feature {
    private IPowered machine;
    private int xPoz;
    private int yPoz;

    public FeaturePower(IPowered machine, int x, int y) {
        this.machine = machine;
        xPoz = x;
        yPoz = y;
    }

    @Override
    public void addTooltip(List tooltip, int mouseX, int mouseY) {
        if (mouseX >= xPoz && mouseX <= xPoz + 13 && mouseY >= yPoz && mouseY <= yPoz + 41) {
            tooltip.add(machine.getPowerText());
            tooltip.add("");
            if (machine.isConsumer()) tooltip.add("Consuming: " + machine.getPowerPerTick() + " RF/t");
            else tooltip.add("Producing: " + machine.getPowerPerTick() + " RF/t");
        }
    }

    @Override
    public void draw(GuiMariculture gui, int x, int y, int mouseX, int mouseY) {
        gui.drawTexturedModalRect(x + xPoz, y + yPoz, 142, 0, 14, 42);

        int power = machine.getPowerScaled(42);
        gui.drawTexturedModalRect(x + xPoz, y + yPoz + 42 - power, 128, 42 - power, 14, power);
    }
}
