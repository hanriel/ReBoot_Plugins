package com.hanriel.theobsidianorder.util;

public class SQLQuerys {
    public static final String
        GET_USR = "SELECT units FROM users WHERE uuid='",
        GET_USR_GROUP = "SELECT group FROM users WHERE uuid=",
        ADD_UPD_USR = "INSERT INTO `users` (`uuid`, `name`, `units`) VALUES ('?', '?', '0') ON DUPLICATE KEY UPDATE `units`=?";
}
