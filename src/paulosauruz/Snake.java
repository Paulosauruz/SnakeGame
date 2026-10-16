package paulosauruz;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    final int defaultPosX = 100;
    final int defaultPosY = 100;
    final int defaultSpeed = 5;
    final int defaultLength = 30;
    final int maxSpeed = 12;

    public int posX;
    public int posY;
    public int speed;
    public int length;

    public List<Integer> posXList;
    public List<Integer> posYList;

    public Snake() {
        this.posX = defaultPosX;
        this.posY = defaultPosY;
        this.speed = defaultSpeed;
        this.length = defaultLength;

        this.posXList = new ArrayList<>();
        this.posYList = new ArrayList<>();

//        for (int i = 0; i < length; i++) {
//            this.posXList.add(-100);
//            this.posYList.add(-100);
//        }
    }

    public void reset() {
        this.posX = defaultPosX;
        this.posY = defaultPosY;
        this.speed = defaultSpeed;
        this.length = defaultLength;

        this.posXList = new ArrayList<>();
        this.posYList = new ArrayList<>();
    }

    public void addLength(int amount) {
        length += amount;
//        for (int i = 0; i < amount; i++) {
//            this.posXList.add(-100);
//            this.posYList.add(-100);
//        }
        if (this.speed < maxSpeed) {
            this.speed = (int) defaultSpeed + ((this.length - defaultLength) / 10);
        }
    }
}
