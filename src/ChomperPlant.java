import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ChomperPlant extends Plant {
    private List<BufferedImage> idleFrames;
    private List<BufferedImage> attackFrames;
    private List<BufferedImage> digestFrames;
    private int frameIndex;
    private int frameTimer;
    private int attackTimer;
    private int digestTimer;
    private boolean attacking;
    private boolean digesting;
    private static final int DIGEST_TIME = 1400;

    public ChomperPlant(int x, int y) {
        super(x, y, 150, 300);
        idleFrames = loadFrames("Plants/Chomper/Chomper/Chomper_", 13);
        attackFrames = loadFrames("Plants/Chomper/ChomperAttack/ChomperAttack_", 9);
        digestFrames = loadFrames("Plants/Chomper/ChomperDigest/ChomperDigest_", 6);
    }

    private List<BufferedImage> loadFrames(String path, int count) {
        List<BufferedImage> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            BufferedImage img = loadResourceImage(path + i + ".png");
            if (img != null) {
                list.add(img);
            }
        }
        return list;
    }

    @Override
    public void update(List<Zombie> zombies, List<Sun> suns) {
        if (digesting) {
            digestTimer++;
            if (digestTimer >= DIGEST_TIME) {
                digesting = false;
                digestTimer = 0;
                frameIndex = 0;
            }
            animate(digestFrames);
            return;
        }

        if (attacking) {
            attackTimer++;
            if (attackTimer >= 25) {
                attacking = false;
                digesting = true;
                attackTimer = 0;
                frameIndex = 0;
            }
            animate(attackFrames);
            return;
        }

        for (Zombie zombie : zombies) {
            if (!zombie.isDead() && Math.abs(zombie.getY() - y) <= 35 && zombie.getX() >= x - 20 && zombie.getX() <= x + 70) {
                if (zombie.getHealth() <= ZombieStats.NORMAL) {
                    zombie.takeDamage(999999);
                    attacking = true;
                    frameIndex = 0;
                }
                break;
            }
        }
        animate(idleFrames);
    }

    private void animate(List<BufferedImage> frames) {
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
        List<BufferedImage> frames = idleFrames;
        if (digesting && !digestFrames.isEmpty()) {
            frames = digestFrames;
        } else if (attacking && !attackFrames.isEmpty()) {
            frames = attackFrames;
        }
        if (!frames.isEmpty()) {
            g2d.drawImage(frames.get(frameIndex % frames.size()), x - 30, y - 45, 70, 70, null);
        }
        drawHealthBar(g);
    }
}
