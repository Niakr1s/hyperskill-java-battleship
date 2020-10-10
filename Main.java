package battleship;

public class Main {
    public static void main(String[] args) throws Board.ShipBuildException {
        Config config = new Config(10);

        Game game = new Game(config);

        game.prepare();
        game.run();
    }
}
