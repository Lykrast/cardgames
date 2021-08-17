package games.fyg;

import java.util.Random;

public class FygBotRandom implements FygPlayer {
	private String name;
	private double chance;
	private Random random;

	public FygBotRandom(String name, double chance) {
		this.name = name;
		this.chance = chance;
		random = new Random();
	}

	public FygBotRandom(String name, double chance, long seed) {
		this.name = name;
		this.chance = chance;
		random = new Random(seed);
	}

	@Override
	public boolean hit(int currentScore, int blanks, int oppMaxScore) {
		return blanks >= 1 || random.nextDouble() <= chance;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name + " (" + (int)(chance * 100) + "%)";
	}

}
