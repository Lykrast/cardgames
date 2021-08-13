package games.riviera;

import cards.Card;
import cards.FantasyNumber;
import cards.FantasySuit;

public class RivieraMove {
	private Card<FantasySuit, FantasyNumber> played;
	private Card<FantasySuit, FantasyNumber> target;
	
	public RivieraMove(Card<FantasySuit, FantasyNumber> played, Card<FantasySuit, FantasyNumber> target) {
		this.played = played;
		this.target = target;
	}
	public RivieraMove(Card<FantasySuit, FantasyNumber> discarded) {
		this(discarded, null);
	}
	
	public Card<FantasySuit, FantasyNumber> getPlayed() {
		return played;
	}
	public Card<FantasySuit, FantasyNumber> getTarget() {
		return target;
	}
	
	public boolean isDiscard() {
		return target == null;
	}
	
	@Override
	public String toString() {
		if (isDiscard()) return "#" + played;
		else return played + " -> " + target;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((played == null) ? 0 : played.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		RivieraMove other = (RivieraMove) obj;
		if (played == null) {
			if (other.played != null) return false;
		}
		else if (!played.equals(other.played)) return false;
		if (target == null) {
			if (other.target != null) return false;
		}
		else if (!target.equals(other.target)) return false;
		return true;
	}
}
