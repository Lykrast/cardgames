package games.riviera;

import java.util.List;

import games.Player;

public interface RivieraPlayer extends Player {
	RivieraMove playMove(List<RivieraMove> possible, RivieraPlayerContext context);
}
