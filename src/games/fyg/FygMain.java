package games.fyg;

public class FygMain {

	public static void main(String[] args) {
		FygPlayer p1 = new FygBotRandom("Dix", 0.1);
		FygPlayer p2 = new FygBotRandom("Quart", 0.25);
		FygPlayer p3 = new FygBotRandom("Demi", 0.5);
		FygPlayer p4 = new FygBotRandom("Trois", 0.75);
		Fyg game = new Fyg(25, 100, p1, p2, p3, p4);
		game.setVerbose(true);
		game.game();
	}

}
