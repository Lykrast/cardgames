package games.triqua;

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

public class Triqua implements Game {
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
	private List<TriquaTrick>[] tricks;
	private List<Card<FantasySuit, FantasyNumber>> discard;
	private boolean verbose = false;
	
	private static final Comparator<Card<FantasySuit, FantasyNumber>> SUIT_NUMBER = CardComparator.suitThenNumber(),
			NUMBER_SUIT = CardComparator.numberThenSuit();
	
	public Triqua(TriquaPlayer p1, TriquaPlayer p2) {
		this(1, p1, p2);
	}
	
	@Override
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	
	@SuppressWarnings("unchecked")
	public Triqua(int decks, TriquaPlayer p1, TriquaPlayer p2) {
		this.players = new TriquaPlayer[]{p1, p2};
		hands = new List[players.length];
		for (int i = 0; i < hands.length; i++) hands[i] = new ArrayList<>();
		tricks = new List[players.length];
		for (int i = 0; i < tricks.length; i++) tricks[i] = new ArrayList<>();
		discard = new ArrayList<>();
		
		deck = new Deck<>();
		for (int i = 0; i < decks; i++) {
			deck.fill(FantasySuit.values(), FantasyNumber.values());
		}
	}
	
	private void dealHands() {
		for (int player = 0; player < players.length; player++) {
			for (int i = 0; i < 10; i++) hands[player].add(deck.draw());
			Collections.sort(hands[player], NUMBER_SUIT);
			if (verbose) System.out.println(players[player].getName() + " dealt " + hands[player]);
			System.out.println(genCombinaisons(hands[player]));
		}
	}
	
	private List<TriquaTrick> genCombinaisons(List<Card<FantasySuit, FantasyNumber>> hand) {
		List<TriquaTrick> combi = new ArrayList<>();
		Collections.sort(hand, NUMBER_SUIT);
		//Collect blanks alongside triples and quadruples
		List<Card<FantasySuit, FantasyNumber>> blanks = new ArrayList<>();
		List<Card<FantasySuit, FantasyNumber>> accumulator = new ArrayList<>();
		FantasyNumber accumulated = null;
		for (Card<FantasySuit, FantasyNumber> card : hand) {
			//Collect blanks
			if (card.number() == FantasyNumber.BLANK) {
				blanks.add(card);
				continue;
			}
			//Collect all cards of the same number, then combines once they're done
			//If don't match, cash in the accumulator then empty it
			if (!accumulator.isEmpty() && card.number() != accumulated) {
				makeTris(combi, blanks, accumulator);
				accumulator.clear();
				accumulated = null;
			}
			//If empty or we match what is accumulated, add to it
			if ((accumulator.isEmpty() && (!card.number().isSpecial() || card.number() == FantasyNumber.DRAGON))
					|| (card.number() == accumulated)) {
				accumulator.add(card);
				accumulated = card.number();
				continue;
			}
		}
		//Cash in the tris for the last accumulator since it didn't get stopped by a conflicting card
		makeTris(combi, blanks, accumulator);
		//Go for streaks
		Collections.sort(hand, SUIT_NUMBER);
		
		return combi;
	}
	
