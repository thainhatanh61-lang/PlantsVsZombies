import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.ImageIcon;

public class LawnMower {
    private int x;
    private int y;
    private boolean active;
    private boolean used;
    private BufferedImage image;
    private ImageIcon activatedEffect;
    private int activatedEffectTimer;
    private static final int SPEED = 14;
    private static final int WIDTH = 71;
    private static final int HEIGHT = 57;
    private static final int TRIGGER_X = 110;
    private static final int INSTANT_KILL_DAMAGE = 999999;

    public LawnMower(int x, int y) {
        this.x = x;
        this.y = y;
        this.active = false;
        this.used = false;
        image = Plant.loadResourceImage("Screen/car.png");
        activatedEffect = new ImageIcon("resources/graphics/Screen/lawnmowerActivated.gif");
    }

    public void update(List<Zombie> zombies) {
        if (used) {
            return;
        }

        if (!active) {
            for (Zombie zombie : zombies) {
                if (!zombie.isDead() && Math.abs(zombie.getY() - y) <= 35 && zombie.getX() <= TRIGGER_X) {
                    active = true;
                    activatedEffectTimer = 35;
                    break;
                }
            }
        }

        if (active) {
            x += SPEED;
            if (activatedEffectTimer > 0) {
                activatedEffectTimer--;
            }
            for (Zombie zombie : zombies) {
                if (!zombie.isDead() && Math.abs(zombie.getY() - y) <= 35 && Math.abs(zombie.getX() - x) <= 45) {
                    zombie.takeDamage(INSTANT_KILL_DAMAGE);
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

        if (active && activatedEffectTimer > 0 && activatedEffect != null) {
            g.drawImage(activatedEffect.getImage(), x - 45, y - 45, 90, 70, null);
        }
    }

    public boolean isUsed() {
        return used;
    }

    public int getY() {
        return y;
    }
}
