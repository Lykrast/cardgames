package games;

public interface Player {
	String getName();
	default void resetState() {}
}
