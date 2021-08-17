package games.riviera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cards.Card;
import cards.CardComparator;
import cards.Deck;
import cards.FantasyNumber;
import cards.FantasySuit;
import games.Game;
import games.GameResult;
import games.Result;

public class Riviera implements Game {
	//Fantasy game
	//2 players, set aside top card for atout suit, 10 cards in each hand, 10 cards in the river visible to each player, reveal atout (8 std)
	//Each turn, turn top card of deck to add to river, then fish or discard
	//To fish, put down a card from hand with a card in the river with same number or suit, then add both to your bank
	//To discard, add a card from your hand to the river, not adding it to your bank
	//Blanks can fish/be fished with any card
	//When both hands are empty, count scores on each bank (which should have 20 cards each if no discard)
	//Blanks don't count for any score (unless maybe you get 3/4?)
	//Each triple number gets point equal to its number (triple 4 gives 4, triple 12 gives 12), dragon and flower are 12
	//Each quadruple is twice the number (quadruple 4 gives 8...)
	//Each card of the current atout suit gives 2 points
	//If you get 8 of the same suit, 40 points (can get multiple times) (for standard it's 7 and 35)
	//Longest straight of numbers of at least 3 is 2 per length (if longest is 3,4,5,6,7, it gives 10 points)
	//If longest straight is 12 (longest possible), then it gives 60 instead (for standard it's 50)
	private Deck<FantasySuit, FantasyNumber> deck;
	private RivieraPlayer[] players;
	private List<Card<FantasySuit, FantasyNumber>>[] hands;
	private List<Card<FantasySuit, FantasyNumber>>[] banks;
	private List<Card<FantasySuit, FantasyNumber>> river;
	private Card<FantasySuit, FantasyNumber> atoutCard;
	private FantasySuit atout;
	private boolean verbose = false;
	

	private static final Comparator<Card<FantasySuit, FantasyNumber>> NUMBER_SUIT = CardComparator.numberThenSuit();
	
	@SuppressWarnings("unchecked")
	public Riviera(RivieraPlayer p1, RivieraPlayer p2) {
		this.players = new RivieraPlayer[]{p1, p2};
		hands = new List[players.length];
		for (int i = 0; i < hands.length; i++) hands[i] = new ArrayList<>();
		banks = new List[players.length];
		for (int i = 0; i < banks.length; i++) banks[i] = new ArrayList<>();
		river = new ArrayList<>();
		
		deck = new Deck<>();
		deck.fill(FantasySuit.values(), FantasyNumber.values());
	}
	
	@Override
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	
	private void reset() {
		deck.add(atoutCard);
		atoutCard = null;
		atout = null;
		deck.addAll(river);
		river.clear();
		for (int i = 0; i < players.length; i++) {
			deck.addAll(hands[i]);
			hands[i].clear();
			deck.addAll(banks[i]);
			banks[i].clear();
		}
	}
	
	private void deal() {
		atoutCard = deck.draw();
		atout = atoutCard.suit();
		if (verbose) System.out.println("Atout " + atoutCard);
		
		for (int player = 0; player < players.length; player++) {
			for (int i = 0; i < 10; i++) hands[player].add(deck.draw());
			Collections.sort(hands[player], NUMBER_SUIT);
			if (verbose) System.out.println(players[player].getName() + " dealt " + hands[player]);
		}
		
		for (int i = 0; i < 10; i++) river.add(deck.draw());
		Collections.sort(river, NUMBER_SUIT);
		if (verbose) System.out.println("River " + river);
	}
	
	private RivieraPlayerContext getContext(int player) {
		return new RivieraPlayerContext(hands[player], river, banks[player], banks[player ^ 1], atout);
	}
	
	private List<RivieraMove> genMoves(int player) {
		List<RivieraMove> moves = new ArrayList<>();
		for (var handCard : hands[player]) {
			moves.add(new RivieraMove(handCard));
			for (var riverCard : river) {
				if (handCard.number() == FantasyNumber.BLANK
						|| riverCard.number() == FantasyNumber.BLANK
						|| handCard.number() == riverCard.number()
						|| handCard.suit() == riverCard.suit()) {
					moves.add(new RivieraMove(handCard, riverCard));
				}
			}
		}
		
		return Collections.unmodifiableList(moves);
	}
	
