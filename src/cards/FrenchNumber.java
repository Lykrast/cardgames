package cards;

public enum FrenchNumber {
	ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING;

	@Override
	public String toString() {
		switch (this) {
			case JACK:
				return "J";
			case QUEEN:
				return "Q";
			case KING:
				return "K";
			default:
				return Integer.toString(ordinal() + 1);
		}
	}
	
	public boolean isFace() {
		return this == JACK || this == QUEEN || this == KING;
	}
}
