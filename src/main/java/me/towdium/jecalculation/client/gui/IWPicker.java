package me.towdium.jecalculation.client.gui;

import me.towdium.jecalculation.client.gui.drawables.WContainer;
import me.towdium.jecalculation.core.labels.ILabel;
import me.towdium.jecalculation.utils.wrappers.Single;

import java.util.function.Consumer;

/**
 * Author: towdium
 * Date:   17-9-28.
 */
public interface IWPicker extends IWidget {
    IWPicker setCallback(Consumer<ILabel> callback);

    class Simple extends WContainer implements IWPicker {
        protected Single<Consumer<ILabel>> callback = new Single<>(null);

        @Override
        public IWPicker setCallback(Consumer<ILabel> callback) {
            this.callback.value = callback;
            return this;
        }
    }
}
