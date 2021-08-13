package games.riviera;

import java.util.Collections;
import java.util.List;

import cards.Card;
import cards.FantasyNumber;
import cards.FantasySuit;

public class RivieraPlayerContext {
	private List<Card<FantasySuit, FantasyNumber>> hand;
	private List<Card<FantasySuit, FantasyNumber>> river;
	private List<Card<FantasySuit, FantasyNumber>> selfBank;
	private List<Card<FantasySuit, FantasyNumber>> oppBank;
	private FantasySuit atout;
	
	public RivieraPlayerContext(List<Card<FantasySuit, FantasyNumber>> hand,
			List<Card<FantasySuit, FantasyNumber>> river, List<Card<FantasySuit, FantasyNumber>> selfBank,
			List<Card<FantasySuit, FantasyNumber>> oppBank, FantasySuit atout) {
		super();
		this.hand = Collections.unmodifiableList(hand);
		this.river = Collections.unmodifiableList(river);
		this.selfBank = Collections.unmodifiableList(selfBank);
		this.oppBank = Collections.unmodifiableList(oppBank);
		this.atout = atout;
	}
	public FantasySuit atout() {
		return atout;
	}
	public List<Card<FantasySuit, FantasyNumber>> hand() {
		return hand;
	}
	public List<Card<FantasySuit, FantasyNumber>> river() {
		return river;
	}
	public List<Card<FantasySuit, FantasyNumber>> selfBank() {
		return selfBank;
	}
	public List<Card<FantasySuit, FantasyNumber>> oppBank() {
		return oppBank;
	}
}
