import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class LawnMower {
    private int x;
    private int y;
    private boolean active;
    private boolean used;
    private BufferedImage image;
    private static final int SPEED = 8;
    private static final int WIDTH = 71;
    private static final int HEIGHT = 57;

    public LawnMower(int x, int y) {
        this.x = x;
        this.y = y;
        this.active = false;
        this.used = false;
        image = Plant.loadResourceImage("Screen/car.png");
    }

    public void update(List<Zombie> zombies) {
        if (used) {
            return;
        }

        if (!active) {
            for (Zombie zombie : zombies) {
                if (!zombie.isDead() && Math.abs(zombie.getY() - y) <= 35 && zombie.getX() <= x + 30) {
                    active = true;
                    break;
                }
            }
        }

        if (active) {
            x += SPEED;
            for (Zombie zombie : zombies) {
                if (!zombie.isDead() && Math.abs(zombie.getY() - y) <= 35 && Math.abs(zombie.getX() - x) <= 45) {
                    zombie.takeDamage(10000);
                }
            }
            if (x > 950) {
                used = true;
                active = false;
            }
        }
    }

    public void draw(Graphics g) {
        if (used) {
            return;
        }

        if (image != null) {
            g.drawImage(image, x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x - 25, y - 15, 50, 30);
            g.setColor(Color.BLACK);
            g.fillOval(x - 20, y + 10, 12, 12);
            g.fillOval(x + 8, y + 10, 12, 12);
        }
    }

    public boolean isUsed() {
        return used;
    }

    public int getY() {
        return y;
    }
}
