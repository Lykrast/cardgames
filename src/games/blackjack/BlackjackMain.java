package games.blackjack;

public class BlackjackMain {

	public static void main(String[] args) {
		BlackjackPlayer bot1 = new BlackjackBotRandom("10%", 0.1);
		BlackjackPlayer bot2 = new BlackjackBotRandom("25%", 0.25);
		BlackjackPlayer bot3 = new BlackjackBotRandom("50%", 0.5);
		BlackjackPlayer bot4 = new BlackjackBotRandom("75%", 0.75);
		Blackjack game = new Blackjack(bot1, bot2, bot3, bot4);
		game.setVerbose(true);
		System.out.println(game.game());
	}

}
