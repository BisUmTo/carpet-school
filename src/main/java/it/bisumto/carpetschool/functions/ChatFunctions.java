package it.bisumto.carpetschool.functions;

import carpet.script.CarpetContext;
import carpet.script.Expression;
import carpet.script.LazyValue;
import carpet.script.exception.InternalExpressionException;
import carpet.script.value.FormattedTextValue;
import carpet.script.value.NumericValue;
import carpet.script.value.StringValue;
import carpet.script.value.Value;
import net.minecraft.command.CommandSource;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.command.TellRawCommand;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChatFunctions {
    public static void apply(Expression expression) {
        // unicode(number)
        expression.addLazyFunction("unicode", 1,  (c, t, lv) -> {
            long code = lv.get(0).evalValue(c).readInteger();
            return (_c, _t) -> new StringValue("" + (char)code);
        });

        // translate(id, args)
        expression.addLazyFunction("translate", -1,  (c, t, lv) -> {
            if ( lv.size() < 1 )
                throw new InternalExpressionException("'translate' statement needs to have at least the translation id");
            String key = lv.get(0).evalValue(c).getString();
            List<String> lstring;
            if (lv.size() > 1)
                lstring = lv.subList(1,lv.size()-1).stream().map(l -> l.evalValue(c).getString()).toList();
            else
                lstring = new ArrayList<>();
            Text text = new TranslatableText(key, lstring.toArray());
            return (_c, _t) -> new FormattedTextValue(text);
        });

        // icon(id)
        expression.addLazyFunction("icon", 1,  (c, t, lv) -> {
            Identifier id = new Identifier(lv.get(0).evalValue(c).getString());
            Item item = Registry.ITEM.get(id);
            String key = "";
            if (Items.AIR != item){
                key += "tryashtar.shulker_preview.";
                key += item instanceof BlockItem ? "block." : "item.";
                key += id.getPath();
                key += ".0";
            }
            Text text = new TranslatableText(key).styled(
                    style -> style.withFont(new Identifier("tryashtar.shulker_preview:test"))
            );
            return (_c, _t) -> new FormattedTextValue(text);
        });

        // offline_statistic(uuid, category, entry)
        expression.addLazyFunction("offline_statistic", 3, (c, t, lv) -> {
            CarpetContext cc = (CarpetContext) c;
            String uuid = lv.get(0).evalValue(c).getString();
            File worldFolder = new File(cc.s.getServer().getSavePath(WorldSavePath.ROOT).toFile(), "stats");
            File statFile = new File(worldFolder, uuid + ".json");
            if (!statFile.exists()) {
                return LazyValue.NULL;
            } else {
                Identifier category;
                Identifier statName;
                try {
                    category = new Identifier(lv.get(1).evalValue(c).getString());
                    statName = new Identifier(lv.get(2).evalValue(c).getString());
                } catch (InvalidIdentifierException var10) {
                    return LazyValue.NULL;
                }
                StatType<?> type = Registry.STAT_TYPE.get(category);
                if (type == null) {
                    return LazyValue.NULL;
                } else {
                    Stat<?> stat = getStat(type, statName);
                    if (stat == null) {
                        return LazyValue.NULL;
                    } else {
                        ServerStatHandler ssh = new ServerStatHandler(cc.s.getServer(), statFile);
                        return (_c, _t) -> new NumericValue(ssh.getStat(stat));
                    }
                }
            }
        });
    }

    private static <T> Stat<T> getStat(StatType<T> type, Identifier id) {
        T key = type.getRegistry().get(id);
        return key != null && type.hasStat(key) ? type.getOrCreateStat(key) : null;
    }
}
