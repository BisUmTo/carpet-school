package it.bisumto.carpetschool.functions;

import carpet.script.Expression;
import carpet.script.exception.InternalExpressionException;
import carpet.script.value.EntityValue;
import carpet.script.value.NumericValue;
import carpet.script.value.Value;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;

public class FlySpeedFunctions {
    public static void apply(Expression expression) {
        // fly_speed(speed)
        expression.addLazyFunction("fly_speed", 2, (c, t, lv) -> {
            Value v = lv.get(0).evalValue(c);
            if (!(v instanceof EntityValue) || !(((EntityValue) v).getEntity() instanceof PlayerEntity e))
                throw new InternalExpressionException("First argument to modify should be a player");
            float speed = (float) lv.get(1).evalValue(c).readDoubleNumber();
            e.getAbilities().setFlySpeed(speed);

            return (_c, _t) -> new NumericValue(speed);
        });
    }

    private static <T> Stat<T> getStat(StatType<T> type, Identifier id) {
        T key = type.getRegistry().get(id);
        return key != null && type.hasStat(key) ? type.getOrCreateStat(key) : null;
    }
}
