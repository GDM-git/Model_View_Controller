package MVCPackage.Controller_pack;

import MVCPackage.Controller;
import MVCPackage.Model_pack.Player;
import MVCPackage.Skill;
import MVCPackage.View_pack.Console;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Properties;

public class Squirrel implements Controller {
    private HashMap<String, Skill> Skills_HashMap = new HashMap<String, Skill>();
    public void begin_single_player() {
        Fabric_skill_add("MVCPackage.Skill_pack.S_11");
//        Fabric_skill_add("MVCPackage.Skill_pack.S_112");
//        Fabric_skill_add("MVCPackage.Skill_pack.S_119");
//        Fabric_skill_add("MVCPackage.Skill_pack.S_124");
        Player player_1 = new Player("Hero");
        Player player_2 = new Player("Monster");
        Console console = new Console();
        String player_1_move, player_2_move;
        int round = 1;
        String message = "";
        Properties properties = new Properties();
        try {
            FileInputStream in = new FileInputStream("Skills.properties");
            properties.load(in);
            for (; round <= 30; round++) {
                if ((player_1.get_Heal_Point(round) <= 0)||(player_2.get_Heal_Point(round) <= 0)) break;
                if (player_1.get_Heal_Point(round) > player_1.get_Heal_Point_Max()) player_1.set_Heal_Point(round, player_1.get_Heal_Point_Max());
                if (player_1.get_Mana_Point(round) > player_1.get_Mana_Point_Max()) player_1.set_Mana_Point(round, player_1.get_Mana_Point_Max());
                if (player_2.get_Heal_Point(round) > player_2.get_Heal_Point_Max()) player_2.set_Heal_Point(round, player_2.get_Heal_Point_Max());
                if (player_2.get_Mana_Point(round) > player_2.get_Mana_Point_Max()) player_2.set_Mana_Point(round, player_2.get_Mana_Point_Max());
                while(true) {
                    player_1_move = console.get_skill();
                    player_1_move = (String) properties.get(player_1_move);
                    if (player_1_move != null) break;
                }
                if (round % 9 == 0) player_2_move = "MVCPackage.Skill_pack.S_19";
                else player_2_move = "MVCPackage.Skill_pack.S_1" + round % 9;
                message = Processing(player_1, player_2, round, player_1_move, player_2_move);
                console.print_result_round(player_1, player_2, round + 1, message);
            }
            if (round == 31) {
                if (player_1.get_Heal_Point(round) + player_1.get_Mana_Point(round) > player_2.get_Heal_Point(round) + player_2.get_Mana_Point(round)) console.print_result("You Win!");
                if (player_1.get_Heal_Point(round) + player_1.get_Mana_Point(round) < player_2.get_Heal_Point(round) + player_2.get_Mana_Point(round)) console.print_result("You Lose...");
                if (player_1.get_Heal_Point(round) + player_1.get_Mana_Point(round) == player_2.get_Heal_Point(round) + player_2.get_Mana_Point(round)) console.print_result("Draw");
            }
            else {
                if ((player_1.get_Heal_Point(round) <= 0)&&(player_2.get_Heal_Point(round) <= 0)) console.print_result("Draw");
                else {
                    if (player_1.get_Heal_Point(round) <= 0) console.print_result("You Lose...");
                    if (player_2.get_Heal_Point(round) <= 0) console.print_result("You Win!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void begin_two_players() {}
    private String Processing(Player player_1, Player player_2, int round, String player_1_move_in, String player_2_move_in) {
        String message_out;
        int Damage;
        if (Fabric_skill_get_priority(player_1_move_in) >= Fabric_skill_get_priority(player_2_move_in)) {
            Fabric_skill_cast(player_1_move_in, player_1, player_2, round);
            Fabric_skill_cast(player_2_move_in, player_2, player_1, round);
            message_out = Fabric_skill_get_message(player_1_move_in, player_1.get_Player_Name()) + "\n" + Fabric_skill_get_message(player_2_move_in, player_2.get_Player_Name());
        }else {
            Fabric_skill_cast(player_2_move_in, player_2, player_1, round);
            Fabric_skill_cast(player_1_move_in, player_1, player_2, round);
            message_out = Fabric_skill_get_message(player_2_move_in, player_2.get_Player_Name()) + "\n" + Fabric_skill_get_message(player_1_move_in, player_1.get_Player_Name());
        }
        player_1.set_Mana_Point(round + 1, player_1.get_Mana_Point(round) + player_1.get_Mana_Point_regeneration(round) + player_1.get_Mana_Point_recovery(round) - player_1.get_Mana_cost(round) - player_1.get_Mana_Point_loss(round));
        Damage = player_1.get_Armor(round) - player_1.get_Damage(round) - player_1.get_Damage_over_time(round);
        if (Damage > 0) Damage = 0;
        if (player_1.get_Mana_Point(round + 1) < 0) Damage = Damage + 2 * player_1.get_Mana_Point(round + 1);
        player_1.set_Heal_Point(round + 1, player_1.get_Heal_Point(round) + Damage + player_1.get_Heal_Point_recovery(round));
        player_2.set_Mana_Point(round + 1, player_2.get_Mana_Point(round) + player_2.get_Mana_Point_regeneration(round) + player_2.get_Mana_Point_recovery(round) - player_2.get_Mana_cost(round) - player_2.get_Mana_Point_loss(round));
        Damage = player_2.get_Armor(round) - player_2.get_Damage(round) - player_2.get_Damage_over_time(round);
        if (Damage > 0) Damage = 0;
        if (player_2.get_Mana_Point(round + 1) < 0) Damage = Damage + 2 * player_2.get_Mana_Point(round + 1);
        player_2.set_Heal_Point(round + 1, player_2.get_Heal_Point(round) + Damage + player_2.get_Heal_Point_recovery(round));
        return message_out;
    }
    private void Fabric_skill_add(String str) {
        try {
            Skill skill = Skills_HashMap.get(str);
            if (skill == null) {
                Class<?> class_operate = Class.forName(str);
                Constructor<?> constructor_operate = class_operate.getConstructor();
                skill = (Skill) constructor_operate.newInstance();
                Skills_HashMap.put(str, skill);
            }
        } catch (ClassNotFoundException | InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    private void Fabric_skill_cast(String str, Player user, Player enemy, int round) {
        try {
            Skill skill = Skills_HashMap.get(str);
            if (skill == null) {
                Class<?> class_operate = Class.forName(str);
                Constructor<?> constructor_operate = class_operate.getConstructor();
                skill = (Skill) constructor_operate.newInstance();
                Skills_HashMap.put(str, skill);
            }
            skill.cast(user, enemy, round);
        } catch (ClassNotFoundException | InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    private int Fabric_skill_get_priority(String str) {
        try {
            Skill skill = Skills_HashMap.get(str);
            if (skill == null) {
                Class<?> class_operate = Class.forName(str);
                Constructor<?> constructor_operate = class_operate.getConstructor();
                skill = (Skill) constructor_operate.newInstance();
                Skills_HashMap.put(str, skill);
            }
            return skill.get_priority();
        } catch (ClassNotFoundException | InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return -999;
    }
    private String Fabric_skill_get_message(String str, String Player_Name_in) {
        try {
            Skill skill = Skills_HashMap.get(str);
            if (skill == null) {
                Class<?> class_operate = Class.forName(str);
                Constructor<?> constructor_operate = class_operate.getConstructor();
                skill = (Skill) constructor_operate.newInstance();
                Skills_HashMap.put(str, skill);
            }
            return skill.get_message(Player_Name_in);
        } catch (ClassNotFoundException | InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return "-999";
    }
}