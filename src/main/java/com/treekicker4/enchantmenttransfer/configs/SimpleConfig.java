package com.treekicker4.enchantmenttransfer.configs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Scanner;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleConfig {
    private static final Logger LOGGER = LogManager.getLogger("SimpleConfig");
    private final HashMap<String, String> config = new HashMap();
    private final ConfigRequest request;
    private boolean broken = false;
    public interface DefaultConfig {
        String get(String var1);

        static String empty(String namespace) {
            return "";
        }
    }
    public static ConfigRequest of(String filename) {
        Path path = FabricLoader.getInstance().getConfigDir();
        return new ConfigRequest(path.resolve(filename + ".properties").toFile(), filename);
    }

    private void createConfig() throws IOException {
        this.request.file.getParentFile().mkdirs();
        Files.createFile(this.request.file.toPath());
        PrintWriter writer = new PrintWriter(this.request.file, "UTF-8");
        writer.write(this.request.getConfig());
        writer.close();
    }

    private void loadConfig() throws IOException {
        Scanner reader = new Scanner(this.request.file);

        for(int line = 1; reader.hasNextLine(); ++line) {
            this.parseConfigEntry(reader.nextLine(), line);
        }

    }

    private void parseConfigEntry(String entry, int line) {
        if (!entry.isEmpty() && !entry.startsWith("#")) {
            String[] parts = entry.split("=", 2);
            if (parts.length != 2) {
                throw new RuntimeException("Syntax error in config file on line " + line + "!");
            }

            String temp = parts[1].split(" #")[0];
            this.config.put(parts[0], temp);
        }

    }

    public SimpleConfig(ConfigRequest request) {
        this.request = request;
        String identifier = "Config '" + request.filename + "'";
        if (!request.file.exists()) {
            LOGGER.info(identifier + " is missing, generating default one...");

            try {
                this.createConfig();
            } catch (IOException var5) {
                LOGGER.error(identifier + " failed to generate!");
                LOGGER.trace(var5);
                this.broken = true;
            }
        }

        if (!this.broken) {
            try {
                this.loadConfig();
            } catch (Exception var4) {
                LOGGER.error(identifier + " failed to load!");
                LOGGER.trace(var4);
                this.broken = true;
            }
        }

    }

    /** @deprecated */
    @Deprecated
    public String get(String key) {
        return (String)this.config.get(key);
    }

    public String getOrDefault(String key, String def) {
        String val = this.get(key);
        return val == null ? def : val;
    }

    public int getOrDefault(String key, int def) {
        try {
            return Integer.parseInt(this.get(key));
        } catch (Exception var4) {
            return def;
        }
    }

    public boolean getOrDefault(String key, boolean def) {
        String val = this.get(key);
        return val != null ? val.equalsIgnoreCase("true") : def;
    }

    public double getOrDefault(String key, double def) {
        try {
            return Double.parseDouble(this.get(key));
        } catch (Exception var5) {
            return def;
        }
    }

    public boolean isBroken() {
        return this.broken;
    }

    public boolean delete() {
        LOGGER.warn("Config '" + this.request.filename + "' was removed from existence! Restart the game to regenerate it.");
        return this.request.file.delete();
    }
}
