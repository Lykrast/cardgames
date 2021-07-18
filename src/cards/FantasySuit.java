package cards;

public enum FantasySuit {
	STAR, LEAF, COIN, SWORD;

	@Override
	public String toString() {
		switch (this) {
			case STAR:
				return "S";
			case LEAF:
				return "L";
			case COIN:
				return "C";
			case SWORD:
				return "W";
		}
		return "?";
	}
}