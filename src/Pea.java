import java.awt.*;
import java.awt.image.BufferedImage;

public class Pea {
    private int x, y;
    private int speed;
    private BufferedImage image;
    private BufferedImage explodeImage;
    private boolean exploding;
    private boolean finished;
    private int explodeTimer;

    public Pea(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 5;
        loadImages();
    }

    private void loadImages() {
        image = Plant.loadResourceImage("Bullets/PeaNormal/PeaNormal_0.png");
        explodeImage = Plant.loadResourceImage("Bullets/PeaNormalExplode/PeaNormalExplode_0.png");
    }

    public void update() {
        if (exploding) {
            explodeTimer++;
            if (explodeTimer > 12) {
                finished = true;
            }
        } else {
            x += speed;
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        if (exploding) {
            if (explodeImage != null) {
                g2d.drawImage(explodeImage, x - 12, y - 12, 24, 24, null);
            } else {
                g.setColor(Color.ORANGE);
                g.fillOval(x - 10, y - 10, 20, 20);
            }
        } else if (image != null) {
            g2d.drawImage(image, x - 10, y - 10, 20, 20, null);
        } else {
            g.setColor(Color.GREEN);
            g.fillOval(x - 5, y - 5, 10, 10);
        }
    }

    public boolean hits(Zombie zombie) {
        if (exploding || finished) {
            return false;
        }
        if (Math.abs(x - zombie.getX()) < 30 && Math.abs(y - zombie.getY()) < 30) {
            explode();
            return true;
        }
        return false;
    }

    private void explode() {
        exploding = true;
        explodeTimer = 0;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isOffScreen() {
        return x > 900;
    }
}
