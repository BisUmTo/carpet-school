package it.bisumto.carpetschool.mixins;

import it.bisumto.carpetschool.CarpetSchoolRules;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {
    @Shadow @Final
    private GameOptions settings;

    /**
     * @author BisUmTo
     */
    @Overwrite
    @Override
    public void tick(boolean slowDown) {
        if(CarpetSchoolRules.EP8_COORDINATE){
            List<Boolean> lb = new ArrayList<>();
            lb.add(this.settings.keyForward.isPressed());
            lb.add(this.settings.keyRight.isPressed());
            lb.add(this.settings.keyBack.isPressed());
            lb.add(this.settings.keyLeft.isPressed());

            int i = 0;
            this.pressingForward = lb.get((CarpetSchoolRules.EP8_COORDINATE_DIR + i++)%4);
            this.pressingRight = lb.get((CarpetSchoolRules.EP8_COORDINATE_DIR + i++)%4);
            this.pressingBack = lb.get((CarpetSchoolRules.EP8_COORDINATE_DIR + i++)%4);
            this.pressingLeft = lb.get((CarpetSchoolRules.EP8_COORDINATE_DIR + i)%4);
        } else {
            this.pressingForward = this.settings.keyForward.isPressed();
            this.pressingRight = this.settings.keyRight.isPressed();
            this.pressingBack = this.settings.keyBack.isPressed();
            this.pressingLeft = this.settings.keyLeft.isPressed();
        }

        this.movementForward = this.pressingForward == this.pressingBack ? 0.0F : (this.pressingForward ? 1.0F : -1.0F);
        this.movementSideways = this.pressingLeft == this.pressingRight ? 0.0F : (this.pressingLeft ? 1.0F : -1.0F);
        this.jumping = this.settings.keyJump.isPressed();
        this.sneaking = this.settings.keySneak.isPressed();
        if (slowDown) {
            this.movementSideways = (float)((double)this.movementSideways * 0.3D);
            this.movementForward = (float)((double)this.movementForward * 0.3D);
        }
    }
}
