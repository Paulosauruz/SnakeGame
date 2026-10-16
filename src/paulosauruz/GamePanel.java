package paulosauruz;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GamePanel extends JPanel implements Runnable {
    // Screen Settings
    final int originalTileSize = 4;
    final int scale = 3;

    final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 64;
    final int maxScreenRow = 48;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenLength = tileSize * maxScreenRow;

    final double FPS = 30;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    volatile boolean running;
    Snake snake = new Snake();
    ArrayList<Food> foodList = new ArrayList<>();
    UI ui = new UI(this);

    // constructor
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenLength));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        running = true;
    }


    // game loop
    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS; // nanosecond per frame
        double nextDrawTime = drawInterval + System.nanoTime();

        while (gameThread != null && running) {

            // update
            update();
            // System.out.println("Frame updated");

            //draw
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000; // turn into millisecond
                remainingTime = remainingTime < 0 ? 0 : remainingTime;

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        gameOverPopup();
    }

    public void update() {
        // System.out.println(Thread.currentThread().getName());

        if (keyH.upPressed) {
            snake.posY -= snake.speed;
        }
        if (keyH.downPressed) {
            snake.posY += snake.speed;
        }
        if (keyH.leftPressed) {
            snake.posX -= snake.speed;
        }
        if (keyH.rightPressed) {
            snake.posX += snake.speed;
        }

        updateSnakeList();
        updateFoodList();
        checkEat();
        checkLose();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // rendering snake
        g2d.setColor(Color.white);
        for (int i = 0; i < snake.posXList.size(); i++) {
            g2d.fillRect(snake.posXList.get(i), snake.posYList.get(i), tileSize, tileSize);
        }

        // rendering food
        for (Food food : foodList) {
            g2d.setColor(food.color);
            g2d.fillOval(food.posX, food.posY, tileSize, tileSize);
        }

        ui.draw(g2d);

        g2d.dispose();
    }

    public void updateSnakeList() {
        snake.posXList.add(snake.posX);
        snake.posYList.add(snake.posY);

        while (snake.posXList.size() > snake.length) {
            snake.posXList.remove(0);
            snake.posYList.remove(0);
        }

//        System.out.println("posX: " + snake.posXList);
//        System.out.println("posY: " + snake.posYList);
    }

    public void updateFoodList() {
        Random random = new Random();
        int token = random.nextInt(100);
        if (token < 10) {
            Food food = new Food(screenWidth, screenLength, Color.orange);
            foodList.add(food);
            System.out.println("New food spawned! Location: (" + food.posX + ", " + food.posY + ")");
        }
    }

    public void checkEat() {
        for (int i = 0; i < snake.posXList.size(); i++) {
            Set<Integer> snakeCollisionX =
                    IntStream.rangeClosed(snake.posXList.get(i) - tileSize, snake.posXList.get(i) + tileSize)
                            .boxed().collect(Collectors.toSet());
            Set<Integer> snakeCollisionY =
                    IntStream.rangeClosed(snake.posYList.get(i) - tileSize, snake.posYList.get(i) + tileSize)
                            .boxed().collect(Collectors.toSet());

            ArrayList<Food> toRemove = new ArrayList<>();
            for (Food food : foodList) {
                if (snakeCollisionX.contains(food.posX) && snakeCollisionY.contains(food.posY)){
                    toRemove.add(food);
                    snake.addLength(3);
                    System.out.println("Ate 1 food! Current length: " + snake.length);
                }
            }
            foodList.removeAll(toRemove);
        }
    }

    public void checkLose() {
        for (int i = 0; i < snake.posXList.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (Stream.of(snake.posXList.get(i), snake.posXList.get(j), snake.posYList.get(i), snake.posYList.get(j)).noneMatch(x -> x.equals(-100))) {
                    if (snake.posXList.get(i).equals(snake.posXList.get(j)) && snake.posYList.get(i).equals(snake.posYList.get(j))) {
                        gameOver();
                        return;
                    }
                }
            }
            if (snake.posXList.get(i) != -100 && snake.posYList.get(i) != -100) {
                if (snake.posXList.get(i) < 0 || snake.posXList.get(i) > screenWidth
                        || snake.posYList.get(i) < 0 || snake.posYList.get(i) > screenLength) {
                    gameOver();
                    return;
                }
            }
        }
    }

    public void gameOver() {
        System.out.println("Game Over");
        System.out.println(snake.posXList);
        System.out.println(snake.posYList);

        running = false;
    }

    public void gameOverPopup() {
        Object[] options = {"Restart", "Exit"};

        int choice = JOptionPane.showOptionDialog(
                this,
                "GAME OVER! Score: " + snake.length,
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[0]
        );

        switch (choice) {
            case 0:
                System.out.println("Restart");
                restartGame();
                break;
            case 1:
                System.out.println("Exit");
                System.exit(0);
                break;
        }
    }

    public void restartGame() {
        System.out.println("Restart Game");
        snake.reset();
        foodList = new ArrayList<>();
        keyH.reset();

        startGameThread();
    }
}
