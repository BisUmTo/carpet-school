package it.bisumto.carpetschool.mixins;

import it.bisumto.carpetschool.config.Config;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "onMouseScroll",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerAbilities;setFlySpeed(F)V"),
            cancellable = true
    )
    void onMouseScrollMixin(long window, double horizontal, double vertical, CallbackInfo ci){
        if(!Config.getInstance().SPECTATORWHEEL) ci.cancel();
    }
}
