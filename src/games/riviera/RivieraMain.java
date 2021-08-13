package games.riviera;

import games.Result;

public class RivieraMain {

	public static void main(String[] args) {
		RivieraPlayer p1 = new RivieraBotHeuristic("Heuri", true, true);
		RivieraPlayer p2 = new RivieraBotHeuristic("Genry", true, true);
		//RivieraPlayer p2 = new RivieraBotRandom("Rando", false);
		Riviera game = new Riviera(p1, p2);
		//game.setVerbose(true);
		//game.game();
		int wins = 0;
		int games = 10000;
		for (int i = 0; i < games; i++) {
			var results = game.game();
			for (var res : results) {
				if (res.player() == p1 && res.result() == Result.WIN) wins += 1;
			}
		}
		System.out.println("Wins " + wins);
		System.out.println("Winrate " + (100.0*wins/games) + "%");
	}

}
