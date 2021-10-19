package it.bisumto.carpetschool;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.script.CarpetExpression;
import carpet.script.CarpetScriptServer;
import carpet.script.bundled.BundledModule;
import it.bisumto.carpetschool.config.Config;
import it.bisumto.carpetschool.events.CarpetSchoolEvents;
import it.bisumto.carpetschool.functions.ChatFunctions;
import it.bisumto.carpetschool.functions.FlySpeedFunctions;
import it.bisumto.carpetschool.mixins.MinecraftClientAccessor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class CarpetSchool implements CarpetExtension, ModInitializer {
    public static final String MOD_ID = "carpet-school";
    public static final String MOD_NAME = "Carpet School";
    public static final String MOD_VERSION = "1.4.34";
    public static final Logger LOG = LogManager.getLogger(MOD_ID);

    @Override
    public void scarpetApi(CarpetExpression expression) {
        ChatFunctions.apply(expression.getExpr());
        FlySpeedFunctions.apply(expression.getExpr());
    }

    @Override
    public String version() {
        return MOD_ID;
    }
    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(CarpetSchoolRules.class);
        CarpetServer.settingsManager.getRule("commandScript").set(null, "ops");

        CarpetScriptServer.registerSettingsApp(defaultScript("monomi", false));
        CarpetScriptServer.registerSettingsApp(defaultScript("frazioni", false));
        CarpetScriptServer.registerSettingsApp(defaultScript("modulo", false));
    }

    @Override
    public void onServerLoaded(MinecraftServer server) {
        reloadConfig();
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

        reloadConfig();
    }

    @Override
    public void onReload(MinecraftServer server) {
        reloadConfig();
    }

    public static void reloadConfig(){
        try {
            Path configDir = FabricLoader.getInstance().getConfigDir().normalize();
            Files.createDirectories(configDir);
            Path configFile = configDir.resolve("carpet-school.json").normalize();
            Config.load(configFile.toFile());
            LOG.info("Config file loaded from " + configFile);

            // Appling startup config
            Config config = Config.getInstance();
            MinecraftClientAccessor client = (MinecraftClientAccessor)MinecraftClient.getInstance();
            client.setMultiplayerEnabled(config.MULTIPLAYER);
            client.setOnlineChatEnabled(config.ONLINECHAT);

            Config.getInstance().toFile(configFile.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
