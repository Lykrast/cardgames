package games.blackjack;

import java.util.ArrayList;
import java.util.List;

import cards.Card;
import cards.Deck;
import cards.FrenchNumber;
import cards.FrenchSuit;
import games.Player;

public class Blackjack {
	private Deck<FrenchSuit, FrenchNumber> deck;
	private BlackjackPlayer[] players;
	private List<Card<FrenchSuit, FrenchNumber>>[] hands;
	private List<Card<FrenchSuit, FrenchNumber>> dealer;
	private boolean verbose = false;
	
	public Blackjack(BlackjackPlayer... players) {
		this(1, players);
	}
	
	@SuppressWarnings("unchecked")
	public Blackjack(int decks, BlackjackPlayer... players) {
		this.players = players;
		hands = new List[players.length];
		for (int i = 0; i < hands.length; i++) hands[i] = new ArrayList<>();
		dealer = new ArrayList<>();
		
		deck = new Deck<>();
		for (int i = 0; i < decks; i++) {
			deck.fill(FrenchSuit.values(), FrenchNumber.values());
		}
	}
	
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	
	private void reset() {
		for (List<Card<FrenchSuit, FrenchNumber>> hand : hands) {
			deck.addAll(hand);
			hand.clear();
		}
		deck.addAll(dealer);
		dealer.clear();
	}
	
	private void deal() {
		for (int i = 0; i < hands.length; i++) {
			hands[i].add(deck.draw());
			hands[i].add(deck.draw());
			if (verbose) System.out.println(players[i].getName() + " dealt " + hands[i]);
		}
		dealer.add(deck.draw());
		dealer.add(deck.draw());
		if (verbose) System.out.println("Dealer has " + dealer.get(0));
	}
	
	public List<Player> game() {
		List<Player> winners = new ArrayList<>();
		deck.shuffle();
		if (verbose) System.out.println("New game");
		
		deal();
		
		reset();
		return winners;
	}

}
