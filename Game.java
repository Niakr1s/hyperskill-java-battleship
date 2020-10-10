package battleship;

public class Game {
    private final Player[] players;
    private int currentPlayer = 0;
    private final Input input;

    public Game(Config config) {
        this.players = new Player[]{
                new Player("Player1", config),
                new Player("Player2", config),
        };
        this.input = new Input();
    }

    public void prepare() {
        for (Player player : players) {
            input.fillBoard(player);
        }
    }

    public void run() {
        Player player, opponent;
        do {
            player = getPlayer();
            opponent = getOpponent();

            System.out.println(opponent.getBoard().toString(true));
            System.out.println("---------------------");
            System.out.println(player.getBoard().toString(false));

            System.out.printf("%s, it's your turn!\n", player.getName());

            input.shoot(opponent);
            switchPlayer();
        } while (!opponent.getBoard().allShipsSunk());
        input.congratulate();
    }

    private Player getPlayer() {
        return this.players[this.currentPlayer];
    }

    private Player getOpponent() {
        return this.players[1 - this.currentPlayer];
    }

    public void switchPlayer() {
        this.currentPlayer = 1 - this.currentPlayer;
    }
}

