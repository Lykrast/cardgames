package games.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cards.Card;
import cards.Deck;
import cards.FrenchNumber;
import cards.FrenchSuit;
import games.Game;
import games.GameResult;
import games.Result;

public class Blackjack implements Game {
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
	
	@Override
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
	
	public static int score(List<Card<FrenchSuit, FrenchNumber>> hand) {
		return score(hand, false);
	}
	
	public static int score(List<Card<FrenchSuit, FrenchNumber>> hand, boolean soft) {
		int score = 0;
		int aces = 0;
		
		for (Card<FrenchSuit, FrenchNumber> card : hand) {
			FrenchNumber num = card.number();
			if (num.isFace()) score += 10;
			else score += num.value();
			
			if (num == FrenchNumber.ACE) aces += 1;
		}
		
		if (!soft) {
			while (aces > 0 && score <= 11) {
				score += 10;
				aces--;
			}
		}
		
		return score;
	}
	
	private void deal() {
		for (int i = 0; i < hands.length; i++) {
			hands[i].add(deck.draw());
			hands[i].add(deck.draw());
			if (verbose) System.out.println(players[i].getName() + " dealt " + hands[i] + " (" + score(hands[i]) + ")");
		}
		dealer.add(deck.draw());
		dealer.add(deck.draw());
		if (verbose) System.out.println("Dealer has " + dealer.get(0));
	}
	
	private int dealerPlays() {
		int score = score(dealer);
		if (verbose) {
			System.out.println("------");
			System.out.println("Dealer's turn");
			System.out.println(dealer + " (" + score + ")");
		}
		
		//Blackjack
		if (score == 21) {
			if (verbose) System.out.println("Blackjack!");
			//Blackjack beats a normal 21
			return 22;
		}
		else {
			while (score < 17) {
				dealer.add(deck.draw());
				score = score(dealer);
				if (verbose) {
					System.out.println("Hit");
					System.out.println(dealer + " (" + score + ")");
				}
				//Bust
				if (score > 21) {
					if (verbose) System.out.println("Bust!");
					score = -1;
					break;
				}
			}
			if (verbose && score > 0) System.out.println("Stand");
		}
		
		return score;
	}
	
	@Override
	public List<GameResult> game() {
		List<GameResult> results = new ArrayList<>();
		deck.shuffle();
		for (int i = 0; i < players.length; i++) players[i].resetState();
		if (verbose) System.out.println("New game");
		
		deal();
		
		//Play
		int[] scores = new int[players.length];
		Card<FrenchSuit, FrenchNumber> dealerCard = dealer.get(0);
		for (int i = 0; i < players.length; i++) {
			BlackjackPlayer player = players[i];
			List<Card<FrenchSuit, FrenchNumber>> hand = hands[i];
			scores[i] = score(hand);
			if (verbose) {
				System.out.println("------");
				System.out.println(player.getName() + " turn");
				System.out.println(hand + " (" + scores[i] + ")");
			}
			//Blackjack
			if (scores[i] == 21) {
				if (verbose) System.out.println("Blackjack!");
				//Blackjack beats a normal 21
				scores[i] = 22;
			}
			else {
				while (player.hit(Collections.unmodifiableList(hand), dealerCard)) {
					hand.add(deck.draw());
					scores[i] = score(hand);
					if (verbose) {
						System.out.println("Hit");
						System.out.println(hand + " (" + scores[i] + ")");
					}
					//Bust
					if (scores[i] > 21) {
						if (verbose) System.out.println("Bust!");
						scores[i] = -1;
						break;
					}
					else if (scores[i] == 21) break;
				}
				if (verbose && scores[i] > 0) System.out.println("Stand");
			}
		}
		
		int dealerScore = dealerPlays();
		for (int i = 0; i < players.length; i++) {
			if (scores[i] > dealerScore) results.add(new GameResult(players[i], Result.WIN));
			else if (dealerScore > 0 && scores[i] == dealerScore) results.add(new GameResult(players[i], Result.DRAW));
			else results.add(new GameResult(players[i], Result.LOOSE));
		}
		
		reset();
		return results;
	}

}
