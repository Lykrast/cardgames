package games.triqua;

import java.util.List;

import cards.Card;
import cards.FantasyNumber;
import cards.FantasySuit;
import games.Player;

public interface TriquaPlayer extends Player {
	//TODO game state awareness
	Card<FantasySuit, FantasyNumber> discard(List<Card<FantasySuit, FantasyNumber>> hand);
	
	//TODO game state awareness
	TriquaTrick playTrick(List<TriquaTrick> playable, List<Card<FantasySuit, FantasyNumber>> hand);

}
