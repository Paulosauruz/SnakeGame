package paulosauruz;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;

    public KeyHandler() {
        this.rightPressed = true;
        this.upPressed = false;
        this.downPressed = false;
        this.leftPressed = false;
    }

    public void reset() {
        this.rightPressed = true;
        this.upPressed = false;
        this.downPressed = false;
        this.leftPressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if ((code == KeyEvent.VK_W || code == KeyEvent.VK_UP) && !downPressed) {
            upPressed = true;
            leftPressed = rightPressed = false;
        }
        if ((code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) && !upPressed) {
            downPressed = true;
            leftPressed = rightPressed = false;
        }
        if ((code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) && !rightPressed) {
            leftPressed = true;
            upPressed = downPressed = false;
        }
        if ((code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) && !leftPressed) {
            rightPressed = true;
            upPressed = downPressed = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
