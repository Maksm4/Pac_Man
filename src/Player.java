import javax.swing.*;
import javax.swing.table.TableModel;

public class Player {
    public int playerX;
    public int playerY;
    private int health;

    public Player(int playerX, int playerY) {
        this.playerX = playerX;
        this.playerY = playerY;
        this.health = 3;
    }

    public int getX() {
        return playerX;
    }

    public int getY() {
        return playerY;
    }

    public void setX(int playerX) {
        this.playerX = playerX;
    }

    public void setY(int playerY) {
        this.playerY = playerY;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}

