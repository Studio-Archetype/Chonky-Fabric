package studio.archetype.chonky.client.screens;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import studio.archetype.chonky.Chonky;

import java.util.ArrayList;
import java.util.List;

public class ChunkDebugGridWidget extends DrawableHelper implements Drawable, Element {

    private static final Identifier CHUNK_GRID_TEXTURE = Chonky.id("textures/gui/debug_grid.png");
    private static final int SIDE_LENGTH = 100;

    private final int x, y;
    private final MinecraftClient client;
    private final TextureManager textureManager;

    private final List<ChunkDrawState> stateList = new ArrayList<>();

    public ChunkDebugGridWidget(int x, int y) {
        this.x = x - SIDE_LENGTH / 2;
        this.y = y - SIDE_LENGTH / 2;
        this.client = MinecraftClient.getInstance();
        this.textureManager = client.getTextureManager();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        textureManager.bindTexture(CHUNK_GRID_TEXTURE);
        drawTexture(matrices, x, y, 0, 0, SIDE_LENGTH, SIDE_LENGTH, SIDE_LENGTH, SIDE_LENGTH);

        assert this.client.player != null;
        updateList(this.client.player.clientWorld);

        for(int i = 0; i < 11; i++) {
            for(int ii = 0; ii < 11; ii++) {
                int index = ii + (i * 11);
                if(index >= stateList.size())
                    continue;
                ChunkDrawState state = stateList.get(index);
                drawChunk(matrices, this.x, this.y, ii, i, state);
            }
        }
    }

    public Pair<ChunkDrawState, Pair<Integer, Integer>> getChunkInfo(double mouseX, double mouseY) {
        if(mouseX < this.x + 1 || mouseX > this.x + SIDE_LENGTH - 1 || mouseY < this.y + 1 || mouseY > this.y + SIDE_LENGTH - 1)
            return null;

        int xChunk = (int)(mouseX - x) / 8;
        int yChunk = (int)(mouseY - y) / 8;

        int index = xChunk + (yChunk * 11);
        ChunkDrawState state = stateList.get(index);
        return new Pair<>(state, new Pair<>(xChunk, yChunk));
    }

    private void updateList(World w) {
        this.stateList.clear();
        for(int i = 0; i < 11; i++) {
            for(int ii = 0; ii < 11; ii++) {
                int chunkX = ii - 5;
                int chunkY = i - 5;
                WorldChunk chunk = w.getChunk(chunkX, chunkY);
                if(chunk.isEmpty())
                    this.stateList.add(ChunkDrawState.MISSING);
                else
                    this.stateList.add(ChunkDrawState.WORKING);
            }
        }
    }

    private void drawChunk(MatrixStack stack, int x, int y, int chunkX, int chunkY, ChunkDrawState state) {
        int drawX = x + (chunkX * state.size) + (chunkX + 1);
        int drawY = y + (chunkY * state.size) + (chunkY + 1);

        state.bind(this.textureManager);
        drawTexture(stack, drawX, drawY, 0, 0, state.size, state.size, state.size, state.size);

        if(chunkX == 5 && chunkY == 5) {
            ChunkDrawState.CENTER.bind(textureManager);
            int centerSize = ChunkDrawState.CENTER.size;
            drawTexture(stack, drawX + ((state.size - centerSize) / 2), drawY + ((state.size - centerSize) / 2), 0, 0, centerSize, centerSize, centerSize, centerSize);
        }
    }

    public enum ChunkDrawState {
        MISSING(Chonky.id("textures/gui/debug_chunk_missing.png"), 8, new TranslatableText("gui.chonky.debug.tile.state.missing").setStyle(Style.EMPTY.withFormatting(Formatting.RED))),
        WORKING(Chonky.id("textures/gui/debug_chunk_working.png"), 8, new TranslatableText("gui.chonky.debug.tile.state.processing").setStyle(Style.EMPTY.withFormatting(Formatting.YELLOW))),
        OK(Chonky.id("textures/gui/debug_chunk_ok.png"), 8, new TranslatableText("gui.chonky.debug.tile.state.ok").setStyle(Style.EMPTY.withFormatting(Formatting.GREEN))),
        CENTER(Chonky.id("textures/gui/debug_chunk_center.png"), 4, null);

        private final Identifier texture;
        private final int size;
        private final Text text;

        ChunkDrawState(Identifier id, int size, Text clear) {
            this.texture = id;
            this.size = size;
            this.text = clear;
        }

        public void bind(TextureManager manager) {
            manager.bindTexture(this.texture);
        }

        public Text text() {
            return text;
        }
    }
}
