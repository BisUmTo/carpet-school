package it.bisumto.carpetschool.mixins;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {
    @Mutable
    @Accessor("multiplayerEnabled")
    boolean getMultiplayerEnabled();

    @Mutable
    @Accessor("multiplayerEnabled")
    void setMultiplayerEnabled(boolean multiplayerEnabled);

    @Mutable
    @Accessor("onlineChatEnabled")
    boolean getOnlineChatEnabled();

    @Mutable
    @Accessor("onlineChatEnabled")
    void setOnlineChatEnabled(boolean onlineChatEnabled);
}
