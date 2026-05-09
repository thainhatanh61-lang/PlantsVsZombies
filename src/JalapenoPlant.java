import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class JalapenoPlant extends Plant {
    private List<BufferedImage> idleFrames;
    private List<BufferedImage> fireFrames;
    private int frameIndex;
    private int fireFrameIndex;
    private int frameTimer;
    private int fuseTimer;
    private int fireTimer;
    private boolean activated;
    private static final int FUSE_TIME = 100;
    private static final int FIRE_TIME = 32;
    private static final int FIRE_DAMAGE = 1800;

    public JalapenoPlant(int x, int y) {
        super(x, y, 125, 300);
        idleFrames = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            BufferedImage img = loadResourceImage("Plants/Jalapeno/Jalapeno/Jalapeno_" + i + ".png");
            if (img != null) {
                idleFrames.add(img);
            }
        }
        fireFrames = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            BufferedImage img = loadResourceImage("Plants/Jalapeno/JalapenoExplode/JalapenoExplode_" + i + ".png");
            if (img != null) {
                fireFrames.add(img);
            }
        }
    }

    @Override
    public void update(List<Zombie> zombies, List<Sun> suns) {
        frameTimer++;
        if (frameTimer >= 4) {
            frameTimer = 0;
            if (!activated && !idleFrames.isEmpty()) {
                frameIndex = (frameIndex + 1) % idleFrames.size();
            } else if (activated && !fireFrames.isEmpty()) {
                fireFrameIndex = (fireFrameIndex + 1) % fireFrames.size();
            }
        }

        if (!activated) {
            fuseTimer++;
            if (fuseTimer >= FUSE_TIME) {
                activated = true;
                fireTimer = FIRE_TIME;
                for (Zombie zombie : zombies) {
                    if (!zombie.isDead() && Math.abs(zombie.getY() - y) <= 35) {
                        zombie.takeDamage(FIRE_DAMAGE);
                        if (zombie.getHealth() <= 0) {
                            zombie.burnKill();
                        }
                    }
                }
            }
        } else {
            fireTimer--;
            if (fireTimer <= 0) {
                health = 0;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        if (activated) {
            for (int fireX = 105; fireX <= 805; fireX += 80) {
                if (!fireFrames.isEmpty()) {
                    g2d.drawImage(fireFrames.get(fireFrameIndex), fireX - 40, y - 50, 90, 100, null);
                } else {
                    g.setColor(new Color(255, 80, 0, 150));
                    g.fillRect(fireX - 40, y - 35, 80, 50);
                }
            }
        }

        if (!activated && !idleFrames.isEmpty()) {
            g2d.drawImage(idleFrames.get(frameIndex), x - 25, y - 35, 50, 60, null);
        }
        if (!activated) {
            drawHealthBar(g);
        }
    }
}
