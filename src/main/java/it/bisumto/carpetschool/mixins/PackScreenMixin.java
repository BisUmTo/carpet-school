package it.bisumto.carpetschool.mixins;

import carpet.CarpetServer;
import carpet.network.CarpetClient;
import it.bisumto.carpetschool.config.Config;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.WorldSavePath;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

@Mixin(PackScreen.class)
public class PackScreenMixin extends Screen {

    protected PackScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init",
            at = @At(value = "RETURN")
    )
    void scriptButton(CallbackInfo ci){
        Path folder = CarpetServer.minecraft_server != null ?
                CarpetServer.minecraft_server.getSavePath(WorldSavePath.ROOT).resolve("scripts"):
                FabricLoader.getInstance().getConfigDir().resolve("carpet/scripts");
        if (!Files.exists(folder)) {
            try {
                Files.createDirectories(folder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 154, this.height - 24, 150, 20, new TranslatableText("script.openFolder"),
                (button) -> Util.getOperatingSystem().open(folder.toFile())
        ));
    }
}
