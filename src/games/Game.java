package games;

import java.util.List;

public interface Game {

	void setVerbose(boolean verbose);

	List<GameResult> game();

}