package cards;

public enum FantasyNumber {
	BLANK, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, ELEVEN, TWELVE, FLOWER, DRAGON;

	@Override
	public String toString() {
		switch (this) {
			case FLOWER:
				return "F";
			case DRAGON:
				return "D";
			default:
				return Integer.toString(ordinal());
		}
	}

	public boolean isSpecial() {
		return this == BLANK || this == FLOWER || this == DRAGON;
	}

	public int value() {
		return ordinal();
	}
}
