import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MushroomProjectile {
    private int x;
    private int y;
    private int frameIndex;
    private int frameTimer;
    private boolean finished;
    private List<BufferedImage> frames;

    public MushroomProjectile(int x, int y) {
        this.x = x;
        this.y = y;
        frames = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BufferedImage img = Plant.loadResourceImage("Plants/mushroom_bullet/mushroom_bullet_" + i + ".png");
            if (img != null) {
                frames.add(img);
            }
        }
    }

    public void update() {
        x += 5;
        if (!frames.isEmpty()) {
            frameTimer++;
            if (frameTimer >= 4) {
                frameTimer = 0;
                frameIndex = (frameIndex + 1) % frames.size();
            }
        }
    }

    public void draw(Graphics g) {
        if (!frames.isEmpty()) {
            g.drawImage(frames.get(frameIndex), x - 10, y - 10, 20, 20, null);
        } else {
            g.setColor(Color.MAGENTA);
            g.fillOval(x - 6, y - 6, 12, 12);
        }
    }

    public boolean hits(Zombie zombie) {
        if (finished) {
            return false;
        }
        if (Math.abs(x - zombie.getX()) < 30 && Math.abs(y - zombie.getY()) < 30) {
            finished = true;
            return true;
        }
        return false;
    }

    public boolean isFinished() {
        return finished || x > 900;
    }
}
