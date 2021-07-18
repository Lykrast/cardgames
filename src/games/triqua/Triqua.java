package games.triqua;

import java.util.ArrayList;
import java.util.List;

import cards.Card;
import cards.Deck;
import cards.FantasyNumber;
import cards.FantasySuit;

public class Triqua {
	//Made up fantasy game
	//Combinations:
	//3 of same number = 3 points
	//4 of same number = 8 points
	//3 numbers in a row same suit = 3 points
	//4 numbers in a row same suit = 8 points
	//2 same number + 1 blank = 2 points
	//3 same number + 1 blank = 6 points
	//3 numbers in a row same suit subbed with 1 blank = 2 points
	//4 numbers in a row same suit subbed with 1 blank = 6 points
	//Flower with 2 same suit (unsure) = 4 points
	//Flower with 3 same suit (unsure) = 10 points
	//3 dragons = 6 points
	//4 dragons = 12 points
	private Deck<FantasySuit, FantasyNumber> deck;
	private TriquaPlayer[] players;
	private List<Card<FantasySuit, FantasyNumber>>[] hands;
	
	public Triqua(TriquaPlayer p1, TriquaPlayer p2) {
		this(1, p1, p2);
	}
	
	@SuppressWarnings("unchecked")
	public Triqua(int decks, TriquaPlayer p1, TriquaPlayer p2) {
		this.players = new TriquaPlayer[]{p1, p2};
		hands = new List[players.length];
		for (int i = 0; i < hands.length; i++) hands[i] = new ArrayList<>();
		
		deck = new Deck<>();
		for (int i = 0; i < decks; i++) {
			deck.fill(FantasySuit.values(), FantasyNumber.values());
		}
	}

}
