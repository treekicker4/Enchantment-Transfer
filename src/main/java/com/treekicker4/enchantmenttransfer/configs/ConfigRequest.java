package com.treekicker4.enchantmenttransfer.configs;

import java.io.File;

public class ConfigRequest {
    public final File file;
    public final String filename;
    public SimpleConfig.DefaultConfig provider;

    public ConfigRequest(File file, String filename) {
        this.file = file;
        this.filename = filename;
        this.provider = SimpleConfig.DefaultConfig::empty;
    }

    public ConfigRequest provider(SimpleConfig.DefaultConfig provider) {
        this.provider = provider;
        return this;
    }

    public SimpleConfig request() {
        return new SimpleConfig(this);
    }

    public String getConfig() {
        return this.provider.get(this.filename) + "\n";
    }
}
