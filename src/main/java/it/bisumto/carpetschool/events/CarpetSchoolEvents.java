package it.bisumto.carpetschool.events;

import carpet.script.CarpetEventServer.Event;
import carpet.script.value.EntityValue;
import carpet.script.value.ListValue;
import carpet.script.value.NumericValue;
import carpet.script.value.StringValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;

public class CarpetSchoolEvents extends Event {
    public static CarpetSchoolEvents PLAYER_DROPS_ITEM_AFTER =
            new CarpetSchoolEvents("player_drops_item_after",2,false){
                @Override
                public void onPlayerDropsItem(ServerPlayerEntity player, ItemEntity itemEntity){
                    handler.call( () -> Arrays.asList(
                            new EntityValue(player),
                            new EntityValue(itemEntity)
                    ), player::getCommandSource);
                }
            };

    public static void noop() { }

    public void onPlayerDropsItem(ServerPlayerEntity player, ItemEntity itemEntity) {}

    public CarpetSchoolEvents(String name, int reqArgs, boolean isGlobalOnly) {
        super(name, reqArgs, isGlobalOnly);
    }
}
