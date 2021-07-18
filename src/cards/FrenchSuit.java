package cards;

public enum FrenchSuit {
	CLUB, SPADE, HEART, DIAMOND;

	@Override
	public String toString() {
		switch (this) {
			case CLUB:
				return "C";
			case SPADE:
				return "S";
			case HEART:
				return "H";
			case DIAMOND:
				return "D";
		}
		return "?";
	}
}
