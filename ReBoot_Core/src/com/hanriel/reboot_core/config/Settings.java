package com.hanriel.reboot_core.config;

import com.hanriel.reboot_core.config.YAML.PluginConfig;
import com.hanriel.reboot_core.config.YAML.SpecialConfig;

public class Settings extends SpecialConfig {

    public String mysql_host = "localhost";
    public String mysql_user = "root";
    public String mysql_password = "111";
    public int anti_click_spam_delay = 200;

    public Settings(PluginConfig config) {
        super(config);
        setHeader("Core configuration file.");
    }


}
