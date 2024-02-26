// Import necessary libraries
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SnakeGame extends JPanel implements KeyListener {
    // Define constants
    private static final int BOX_SIZE = 20; // Size of each grid box
    private static final int WIDTH = 30; // Number of horizontal boxes
    private static final int HEIGHT = 20; // Number of vertical boxes

    private int[][] grid; // Grid representing the game environment
    private int snakeLength;
    private int[] snakeX, snakeY;
    private int foodX, foodY;
    private boolean gameOver;
    private Direction direction;

    // Enum to represent directions
    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public SnakeGame() {
        // Initialize game variables
        grid = new int[WIDTH][HEIGHT];
        snakeLength = 1;
        snakeX = new int[WIDTH * HEIGHT];
        snakeY = new int[WIDTH * HEIGHT];
        snakeX[0] = WIDTH / 2;
        snakeY[0] = HEIGHT / 2;
        gameOver = false;
        direction = Direction.RIGHT;

        // Set up the game window
        setPreferredSize(new Dimension(WIDTH * BOX_SIZE, HEIGHT * BOX_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        // Start the game loop
        new Thread(() -> {
            while (!gameOver) {
                update();
                repaint();
                try {
                    Thread.sleep(100); // Adjust the speed of the game
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void update() {
        // Update snake's position based on direction
        int newX = snakeX[0];
        int newY = snakeY[0];
        switch (direction) {
            case UP:
                newY--;
                break;
            case DOWN:
                newY++;
                break;
            case LEFT:
                newX--;
                break;
            case RIGHT:
                newX++;
                break;
        }

        // Check for collisions
        if (newX < 0 || newX >= WIDTH || newY < 0 || newY >= HEIGHT) {
            gameOver = true; // Hit the wall
            return;
        }
        for (int i = 1; i < snakeLength; i++) {
            if (newX == snakeX[i] && newY == snakeY[i]) {
                gameOver = true; // Hit itself
                return;
            }
        }

        // Move the snake
        for (int i = snakeLength - 1; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }
        snakeX[0] = newX;
        snakeY[0] = newY;

        // Check if snake eats food
        if (newX == foodX && newY == foodY) {
            snakeLength++;
            placeFood();
        }
    }

    private void placeFood() {
        // Place food at random position
        foodX = (int) (Math.random() * WIDTH);
        foodY = (int) (Math.random() * HEIGHT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the snake
        g.setColor(Color.GREEN);
        for (int i = 0; i < snakeLength; i++) {
            g.fillRect(snakeX[i] * BOX_SIZE, snakeY[i] * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        }
        // Draw the food
        g.setColor(Color.RED);
        g.fillRect(foodX * BOX_SIZE, foodY * BOX_SIZE, BOX_SIZE, BOX_SIZE);

        // Game over message
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over", WIDTH * BOX_SIZE / 2 - 100, HEIGHT * BOX_SIZE / 2);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Change snake's direction based on user input
        int key = e.getKeyCode();
        if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && direction != Direction.DOWN) {
            direction = Direction.UP;
        } else if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && direction != Direction.UP) {
            direction = Direction.DOWN;
        } else if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && direction != Direction.RIGHT) {
            direction = Direction.LEFT;
        } else if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && direction != Direction.LEFT) {
            direction = Direction.RIGHT;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    // Main method to start the game
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new SnakeGame());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
