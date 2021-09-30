package it.bisumto.carpetschool;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.script.CarpetExpression;
import carpet.script.CarpetScriptServer;
import carpet.script.bundled.BundledModule;
import it.bisumto.carpetschool.events.CarpetSchoolEvents;
import it.bisumto.carpetschool.functions.ChatFunctions;
import net.fabricmc.api.ModInitializer;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class CarpetSchool implements CarpetExtension, ModInitializer {
    public static final String MOD_ID = "carpet-school";
    public static final String MOD_NAME = "Carpet School";
    public static final String MOD_VERSION = "1.4.34";
    public static final Logger LOG = LogManager.getLogger(MOD_ID);

    @Override
    public void scarpetApi(CarpetExpression expression) {
        ChatFunctions.apply(expression.getExpr());
    }

    @Override
    public String version() {
        return MOD_ID;
    }
    @Override
    public void onGameStarted() {
        CarpetScriptServer.registerSettingsApp(defaultScript("monomi", false));
    }

    private static BundledModule defaultScript(String scriptName, boolean isLibrary) {
        BundledModule module = new BundledModule(scriptName.toLowerCase(Locale.ROOT), null, false);
        try {
            module = new BundledModule(scriptName.toLowerCase(Locale.ROOT),
                    IOUtils.toString(
                            BundledModule.class.getClassLoader().getResourceAsStream(".data/" + MOD_ID + "/scripts/" + scriptName + (isLibrary ? ".scl" : ".sc")),
                            StandardCharsets.UTF_8
                    ), isLibrary);
        } catch (NullPointerException | IOException ignored) {
        }
        return module;
    }

    @Override
    public void onInitialize() {
        CarpetSchoolEvents.noop();
        CarpetServer.manageExtension(new CarpetSchool());
    }
}
