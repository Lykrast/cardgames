package games.riviera;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RivieraBotRandom implements RivieraPlayer {
	private String name;
	private Random random;
	//If true, can randomly discard cards even when it could actually fish
	private boolean playDiscards;

	public RivieraBotRandom(String name, boolean playDiscards) {
		this.name = name;
		this.playDiscards = playDiscards;
		random = new Random();
	}

	public RivieraBotRandom(String name, boolean playDiscards, long seed) {
		this.name = name;
		this.playDiscards = playDiscards;
		random = new Random(seed);
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		if (playDiscards) return name + " (D)";
		else return name;
	}

	@Override
	public RivieraMove playMove(List<RivieraMove> possible, RivieraPlayerContext context) {
		if (!playDiscards) {
			List<RivieraMove> filtered = new ArrayList<>();
			for (RivieraMove m : possible) if (!m.isDiscard()) filtered.add(m);
			if (!filtered.isEmpty()) return filtered.get(random.nextInt(filtered.size()));
		}
		return possible.get(random.nextInt(possible.size()));
	}

}
