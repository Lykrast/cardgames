package games.triqua;

public class TriquaMain {

	public static void main(String[] args) {
		TriquaPlayer p1 = new TriquaBotRandom("Wouane");
		TriquaPlayer p2 = new TriquaBotRandom("Tou");
		Triqua game = new Triqua(p1, p2);
		game.setVerbose(true);
		game.game();
	}

}
