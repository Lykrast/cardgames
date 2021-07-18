package cards;

public class Card<S, N> {
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
}
