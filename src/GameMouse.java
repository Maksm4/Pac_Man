import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class GameMouse implements MouseListener {

    private final GamePanel gamePanel;

    public GameMouse(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        gamePanel.setCursor(gamePanel.getToolkit().createCustomCursor(
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                new Point(),
                null)); //from stackoverflow
        // https://stackoverflow.com/questions/1984071/how-to-hide-cursor-in-a-swing-application
    }

    @Override
    public void mouseExited(MouseEvent e) {
        gamePanel.setCursor(Cursor.getDefaultCursor());
    }
}
