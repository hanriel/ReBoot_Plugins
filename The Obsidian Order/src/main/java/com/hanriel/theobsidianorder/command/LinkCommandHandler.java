package com.hanriel.theobsidianorder.command;

import com.hanriel.theobsidianorder.util.MySQL;
import com.hanriel.theobsidianorder.util.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Statement;


public class LinkCommandHandler extends CommandFramework {

    private static MySQL mySQL = new MySQL();

    public LinkCommandHandler(String label) {
        super(label);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        System.out.print(args.length);
        switch (args.length) {
            case 0: //Если 1 аргумент, то стартуем
                Utils.sendMsg(sender, "&eНоль аргументов!");
                break;
            case 1: //Если 1 аргумент, то стартуем
                try {
                    String statement = "UPDATE too_users SET uuid=" + ((Player)sender).getUniqueId().toString() + " WHERE `token`='" + args[0] + "'";
                    Statement st = mySQL.getConnection().createStatement();
                    st.executeUpdate(statement);
                    Utils.sendMsg(sender, "&eВаш аккаунт привязан!");
                } catch (Exception e){
                    Utils.sendMsg(sender, "&сОшибка при запросе!");
                    e.printStackTrace();
                }
                break;
            default:
                Utils.sendMsg(sender, "&cНеверное число аргументов. Проверьте команду");
        }
    }
}
