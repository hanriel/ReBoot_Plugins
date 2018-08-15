package com.skyandforest.reboot_core.playerData;

public class SQLQuerys {
    public static final String
        GET_USER_DATA = "SELECT units,bits FROM users WHERE uuid=",
        GET_USER_GROUP = "SELECT group FROM users WHERE uuid=",

        ADD_USER = "INSERT uuid,name TO users";
}
