package me.towdium.jecalculation.gui.widgets;

import me.towdium.jecalculation.gui.JecaGui;
import me.towdium.jecalculation.utils.Utilities.I18n;
import org.lwjgl.input.Keyboard;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

import static me.towdium.jecalculation.gui.JecaGui.Font.PLAIN;
import static me.towdium.jecalculation.gui.JecaGui.Font.SHADOW;
import static me.towdium.jecalculation.gui.Resource.*;

/**
 * Author: Towdium
 * Date: 18-9-23
 */
@ParametersAreNonnullByDefault
public class WHelp extends WTooltip {
    protected String key;

    public WHelp(String content) {
        super("common.help");
        key = content;
    }

    @Override
    public void onDraw(JecaGui gui, int xMouse, int yMouse) {
        gui.drawResourceContinuous(WGT_PANEL_N, -21, 0, 25, 24, 4);
        gui.drawResource(WGT_HELP_N, -19, 2);
    }

    @Override
    public boolean mouseIn(int xMouse, int yMouse) {
        return JecaGui.mouseIn(-21, 0, 24, 24, xMouse, yMouse);
    }

    @Override
    public boolean onClicked(JecaGui gui, int xMouse, int yMouse, int button) {
        boolean ret = mouseIn(xMouse, yMouse);
        if (ret)
            gui.root.add(new Doc());
        return ret;
    }

    private class Doc extends WContainer {
        public Doc() {
            Text tContent = new Text();
            WSwitcher sPage = new WSwitcher(7, 146, 162, tContent.amount());
            sPage.setListener(i -> tContent.setPage(i.getIndex()));
            WText tTitle = new WText(7, 7, SHADOW, I18n.format("gui." + WHelp.this.key + ".title"));
            addAll(new WPanel(), new Icon(), tTitle, tContent, sPage);
        }

        @Override
        public boolean onClicked(JecaGui gui, int xMouse, int yMouse, int button) {
            if (!super.onClicked(gui, xMouse, yMouse, button))
                gui.root.remove(this);
            return true;
        }

        public class Text implements IWidget {
            List<List<String>> pages = new ArrayList<>();
            int page;

            public Text() {
                List<String> ss = I18n.wrap(I18n.format("gui." + WHelp.this.key + ".help"), 162);
                List<String> tmp = new ArrayList<>();
                int count = 0;
                for (String s : ss) {
                    if (s.endsWith("\f")) {
                        tmp.add(s.substring(0, s.length() - 1));
                        pages.add(tmp);
                        tmp = new ArrayList<>();
                        count = 0;
                    } else if (count == 11) {
                        tmp.add(s);
                        pages.add(tmp);
                        tmp = new ArrayList<>();
                        count = 0;
                    } else {
                        tmp.add(s);
                        count++;
                    }
                }
                if (!tmp.isEmpty())
                    pages.add(tmp);
            }

            @Override
            public void onDraw(JecaGui gui, int xMouse, int yMouse) {
                gui.drawSplitText(7, 21, PLAIN, pages.get(page));
            }

            public int amount() {
                return pages.size();
            }

            public void setPage(int i) {
                page = i;
            }
        }

        public class Icon extends WTooltip {
            public Icon() {
                super("common.close");
            }

            @Override
            public void onDraw(JecaGui gui, int xMouse, int yMouse) {
                gui.drawResourceContinuous(WGT_HELP_B, -21, 0, 25, 24, 4);
                gui.drawResource(WGT_HELP_F, -19, 2);
            }

            @Override
            public boolean onClicked(JecaGui gui, int xMouse, int yMouse, int button) {
                boolean ret = mouseIn(xMouse, yMouse);
                if (ret)
                    gui.root.remove(Doc.this);
                return ret;
            }

            @Override
            public boolean mouseIn(int xMouse, int yMouse) {
                return JecaGui.mouseIn(-21, 0, 24, 24, xMouse, yMouse);
            }

            @Override
            public boolean onKey(JecaGui gui, char ch, int code) {
                if (super.onKey(gui, ch, code))
                    return true;
                if (code == Keyboard.KEY_ESCAPE) {
                    gui.root.remove(Doc.this);
                    return true;
                } else
                    return false;
            }
        }
    }
}
