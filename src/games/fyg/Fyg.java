package games.fyg;

import java.util.ArrayList;
import java.util.List;

import cards.Card;
import cards.Deck;
import cards.FantasyNumber;
import cards.FantasySuit;
import games.Game;
import games.GameResult;

public class Fyg implements Game {
	//Fantasy card game inspired by "pig" dice game and blackjack (name is pun)
	private Deck<FantasySuit, FantasyNumber> deck;
	private FygPlayer[] players;
	private int[] score; 
	private List<Card<FantasySuit, FantasyNumber>> current;
	private List<Card<FantasySuit, FantasyNumber>> discard;
	private int cap;
	private int target;
	private boolean verbose = false;
	
	public Fyg(int cap, int target, FygPlayer... players) {
		this.cap = cap;
		this.target = target;
		this.players = players;
		score = new int[players.length];

		deck = new Deck<>();
		deck.fill(FantasySuit.values(), FantasyNumber.values());
		current = new ArrayList<>();
		discard = new ArrayList<>();
	}

	@Override
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	
	private void reset() {
		deck.addAll(current);
		current.clear();
		deck.addAll(discard);
		discard.clear();
		for (int i = 0; i < players.length; i++) score[i] = 0;
	}
	
	private int value(Card<FantasySuit, FantasyNumber> card) {
		if (card.number() == FantasyNumber.DRAGON || card.number() == FantasyNumber.FLOWER) return 12;
		//Ordinals are 0 for blanks and 1-12 for the numbers
		else return card.number().ordinal();
	}
	
	//True if game is over
	private boolean turn(int player) {
		if (verbose) {
			System.out.println("------");
			System.out.println(players[player] + " turn");
		}
		int currentScore = 0;
		List<Card<FantasySuit, FantasyNumber>> currentBlanks = new ArrayList<>();
		
		while (currentScore < cap && deck.hasCards()) {
			var drawn = deck.draw();
			if (verbose) System.out.println("Drew " + drawn);
			for (int i = 0; i < players.length; i++) players[i].informDrawn(drawn);
			
			//Strictly more, bust unless a blank protects
			if (currentScore + value(drawn) > cap) {
				if (!currentBlanks.isEmpty()) {
					var blank = currentBlanks.remove(0);
					current.remove(blank);
					discard.add(blank);
					discard.add(drawn);
					if (verbose) System.out.println("Bust averted using " + blank + " (" + currentBlanks.size() + " left)");
				}
				else {
					if (verbose) System.out.println("Bust!");
					currentScore = 0;
					break;
				}
			}
			else {
				current.add(drawn);
				currentScore += value(drawn);
				if (drawn.number() == FantasyNumber.BLANK) currentBlanks.add(drawn);
			}

			if (verbose) System.out.println(currentScore + " (" + currentBlanks.size() + " blanks) with " + current);
			//Don't authorize hitting when exact, so decision only when score is strictly less
			if (currentScore + value(drawn) < cap) {
				int highestScore = -1;
				for (int i = 0; i < players.length; i++) if (i != player && score[i] > highestScore) highestScore = score[i];
				if (players[player].hit(currentScore, currentBlanks.size(), highestScore)) {
					if (verbose) System.out.println("Hit");
				}
				else {
					if (verbose) System.out.println("Stand");
					break;
				}
			}
		}
		
		if (verbose) System.out.println("Score " + score[player] + " -> " + (score[player] + currentScore));
		score[player] += currentScore;
		
		discard.addAll(current);
		current.clear();
		
		return !deck.hasCards() || score[player] >= target;
	}

	@Override
	public List<GameResult> game() {
		List<GameResult> results = new ArrayList<>();
		deck.shuffle();
		for (int i = 0; i < players.length; i++) {
			players[i].resetState();
			players[i].informParameters(cap, target);
		}
		if (verbose) System.out.println("New game");
		
		int currentPlayer = 0;
		while (!turn(currentPlayer)) {
			currentPlayer++;
			if (currentPlayer >= players.length) currentPlayer = 0;
		}

		if (verbose) System.out.println("Game end");
		reset();
		return results;
	}

}
