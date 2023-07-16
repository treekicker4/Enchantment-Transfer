package com.treekicker4.enchantmenttransfer.configs;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;

public class ModConfigProvider implements SimpleConfig.DefaultConfig {
    private String configContents = "";
    private final List<Pair> configsList = new ArrayList();

    public ModConfigProvider() {
    }

    public List<Pair> getConfigsList() {
        return this.configsList;
    }

    public void addKeyValuePair(Pair<String, ?> keyValuePair, String comment) {
        this.configsList.add(keyValuePair);
        this.configContents = this.configContents + (String)keyValuePair.getFirst() + "=" + keyValuePair.getSecond() + " #" + comment + " | default: " + keyValuePair.getSecond() + "\n";
    }

    public String get(String namespace) {
        return this.configContents;
    }
}
