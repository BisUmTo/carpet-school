package it.bisumto.carpetschool.mixins;

import it.bisumto.carpetschool.CarpetSchool;
import it.bisumto.carpetschool.config.Config;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "render",
            at = @At(value = "RETURN")
    )
    void renderSchoolName(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci){
        drawStringWithShadow(matrices, this.textRenderer, Config.getInstance().SCHOOLNAME, 2, 2, 16777215);
    }

    @Inject(method = "mouseClicked",
            at = @At(value = "RETURN", ordinal = 2)
    )
    void reloadConfig(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir){
        int textWidth = this.textRenderer.getWidth(Config.getInstance().SCHOOLNAME);
        if (mouseX < (double)textWidth && mouseY < (double)10) {
            CarpetSchool.reloadConfig();
            this.client.setScreen((Screen)null);
        }
    }
}
