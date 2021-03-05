package studio.archetype.chonky.client.screens;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.codecs.PairCodec;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class ChunkDebugScreen extends Screen {

    private static final TranslatableText TITLE = new TranslatableText("gui.chonky.debug.title");

    private ChunkDebugGridWidget grid;

    public ChunkDebugScreen() {
        super(new TranslatableText("gui.chonky.debug.title"));
    }

    @Override
    protected void init() {
        this.grid = new ChunkDebugGridWidget(this.width / 2, this.height / 2);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float delta) {
        renderBackground(stack);

        drawCenteredText(stack, this.textRenderer, TITLE, this.width / 2, 10, 16777215);

        this.grid.render(stack, mouseX, mouseY, delta);

        Pair<ChunkDebugGridWidget.ChunkDrawState, Pair<Integer, Integer>> chunkData = this.grid.getChunkInfo(mouseX, mouseY);
        if(chunkData != null)
            this.renderTooltip(
                    stack,
                    Lists.newArrayList(
                            new TranslatableText("gui.chonky.debug.tile.coords", chunkData.getSecond().getFirst() - 5, chunkData.getSecond().getSecond() - 5).setStyle(Style.EMPTY.withFormatting(Formatting.GRAY)),
                            new TranslatableText("gui.chonky.debug.tile.status", chunkData.getFirst().text()).setStyle(Style.EMPTY.withFormatting(Formatting.GRAY))),
                    mouseX, mouseY);

        super.render(stack, mouseX, mouseY, delta);
    }
}
