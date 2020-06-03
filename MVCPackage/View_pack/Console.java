package MVCPackage.View_pack;

import MVCPackage.Model_pack.Player;
import MVCPackage.Model_pack.Round;
import MVCPackage.View;

import java.util.Scanner;

public class Console implements View {
    public String get_skill() {
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }
    public void print_result_round(Player player_1, Player player_2, int round, String message) {
        System.out.println("Результат раунда №" + (round - 1));
        System.out.println(message);
        System.out.println("Игрок " + player_1.get_Player_Name() + ": HP = " + player_1.get_Heal_Point(round) + "(" + (player_1.get_Heal_Point(round) - player_1.get_Heal_Point(round - 1)) + ") ; MP = " + (player_1.get_Mana_Point(round)) + "(" + (player_1.get_Mana_Point(round) - player_1.get_Mana_Point(round - 1)) + ")");
        System.out.println("Игрок " + player_2.get_Player_Name() + ": HP = " + player_2.get_Heal_Point(round) + "(" + (player_2.get_Heal_Point(round) - player_2.get_Heal_Point(round - 1)) + ") ; MP = " + (player_2.get_Mana_Point(round)) + "(" + (player_2.get_Mana_Point(round) - player_2.get_Mana_Point(round - 1)) + ")");
    }
    public void print_result(String str) {
        System.out.println("\n**************************************************************************");
        System.out.println(str);
        System.out.println("**************************************************************************");
    }
}