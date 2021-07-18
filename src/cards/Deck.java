package cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck<S, N> {
	private List<Card<S, N>> cards;
	
	public Deck() {
		cards = new ArrayList<>();
	}
	
	public void empty() {
		cards.clear();
	}
	
	public int count() {
		return cards.size();
	}
	
	public void add(Card<S, N> card) {
		cards.add(card);
	}
	
	public void addAll(List<Card<S, N>> cards) {
		cards.addAll(cards);
	}
	
	public Card<S, N> draw() {
		return cards.remove(0);
	}
	
	public void fill(S[] suits, N[] numbers) {
		for (S s : suits) {
			for (N n : numbers) {
				cards.add(new Card<S, N>(s, n));
			}
		}
	}
	
	public void shuffle() {
		Collections.shuffle(cards);
	}
	
	@Override
	public String toString() {
		return cards.toString();
	}

}
