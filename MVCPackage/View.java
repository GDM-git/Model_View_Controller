package MVCPackage;

import MVCPackage.Model_pack.Player;

public interface View {
    String get_skill();
    void print_result_round(Player player_1, Player player_2, int round, String message);
    void print_result(String str);
}
