package cards;

public class Card<S extends Comparable<S>, N extends Comparable<N>> {
	private final S suit;
	private final N number;

	public Card(S suit, N number) {
		this.suit = suit;
		this.number = number;
	}

	public S suit() {
		return suit;
	}

	public N number() {
		return number;
	}
	
	@Override
	public String toString() {
		return number.toString() + suit.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((suit == null) ? 0 : suit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Card<?, ?> other = (Card<?, ?>) obj;
		if (number == null) {
			if (other.number != null) return false;
		}
		else if (!number.equals(other.number)) return false;
		if (suit == null) {
			if (other.suit != null) return false;
		}
		else if (!suit.equals(other.suit)) return false;
		return true;
	}
}
