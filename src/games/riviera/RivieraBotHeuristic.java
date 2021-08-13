package games.riviera;

import java.util.ArrayList;
import java.util.List;

import cards.Card;
import cards.FantasyNumber;
import cards.FantasySuit;

public class RivieraBotHeuristic implements RivieraPlayer {
	//Makes whichever move improves current score the most by a metric
	private String name;
	//Does it consider its hand as already secured?
	private boolean considersHand;
	//Does it check its opponent?
	private boolean checkOpponent;

	public RivieraBotHeuristic(String name, boolean considersHand, boolean checkOpponent) {
		this.name = name;
		this.considersHand = considersHand;
		this.checkOpponent = checkOpponent;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		if (considersHand && checkOpponent) return name + " (HO)";
		else if (considersHand) return name + " (H)";
		else if (checkOpponent) return name + " (O)";
		else return name;
	}
	
	private int evaluateMove(RivieraMove move, RivieraPlayerContext context) {
		List<Card<FantasySuit, FantasyNumber>> bank = new ArrayList<>();
		bank.addAll(context.selfBank());
		
		if (considersHand) {
			bank.addAll(context.hand());
			//Won't be in the bank if discarded, will be readded if it's a fishing
			bank.remove(move.getPlayed());
		}
		if (!move.isDiscard()) {
			bank.add(move.getPlayed());
			bank.add(move.getTarget());
		}
		
		return Riviera.evaluateBank(bank, context.atout(), false);
	}

	@Override
	public RivieraMove playMove(List<RivieraMove> possible, RivieraPlayerContext context) {
		//If checks opponent, find the 2 best cards it could take, assume it'll take the best card, or the second best if we take it
		int oppBestScore = -1;
		int oppSecondBestScore = -1;
		Card<FantasySuit, FantasyNumber> oppBest = null;
		if (checkOpponent) {
			List<Card<FantasySuit, FantasyNumber>> movedBank = new ArrayList<>();
			movedBank.addAll(context.oppBank());
			int starting = Riviera.evaluateBank(movedBank, context.atout(), false);
			for (var card : context.river()) {
				movedBank.add(card);
				int diff = Riviera.evaluateBank(movedBank, context.atout(), false) - starting;
				movedBank.remove(card);
				if (oppBest == null || diff > oppBestScore) {
					oppSecondBestScore = oppBestScore;
					oppBestScore = diff;
					oppBest = card;
				}
				else if (diff > oppSecondBestScore) {
					oppSecondBestScore = diff;
				}
			}
		}
		
		int maxScore = -1;
		RivieraMove chosen = null;
		for (RivieraMove move : possible) {
			int score = evaluateMove(move, context);
			if (checkOpponent) {
				if (!move.isDiscard() && move.getTarget().equals(oppBest)) score -= oppSecondBestScore;
				else score -= oppBestScore;
			}
			//System.out.println(move + " - " + score);
			//At equal potential score, try to not discard
			if (chosen == null || score > maxScore || (chosen != null && score == maxScore && chosen.isDiscard())) {
				maxScore = score;
				chosen = move;
			}
		}
		return chosen;
	}

}
