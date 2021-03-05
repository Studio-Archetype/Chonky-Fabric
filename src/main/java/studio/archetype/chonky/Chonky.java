package studio.archetype.chonky;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class Chonky implements ModInitializer {

    public static final String MOD_ID = "chonky";

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    @Override
    public void onInitialize() {

    }
}
