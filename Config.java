package battleship;

public class Config {
    private final int dimension;
    private final Ship[] allowedShips;

    private static final Ship[] defaultAllowedShips = new Ship[]{
            new Ship("Aircraft Carrier", 5),
            new Ship("Battleship", 4),
            new Ship("Submarine", 3),
            new Ship("Cruiser", 3),
            new Ship("Destroyer", 2),
    };

    public Config(int dimension) {
        this(dimension, defaultAllowedShips);
    }

    public Config(int dimension, Ship[] allowedShips) {
        this.dimension = dimension;
        this.allowedShips = allowedShips;
    }

    public int getDimension() {
        return dimension;
    }

    public Ship[] getAllowedShips() {
        return allowedShips;
    }
}
