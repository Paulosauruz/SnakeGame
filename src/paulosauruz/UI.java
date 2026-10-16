package paulosauruz;

import java.awt.*;

public class UI {
    GamePanel gp;
    Font arial_40;
    Color color;

    public UI(GamePanel gp) {
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 20);
        color = new Color(255, 255, 255, 200);
    }

    public void draw(Graphics2D g2d) {
        g2d.setFont(arial_40);
        g2d.setColor(color);
        g2d.drawString("Score: " + gp.snake.length, gp.screenWidth - 100, 30);
        g2d.drawString("Current Speed: " + gp.snake.speed, gp.screenWidth - 170, 60);
    }
}
