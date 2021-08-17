package games.fyg;

import cards.Card;
import cards.FantasyNumber;
import cards.FantasySuit;
import games.Player;

public interface FygPlayer extends Player {
	boolean hit(int currentScore, int blanks, int oppMaxScore);
	
	default void informParameters(int cap, int target) {}
	default void informDrawn(Card<FantasySuit, FantasyNumber> card) {}

}
