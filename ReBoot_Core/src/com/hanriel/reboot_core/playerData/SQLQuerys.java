package com.hanriel.reboot_core.playerData;

public class SQLQuerys {
    public static final String
        GET_USR = "SELECT units,bits FROM users WHERE uuid='",
        GET_USR_GROUP = "SELECT group FROM users WHERE uuid=",
        ADD_UPD_USR = "INSERT INTO `users` (`uuid`, `name`, `units`, `bits`) VALUES ('?', '?', '0', '0') ON DUPLICATE KEY UPDATE `units`=?,`bits`=?";
}
