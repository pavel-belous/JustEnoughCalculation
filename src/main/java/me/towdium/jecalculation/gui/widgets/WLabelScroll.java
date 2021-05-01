package me.towdium.jecalculation.gui.widgets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.towdium.jecalculation.gui.JecaGui;
import me.towdium.jecalculation.data.label.ILabel;
import me.towdium.jecalculation.utils.Utilities;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Author: towdium
 * Date:   17-9-17.
 */
@ParametersAreNonnullByDefault
@SideOnly(Side.CLIENT)
public class WLabelScroll extends WContainer implements ISearchable {
    protected List<ILabel> labels = new ArrayList<>();
    protected List<ILabel> filtered = new ArrayList<>();
    protected WLabelGroup labelGroup;
    protected WScroll scroll;
    protected int xPos, yPos, column, row, current;
    protected String filter = "";
    protected ListenerValue<? super WLabelScroll, ILabel> listener;

    public WLabelScroll(int xPos, int yPos, int column, int row, WLabel.Mode mode, boolean drawConnection) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.column = column;
        this.row = row;
        labelGroup = new WLabelGroup(xPos, yPos, column, row, mode).setListener((i, v) -> {
            if (listener != null) listener.invoke(this, i.get(v));
        });
        scroll = new WScroll(xPos + column * 18 + 4, yPos, row * 18).setListener(i -> update(i.getCurrent()));
        add(labelGroup);
        add(scroll);
        if (drawConnection) add(new WRectangle(xPos + column * 18, yPos, 4, row * 18, JecaGui.COLOR_GUI_GREY));
    }

    public void update(float f) {
        if (f < 0) f = 0;
        if (f > 1) f = 1;
        int step = getStepAmount();
        current = (int) (step * f);
        if (current == step) current--;
        labelGroup.setLabel(filtered, current * column);
    }

    @Override
    public boolean onScroll(JecaGui gui, int xMouse, int yMouse, int diff) {
        boolean in = JecaGui.mouseIn(xPos, yPos, column * 18, row * 18, xMouse, yMouse);
        if (in) {
            float pos = getPos(current - diff);
            scroll.setCurrent(pos);
            update(pos);
        }
        return in;
    }

    private float getPos(int step) {
        return 1f / (getStepAmount() - 1) * step;
    }

    public WLabelScroll setLabels(List<ILabel> labels) {
        this.labels = labels;
        setFilter(filter);
        return this;
    }

    public boolean setFilter(String str) {
        filter = str;
        filtered = labels.stream().filter(l -> Utilities.I18n.contains(l.getDisplayName().toLowerCase(), str.toLowerCase()))
                         .collect(Collectors.toList());
        scroll.setCurrent(0);
        update(0);
        return filtered.size() != 0;
    }

    @SuppressWarnings("UnusedReturnValue")
    public WLabelScroll setListener(ListenerValue<? super WLabelScroll, ILabel> listener) {
        this.listener = listener;
        return this;
    }

    private int getStepAmount() {
        int line = (labels.size() + column - 1) / column;
        return Math.max(line - row + 1, 1);
    }
}
