package games.triqua;

import java.util.ArrayList;
import java.util.List;

import cards.Card;
import cards.FantasyNumber;
import cards.FantasySuit;

public class TriquaTrick {
	private List<Card<FantasySuit, FantasyNumber>> cards;
	private int score;
	
	//Does not check if the combi is valid or if the score is correct
	public TriquaTrick(int score, Card<FantasySuit, FantasyNumber> card1, Card<FantasySuit, FantasyNumber> card2, Card<FantasySuit, FantasyNumber> card3) {
		cards = new ArrayList<>();
		cards.add(card1);
		cards.add(card2);
		cards.add(card3);
		this.score = score;
	}
	
	public TriquaTrick(int score, Card<FantasySuit, FantasyNumber> card1, Card<FantasySuit, FantasyNumber> card2, Card<FantasySuit, FantasyNumber> card3, Card<FantasySuit, FantasyNumber> card4) {
		cards = new ArrayList<>();
		cards.add(card1);
		cards.add(card2);
		cards.add(card3);
		cards.add(card4);
		this.score = score;
	}


	public List<Card<FantasySuit, FantasyNumber>> getCards() {
		return cards;
	}

	public int getScore() {
		return score;
	}

	@Override
	public String toString() {
		return cards + " (" + score + ")";
	}
}
