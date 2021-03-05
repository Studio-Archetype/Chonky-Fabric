package studio.archetype.chonky.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.world.chunk.WorldChunk;
import org.lwjgl.glfw.GLFW;
import studio.archetype.chonky.client.screens.ChunkDebugScreen;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class ChonkyClient implements ClientModInitializer {

    private KeyBinding openDebugGui;

    @Override
    public void onInitializeClient() {
        if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
            registerDebugCommands();

            this.openDebugGui = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.chonky.debug.open_gui",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_O,
                    "category.chonky.keybinds"
            ));

            ClientTickEvents.END_CLIENT_TICK.register(c -> {
                if(c.player != null) {
                    if(openDebugGui.wasPressed())
                        c.openScreen(new ChunkDebugScreen());
                }
            });
        }
    }

    private void registerDebugCommands() {
        literal("chonky")
                .then(literal("screen")
                        .executes(ctx -> {
                            ctx.getSource().getClient().openScreen(new ChunkDebugScreen());
                            return 1;
                        }));
    }
}
