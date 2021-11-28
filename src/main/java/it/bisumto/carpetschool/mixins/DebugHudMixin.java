package it.bisumto.carpetschool.mixins;

import com.google.common.base.Preconditions;
import net.minecraft.SharedConstants;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(DebugHud.class)
public class DebugHudMixin {
    @Final
    @Shadow
    private MinecraftClient client;

    private final String VERSION = "1.18"; // SharedConstants.getGameVersion().getName();
    private final String VERSION_NAME = "1.18"; // this.client.getGameVersion();
    private final String MODDED = "vanilla"; // ClientBrandRetriever.getClientModName();
    private final String VERSION_TYPE = "release"; // this.client.getVersionType();

    @Redirect(method = "getLeftText",at = @At(
            value ="INVOKE",
            target = "Lcom/google/common/collect/Lists;newArrayList([Ljava/lang/Object;)Ljava/util/ArrayList;"
    ))
    public <E> ArrayList<E> changeVersion(E[] elements){
        Preconditions.checkNotNull(elements);
        ArrayList<E> list = new ArrayList();
        list.add((E)(
                "Minecraft " + VERSION +
                " (" + VERSION_NAME +
                "/" + MODDED +
                ("release".equalsIgnoreCase(VERSION_TYPE) ?
                        "" :
                        "/" + VERSION_TYPE) + ")"
        ));
        list.addAll(Arrays.asList(elements).subList(1, elements.length));
        return list;
    }


}
