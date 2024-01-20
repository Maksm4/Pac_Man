import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyMovement implements KeyListener {

    private boolean up, down, left, right, shortcut; //false at start

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int pressedKey = e.getKeyCode();

        if (pressedKey == KeyEvent.VK_W || pressedKey == KeyEvent.VK_UP) {
            up = true;
        } else if (pressedKey == KeyEvent.VK_S || pressedKey == KeyEvent.VK_DOWN) {
            down = true;
        } else if (pressedKey == KeyEvent.VK_D || pressedKey == KeyEvent.VK_RIGHT) {
            right = true;
        } else if (pressedKey == KeyEvent.VK_A || pressedKey == KeyEvent.VK_LEFT) {
            left = true;
        } else if (e.isAltDown() && e.isControlDown() && pressedKey == KeyEvent.VK_Q) {
            shortcut = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int pressedKey = e.getKeyCode();

        if (pressedKey == KeyEvent.VK_W || pressedKey == KeyEvent.VK_UP) {
            up = false;
        } else if (pressedKey == KeyEvent.VK_S || pressedKey == KeyEvent.VK_DOWN) {
            down = false;
        } else if (pressedKey == KeyEvent.VK_D || pressedKey == KeyEvent.VK_RIGHT) {
            right = false;
        } else if (pressedKey == KeyEvent.VK_A || pressedKey == KeyEvent.VK_LEFT) {
            left = false;
        }
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isShortcut() {
        return shortcut;
    }
}
