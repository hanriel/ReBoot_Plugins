package com.skyandforest.reboot_core.playerData;

public class SQLQuerys {
    public static final String
        GET_USR = "SELECT units,bits FROM user WHERE uuid='",
        GET_USR_GROUP = "SELECT group FROM user WHERE uuid=",
        ADD_UPD_USR = "INSERT INTO `user` (`uuid`, `name`, `units`, `bits`) VALUES ('?', '?', '0', '0') ON DUPLICATE KEY UPDATE `units`=?,`bits`=?";
}
