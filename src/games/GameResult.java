package games;

public class GameResult {
	private final Player player;
	private final Result result;
	
	public GameResult(Player player, Result result) {
		this.player = player;
		this.result = result;
	}

	public Player player() {
		return player;
	}

	public Result result() {
		return result;
	}
	
	@Override
	public String toString() {
		return player + " - " + result;
	}
}
