import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SpikeweedPlant extends Plant {
    private List<BufferedImage> frames;
    private int frameIndex;
    private int frameTimer;
    private int damageTimer;

    public SpikeweedPlant(int x, int y) {
        super(x, y, 100, 300);
        frames = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            BufferedImage img = loadResourceImage("Plants/Spikeweed/Spikeweed/Spikeweed_" + i + ".png");
            if (img != null) {
                frames.add(img);
            }
        }
    }

    @Override
    public void update(List<Zombie> zombies, List<Sun> suns) {
        damageTimer++;
        if (damageTimer >= 30) {
            damageTimer = 0;
            for (Zombie zombie : zombies) {
                if (!zombie.isDead() && Math.abs(zombie.getY() - y) <= 35 && Math.abs(zombie.getX() - x) <= 50) {
                    zombie.takeDamage(20);
                }
            }
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
            g2d.drawImage(frames.get(frameIndex), x - 35, y - 22, 70, 35, null);
        }
    }
}
