package com.hanriel.reboot_npc.config;

import com.hanriel.reboot_core.config.YAML.PluginConfig;
import com.hanriel.reboot_core.config.YAML.SpecialConfig;

public class NpcList extends SpecialConfig {

    public NpcList(PluginConfig config) {
        super(config);
        setHeader("NPC saving file.");
    }

}
