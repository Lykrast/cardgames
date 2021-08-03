package cards;

import java.util.Comparator;

public class CardComparator<S extends Comparable<S>, N extends Comparable<N>> implements Comparator<Card<S,N>> {
	private boolean suitFirst;
	
	private CardComparator(boolean suitFirst) {
		this.suitFirst = suitFirst;
	}
	
	public static <S extends Comparable<S>, N extends Comparable<N>> CardComparator<S,N> suitThenNumber() {
		return new CardComparator<S, N>(true);
	}
	
	public static <S extends Comparable<S>, N extends Comparable<N>> CardComparator<S,N> numberThenSuit() {
		return new CardComparator<S, N>(false);
	}

	@Override
	public int compare(Card<S, N> o1, Card<S, N> o2) {
		int compare;
		if (suitFirst) {
			compare = o1.suit().compareTo(o2.suit());
			if (compare != 0) return compare;
			else return o1.number().compareTo(o2.number());
		}
		else {
			compare = o1.number().compareTo(o2.number());
			if (compare != 0) return compare;
			else return o1.suit().compareTo(o2.suit());
		}
	}

}
