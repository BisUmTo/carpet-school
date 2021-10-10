package it.bisumto.carpetschool.mixins;

import it.bisumto.carpetschool.config.Config;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {
    @Shadow
    private CyclingButtonWidget<Boolean> enableCheatsButton;

    @Inject(method = "tweakDefaultsTo",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;updateSettingsLabels()V")
    )
    void disableOpenToLan(CallbackInfo ci){
        if(!Config.getInstance().COMMANDS) {
            this.enableCheatsButton.active = false;
            this.enableCheatsButton.setValue(false);
        }
    }
}
