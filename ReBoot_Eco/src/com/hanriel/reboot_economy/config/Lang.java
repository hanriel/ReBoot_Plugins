package com.hanriel.reboot_economy.config;

import com.hanriel.reboot_economy.config.YAML.PluginConfig;
import com.hanriel.reboot_economy.config.YAML.SpecialConfig;

public class Lang extends SpecialConfig {

    public String no_open_permission = "&cYou don't have permission &e{permission} &cto use shop.";
    public String no_required_item = "&cYou must have &e{amount}x {material} &c(ID: {id}, data value: {datavalue}) for this.";
    public String no_money = "&cYou need {money}$ for this.";
    public String no_slots = "&3You need empty slot for this.";
    public String any = "any"; // Used in no_required_item when data value is not restrictive.
    public String paymentconfirmation_title = "&3Payment confirmation";
    public String shop_title = "&3ReBoot Shop";

    public Lang(PluginConfig config) {
        super(config);
    }
}
