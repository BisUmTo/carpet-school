package it.bisumto.carpetschool.mixins;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static it.bisumto.carpetschool.events.CarpetSchoolEvents.PLAYER_DROPS_ITEM_AFTER;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;",
            at = @At(value = "RETURN", ordinal = 1),
            locals = LocalCapture.CAPTURE_FAILHARD)
    void onPlayerDropsItemAfter(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir, ItemEntity itemEntity){
        PLAYER_DROPS_ITEM_AFTER.onPlayerDropsItem((ServerPlayerEntity)((Object) this), itemEntity);
    }
}
