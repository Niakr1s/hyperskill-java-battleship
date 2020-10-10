package battleship;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;

public class Board {
    private final Config config;
    Cell[][] area;

    public Board(Config config) {
        this.config = config;
        this.area = new Cell[this.config.getDimension()][this.config.getDimension()];
    }

    public Cell getCell(Pos pos) {
        return this.area[pos.row][pos.col];
    }

    private void setCell(Pos pos, Cell cell) {
        this.area[pos.row][pos.col] = cell;
    }

    public boolean isInside(Pos pos) {
        try {
            this.getCell(pos);
            return true;
        } catch (ArrayIndexOutOfBoundsException ignored) {
            return false;
        }
    }

    private boolean isOccupied(Pos pos) {
        try {
            Cell cell = this.getCell(pos);
            return !Objects.isNull(cell);
        } catch (ArrayIndexOutOfBoundsException ignored) {
            return false;
        }
    }

    private boolean allowedForBuild(Pos pos) {
        if (this.isOccupied(pos)) {
            return false;
        }
        Pos[] adjacentPoses = pos.getAdjacentPoses();
        for (Pos adjacentPos : adjacentPoses) {
            if (isOccupied(adjacentPos)) {
                return false;
            }
        }
        return true;
    }

    public boolean isSunk(Ship ship) {
        for (int row = 0; row < config.getDimension(); row++) {
            for (int col = 0; col < config.getDimension(); col++) {
                Cell cell = getCell(new Pos(row, col));
                if (!Objects.isNull(cell) && cell.getShip() == ship && cell.getType() == Cell.Type.UNDAMAGED_SHIP) {
                    return false;
                }
            }
        }
        return true;
    }

    public static class WrongCoordinatesException extends Exception {
        public WrongCoordinatesException() {
            super("You entered the wrong coordinates!");
        }
    }

    public Cell shoot(Pos pos) throws WrongCoordinatesException {
        if (!isInside(pos)) {
            throw new WrongCoordinatesException();
        }
        Cell cell = getCell(pos);
        Cell newCell;
        if (!Objects.isNull(cell) && !Objects.isNull(cell.getShip())) {
            newCell = new Cell(Cell.Type.HIT_SHIP, cell.getShip());
        } else {
            newCell = new Cell(Cell.Type.MISSED);
        }
        setCell(pos, newCell);
        return newCell;
    }

    public boolean allShipsSunk() {
        for (int row = 0; row < config.getDimension(); row++) {
            for (int col = 0; col < config.getDimension(); col++) {
                Cell cell = getCell(new Pos(row, col));
                if (!Objects.isNull(cell) && cell.getType() == Cell.Type.UNDAMAGED_SHIP) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return this.toString(false);
    }

    public String toString(boolean fogOfWar) {
        final StringBuilder res = new StringBuilder(Str.SPACE.repeat(2));

        // header
        for (int row = 0; row < this.config.getDimension(); row++) {
            res.append(row + 1).append(Str.SPACE);
        }
        res.append(Str.NEWLINE);

        // main area
        for (int row = 0, alpha = 'A'; row < this.config.getDimension(); row++, alpha++) {
            res.append((char) alpha).append(Str.SPACE);
            for (int col = 0; col < this.config.getDimension(); col++) {
                Cell cell = getCell(new Pos(row, col));
                String cellString;
                if (fogOfWar) {
                    cellString = Objects.isNull(cell) || !cell.getType().shouldShowInFog()
                            ? Str.EMPTY_CELL
                            : cell.toString();
                } else {
                    cellString = Objects.isNull(cell)
                            ? Str.EMPTY_CELL
                            : cell.toString();
                }
                res.append(cellString).append(Str.SPACE);
            }
            res.append(Str.NEWLINE);
        }

        return res.toString();
    }

    public static class ShipBuildException extends Exception {
        public ShipBuildException(String message) {
            super(message);
        }
    }

    public class ShipBuilder {
        private final Queue<Ship> remainedShips;

        public ShipBuilder() {
            this(config.getAllowedShips());
        }

        public ShipBuilder(Ship[] allowedShips) {
            this.remainedShips = new ArrayDeque<>(Arrays.asList(allowedShips));
        }

        public int shipsRemained() {
            return this.remainedShips.size();
        }

        public Ship nextShip() {
            return this.remainedShips.peek();
        }

        public void createShip(Pos start, Pos end) throws ShipBuildException {
            if (!isInside(start) || !isInside(end)) {
                throw new ShipBuildException("Wrong ship location!");
            }
            Ship ship = this.nextShip();
            if (Objects.isNull(ship)) {
                throw new ShipBuildException("No ships remained!");
            }
            Pos[] posesBetween = start.posesBetween(end);
            if (posesBetween.length != ship.getLength()) {
                throw new ShipBuildException(String.format("Wrong length of the %s!", ship.getName()));
            }
            if (!Arrays.stream(posesBetween).allMatch(Board.this::allowedForBuild)) {
                throw new ShipBuildException("You placed it too close to another one.");
            }
            for (Pos pos : posesBetween) {
                setCell(pos, new Cell(Cell.Type.UNDAMAGED_SHIP, ship));
            }
            this.remainedShips.poll();
        }
    }
}
