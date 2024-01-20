import javax.swing.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Ghost implements Runnable {
    private int row;
    private int column;
    private boolean alive;
    private ImageIcon icon;

    private Direction currentDirection;
    private Direction previousDirection;
    private JTable table;
    private final GameTableModel model;
    private ArrayList<Obstacle> walls;
    private final ConcurrentLinkedQueue<Coin> coins;


    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }


    public Ghost(ConcurrentLinkedQueue<Coin> coins, ArrayList<Obstacle> walls, GameTableModel model, JTable table, ImageIcon ghostIcon, int column, int row) {
        this.row = row / 2 + 2;
        this.column = column / 2;
        this.currentDirection = Direction.RIGHT;
        this.previousDirection = Direction.LEFT;
        this.alive = true;
        this.table = table;
        this.model = model;
        this.walls = walls;
        this.icon = ghostIcon;
        this.coins = coins;


    }


    public void move() {


        int rand = new Random().nextInt(4);

        Direction nextDirection = null;
        switch (rand) {
            case 0 -> nextDirection = Direction.UP;
            case 1 -> nextDirection = Direction.DOWN;
            case 2 -> nextDirection = Direction.LEFT;
            case 3 -> nextDirection = Direction.RIGHT;
        }

        if (nextDirection != getOppositeDirection(previousDirection)) {
            previousDirection = currentDirection;
            currentDirection = nextDirection;
        }


        if (currentDirection == Direction.UP) {

            for (int i = 0; i < 5; i++) {
                if (!isWall(row - 1, column)) {
                    model.setValueAt(null, row, column);
                    if (GamePanel.isCoin(row, column)) {
                        for (Coin coin : coins) {
                            if (coin.getRow() == row && coin.getColumn() == column) {
                                if (!coin.isCollected()) {
                                    model.setValueAt(coin.getIcon(), row, column);
                                }

                                break;
                            }
                        }

                    }

                    this.row -= 1;

                    model.setValueAt(this.getIcon(), row, column);


                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else break;
            }


        } else if (currentDirection == Direction.DOWN) {

            for (int i = 0; i < 5; i++) {
                if (!isWall(row + 1, column)) {
                    model.setValueAt(null, row, column);
                    if (GamePanel.isCoin(row, column)) {
                        for (Coin coin : coins) {
                            if (coin.getRow() == row && coin.getColumn() == column) {
                                if (!coin.isCollected()) {
                                    model.setValueAt(coin.getIcon(), row, column);
                                }

                                break;
                            }
                        }
                    }
                    this.row += 1;

                    model.setValueAt(this.getIcon(), row, column);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else break;
            }


        } else if (currentDirection == Direction.LEFT) {

            for (int i = 0; i < 5; i++) {

                if (!isWall(row, column - 1)) {
                    model.setValueAt(null, row, column);
                    if (GamePanel.isCoin(row, column)) {
                        for (Coin coin : coins) {
                            if (coin.getRow() == row && coin.getColumn() == column) {
                                if (!coin.isCollected()) {
                                    model.setValueAt(coin.getIcon(), row, column);
                                }
                                break;
                            }
                        }
                    }
                    this.column -= 1;
                    model.setValueAt(this.getIcon(), row, column);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else break;

            }


        } else if (currentDirection == Direction.RIGHT) {

            for (int i = 0; i < 5; i++) {
                if (!isWall(row, column + 1)) {
                    model.setValueAt(null, row, column);
                    if (GamePanel.isCoin(row, column)) {
                        for (Coin coin : coins) {
                            if (coin.getRow() == row && coin.getColumn() == column) {
                                if (!coin.isCollected()) {
                                    model.setValueAt(coin.getIcon(), row, column);
                                }
                                break;
                            }
                        }
                    }

                    this.column += 1;

                    model.setValueAt(this.getIcon(), row, column);


                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else break;
            }


        }


    }


    public void run() {
        while (alive) {
            move();
            table.repaint();

        }
    }

    private Direction getOppositeDirection(Direction direction) {
        return switch (direction) {
            case UP -> Direction.DOWN;
            case DOWN -> Direction.UP;
            case LEFT -> Direction.RIGHT;
            case RIGHT -> Direction.LEFT;
            default -> null;
        };
    }

    public boolean isAlive() {
        return alive;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public Direction getPreviousDirection() {
        return previousDirection;
    }

    private boolean isWall(int row, int column) {
        for (Obstacle wall : walls) {
            if (wall.getRow() == row && wall.getColumn() == column) {
                return true;
            }
        }
        return false;
    }

    public boolean isCoinCollected(int row, int column) {
        for (Coin coin : coins) {
            if (coin.getRow() == row && coin.getColumn() == column) {
                return coin.isCollected();

            }
        }
        return false;

    }


    private void setRow(int row) {
        this.row = row;
    }

    private void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
