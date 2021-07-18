package games.blackjack;

import java.util.List;

import cards.Card;
import cards.FrenchNumber;
import cards.FrenchSuit;
import games.Player;

public interface BlackjackPlayer extends Player {
	boolean hit(List<Card<FrenchSuit, FrenchNumber>> cards, Card<FrenchSuit, FrenchNumber> dealer);

}
