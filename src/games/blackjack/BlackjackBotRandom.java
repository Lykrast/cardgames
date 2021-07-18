package games.blackjack;

import java.util.List;
import java.util.Random;

import cards.Card;
import cards.FrenchNumber;
import cards.FrenchSuit;

public class BlackjackBotRandom implements BlackjackPlayer {
	private String name;
	private double chance;
	private Random random;

	public BlackjackBotRandom(String name, double chance) {
		this.name = name;
		this.chance = chance;
		random = new Random();
	}

	public BlackjackBotRandom(String name, double chance, long seed) {
		this.name = name;
		this.chance = chance;
		random = new Random(seed);
	}

	@Override
	public boolean hit(List<Card<FrenchSuit, FrenchNumber>> cards, Card<FrenchSuit, FrenchNumber> dealer) {
		return random.nextDouble() <= chance;
	}

	@Override
	public String getName() {
		return name;
	}

}