	private void makeTris(List<TriquaTrick> combi, List<Card<FantasySuit, FantasyNumber>> blanks, List<Card<FantasySuit, FantasyNumber>> accumulator) {
		//No tris to make in this case
		if (accumulator.size() < 2 || (blanks.isEmpty() && accumulator.size() < 3)) return;
		//No blanks
		boolean dragon = accumulator.get(0).number() == FantasyNumber.DRAGON;
		if (accumulator.size() == 3) combi.add(new TriquaTrick(dragon ? 6 : 3, accumulator.get(0), accumulator.get(1), accumulator.get(2)));
		else if (accumulator.size() == 4) {
			Card<FantasySuit, FantasyNumber> c0 = accumulator.get(0), c1 = accumulator.get(1), c2 = accumulator.get(2), c3 = accumulator.get(3);
			combi.add(new TriquaTrick(dragon ? 12 : 8, c0, c1, c2, c3));
			combi.add(new TriquaTrick(dragon ? 6 : 3, c0, c1, c2));
			combi.add(new TriquaTrick(dragon ? 6 : 3, c0, c1, c3));
			combi.add(new TriquaTrick(dragon ? 6 : 3, c0, c2, c3));
			combi.add(new TriquaTrick(dragon ? 6 : 3, c1, c2, c3));
		}
		//Dragons don't get blanks
		if (dragon || blanks.isEmpty()) return;
		if (accumulator.size() == 2) {
			Card<FantasySuit, FantasyNumber> c0 = accumulator.get(0), c1 = accumulator.get(1);
			for (Card<FantasySuit, FantasyNumber> b : blanks) combi.add(new TriquaTrick(2, b, c0, c1));
		}
		else if (accumulator.size() == 3) {
			Card<FantasySuit, FantasyNumber> c0 = accumulator.get(0), c1 = accumulator.get(1), c2 = accumulator.get(2);
			for (Card<FantasySuit, FantasyNumber> b : blanks) {
				combi.add(new TriquaTrick(6, b, c0, c1, c2));
				combi.add(new TriquaTrick(2, b, c0, c1));
				combi.add(new TriquaTrick(2, b, c0, c2));
				combi.add(new TriquaTrick(2, b, c1, c2));
			}
		}
		else if (accumulator.size() == 4) {
			Card<FantasySuit, FantasyNumber> c0 = accumulator.get(0), c1 = accumulator.get(1), c2 = accumulator.get(2), c3 = accumulator.get(3);
			for (Card<FantasySuit, FantasyNumber> b : blanks) {
				//Oh boi
				combi.add(new TriquaTrick(6, b, c0, c1, c2));
				combi.add(new TriquaTrick(6, b, c0, c1, c3));
				combi.add(new TriquaTrick(6, b, c0, c2, c3));
				combi.add(new TriquaTrick(6, b, c1, c2, c3));
				combi.add(new TriquaTrick(2, b, c0, c1));
				combi.add(new TriquaTrick(2, b, c0, c2));
				combi.add(new TriquaTrick(2, b, c0, c3));
				combi.add(new TriquaTrick(2, b, c1, c2));
				combi.add(new TriquaTrick(2, b, c1, c3));
				combi.add(new TriquaTrick(2, b, c2, c3));
			}
		}
	}
	
	private boolean turn(int player) {
		//Returns true if the state is ready to end
		if (verbose) {
			System.out.println("------");
			System.out.println(players[player].getName() + " turn");
			System.out.println("Drew " + deck.peek());
		}
		hands[player].add(deck.draw());
		if (verbose) System.out.println(hands[player]);
		List<TriquaTrick> possible = genCombinaisons(hands[player]);
		if (!possible.isEmpty()) {
			TriquaTrick trick = players[player].playTrick(possible, hands[player]);
			//Put the trick in play and remove it from the hand
			if (trick != null && possible.contains(trick)) {
				tricks[player].add(trick);
				for (var card : trick.getCards()) hands[player].remove(card);
				if (verbose) System.out.println("Played " + trick);
			}
		}
		//Can have an empty hand if last trick finished it
		if (!hands[player].isEmpty()) {
			Card<FantasySuit, FantasyNumber> discarded = null;
			while (!hands[player].contains(discarded)) discarded = players[player].discard(hands[player]);
			discard.add(discarded);
			hands[player].remove(discarded);
			if (verbose) System.out.println("Discarded " + discarded);
		}
		if (verbose) System.out.println("Board is " + tricks[player]);
		return tricks[player].size() >= 3;
	}
	
	private void reset() {
		deck.addAll(discard);
		discard.clear();
		for (int i = 0; i < players.length; i++) {
			deck.addAll(hands[i]);
			hands[i].clear();
			for (TriquaTrick trick : tricks[i]) deck.addAll(trick.getCards());
			tricks[i].clear();
		}
	}
	
	@Override
	public List<GameResult> game() {
		List<GameResult> results = new ArrayList<>();
		deck.shuffle();
		if (verbose) System.out.println("New game");
		
		dealHands();
		
		int endOnNext = -1;
		boolean end = false;
		while (deck.count() > 0 && !end) {
			for (int i = 0; i < players.length; i++) {
				if (i == endOnNext) {
					end = true;
					break;
				}
				if (turn(i) && endOnNext < 0) {
					endOnNext = i;
					if (verbose) System.out.println("Game will end on " + players[i].getName() + " next turn");
				}
			}
		}
		if (verbose) {
			if (deck.count() <= 0) System.out.println("Deck emptied, game end");
			else System.out.println("Game end");
		}
		
		reset();
		return results;
	}

}
