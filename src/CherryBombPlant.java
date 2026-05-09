import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class CherryBombPlant extends Plant {
    private List<BufferedImage> frames;
    private int frameIndex;
    private int frameTimer;
    private int fuseTimer;
    private boolean exploded;
    private static final int FUSE_TIME = 100;
    private static final int EXPLOSION_TIME = 25;
    private static final int EXPLOSION_RADIUS = 150;

    public CherryBombPlant(int x, int y) {
        super(x, y, 150, 300);
        frames = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            BufferedImage img = loadResourceImage("Plants/CherryBomb/CherryBomb_" + i + ".png");
            if (img != null) {
                frames.add(img);
            }
        }
    }

    @Override
    public void update(List<Zombie> zombies, List<Sun> suns) {
        fuseTimer++;
        if (!exploded && fuseTimer >= FUSE_TIME) {
            exploded = true;
            for (Zombie zombie : zombies) {
                if (!zombie.isDead() && Math.hypot(zombie.getX() - x, zombie.getY() - y) <= EXPLOSION_RADIUS) {
                    zombie.takeDamage(1800);
                }
            }
        }
        if (exploded && fuseTimer >= FUSE_TIME + EXPLOSION_TIME) {
            health = 0;
        }

        if (!frames.isEmpty()) {
            frameTimer++;
            if (frameTimer >= 4) {
                frameTimer = 0;
                frameIndex = (frameIndex + 1) % frames.size();
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        if (!frames.isEmpty()) {
            int size = exploded ? 90 : 55;
            g2d.drawImage(frames.get(frameIndex), x - size / 2, y - size / 2, size, size, null);
        }
        if (!exploded) {
            drawHealthBar(g);
        }
    }
}
