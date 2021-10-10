package it.bisumto.carpetschool.mixins;

import it.bisumto.carpetschool.config.Config;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin {

    @Inject(method = "initWidgets",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;isIntegratedServerRunning()Z"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    void disableOpenToLan(CallbackInfo ci, String s, ButtonWidget buttonWidget){
        if(!Config.getInstance().MULTIPLAYER) buttonWidget.visible = false;
    }
}
