import javax.swing.*;

public class Coin extends Obstacle {
    private boolean collected;
    private ImageIcon icon;

    public Coin(ImageIcon icon, int row, int column) {
        super(row, column);
        this.icon = icon;
        this.collected = false;
    }

    public Coin(int row, int column) {
        super(row, column);
    }


    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public ImageIcon getIcon() {
        return icon;
    }


    @Override
    public void setRow(int row) {
        super.setRow(row);
    }

    @Override
    public void setColumn(int column) {
        super.setColumn(column);
    }
}
