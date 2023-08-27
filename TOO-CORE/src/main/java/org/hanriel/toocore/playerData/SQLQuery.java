package org.hanriel.toocore.playerData;

public class SQLQuery {
    public static final String
        GET_USR = "SELECT units,bits FROM too_statistics WHERE uuid='",
        ADD_UPD_USR = "INSERT INTO `users` (`uuid`, `name`, `units`, `bits`) VALUES ('?', '?', '0', '0') ON DUPLICATE KEY UPDATE `units`=?,`bits`=?";
}
