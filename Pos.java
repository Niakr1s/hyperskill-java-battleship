package battleship;

import java.util.Objects;

public class Pos {
    int row;
    int col;

    public Pos(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Pos(String str) {
        this.row = str.charAt(0) - 'A';
        this.col = Integer.parseInt(str.substring(1)) - 1;
    }

    public boolean onOneLine(Pos other) {
        return this.posesBetween(other).length != 0;
    }

    /**
     * @return empty array, if Pos's are not one one line.
     */
    public Pos[] posesBetween(Pos other) {
        if (this.row == other.row) {
            int row = this.row;
            int length = Math.abs(this.col - other.col) + 1;
            int start = Math.min(this.col, other.col);
            int end = start + length;
            Pos[] res = new Pos[length];
            for (int col = start, i = 0; col < end; col++, i++) {
                res[i] = new Pos(row, col);
            }
            return res;
        } else if (this.col == other.col) {
            int col = this.col;
            int length = Math.abs(this.row - other.row) + 1;
            int start = Math.min(this.row, other.row);
            int end = start + length;
            Pos[] res = new Pos[length];
            for (int row = start, i = 0; row < end; row++, i++) {
                res[i] = new Pos(row, col);
            }
            return res;
        }
        return new Pos[0];
    }

    static final Pos[] offsets = new Pos[]{
            new Pos(-1, -1),
            new Pos(-1, 0),
            new Pos(-1, 1),
            new Pos(0, -1),
            new Pos(0, 1),
            new Pos(1, -1),
            new Pos(1, 0),
            new Pos(1, 1),
    };

    public Pos[] getAdjacentPoses() {
        Pos[] res = new Pos[offsets.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = this.plus(offsets[i]);
        }
        return res;
    }

    public Pos plus(Pos other) {
        return new Pos(this.row + other.row, this.col + other.col);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pos pos = (Pos) o;
        return row == pos.row &&
                col == pos.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
