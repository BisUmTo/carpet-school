package it.bisumto.carpetschool.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.*;

/**
 * Copyright © 2013 by Frank Roth
 * Java JSON-Config-File is a simple GSON based implementation to save Objects or primitive values like in a property .ini file.
 * You can find the original source <a href="https://github.com/frankred/json-config-file">here</a>.
 */

public class Config {

    // DON'T TOUCH THE FOLLOWING CODE
    private static Config instance;
    // Hier schreibst du deine Attribute hin
    @SerializedName("multiplayer")
    public boolean MULTIPLAYER;
    @SerializedName("onlinechat")
    public boolean ONLINECHAT;
    @SerializedName("commands")
    public boolean COMMANDS;

    public Config() {
        // Hier die Standardwerte falls das jeweiligen Attribut nicht in der
        // config.json enthalten ist.
        this.MULTIPLAYER = false;
        this.ONLINECHAT = false;
        this.COMMANDS = false;
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = fromDefaults();
        }
        return instance;
    }

    public static void load(File file) {
        instance = fromFile(file);

        // no config file found
        if (instance == null) {
            instance = fromDefaults();
        }
    }

    public static void load(String file) {
        load(new File(file));
    }

    private static Config fromDefaults() {
        Config config = new Config();
        return config;
    }

    private static Config fromFile(File configFile) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)));
            return gson.fromJson(reader, Config.class);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public void toFile(String file) {
        toFile(new File(file));
    }

    public void toFile(File file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonConfig = gson.toJson(this);
        FileWriter writer;
        try {
            writer = new FileWriter(file);
            writer.write(jsonConfig);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
