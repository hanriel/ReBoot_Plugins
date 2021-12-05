package com.hanriel.theobsidianorder.config;


import com.hanriel.theobsidianorder.config.YAML.PluginConfig;
import com.hanriel.theobsidianorder.config.YAML.SpecialConfig;

public class NpcList extends SpecialConfig {

    public NpcList(PluginConfig config) {
        super(config);
        setHeader("NPC saving file.");
    }

}
