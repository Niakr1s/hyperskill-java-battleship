package battleship;

class Player {
    private final String name;
    private final Board board;

    public Player(String name, Config config) {
        this.name = name;
        this.board = new Board(config);
    }

    public String getName() {
        return name;
    }

    public Board getBoard() {
        return board;
    }
}
