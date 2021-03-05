package studio.archetype.chonky.client.extensions.mixin;

import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldChunk.class)
public class WorldChunkMixin implements WorldChunkExt {

    private String data;

    @Override
    public String getData() {
        return data;
    }

    @Override
    public void setData(String data) {
        this.data = data;
    }
}
