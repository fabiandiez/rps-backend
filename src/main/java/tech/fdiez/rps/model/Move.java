package tech.fdiez.rps.model;

public enum Move {
    ROCK,
    PAPER,
    SCISSORS;

    public boolean beats(Move move) {
        return switch (this) {
            case ROCK -> move == SCISSORS;
            case PAPER -> move == ROCK;
            case SCISSORS -> move == PAPER;
        };
    }
}