	private void turn(int player) {
		var drawn = deck.draw();
		river.add(drawn);
		Collections.sort(river, NUMBER_SUIT);
		List<RivieraMove> moves = genMoves(player);
		if (verbose) {
			System.out.println("------");
			System.out.println(players[player] + " turn");
			if (verbose) System.out.println("Drew " + drawn + " for the river");
			System.out.println("Hand " + hands[player]);
			System.out.println("River " + river);
			System.out.println("Bank " + banks[player]);
			//System.out.println("Possible moves " + moves);
		}
		
		RivieraMove move = null;
		RivieraPlayerContext context = getContext(player);
		do {
			move = players[player].playMove(moves, context);
		} while (!moves.contains(move));
		if (verbose) System.out.println("Played " + move);

		hands[player].remove(move.getPlayed());
		if (move.isDiscard()) river.add(move.getPlayed());
		else {
			river.remove(move.getTarget());
			banks[player].add(move.getPlayed());
			banks[player].add(move.getTarget());
			Collections.sort(banks[player], NUMBER_SUIT);
		}
	}
	
	public static int evaluateBank(List<Card<FantasySuit, FantasyNumber>> bank, FantasySuit atout, boolean verbose) {
		int totalTriples = 0, totalNumbers = 0, totalSuits = 0, totalAtouts = 0;
		int[] countSuit = new int[4];
		boolean[] hasNumber = new boolean[12];
		//Sort for triples and quadras
		Collections.sort(bank, NUMBER_SUIT);

		FantasyNumber accumulated = null;
		int accumulatedCount = 0;
		for (var card : bank) {
			//Blanks are ordinal 0, numbers are 1-12
			if (!card.number().isSpecial()) hasNumber[card.number().ordinal() - 1] = true;
			if (card.number() != FantasyNumber.BLANK) {
				countSuit[card.suit().ordinal()] += 1;
				if (atout != null && card.suit() == atout) totalAtouts += 2;
			}
			
			//Catch triples and quadras
			if ((accumulated == null || card.number() != accumulated) && card.number() != FantasyNumber.BLANK) {
				if (accumulatedCount >= 3) {
					int val = accumulated.isSpecial() ? 12 : accumulated.ordinal();
					if (accumulatedCount == 3) totalTriples += val;
					else totalTriples += val*2;
				}
				accumulated = card.number();
				accumulatedCount = 1;
			}
			else if (card.number() == accumulated) accumulatedCount += 1;
		}
		//Catch triples at the end
		if (accumulatedCount >= 3) {
			int val = accumulated.isSpecial() ? 12 : accumulated.ordinal();
			if (accumulatedCount == 3) totalTriples += val;
			else totalTriples += val*2;
		}
		
		//Suits
		for (int i = 0; i < countSuit.length; i++) {
			if (countSuit[i] >= 8) totalSuits += 40;
		}
		
		//Straights
		int longest = -1;
		int current = 0;
		for (int i = 0; i < hasNumber.length; i++) {
			if (hasNumber[i]) {
				current += 1;
				if (current > longest) longest = current;
			}
			else current = 0;
		}
		if (longest >= 3) totalNumbers += longest >= 12 ? 5*longest : 2*longest;
		
		if (verbose) {
			System.out.println("Matches - " + totalTriples);
			System.out.println("Atout - " + totalAtouts);
			System.out.println("Highest straight - " + totalNumbers);
			System.out.println("Dominant suits - " + totalSuits);
			System.out.println("Total - " + (totalTriples + totalNumbers + totalSuits + totalAtouts));
		}
		
		return totalTriples + totalNumbers + totalSuits + totalAtouts;
	}
	
	@Override
	public List<GameResult> game() {
		List<GameResult> results = new ArrayList<>();
		deck.shuffle();
		for (int i = 0; i < players.length; i++) players[i].resetState();
		if (verbose) System.out.println("New game");
		
		deal();
		
		while (!hands[0].isEmpty()) {
			for (int i = 0; i < players.length; i++) turn(i);
		}
		
		if (verbose) {
			System.out.println("======");
			System.out.println("Game end");
		}
		
		int[] score = new int[players.length];
		int maxScore = -1;
		for (int i = 0; i < players.length; i++) {
			if (verbose) {
				System.out.println("------");
				System.out.println(players[i] + " score");
				System.out.println(banks[i]);
			}
			score[i] = evaluateBank(banks[i], atout, verbose);
			if (score[i] > maxScore) maxScore = score[i];
		}
		
		for (int i = 0; i < players.length; i++) {
			if (score[i] == maxScore) {
				if (verbose) System.out.println(players[i] + " wins");
				results.add(new GameResult(players[i], Result.WIN));
			}
			else results.add(new GameResult(players[i], Result.LOOSE));
		}
		
		reset();
		return results;
	}
	
}
