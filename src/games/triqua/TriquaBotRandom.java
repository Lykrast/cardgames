package games.triqua;

import java.util.List;
import java.util.Random;

import cards.Card;
import cards.FantasyNumber;
import cards.FantasySuit;

public class TriquaBotRandom implements TriquaPlayer {
	private String name;
	private Random random;

	public TriquaBotRandom(String name) {
		this.name = name;
		random = new Random();
	}

	public TriquaBotRandom(String name, long seed) {
		this.name = name;
		random = new Random(seed);
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public Card<FantasySuit, FantasyNumber> discard(List<Card<FantasySuit, FantasyNumber>> hand) {
		return hand.get(random.nextInt(hand.size()));
	}

	@Override
	public TriquaTrick playTrick(List<TriquaTrick> playable, List<Card<FantasySuit, FantasyNumber>> hand) {
		return playable.get(random.nextInt(playable.size()));
	}

}
