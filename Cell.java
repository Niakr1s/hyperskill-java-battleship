package battleship;

public class Cell {
    public enum Type {
        UNDAMAGED_SHIP("O", false),
        MISSED("M", true),
        HIT_SHIP("X", true);

        private final String str;
        private final boolean showInFog;

        Type(String str, boolean showInFog) {
            this.str = str;
            this.showInFog = showInFog;
        }

        public boolean shouldShowInFog() {
            return showInFog;
        }

        @Override
        public String toString() {
            return this.str;
        }
    }

    private final Type type;
    private final Ship ship;

    public Cell(Type type) {
        this.type = type;
        this.ship = null;
    }
    public Cell(Type type, Ship ship) {
        this.type = type;
        this.ship = ship;
    }

    public Type getType() {
        return type;
    }

    public Ship getShip() {
        return ship;
    }

    @Override
    public String toString() {
        return this.type.toString();
    }
}
