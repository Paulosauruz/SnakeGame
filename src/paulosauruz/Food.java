package paulosauruz;

import java.awt.*;
import java.util.Random;

public class Food {
    public int posX, posY;
    public int xlim, ylim;
    public Color color;

    public Food(int xlim, int ylim, Color color) {
        this.xlim = xlim;
        this.ylim = ylim;
        this.color = color;

        Random random = new Random();
        posX = random.nextInt(xlim);
        posY = random.nextInt(ylim);
    }
}
