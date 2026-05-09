import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class IceShroomPlant extends Plant {
    private List<BufferedImage> frames;
    private int frameIndex;
    private int frameTimer;
    private int fuseTimer;
    private int removeTimer;
    private boolean activated;
    private static final int FUSE_TIME = 100;
    private static final int FREEZE_TIME = 333;
    private static final int CHILL_TIME = 167;

    public IceShroomPlant(int x, int y) {
        super(x, y, 75, 300);
        frames = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            BufferedImage img = loadResourceImage("Plants/IceShroom/IceShroom/IceShroom_" + i + ".png");
            if (img != null) {
                frames.add(img);
            }
        }
    }

    @Override
    public void update(List<Zombie> zombies, List<Sun> suns) {
        frameTimer++;
        if (frameTimer >= 4 && !frames.isEmpty()) {
            frameTimer = 0;
            frameIndex = (frameIndex + 1) % frames.size();
        }

        if (!activated) {
            fuseTimer++;
            if (fuseTimer >= FUSE_TIME) {
                activated = true;
                for (Zombie zombie : zombies) {
                    zombie.freeze(FREEZE_TIME, CHILL_TIME);
                }
            }
        } else {
            removeTimer++;
            if (removeTimer >= 30) {
                health = 0;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        if (!frames.isEmpty()) {
            g2d.drawImage(frames.get(frameIndex), x - 25, y - 30, 50, 50, null);
        }
        if (activated) {
            g.setColor(new Color(120, 200, 255, 120));
            g.fillOval(x - 45, y - 45, 90, 90);
        } else {
            drawHealthBar(g);
        }
    }
}
