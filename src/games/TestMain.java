package games;

import cards.Deck;
import cards.FrenchNumber;
import cards.FrenchSuit;

public class TestMain {

	public static void main(String[] args) {
		Deck<FrenchSuit, FrenchNumber> deck = new Deck<FrenchSuit, FrenchNumber>();
		deck.fill(FrenchSuit.values(), FrenchNumber.values());
		deck.shuffle();
		System.out.println(deck);
		System.out.println(deck.count());
		System.out.println(deck.draw());
		System.out.println(deck.draw());
		System.out.println(deck);
		System.out.println(deck.count());
	}

}
