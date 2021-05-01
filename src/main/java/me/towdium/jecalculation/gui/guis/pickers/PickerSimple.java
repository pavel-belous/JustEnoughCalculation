package me.towdium.jecalculation.gui.guis.pickers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.towdium.jecalculation.gui.Resource;
import me.towdium.jecalculation.gui.guis.IGui;
import me.towdium.jecalculation.gui.widgets.*;
import me.towdium.jecalculation.data.label.ILabel;
import me.towdium.jecalculation.data.label.labels.LFluidStack;
import me.towdium.jecalculation.data.label.labels.LOreDict;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static me.towdium.jecalculation.gui.Resource.ICN_TEXT;

/**
 * Author: towdium
 * Date:   17-9-28.
 */
@ParametersAreNonnullByDefault
@SideOnly(Side.CLIENT)
public class PickerSimple extends IPicker.Impl implements IGui {
    /**
     * @param labels  label to be displayed for selection
     * @param l18nKey localization key for help string,
     *                entire key should be "gui.l18nKey.help.tooltip"
     */
    public PickerSimple(List<ILabel> labels, String l18nKey) {
        WLabelScroll ls = new WLabelScroll(7, 33, 8, 7, WLabel.Mode.PICKER, true).setLabels(labels)
                                                                                 .setListener((i, v) -> notifyLsnr(v));
        add(new WSearch(26, 7, 90, ls));
        add(new WIcon(7, 7, 20, 20, ICN_TEXT, l18nKey + ".text"));
        add(ls);
    }


    public static class FluidStack extends PickerSimple {
        public FluidStack() {
            super(FluidRegistry.getRegisteredFluids().values().stream()
                               .map(fluid -> new LFluidStack(1000, fluid)).collect(Collectors.toList()),
                  "picker_fluid_stack");
        }
    }

    public static class OreDict extends PickerSimple {
        public OreDict() {
            super(generate(), "picker_ore_dict");
        }

        static List<ILabel> generate() {
            List<ILabel> present = new ArrayList<>();
            List<ILabel> empty = new ArrayList<>();
            Arrays.stream(OreDictionary.getOreNames()).map(LOreDict::new).forEach(i -> (i.isEmpty() ? empty : present).add(i));
            present.addAll(empty);
            return present;
        }
    }
}
