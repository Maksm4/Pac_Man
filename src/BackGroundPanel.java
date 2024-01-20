import javax.swing.*;
import java.awt.*;

public class BackGroundPanel extends JPanel {
    private ImageIcon backgroundImage;

    public BackGroundPanel(ImageIcon backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
    }

}
