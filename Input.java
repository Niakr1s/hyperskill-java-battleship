package battleship;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.Scanner;

public class Input {
    private static Scanner in = new Scanner(System.in);

    public void askCoordinates(Ship nextShip) {
        System.out.printf("Enter the coordinates of the %s (%d cells):\n", nextShip.getName(), nextShip.getLength());
    }

    public Pos getPos() {
        return new Pos(in.next());
    }

    private void waitForEnter() {
        System.out.println("Press Enter and pass the move to another player");
        in.nextLine();
        in.nextLine();
    }

    private String exceptionToString(Exception e) {
        return String.format("Error! %s Try again:\n", e.getMessage());
    }

    public void fillBoard(Player player) {
        Board.ShipBuilder builder = player.getBoard().new ShipBuilder();

        System.out.printf("%s, place your ships on the game field\n", player.getName());
        System.out.println(player.getBoard());
        while (builder.shipsRemained() > 0) {
            this.askCoordinates(builder.nextShip());
            Pos start = this.getPos();
            Pos end = this.getPos();
            try {
                builder.createShip(start, end);
                System.out.println(player.getBoard());
            } catch (Board.ShipBuildException e) {
                System.out.println(exceptionToString(e));
            }
        }
        waitForEnter();
    }

    public void startGame() {
        System.out.println("The game starts!");
    }

    public void congratulate() {
        System.out.println("You sank the last ship. You won. Congratulations!");
    }

    public void shoot(Player player) {
        try {
            Pos pos = getPos();
            Cell cell = player.getBoard().shoot(pos);
            String message;
            switch (cell.getType()) {
                case MISSED:
                    message = "You missed!";
                    break;
                case HIT_SHIP:
                    message = player.getBoard().isSunk(cell.getShip())
                            ? "You sank a ship!"
                            : "You hit a ship!";
                    break;
                default:
                    throw new RuntimeException("never");
            }
            if (!player.getBoard().allShipsSunk()) {
                System.out.println(message);
                this.waitForEnter();
            }
        } catch (Board.WrongCoordinatesException e) {
            shoot(player);
        }
    }
}
